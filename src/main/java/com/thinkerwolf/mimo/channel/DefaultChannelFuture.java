package com.thinkerwolf.mimo.channel;

import com.thinkerwolf.mimo.concurrent.AbstractFuture;

public class DefaultChannelFuture extends AbstractFuture<Void> implements ChannelFuture {

	private Channel channel;

	public DefaultChannelFuture(Channel channel) {
		this.channel = channel;
	}

	@Override
	public Channel channel() {
		return channel;
	}

}
