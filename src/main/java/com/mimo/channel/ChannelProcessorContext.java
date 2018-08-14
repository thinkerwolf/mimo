package com.mimo.channel;

import com.mimo.channel.event.ChannelEvent;
import com.mimo.processor.ChannelProcessor;

public interface ChannelProcessorContext {

	ChannelProcessorChain chain();

	Channel channel();

	ChannelProcessor processor();

	String getName();

	void sendInbound(ChannelEvent event);

	void sendOutbound(ChannelEvent event);

	void sendException(Throwable etx);
	
	void sendConnected(ChannelEvent event);
	void sendAccepted(ChannelEvent event);
	void write(Object msg, ChannelEvent event);

}
