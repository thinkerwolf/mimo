package com.thinkerwolf.mimo.channel;

public interface RunLoopGroup {
	
	RunLoop newRunLoop();

	RunLoop next();
	
	ChannelFuture register(Channel channel);
	
	ChannelFuture register(Channel channel, ChannelPromise promise);
	
	void startExecutor();
	
}
