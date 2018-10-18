package com.mimo.test.processor;

import java.nio.ByteBuffer;

import com.thinkerwolf.mimo.channel.ChannelProcessorContext;
import com.thinkerwolf.mimo.channel.event.WriteEvent;
import com.thinkerwolf.mimo.processor.ChannelOutboundProcessorAdapter;

public class TestClientChannelOutbound extends ChannelOutboundProcessorAdapter {

	@Override
	public void exceptionCaught(ChannelProcessorContext ctx, Throwable throwable) {
		throwable.printStackTrace();
	}

	@Override
	protected void write(ChannelProcessorContext ctx, WriteEvent event) {
		String s = (String) event.writeObject();
		ByteBuffer res = ByteBuffer.wrap(s.getBytes());
		ctx.write(res, event);
	}

}
