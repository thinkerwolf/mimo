package com.thinkerwolf.mimo.channel.nio;

import static com.thinkerwolf.mimo.channel.Channels.*;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thinkerwolf.mimo.channel.AbstractChannel;
import com.thinkerwolf.mimo.channel.ChannelProcessorChain;
import com.thinkerwolf.mimo.channel.ChannelPromise;
import com.thinkerwolf.mimo.channel.RunLoop;
import com.thinkerwolf.mimo.util.ObjectUtil;

public abstract class AbstractNioChannel extends AbstractChannel {
	private static final Logger logger = LoggerFactory.getLogger(AbstractNioChannel.class);
	protected SelectableChannel ch;
	private SelectionKey selectionKey;
	protected SocketAddress localAddress;
	protected SocketAddress remoteAddress;

	protected AbstractNioChannel(SelectableChannel ch, ChannelProcessorChain chain) {
		super(chain);
		this.ch = ch;
		try {
			ch.configureBlocking(false);
		} catch (IOException e) {
			try {
				ch.close();
			} catch (IOException e2) {
				if (logger.isWarnEnabled()) {
					logger.warn("Failed to close a partially initialized socket.", e2);
				}
			}
			throw new RuntimeException("Failed to enter non-blocking mode.", e);
		}
	}

	public AbstractNioChannel(SelectableChannel ch) {
		this(ch, null);
	}

	public SelectableChannel javaChannel() {
		return ch;
	}

	public SelectionKey selectionKey() {
		return ObjectUtil.isNotNull(selectionKey, "selectionKey is null");
	}

	public void connect(SocketAddress localAddress, SocketAddress remoteAddress) {
		try {
			if (doConnect(localAddress, remoteAddress)) {
				if (localAddress != null) {
					this.finishConnectPromise.setSuccess(null);
				}
				logger.debug(this.getClass().getSimpleName() + " : connect success");
			}
		} catch (Exception e) {
			logger.debug(this.getClass().getSimpleName() + " : connect fail");
		}

	}

	protected abstract boolean doConnect(SocketAddress localAddress, SocketAddress remoteAddress) throws Exception;

	@Override
	public void register(RunLoop runLoop, ChannelPromise promise) {
		try {
			SelectableChannel channel = javaChannel();
			Selector selector = ((NioRunLoop) runLoop).getNioSelector();
			selectionKey = channel.register(selector, 0, this);
			beginRead(selectionKey);
			selector.wakeup();
			if (promise != null) {
				this.finishConnectPromise = promise;
			}
			promise.setSuccess(null);
		} catch (Exception e) {
			promise.setFailure(null);
			throw new RuntimeException(e);
		}
	}

	protected void beginRead(SelectionKey selectionKey) {

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

	protected abstract void doRead();

	protected abstract void doConnect() throws IOException;

	protected abstract class AbstractNioUnderLayer extends AbstractUnderLayer {

		@Override
		public void register(RunLoop runLoop) throws IOException {
			SelectableChannel ch = javaChannel();
		}

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

	}

}
