package com.mimo.channel;

import com.mimo.processor.ChannelProcessor;

/**
 * Channel处理链路
 * 
 * @author wukai
 *
 */
public interface ChannelProcessorChain {

	void addLast(String name, ChannelProcessor processor);

	void addFirst(String name, ChannelProcessor processor);

	void addLast(ChannelProcessor processor);

	void addFirst(ChannelProcessor processor);
	
	void sendInbound(ChannelEvent ce);
	
	void sendOutbound(ChannelEvent ce);
	
	void sendException(boolean inbound, Throwable etx);
	
}
