package com.mimo.processor;

import com.mimo.channel.ChannelEvent;
import com.mimo.channel.ChannelProcessorContext;

/**
 * 输入处理
 * 
 * @author wukai
 *
 */
public interface ChannelInboundProcessor extends ChannelProcessor {

	void handleInbound(ChannelProcessorContext ctx, ChannelEvent event);

}
