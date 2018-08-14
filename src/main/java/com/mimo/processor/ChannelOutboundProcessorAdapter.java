package com.mimo.processor;

import com.mimo.channel.ChannelEvent;
import com.mimo.channel.ChannelProcessorContext;
import com.mimo.channel.WriteEvent;

public abstract class ChannelOutboundProcessorAdapter implements ChannelOutboundProcessor {
    @Override
    public void handleOutbound(ChannelProcessorContext ctx, ChannelEvent event) {
        if (event instanceof WriteEvent) {
            write(ctx, (WriteEvent) event);
        } else {
            ctx.sendOutbound(event);
        }
    }

    protected abstract void write(ChannelProcessorContext ctx, WriteEvent event);

}
