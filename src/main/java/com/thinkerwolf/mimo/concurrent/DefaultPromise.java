package com.thinkerwolf.mimo.concurrent;

/**
 * 
 * @author wukai
 *
 * @param <V>
 */
@SuppressWarnings({ "unchecked" })
public class DefaultPromise<V> extends AbstractFuture<V> implements Promise<V> {

	@Override
	public synchronized Promise<V> setSuccess(V result) {
		if (!isDone()) {
			this.result = result;
			this.waitState = SUCCESS;
			notifyListeners(listenerArr);
			checkNotifyWaiters();
			return this;
		}
		return null;
	}

	@Override
	public synchronized Promise<V> setFailure(V result) {
		if (!isDone()) {
			this.result = result;
			this.waitState = FAIL;
			notifyListeners(listenerArr);
			checkNotifyWaiters();
			return this;
		}
		return null;
	}

	@Override
	public Promise<V> sync() throws InterruptedException {
		return (Promise<V>) super.sync();
	}

	@Override
	public Promise<V> addListener(GenericFutureListener<? extends Future<? super V>> listener) {
		return (Promise<V>) super.addListener(listener);
	}

	@Override
	public Promise<V> addListeners(GenericFutureListener<? extends Future<? super V>>... listeners) {
		return (Promise<V>) super.addListeners(listeners);
	}

	@Override
	public Promise<V> removeListeners(GenericFutureListener<? extends Future<? super V>>... listeners) {
		return (Promise<V>) super.removeListeners(listeners);
	}

	@Override
	public Promise<V> removeListener(GenericFutureListener<? extends Future<? super V>> listener) {
		return (Promise<V>) super.removeListener(listener);
	}

	private synchronized void checkNotifyWaiters() {
		if (waiters > 0) {
			notifyAll();
		}
	}

}
