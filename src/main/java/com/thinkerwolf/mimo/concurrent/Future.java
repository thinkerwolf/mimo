package com.thinkerwolf.mimo.concurrent;

import java.util.concurrent.ExecutionException;

/**
 * 扩展Java的Future接口
 * 
 * @author wukai
 *
 * @param <V>
 */
public interface Future<V> extends java.util.concurrent.Future<V> {
	
	Future<V> sync() throws InterruptedException;

	@SuppressWarnings("unchecked")
	Future<V> addListeners(GenericFutureListener<? extends Future<? super V>>... listeners);

	Future<V> addListener(GenericFutureListener<? extends Future<? super V>> listener);

	Future<V> removeListener(GenericFutureListener<? extends Future<? super V>> listener);

	@SuppressWarnings("unchecked")
	Future<V> removeListeners(GenericFutureListener<? extends Future<? super V>>... listeners);

	@Override
	V get() throws InterruptedException, ExecutionException;

	V getNow();
	
	boolean isSuccess();

}
