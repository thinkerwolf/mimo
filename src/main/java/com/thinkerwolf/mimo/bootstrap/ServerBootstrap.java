package com.thinkerwolf.mimo.bootstrap;

import java.net.SocketAddress;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thinkerwolf.mimo.channel.Channel;
import com.thinkerwolf.mimo.channel.ChannelFuture;
import com.thinkerwolf.mimo.channel.ChannelProcessorContext;
import com.thinkerwolf.mimo.channel.DefaultChannelPromise;
import com.thinkerwolf.mimo.channel.RunLoopGroup;
import com.thinkerwolf.mimo.channel.event.ChannelEvent;
import com.thinkerwolf.mimo.processor.ChannelInboundProcessor;
import com.thinkerwolf.mimo.util.NetUtils;

/**
 * 服务端启动类
 * 
 * @author wukai
 *
 */
public class ServerBootstrap extends AbstractBootstrap<ServerBootstrap> {

	private static final Logger logger = LoggerFactory.getLogger(ServerBootstrap.class);

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
	void init(Channel channel) {
		// workerGroup执行读写任务
		// group.register(channel)
		workerGroup.startExecutor();
		channel.chain().addFirst(new ServerAcceptor(workerGroup));
	}

	@Override
	protected SocketAddress localAddress() {
		return localAddress;
	}

	@Override
	protected SocketAddress remoteAddress() {
		return null;
	}

	private static class ServerAcceptor implements ChannelInboundProcessor {

		private RunLoopGroup workerGroup;

		public ServerAcceptor(RunLoopGroup workerGroup) {
			this.workerGroup = workerGroup;
		}

		@Override
		public void exceptionCaught(ChannelProcessorContext ctx, Throwable etx) {
			ctx.sendException(etx);
		}

		@Override
		public void channelConnected(ChannelProcessorContext ctx, ChannelEvent event) {
			ctx.sendConnected(event);
		}

		@Override
		public void channelAccepted(ChannelProcessorContext ctx, ChannelEvent event) {
			Channel channel = event.channel();
			StopWatch sw = new StopWatch();
			sw.start();
			workerGroup.register(channel, new DefaultChannelPromise(channel));
			sw.stop();
			logger.debug("Server channel register time : {}", sw.getTime());
			ctx.sendAccepted(event);
		}

		@Override
		public void handleInbound(ChannelProcessorContext ctx, ChannelEvent event) {
			ctx.sendInbound(event);
		}

	}

}
