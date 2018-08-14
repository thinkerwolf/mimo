package com.mimo.channel.nio;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mimo.channel.AbstractChannel;
import com.mimo.channel.ChannelProcessorChain;

import static com.mimo.channel.Channels.*;
import com.mimo.channel.RunLoop;
import com.mimo.concurrent.ChannelFuture;
import com.mimo.util.NetUtils;
import com.mimo.util.ObjectUtil;

public abstract class AbstractNioChannel extends AbstractChannel {
	private static final Logger logger = LoggerFactory.getLogger(AbstractNioChannel.class);
	protected SelectableChannel ch;
	private SelectionKey selectionKey;

	protected AbstractNioChannel(SelectableChannel ch, ChannelProcessorChain chain) {
		super(chain);
		this.ch = ch;
	}

	public AbstractNioChannel(SelectableChannel ch) {
		super();
		this.ch = ch;
	}

	public SelectableChannel nioChannel() {
		return ch;
	}

	public SelectionKey selectionKey() {
		return ObjectUtil.isNotNull(selectionKey, "selectionKey is null");
	}

	public void connect(SocketAddress localAddress, SocketAddress remoteAddress) {
		try {
			if (doConnect(localAddress, remoteAddress)) {
				if (localAddress != null) {
					this.finishConnectFuture.setSuccess(null);
				}
				logger.debug(this.getClass().getSimpleName() + " : connect success");
			}
		} catch (Exception e) {
			logger.debug(this.getClass().getSimpleName() + " : connect fail");
		}

	}

	public abstract boolean doConnect(SocketAddress localAddress, SocketAddress remoteAddress) throws Exception;

	public abstract boolean doAccept(Selector selector) throws IOException;

	@Override
	public void register(RunLoop runLoop, ChannelFuture future) {
		try {
			nioChannel().configureBlocking(false);
			selectionKey = nioChannel().register(((NioRunLoop) runLoop).getNioSelector(), 0, this);
			if (future != null) {
				this.finishConnectFuture = future;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private SocketChannel socketChannel() {
		try {
			if (ch instanceof SocketChannel) {
				return (SocketChannel) ch;
			} else {
				return NetUtils.accept((ServerSocketChannel) ch);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 读取
	 * 
	 * @param sk
	 */
	void read(SelectionKey sk) {
		SocketChannel channel = (SocketChannel) sk.channel();
		NioSocketChannel nioChannel = (NioSocketChannel) sk.attachment();
		if (channel != null) {
			try {
				int totalReadNum = 0;
				int tmpReadNum;

				// FIXME 内存泄漏问题
				ByteBuffer bb = ByteBuffer.allocateDirect(100);
				List<ByteBuffer> list = new ArrayList<>();
				while ((tmpReadNum = channel.read(bb)) > 0) {
					totalReadNum += tmpReadNum;
					list.add(bb);
					bb = ByteBuffer.allocateDirect(100);
				}
				ByteBuffer buf = ByteBuffer.allocate(totalReadNum);
				for (ByteBuffer b : list) {
					b.flip();
					buf.put(b);
				}
				buf.flip();
				fireMessageReceived(this.chain(), nioChannel, buf);
			} catch (Exception e) {
				try {
					channel.close();
					channel.socket().close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				fireExceptionCaught(chain(), e, true);
			}
		}
	}

	/**
	 * 接受
	 * 
	 * @param sk
	 * @throws Throwable
	 */
	void accept(SelectionKey sk) throws Throwable {
		if (doAccept(sk.selector())) {
			logger.debug("channel accept success");
		} else {
			logger.debug("channel accept error");
		}

	}

	/**
	 * 连接
	 * 
	 * @param sk
	 * @throws Throwable
	 */
	void connect(SelectionKey sk) throws Throwable {
		int ops = sk.interestOps();
		ops &= ~SelectionKey.OP_CONNECT;
		ops |= SelectionKey.OP_READ;
		sk.interestOps(ops);
		SocketChannel channel = (SocketChannel) ch;
		channel.finishConnect();
		this.finishConnectFuture.setSuccess(null);
		logger.debug("channel connect!" + (sk.channel() == ch));
	}

	@Override
	public void write(Object obj) {
		fireMessageWrite(this.chain(), this, obj, false);
	}

	@Override
	public void writeAndFlush(Object obj) {
		fireMessageWrite(this.chain(), this, obj, true);
	}

	@Override
	public void flush() {
	}

    // 地层的操作，如read，write


	public void writeInner(Object obj, boolean flush) {
		if (!(obj instanceof ByteBuffer)) {
			throw new IllegalArgumentException();
		}
		doWrite((ByteBuffer) obj, flush);
	}


	protected void doClose() throws IOException {}
	protected abstract void doWrite(ByteBuffer buf, boolean flush);
	//protected abstract void doRead(SocketChannel nioChannel, NioSocketChannel channel);
}
