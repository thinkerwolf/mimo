package com.thinkerwolf.mimo.channel.nio;

import static com.thinkerwolf.mimo.channel.Channels.*;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import com.thinkerwolf.mimo.channel.Channel;
import com.thinkerwolf.mimo.util.NetUtils;
import com.thinkerwolf.mimo.util.PlatformUtil;
/**
 * Nio服务端实现
 * 
 * @author wukai
 *
 */
public class NioServerSocketChannel extends AbstractNioChannel implements com.thinkerwolf.mimo.channel.ServerSocketChannel {


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



	@Override
	public ServerSocketChannel javaChannel() {
		return (ServerSocketChannel) super.javaChannel();
	}

	/**
	 * @param localAddress
	 * @param remoteAddress
	 * @return
	 */
	@Override
	public boolean doConnect(SocketAddress localAddress, SocketAddress remoteAddress) throws Exception {
		try {
			NetUtils.bind(javaChannel(), localAddress);
			selectionKey().interestOps(SelectionKey.OP_ACCEPT);
			this.localAddress = localAddress;
			this.remoteAddress = remoteAddress;
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			javaChannel().close();
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
	
	@Override
	protected void doRead() {
		// do nothing
	}
	
	@Override
	protected void doConnect() throws IOException {
		// do nothing
	}
	
	class NioServerUnderLayer extends AbstractNioUnderLayer {
		
		@Override
		public Channel accept() throws IOException {
			SocketChannel sc = NetUtils.accept(javaChannel());
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
