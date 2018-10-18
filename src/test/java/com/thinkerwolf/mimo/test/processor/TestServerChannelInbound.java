package com.thinkerwolf.mimo.test.processor;

import java.nio.ByteBuffer;

import com.thinkerwolf.mimo.channel.ChannelProcessorContext;
import com.thinkerwolf.mimo.channel.event.ChannelEvent;
import com.thinkerwolf.mimo.channel.event.MessageEvent;
import com.thinkerwolf.mimo.processor.SimpleChannelInboundProcessor;

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

	@Override
	public void channelConnected(ChannelProcessorContext ctx, ChannelEvent event) {
		System.out.println("Channel Connected " + event.channel());
	}

	@Override
	public void channelAccepted(ChannelProcessorContext ctx, ChannelEvent event) {
		System.out.println("Channel Accepted " + event.channel());
		super.channelAccepted(ctx, event);
	}
}
