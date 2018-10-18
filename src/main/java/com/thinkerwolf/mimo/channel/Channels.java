package com.thinkerwolf.mimo.channel;

import com.thinkerwolf.mimo.channel.event.ChannelEvent;
import com.thinkerwolf.mimo.channel.event.MessageEventImpl;
import com.thinkerwolf.mimo.channel.event.WriteEventImpl;

public class Channels {

	public static void fireMessageReceived(ChannelProcessorChain chain, Channel channel, Object object) {
		chain.sendInbound(new MessageEventImpl(object, channel));
	}

	public static void fireMessageWriteComplete(ChannelProcessorChain chain, Channel channel, Object object) {
		chain.sendInbound(new MessageEventImpl(object, channel));
	}

	public static void fireMessageWrite(ChannelProcessorChain chain, Channel channel, Object object, boolean flush) {
		chain.sendOutbound(new WriteEventImpl(object, channel, flush));
	}

	public static void fireExceptionCaught(ChannelProcessorChain chain, Throwable etx, boolean inbound) {
		// chain.sendException();
		chain.sendException(inbound, etx);
	}

	public static void fireChannelConnected(ChannelProcessorChain chain, final Channel channel) {
		chain.sendConnected(new ChannelEvent() {
			@Override
			public Channel channel() {
				return channel;
			}
		});
	}

	public static void fireChannelAccepted(ChannelProcessorChain chain, final Channel channel) {
		chain.sendAccepted(new ChannelEvent() {
			@Override
			public Channel channel() {
				return channel;
			}
		});
	}

}
