package com.thinkerwolf.mimo.test.processor;

import java.nio.ByteBuffer;

import com.thinkerwolf.mimo.channel.ChannelProcessorContext;
import com.thinkerwolf.mimo.channel.event.ChannelEvent;
import com.thinkerwolf.mimo.channel.event.MessageEvent;
import com.thinkerwolf.mimo.processor.SimpleChannelInboundProcessor;

public class TestClientChannelInbound extends SimpleChannelInboundProcessor {

	@Override
	public void exceptionCaught(ChannelProcessorContext ctx, Throwable throwable) {

	}

	@Override
	public void messageReceived(ChannelProcessorContext ctx, MessageEvent message) {
		Object obj = message.message();
		ByteBuffer bb = (ByteBuffer) obj;
		StringBuilder sb = new StringBuilder();
		while (bb.hasRemaining()) {
			sb.append((char) bb.get());
		}
		System.out.println("messageReceived : " + sb);
	}

	@Override
	public void channelConnected(ChannelProcessorContext ctx, ChannelEvent event) {
		System.out.println("Channel connected " + event.channel());
	}
}
