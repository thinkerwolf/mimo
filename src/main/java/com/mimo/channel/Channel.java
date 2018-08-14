package com.mimo.channel;

import java.net.SocketAddress;

import com.mimo.concurrent.ChannelFuture;

public interface Channel {

	void register(RunLoop runLoop, ChannelFuture future);

	void connect(SocketAddress localAddress, SocketAddress remoteAddress);
	
	void write(Object obj);
	
	void writeAndFlush(Object obj);
	
	void flush();
	
	ChannelProcessorChain chain();
	
	void writeInner(Object obj, boolean flush);




}
