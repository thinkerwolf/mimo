package com.mimo.test.processor;

import java.nio.ByteBuffer;

import com.mimo.channel.ChannelProcessorContext;
import com.mimo.channel.MessageEvent;
import com.mimo.processor.SimpleChannelInboundProcessor;

public class TestServerChannelInbound extends SimpleChannelInboundProcessor {

	@Override
	public void messageReceived(ChannelProcessorContext ctx, MessageEvent message) {
		Object obj = message.message();
		ByteBuffer bb = (ByteBuffer) obj;
		StringBuilder sb = new StringBuilder();
		while (bb.hasRemaining()) {
			sb.append((char) bb.get());
		}
		System.out.println("messageReceived : " + sb);
		
		message.channel().writeAndFlush("423212123");
		
	}

	@Override
	public void exceptionCaught(ChannelProcessorContext ctx, Throwable throwable) {
		throwable.printStackTrace();
	}

}
