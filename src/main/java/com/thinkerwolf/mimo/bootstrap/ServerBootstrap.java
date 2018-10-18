package com.thinkerwolf.mimo.bootstrap;

import java.net.SocketAddress;

import com.thinkerwolf.mimo.channel.RunLoopGroup;
import com.thinkerwolf.mimo.concurrent.ChannelFuture;
import com.thinkerwolf.mimo.util.NetUtils;

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
