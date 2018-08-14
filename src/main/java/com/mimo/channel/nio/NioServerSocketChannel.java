package com.mimo.channel.nio;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import com.mimo.util.NetUtils;
import com.mimo.util.PlatformUtil;

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

	@Override
	public boolean doAccept(Selector selector) throws IOException {
		SocketChannel sc = nioChannel().accept();
		if (sc != null) {
			sc.configureBlocking(false);
			NioSocketChannel nsc = new NioSocketChannel(sc, this.chain());
			sc.register(selector, SelectionKey.OP_READ, nsc);
			return true;
		}
		return false;
	}

	public void doWrite(ByteBuffer buf, boolean flush) {
		throw new RuntimeException("NioServerSocket can't write");
	}

}
