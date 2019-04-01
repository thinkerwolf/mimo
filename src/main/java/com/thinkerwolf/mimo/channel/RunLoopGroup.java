package com.thinkerwolf.mimo.channel;

import com.thinkerwolf.mimo.concurrent.ChannelFuture;

public interface RunLoopGroup {
	
	RunLoop newRunLoop();

	RunLoop next();
	
	ChannelFuture register(Channel channel);
	
	ChannelFuture register(Channel channel, ChannelFuture future);
	
	void startExecutor();
	
}
