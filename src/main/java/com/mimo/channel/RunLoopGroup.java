package com.mimo.channel;

import com.mimo.concurrent.ChannelFuture;

public interface RunLoopGroup {

	RunLoop newRunLoop();

	RunLoop next();
	
	ChannelFuture register(Channel channel);
	
	ChannelFuture register(Channel channel, ChannelFuture future);
	
	void startExecutor();
	
}
