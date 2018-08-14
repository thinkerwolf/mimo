package com.mimo.concurrent;

import com.mimo.channel.Channel;

public class DefaultChannelFuture extends AbstractFuture<Void> implements ChannelFuture {

	private Channel channel;

	public DefaultChannelFuture(Channel channel) {
		this.channel = channel;
	}

	@Override
	public void addListener() {

	}

	@Override
	public Channel channel() {
		return channel;
	}

}
