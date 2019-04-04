package com.thinkerwolf.mimo.channel.nio;

import static com.thinkerwolf.mimo.channel.Channels.*;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import com.thinkerwolf.mimo.channel.Channel;
import com.thinkerwolf.mimo.channel.ChannelProcessorChain;
import com.thinkerwolf.mimo.util.NetUtils;
import com.thinkerwolf.mimo.util.PlatformUtil;

public class NioSocketChannel extends AbstractNioChannel implements com.thinkerwolf.mimo.channel.SocketChannel {

	private static SocketChannel newSocket() {
		try {
			return PlatformUtil.DEFAULT_PROVIDER.openSocketChannel();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public NioSocketChannel() {
		super(newSocket());
		this.server = false;
	}

	NioSocketChannel(SocketChannel socketChannel, ChannelProcessorChain chain) {
		super(socketChannel, chain);
		this.server = false;
	}

	@Override
	protected void beginRead(SelectionKey selectionKey) {
		selectionKey.interestOps(selectionKey.interestOps() | SelectionKey.OP_READ);
	}

	@Override
	public ChannelProcessorChain chain() {
		return super.chain();
	}

	@Override
	public SocketChannel javaChannel() {
		return (SocketChannel) super.javaChannel();
	}

	@Override
	public boolean doConnect(SocketAddress localAddress, SocketAddress remoteAddress) throws Exception {
		boolean success = false;
		try {
			boolean connected = NetUtils.connect(javaChannel(), remoteAddress);
			if (!connected) {
				selectionKey().interestOps(SelectionKey.OP_CONNECT);
			}
			success = true;
			this.localAddress = localAddress;
			this.remoteAddress = remoteAddress;
			return connected;
		} catch (IOException e) {
			if (!success) {
				doClose();
			}
		}
		return false;
	}

	@Override
	protected void doClose() throws IOException {
		super.doClose();
		javaChannel().close();
	}

	public void doWrite(ByteBuffer buf, boolean flush) {
		try {
			final SocketChannel sc = javaChannel();
			if (flush) {
				sc.write(buf);
				while (sc.write(buf) > 0) {

				}
			} else {
				// 新建write task

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void doRead() {
		UnderLayer underLayer = underLayer();
		NioSocketChannel channel = NioSocketChannel.this;
		SocketChannel nioChannel = channel.javaChannel();
		if (nioChannel != null) {
			if (!nioChannel.isOpen()) {

				return;
			}
			try {
				ByteBuffer bb = ByteBuffer.allocateDirect(100);
				nioChannel.read(bb);

				// FIXME 处理read
				int totalReadNum = 0;
				int tmpReadNum;
				// List<ByteBuffer> list = new ArrayList<>();
				// while ((tmpReadNum = nioChannel.read(bb)) > 0) {
				// totalReadNum += tmpReadNum;
				// list.add(bb);
				// bb = ByteBuffer.allocateDirect(100);
				// }
				// ByteBuffer buf = ByteBuffer.allocate(totalReadNum);
				// for (ByteBuffer b : list) {
				// b.flip();
				// buf.put(b);
				// }
				// buf.flip();

				int op = selectionKey().interestOps();
				selectionKey().interestOps(op & ~SelectionKey.OP_READ);
				bb.flip();
				fireMessageReceived(channel.chain(), channel, bb);
			} catch (Exception e) {
				try {
					underLayer.close();
				} catch (IOException e1) {
					// Ignore
				}
				fireExceptionCaught(chain(), e, true);
			}
		}
	}

	@Override
	protected void doConnect() throws IOException {
		NioSocketChannel channel = NioSocketChannel.this;
		SocketChannel nioChannel = channel.javaChannel();
		boolean success = nioChannel.finishConnect();
		if (success) {
			channel.finishConnectPromise.setSuccess(null);
			fireChannelConnected(channel.chain(), channel);
		} else {
			channel.finishConnectPromise.setFailure(null);
		}
	}

	class NioUnderLayer extends AbstractNioUnderLayer {

		@Override
		public Channel accept() throws IOException {
			// do nothing
			throw new RuntimeException("NioServerSocket can't accept");
		}

	}

	@Override
	protected UnderLayer newUnderLayer() {
		return new NioUnderLayer();
	}

}
