package com.mimo.channel;

public class Channels {


	public static void fireMessageReceived(ChannelProcessorChain chain, Channel channel, Object object) {
		chain.sendInbound(new MessageEventImpl(object, channel));
	}

	public static void fireMessageSended() {

	}
	
	public static void fireMessageWriteComplete(ChannelProcessorChain chain, Channel channel, Object object) {
		chain.sendInbound(new MessageEventImpl(object, channel));
	}
	
	public static void fireMessageWrite(ChannelProcessorChain chain, Channel channel, Object object, boolean flush) {
		chain.sendOutbound(new WriteEventImpl(object, channel, flush));
	}
	
	public static void fireExceptionCaught(ChannelProcessorChain chain, Throwable etx, boolean inbound) {
		//chain.sendException();
        chain.sendException(inbound, etx);
	}
	
	public static void fireChannelConnected() {

	}

	public static void fireChannelAccepted() {

	}

}
