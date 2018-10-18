package com.thinkerwolf.mimo.processor;

import com.thinkerwolf.mimo.channel.ChannelProcessorContext;
import com.thinkerwolf.mimo.channel.event.ChannelEvent;
import com.thinkerwolf.mimo.channel.event.MessageEvent;

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
