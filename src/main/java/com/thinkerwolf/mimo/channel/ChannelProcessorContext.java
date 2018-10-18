package com.thinkerwolf.mimo.channel;

import com.thinkerwolf.mimo.channel.event.ChannelEvent;
import com.thinkerwolf.mimo.processor.ChannelProcessor;

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
