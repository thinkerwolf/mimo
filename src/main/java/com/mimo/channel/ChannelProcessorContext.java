package com.mimo.channel;

import com.mimo.processor.ChannelProcessor;

public interface ChannelProcessorContext {

	ChannelProcessorChain chain();

	Channel channel();
	
	ChannelProcessor processor();
	
	String getName();
	
	void sendInbound(ChannelEvent ce);
	
	void sendOutbound(ChannelEvent ce);
	
	void write(Object msg, ChannelEvent ce);
	
}
