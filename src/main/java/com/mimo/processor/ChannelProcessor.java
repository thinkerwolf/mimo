package com.mimo.processor;

import com.mimo.channel.ChannelProcessorContext;

public interface ChannelProcessor {

	/**
	 * 捕获到异常
	 * 
	 * @param throwable
	 */
	void exceptionCaught(ChannelProcessorContext ctx, Throwable throwable);
	
}
