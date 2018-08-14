package com.mimo.channel.event;

import com.mimo.channel.Channel;

/**
 * 写事件
 */
public class WriteEventImpl implements WriteEvent {

	private Channel channel;

	private Object obj;

	private boolean flush;

	public WriteEventImpl(Object obj, Channel channel, boolean flush) {
		this.channel = channel;
		this.obj = obj;
		this.flush = flush;
	}

	@Override
	public Channel channel() {
		return channel;
	}

	@Override
	public Object writeObject() {
		return obj;
	}

	@Override
	public boolean isFlush() {
		return flush;
	}


}
