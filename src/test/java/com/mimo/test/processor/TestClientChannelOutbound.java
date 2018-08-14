package com.mimo.test.processor;

import java.nio.ByteBuffer;

import com.mimo.channel.ChannelProcessorContext;
import com.mimo.channel.WriteEvent;
import com.mimo.processor.ChannelOutboundProcessorAdapter;

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
