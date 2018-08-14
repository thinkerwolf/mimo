package com.mimo.concurrent;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultThreadFactory implements ThreadFactory {

	private static final AtomicInteger AI = new AtomicInteger();
	private static final String DEFAULT_NAME_PREFIX = "mimo_thread";
	public static final DefaultThreadFactory INSTANCE = new DefaultThreadFactory(DEFAULT_NAME_PREFIX);

	private String prefix = DEFAULT_NAME_PREFIX;

	public DefaultThreadFactory(String prefix) {
		this.prefix = prefix == null ? DEFAULT_NAME_PREFIX : prefix;
	}

	public DefaultThreadFactory() {
	}

	public Thread newThread(Runnable r) {
		Thread thread = new Thread(r);
		thread.setName(prefix + "_" + AI.incrementAndGet());
		thread.setDaemon(false);
		thread.setPriority(Thread.NORM_PRIORITY);
		return thread;
	}

}
