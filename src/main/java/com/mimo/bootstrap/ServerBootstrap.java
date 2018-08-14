package com.mimo.bootstrap;

import java.net.SocketAddress;

import com.mimo.channel.RunLoopGroup;
import com.mimo.concurrent.ChannelFuture;
import com.mimo.util.NetUtils;

/**
 * 服务端启动类
 * 
 * @author wukai
 *
 */
public class ServerBootstrap extends AbstractBootstrap<ServerBootstrap> {

	private SocketAddress localAddress;
	private RunLoopGroup workerGroup;

	public ChannelFuture bind(SocketAddress address) {
		this.localAddress = address;
		return initAndRegister();
	}

	public ChannelFuture bind(int port) {
		return bind(NetUtils.getLocalSocketAddress(port));
	}

	public ServerBootstrap group(RunLoopGroup bossGroup, RunLoopGroup workerGroup) {
		if (bossGroup == null || workerGroup == null) {
			throw new IllegalArgumentException("bossGroup and workerGroup can't be null");
		}
		super.group(bossGroup);
		this.workerGroup = workerGroup;
		return this;
	}
	
	
	@Override
	void init() {
		// workerGroup执行读写任务

	}

	@Override
	protected SocketAddress localAddress() {
		return localAddress;
	}

	@Override
	protected SocketAddress remoteAddress() {
		return null;
	}

}
