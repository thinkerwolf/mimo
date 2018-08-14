package com.mimo.channel;

import java.util.concurrent.Executor;

import com.mimo.concurrent.ChannelFuture;

public interface RunLoop extends Executor {
	
	/** 注册Channel */
	void register(Channel channel);
	/** 注册Channel */
	void register(Channel channel, ChannelFuture future);
}
