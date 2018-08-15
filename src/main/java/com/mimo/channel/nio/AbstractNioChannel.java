package com.mimo.channel.nio;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mimo.channel.AbstractChannel;
import com.mimo.channel.ChannelProcessorChain;

import static com.mimo.channel.Channels.*;
import com.mimo.channel.RunLoop;
import com.mimo.concurrent.ChannelFuture;
import com.mimo.util.ObjectUtil;

public abstract class AbstractNioChannel extends AbstractChannel {
	private static final Logger logger = LoggerFactory.getLogger(AbstractNioChannel.class);
	protected SelectableChannel ch;
	private SelectionKey selectionKey;
	protected SocketAddress localAddress;

	protected SocketAddress remoteAddress;
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

	protected abstract boolean doConnect(SocketAddress localAddress, SocketAddress remoteAddress) throws Exception;

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
	public SocketAddress getLocalAddress() {
		return localAddress;
	}

	public SocketAddress getRemoteAddress() {
		return remoteAddress;
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

	protected void doClose() throws IOException {
	}

	protected abstract void doWrite(ByteBuffer buf, boolean flush);

	public void writeInner(Object obj, boolean flush) {
		if (!(obj instanceof ByteBuffer)) {
			throw new IllegalArgumentException();
		}
		doWrite((ByteBuffer) obj, flush);
	}

	protected abstract class AbstractNioUnderLayer extends AbstractUnderLayer {

		@Override
		public void read() {
			doRead();
		}

		@Override
		public void connect() throws IOException {
			doConnect();
		}

		@Override
		public void close() throws IOException {
			AbstractNioChannel.this.ch.close();
		}

		protected abstract void doConnect() throws IOException;

		protected abstract void doRead();
	}

}
