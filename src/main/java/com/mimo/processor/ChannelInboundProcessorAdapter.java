package com.mimo.processor;

import com.mimo.channel.ChannelProcessorContext;
import com.mimo.channel.event.ChannelEvent;

public abstract class ChannelInboundProcessorAdapter implements ChannelInboundProcessor {

	@Override
	public void exceptionCaught(ChannelProcessorContext ctx, Throwable throwable) {
		ctx.sendException(throwable);
	}

	@Override
	public void handleInbound(ChannelProcessorContext ctx, ChannelEvent event) {
		ctx.sendInbound(event);
	}
	
	@Override
	public void channelConnected(ChannelProcessorContext ctx, ChannelEvent event) {
		ctx.sendConnected(event);
	}
	
	@Override
	public void channelAccepted(ChannelProcessorContext ctx, ChannelEvent event) {
		ctx.sendAccepted(event);
	}
}
