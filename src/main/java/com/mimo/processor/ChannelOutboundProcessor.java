package com.mimo.processor;

import com.mimo.channel.ChannelProcessorContext;
import com.mimo.channel.event.ChannelEvent;

/**
 * 输出处理
 * 
 * @author wukai
 *
 */
public interface ChannelOutboundProcessor extends ChannelProcessor {
	
	void handleOutbound(ChannelProcessorContext ctx, ChannelEvent event);
	
	
}
