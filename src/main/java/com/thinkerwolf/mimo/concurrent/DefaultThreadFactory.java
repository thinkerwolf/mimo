package com.thinkerwolf.mimo.concurrent;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class DefaultThreadFactory implements ThreadFactory {

	private static final String DEFAULT_NAME_PREFIX = "mimo-thread";
	public static final DefaultThreadFactory INSTANCE = new DefaultThreadFactory(DEFAULT_NAME_PREFIX);

	private String prefix = DEFAULT_NAME_PREFIX;
	private AtomicInteger ID = new AtomicInteger();

	public DefaultThreadFactory(String prefix) {
		this.prefix = prefix == null ? DEFAULT_NAME_PREFIX : prefix;
		this.ID = new AtomicInteger(1);
	}
	
	public DefaultThreadFactory() {
		this(DEFAULT_NAME_PREFIX);
	}

	public Thread newThread(Runnable r) {
		Thread thread = new Thread(r);
		thread.setName(prefix + "-" + ID.incrementAndGet());
		thread.setDaemon(false);
		thread.setPriority(Thread.NORM_PRIORITY);
		return thread;
	}

}
