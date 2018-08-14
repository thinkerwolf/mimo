package com.mimo.test.processor;

import java.nio.ByteBuffer;

import com.mimo.channel.ChannelEvent;
import com.mimo.channel.ChannelProcessorContext;
import com.mimo.channel.MessageEvent;
import com.mimo.channel.WriteEvent;
import com.mimo.processor.ChannelOutboundProcessor;

public class TestServerChannelOutbound implements ChannelOutboundProcessor {

	@Override
	public void exceptionCaught(ChannelProcessorContext ctx, Throwable throwable) {

	}

	@Override
	public void handleOutbound(ChannelProcessorContext ctx, ChannelEvent event) {
		if (event instanceof WriteEvent) {
			WriteEvent me = (WriteEvent) event;
			String s = (String) me.writeObject();
			ByteBuffer res = ByteBuffer.wrap(s.getBytes());
			ctx.write(res, event);
		}
	}

}
