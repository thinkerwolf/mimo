package com.mimo.bootstrap;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import com.mimo.concurrent.ChannelFuture;

/**
 * 客户端启动类
 * 
 * @author wukai
 *
 */
public class Bootstrap extends AbstractBootstrap<Bootstrap> {

	// private static final Logger logger =
	// LoggerFactory.getLogger(Bootstrap.class);

	private SocketAddress remoteAddress;

	public Bootstrap() {

	}

	public ChannelFuture connect(String host, int port) {
		return this.connect(new InetSocketAddress(host, port));
	}

	public ChannelFuture connect(SocketAddress address) {
		this.remoteAddress = address;
		return initAndRegister();
	}

	@Override
	void init() {

	}

	@Override
	protected SocketAddress localAddress() {
		return null;
	}

	@Override
	protected SocketAddress remoteAddress() {
		return remoteAddress;
	}

}
