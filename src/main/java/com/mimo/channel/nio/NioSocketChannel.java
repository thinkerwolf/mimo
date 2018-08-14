package com.mimo.channel.nio;

import static com.mimo.channel.Channels.*;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import com.mimo.channel.Channel;
import com.mimo.channel.ChannelProcessorChain;
import com.mimo.util.NetUtils;
import com.mimo.util.PlatformUtil;

public class NioSocketChannel extends AbstractNioChannel implements com.mimo.channel.SocketChannel {

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
	public ChannelProcessorChain chain() {
		return super.chain();
	}

	@Override
	public SocketChannel nioChannel() {
		return (SocketChannel) super.nioChannel();
	}

	@Override
	public boolean doConnect(SocketAddress localAddress, SocketAddress remoteAddress) throws Exception {
		boolean success = false;
		try {
			boolean connected = NetUtils.connect(nioChannel(), remoteAddress);
			if (!connected) {
				selectionKey().interestOps(SelectionKey.OP_CONNECT);
			}
			success = true;
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
		nioChannel().close();
	}

	@Override
	protected boolean doAccept(Selector selector) throws IOException {
		throw new UnsupportedOperationException("client can't accept");
	}

	public void doWrite(ByteBuffer buf, boolean flush) {
		try {
			final SocketChannel sc = nioChannel();
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

	class NioUnderLayer extends AbstractNioUnderLayer {
		@Override
		protected void doRead() {
			NioSocketChannel channel = NioSocketChannel.this;
			SocketChannel nioChannel = channel.nioChannel();
			if (nioChannel != null) {
				try {
					int totalReadNum = 0;
					int tmpReadNum;

					ByteBuffer bb = ByteBuffer.allocateDirect(100);
					List<ByteBuffer> list = new ArrayList<>();
					while ((tmpReadNum = nioChannel.read(bb)) > 0) {
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
					fireMessageReceived(channel.chain(), channel, buf);
				} catch (Exception e) {
					try {
						close();
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
			SocketChannel nioChannel = channel.nioChannel();
			nioChannel.finishConnect();
			channel.finishConnectFuture.setSuccess(null);
			fireChannelConnected(channel.chain(), channel);
		}

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
