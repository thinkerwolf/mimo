package com.thinkerwolf.mimo.channel;

import java.util.concurrent.Executor;

public interface RunLoop extends Executor {
	
	/** 注册Channel */
	void register(Channel channel);
	/** 注册Channel */
	void register(Channel channel, ChannelPromise promise);
	
	RunLoopGroup parent();
	
}
