package com.mimo.concurrent;

public interface ChannelFutureListener extends GenericFutureListener<ChannelFuture> {

	void operationComplete(ChannelFuture future);

}
