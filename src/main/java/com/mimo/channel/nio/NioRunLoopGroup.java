package com.mimo.channel.nio;

import com.mimo.channel.Channel;
import com.mimo.channel.RunLoop;
import com.mimo.channel.RunLoopGroup;
import com.mimo.concurrent.ChannelFuture;
import com.mimo.concurrent.DefaultChannelFuture;
import com.mimo.concurrent.DefaultThreadFactory;
import com.mimo.util.PlatformUtil;
import com.mimo.util.RandomUtil;

/**
 * Loop group
 * 
 * @author wukai
 *
 */
public class NioRunLoopGroup implements RunLoopGroup {

	private static final int DEFAULT_GROUP_NUM = Math.max(1, Runtime.getRuntime().availableProcessors() * 2);

	private static final Runnable RUN_LOOP_START_RUNNABLE = new Runnable() {

		@Override
		public void run() {

		}
	};

	private final RunLoop[] children;

	public NioRunLoopGroup() {
		this(0);
	}

	public NioRunLoopGroup(int nThreads) {
		nThreads = nThreads <= 0 ? DEFAULT_GROUP_NUM : nThreads;
		children = new RunLoop[nThreads];
		for (int i = 0; i < nThreads; i++) {
			children[i] = newRunLoop();
		}
	}

	public RunLoop newRunLoop() {
		return new NioRunLoop(DefaultThreadFactory.INSTANCE, PlatformUtil.DEFAULT_PROVIDER);
	}

	public RunLoop next() {
		// 暂时随机
		return children[RandomUtil.nextInt(children.length)];
	}

	/**
	 * 注册
	 */
	public ChannelFuture register(Channel channel) {
		return register(channel, new DefaultChannelFuture(channel));
	}

	@Override
	public ChannelFuture register(Channel channel, ChannelFuture future) {
		for (RunLoop runLoop : children) {
			runLoop.register(channel, future);
		}
		return future;
	}

	@Override
	public void startExecutor() {
		for (RunLoop runLoop : children) {
			runLoop.execute(RUN_LOOP_START_RUNNABLE);
		}
	}

}
