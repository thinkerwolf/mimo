package com.thinkerwolf.mimo.concurrent;

public interface GenericFutureListener<F extends Future<?>> {
	void operationComplete(F future) throws Exception;
}
