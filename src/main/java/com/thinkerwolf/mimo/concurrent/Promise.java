package com.thinkerwolf.mimo.concurrent;

public interface Promise<V> extends Future<V> {

	Promise<V> setSuccess(V result);

	Promise<V> setFailure(V result);
	
	@Override
	Promise<V> sync() throws InterruptedException;

	@Override
	Promise<V> addListener(GenericFutureListener<? extends Future<? super V>> listener);

	@SuppressWarnings("unchecked")
	@Override
	Promise<V> addListeners(GenericFutureListener<? extends Future<? super V>>... listeners);

	@Override
	Promise<V> removeListener(GenericFutureListener<? extends Future<? super V>> listener);

	@SuppressWarnings("unchecked")
	@Override
	Promise<V> removeListeners(GenericFutureListener<? extends Future<? super V>>... listeners);
}
