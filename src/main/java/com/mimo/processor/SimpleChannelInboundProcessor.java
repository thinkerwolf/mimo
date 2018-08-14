package com.mimo.processor;

import com.mimo.channel.ChannelEvent;
import com.mimo.channel.ChannelProcessorContext;
import com.mimo.channel.MessageEvent;

public abstract class SimpleChannelInboundProcessor implements ChannelInboundProcessor {

	@Override
	public void handleInbound(ChannelProcessorContext ctx, ChannelEvent event) {
		if (event instanceof MessageEvent) {
			messageReceived(ctx, (MessageEvent) event);
		}
		
		ctx.sendInbound(event);
	}

	public abstract void messageReceived(ChannelProcessorContext ctx, MessageEvent message);

}
