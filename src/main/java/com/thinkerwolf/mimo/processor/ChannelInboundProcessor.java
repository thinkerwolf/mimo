package com.thinkerwolf.mimo.processor;

import com.thinkerwolf.mimo.channel.ChannelProcessorContext;
import com.thinkerwolf.mimo.channel.event.ChannelEvent;

/**
 * 输入处理
 * 
 * @author wukai
 *
 */
public interface ChannelInboundProcessor extends ChannelProcessor {

	void handleInbound(ChannelProcessorContext ctx, ChannelEvent event);

	
}
