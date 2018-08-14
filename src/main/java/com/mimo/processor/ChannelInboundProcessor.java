package com.mimo.processor;

import com.mimo.channel.ChannelProcessorContext;
import com.mimo.channel.event.ChannelEvent;

/**
 * 输入处理
 * 
 * @author wukai
 *
 */
public interface ChannelInboundProcessor extends ChannelProcessor {

	void handleInbound(ChannelProcessorContext ctx, ChannelEvent event);

	
}
