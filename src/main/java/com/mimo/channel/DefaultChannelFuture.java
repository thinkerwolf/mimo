package com.mimo.channel;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 
 * @author wukai
 *
 */
public class DefaultChannelFuture implements ChannelFuture {
	Channel channel;

	public DefaultChannelFuture(Channel channel) {
		this.channel = channel;
	}

	public boolean cancel(boolean mayInterruptIfRunning) {
		return false;
	}

	public boolean isCancelled() {
		return false;
	}

	public boolean isDone() {
		return false;
	}

	public Void get() throws InterruptedException, ExecutionException {
		return null;
	}

	public Void get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		return null;
	}

	public Channel channel() {
		return channel;
	}

}
