package com.thinkerwolf.mimo.concurrent;

public interface Future<V> extends java.util.concurrent.Future<V> {

	void addListener();

	Future<V> sync() throws InterruptedException;
	
	void setSuccess(V result);
	
	void setFailure(V result);
	
}
