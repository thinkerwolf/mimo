package com.mimo.processor;

import com.mimo.channel.ChannelEvent;
import com.mimo.channel.ChannelProcessorContext;

/**
 * 输出处理
 * 
 * @author wukai
 *
 */
public interface ChannelOutboundProcessor extends ChannelProcessor {
	
	void handleOutbound(ChannelProcessorContext ctx, ChannelEvent event);
	
	
}
