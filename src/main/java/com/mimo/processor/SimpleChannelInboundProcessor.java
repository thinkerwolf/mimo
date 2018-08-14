package com.mimo.processor;

import com.mimo.channel.ChannelProcessorContext;
import com.mimo.channel.event.ChannelEvent;
import com.mimo.channel.event.MessageEvent;

public abstract class SimpleChannelInboundProcessor extends ChannelInboundProcessorAdapter {

	@Override
	public void handleInbound(ChannelProcessorContext ctx, ChannelEvent event) {
		if (event instanceof MessageEvent) {
			messageReceived(ctx, (MessageEvent) event);
		} else {
			
		}
		
		ctx.sendInbound(event);
	}

	public abstract void messageReceived(ChannelProcessorContext ctx, MessageEvent message);
	
	
	
}
