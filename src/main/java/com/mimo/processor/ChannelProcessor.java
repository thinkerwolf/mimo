package com.mimo.processor;

import com.mimo.channel.ChannelProcessorContext;
import com.mimo.channel.event.ChannelEvent;

public interface ChannelProcessor {

	/**
	 * 捕获到异常
	 * 
	 * @param throwable
	 */
	void exceptionCaught(ChannelProcessorContext ctx, Throwable throwable);

	void channelConnected(ChannelProcessorContext ctx, ChannelEvent event);
	
	void channelAccepted(ChannelProcessorContext ctx, ChannelEvent event);
}
