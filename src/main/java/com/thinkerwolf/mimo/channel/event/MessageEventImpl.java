package com.thinkerwolf.mimo.channel.event;

import com.thinkerwolf.mimo.channel.Channel;

public class MessageEventImpl implements MessageEvent {

	private Object message;

	private Channel channel;

	public MessageEventImpl(Object message, Channel channel) {
		this.message = message;
		this.channel = channel;
	}

	@Override
	public Channel channel() {
		return channel;
	}

	@Override
	public Object message() {
		return message;
	}

}
