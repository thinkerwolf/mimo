package com.mimo.channel.nio;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import com.mimo.channel.Channel;
import com.mimo.util.NetUtils;
import com.mimo.util.PlatformUtil;

import static com.mimo.channel.Channels.*;
/**
 * Nio服务端实现
 * 
 * @author wukai
 *
 */
public class NioServerSocketChannel extends AbstractNioChannel implements com.mimo.channel.ServerSocketChannel {

	private SocketAddress localAddress;

	private SocketAddress remoteAddress;

	private static ServerSocketChannel newSocket() {
		try {
			return PlatformUtil.DEFAULT_PROVIDER.openServerSocketChannel();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public NioServerSocketChannel() {
		super(newSocket());
		this.server = true;
	}

	public SocketAddress getLocalAddress() {
		return localAddress;
	}

	public SocketAddress getRemoteAddress() {
		return remoteAddress;
	}

	@Override
	public ServerSocketChannel nioChannel() {
		return (ServerSocketChannel) super.nioChannel();
	}

	/**
	 * @param localAddress
	 * @param remoteAddress
	 * @return
	 */
	@Override
	public boolean doConnect(SocketAddress localAddress, SocketAddress remoteAddress) throws Exception {
		try {
			NetUtils.bind(nioChannel(), localAddress);
			selectionKey().interestOps(SelectionKey.OP_ACCEPT);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			nioChannel().close();
		}
		return false;
	}

	@Override
	protected void doClose() throws IOException {
		super.doClose();
	}

	public void doWrite(ByteBuffer buf, boolean flush) {
		throw new RuntimeException("NioServerSocket can't write");
	}

	class NioServerUnderLayer extends AbstractNioUnderLayer {

		@Override
		protected void doRead() {
			// do nothing
		}

		@Override
		protected void doConnect() throws IOException {
			// do nothing
		}

		@Override
		public Channel accept() throws IOException {
			SocketChannel sc = NetUtils.accept(nioChannel());
			if (sc != null) {
				sc.configureBlocking(false);
				NioSocketChannel nsc = new NioSocketChannel(sc, NioServerSocketChannel.this.chain());
				fireChannelAccepted(NioServerSocketChannel.this.chain(), nsc);
				return nsc;
			}
			return null;
		}
	}

	@Override
	protected UnderLayer newUnderLayer() {
		return new NioServerUnderLayer();
	}

}
