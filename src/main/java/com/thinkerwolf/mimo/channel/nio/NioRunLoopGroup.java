package com.thinkerwolf.mimo.channel.nio;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import com.thinkerwolf.mimo.channel.Channel;
import com.thinkerwolf.mimo.channel.ChannelFuture;
import com.thinkerwolf.mimo.channel.ChannelPromise;
import com.thinkerwolf.mimo.channel.DefaultChannelPromise;
import com.thinkerwolf.mimo.channel.RunLoop;
import com.thinkerwolf.mimo.channel.RunLoopGroup;
import com.thinkerwolf.mimo.concurrent.DefaultThreadFactory;
import com.thinkerwolf.mimo.util.PlatformUtil;

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

	public static final AtomicInteger GROUP_ID = new AtomicInteger(1);

	private final RunLoop[] children;

	int next = 0;

	ThreadFactory threadFactory;

	public NioRunLoopGroup() {
		this(DEFAULT_GROUP_NUM);
	}

	public NioRunLoopGroup(int nThreads) {
		nThreads = nThreads <= 0 ? DEFAULT_GROUP_NUM : nThreads;
		this.children = new RunLoop[nThreads];
		this.threadFactory = new DefaultThreadFactory("nio-thread-" + GROUP_ID.getAndIncrement());
		for (int i = 0; i < nThreads; i++) {
			children[i] = newRunLoop();
		}
	}

	public RunLoop newRunLoop() {
		return new NioRunLoop(threadFactory, PlatformUtil.DEFAULT_PROVIDER, this);
	}

	public RunLoop next() {
		RunLoop runLoop = children[next];
		synchronized (this) {
			if (++next >= children.length)
				next = 0;
		}
		return runLoop;
	}

	/**
	 * 注册
	 */
	public ChannelFuture register(Channel channel) {
		return register(channel, new DefaultChannelPromise(channel));
	}

	@Override
	public ChannelFuture register(Channel channel, ChannelPromise promise) {
		next().register(channel, promise);
		return promise;
	}

	@Override
	public void startExecutor() {
		for (RunLoop runLoop : children) {
			runLoop.execute(RUN_LOOP_START_RUNNABLE);
		}
	}

}
