package com.thinkerwolf.mimo.processor;

import com.thinkerwolf.mimo.channel.ChannelProcessorContext;
import com.thinkerwolf.mimo.channel.event.ChannelEvent;

/**
 * 输出处理
 * 
 * @author wukai
 *
 */
public interface ChannelOutboundProcessor extends ChannelProcessor {
	
	void handleOutbound(ChannelProcessorContext ctx, ChannelEvent event);
	
	
}
