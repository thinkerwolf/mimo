package com.thinkerwolf.mimo.concurrent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public abstract class AbstractFuture<V> implements Future<V> {

	public static final Object STATE_FINISH = new Object();
	public static final Object STATE_START = new Object();

	private V result;
	private volatile int waiters;
	private Object waitState;

	@Override
	public boolean cancel(boolean b) {
		return false;
	}

	@Override
	public boolean isCancelled() {
		return false;
	}

	@Override
	public synchronized boolean isDone() {
		return this.waitState == STATE_FINISH;
	}

	@Override
	public V get() throws InterruptedException, ExecutionException {
		return result;
	}

	@Override
	public V get(long paramLong, TimeUnit paramTimeUnit)
			throws InterruptedException, ExecutionException, TimeoutException {
		paramTimeUnit.wait(paramLong);
		return result;
	}

	@Override
	public void setSuccess(V result) {
		this.result = result;
		this.waitState = STATE_FINISH;
		checkNotifyWaiters();
	}

	@Override
	public synchronized Future<V> sync() throws InterruptedException {
		if (isDone()) {
			return this;
		}
		if (Thread.interrupted()) {
			throw new InterruptedException("Thread " + Thread.currentThread().getName() + " is interrupted");
		}
		while (!isDone()) {
			this.waiters++;
			try {
				wait();
			} catch (Exception e) {

			} finally {
				this.waiters--;
			}
		}
		return this;
	}

	private synchronized void checkNotifyWaiters() {
		if (waiters > 0) {
			notifyAll();
		}
	}

	@Override
	public void setFailure(V result) {
		
	}
	
	

}
