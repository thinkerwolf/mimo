package com.thinkerwolf.mimo.channel;

import com.thinkerwolf.mimo.concurrent.DefaultPromise;

public class DefaultChannelPromise extends DefaultPromise<Void> implements ChannelPromise {

	private Channel channel;

	public DefaultChannelPromise(Channel channel) {
		super();
		this.channel = channel;
	}

	@Override
	public Channel channel() {
		return channel;
	}

}
