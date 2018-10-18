package com.thinkerwolf.mimo.processor;

import com.thinkerwolf.mimo.channel.ChannelProcessorContext;
import com.thinkerwolf.mimo.channel.event.ChannelEvent;
import com.thinkerwolf.mimo.channel.event.WriteEvent;

public abstract class ChannelOutboundProcessorAdapter implements ChannelOutboundProcessor {
	@Override
	public void handleOutbound(ChannelProcessorContext ctx, ChannelEvent event) {
		if (event instanceof WriteEvent) {
			write(ctx, (WriteEvent) event);
		} else {
			ctx.sendOutbound(event);
		}
	}

	@Override
	public void exceptionCaught(ChannelProcessorContext ctx, Throwable throwable) {
		ctx.sendException(throwable);
	}

	@Override
	public void channelConnected(ChannelProcessorContext ctx, ChannelEvent event) {
		ctx.sendConnected(event);
	}

	@Override
	public void channelAccepted(ChannelProcessorContext ctx, ChannelEvent event) {
		ctx.sendAccepted(event);
	}
	
	protected abstract void write(ChannelProcessorContext ctx, WriteEvent event);
}
