package com.thinkerwolf.mimo.bootstrap;

import java.net.SocketAddress;

import com.thinkerwolf.mimo.channel.Channel;
import com.thinkerwolf.mimo.channel.ChannelFactory;
import com.thinkerwolf.mimo.channel.ChannelInitializer;
import com.thinkerwolf.mimo.channel.RunLoopGroup;
import com.thinkerwolf.mimo.concurrent.ChannelFuture;

public abstract class AbstractBootstrap<B extends AbstractBootstrap<B>> {

	protected RunLoopGroup group;
	protected ChannelFactory<Channel> channelFactory;
	protected ChannelInitializer initializer;

	@SuppressWarnings("unchecked")
	public B channel(Class<? extends Channel> clazz) {
		this.channelFactory = new BootstrapChannelFactory<Channel>(clazz);
		return (B) this;
	}

	@SuppressWarnings("unchecked")
	public B group(RunLoopGroup group) {
		this.group = group;
		return (B) this;
	}

	abstract void init(Channel channel);

	protected ChannelFuture initAndRegister() {
		final Channel channel = channelFactory.newChannel();
		initializer.initChannel(channel);
		ChannelFuture future = group.register(channel);
		channel.connect(localAddress(), remoteAddress());
		init(channel);
		group.startExecutor();
		return future;
	}

	protected abstract SocketAddress localAddress();

	protected abstract SocketAddress remoteAddress();

	@SuppressWarnings("unchecked")
	public B channelInitialize(ChannelInitializer initializer) {
		this.initializer = initializer;
		return (B) this;
	}

	private static final class BootstrapChannelFactory<C extends Channel> implements ChannelFactory<Channel> {
		private Class<? extends Channel> clazz;

		public BootstrapChannelFactory(Class<? extends Channel> clazz) {
			this.clazz = clazz;
		}

		@SuppressWarnings("unchecked")
		public C newChannel() {
			try {
				return (C) this.clazz.newInstance();
			} catch (Exception e) {
				throw new RuntimeException("channel init error");
			}
		}
	}

}
