package com.thinkerwolf.mimo.concurrent;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings({ "rawtypes", "unchecked" })
public abstract class AbstractFuture<V> implements Future<V> {

	public static final Logger logger = LoggerFactory.getLogger(AbstractFuture.class);

	public static final Object SUCCESS = new Object();
	public static final Object FAIL = new Object();
	public static final Object UNCANCELLABLE = new Object();

	protected V result;
	protected volatile int waiters;
	protected Object waitState;
	protected GenericFutureListener[] listenerArr;

	public AbstractFuture() {
		this.listenerArr = new GenericFutureListener[0];
		this.waitState = UNCANCELLABLE;
	}

	@Override
	public boolean cancel(boolean b) {
		return false;
	}

	@Override
	public boolean isCancelled() {
		return false;
	}

	@Override
	public boolean isSuccess() {
		return this.waitState == SUCCESS;
	}

	@Override
	public synchronized boolean isDone() {
		return this.waitState != UNCANCELLABLE;
	}

	@Override
	public V get() throws InterruptedException, ExecutionException {
		if (isDone()) {
			return result;
		}
		Thread thread = Thread.currentThread();
		thread.wait();
		return result;
	}

	@Override
	public V get(long paramLong, TimeUnit paramTimeUnit)
			throws InterruptedException, ExecutionException, TimeoutException {
		paramTimeUnit.wait(paramLong);
		return result;
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

	@Override
	public V getNow() {
		return result;
	}

	@Override
	public Future<V> addListener(GenericFutureListener<? extends Future<? super V>> listener) {
		if (isDone()) {
			notifyListener(listener);
			return this;
		}
		synchronized (this) {
			GenericFutureListener[] listenerArr = this.listenerArr;
			int length = listenerArr.length;
			listenerArr = Arrays.copyOf(listenerArr, length + 1);
			listenerArr[length] = listener;
			this.listenerArr = listenerArr;
		}
		return this;
	}

	@Override
	public Future<V> addListeners(GenericFutureListener<? extends Future<? super V>>... listeners) {
		if (isDone()) {
			notifyListeners(listeners);
			return this;
		}
		synchronized (this) {
			GenericFutureListener[] listenerArr = this.listenerArr;
			int length = listenerArr.length;
			listenerArr = Arrays.copyOf(listenerArr, length + listeners.length);
			for (int i = 0; i < listeners.length; i++) {
				listenerArr[length + i] = listeners[i];
			}
			this.listenerArr = listenerArr;

			if (logger.isDebugEnabled()) {
				logger.debug("addListeners oldListenerArr : {}, newListenerArr : {}", listenerArr, this.listenerArr);
			}
		}
		return this;
	}

	@Override
	public Future<V> removeListener(GenericFutureListener<? extends Future<? super V>> listener) {
		if (removeCheck()) {
			return this;
		}
		synchronized (this) {
			GenericFutureListener[] listenerArr = this.listenerArr;
			int length = listenerArr.length;
			GenericFutureListener[] newListenerArr = new GenericFutureListener[length];
			int destPos = -1;
			for (int i = 0; i < length; i++) {
				if (listenerArr[i] != listener) {
					System.arraycopy(listenerArr, i, newListenerArr, ++destPos, 1);
				} else {
					listenerArr[i] = null;
				}
			}
			if (destPos != length - 1) {
				GenericFutureListener[] newArr = new GenericFutureListener[destPos + 1];
				System.arraycopy(newListenerArr, 0, newArr, 0, destPos + 1);
				this.listenerArr = newArr;
			} else {
				this.listenerArr = newListenerArr;
			}
		}
		return this;
	}

	@Override
	public Future<V> removeListeners(GenericFutureListener<? extends Future<? super V>>... listeners) {
		if (removeCheck()) {
			return this;
		}
		synchronized (this) {
			GenericFutureListener[] listenerArr = this.listenerArr;
			int length = listenerArr.length;
			GenericFutureListener[] newListenerArr = new GenericFutureListener[length];
			int destPos = -1;
			for (int i = 0; i < listenerArr.length; i++) {
				boolean contain = false;
				for (int j = 0; j < listeners.length; j++) {
					if (listenerArr[i] == listeners[j]) {
						contain = true;
						break;
					}
				}
				if (!contain) {
					System.arraycopy(listenerArr, i, newListenerArr, ++destPos, 1);
				}
			}
			if (destPos != length - 1) {
				GenericFutureListener[] newArr = new GenericFutureListener[destPos + 1];
				System.arraycopy(newListenerArr, 0, newArr, 0, destPos + 1);
				this.listenerArr = newArr;
			} else {
				this.listenerArr = newListenerArr;
			}

			if (logger.isDebugEnabled()) {
				logger.debug("removeListeners oldListenerArr : {}, newListenerArr : {}", listenerArr, this.listenerArr);
			}
		}
		return this;
	}

	private synchronized boolean removeCheck() {
		if (listenerArr.length == 0) {
			return false;
		}
		return true;
	}

	protected void notifyListener(final GenericFutureListener listener) {
		if (isDone()) {
			try {
				listener.operationComplete(this);
			} catch (Exception e) {
				logger.error("listener", e);
				;
			} finally {
				removeListener(listener);
			}
		}
	}

	protected void notifyListeners(GenericFutureListener... listeners) {
		if (!isDone()) {
			return;
		}
		for (GenericFutureListener listener : listeners) {
			if (listener != null) {
				notifyListener(listener);
			}
		}
	}

}
