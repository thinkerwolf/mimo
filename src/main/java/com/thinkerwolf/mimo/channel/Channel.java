package com.thinkerwolf.mimo.channel;

import java.io.IOException;
import java.net.SocketAddress;

import com.thinkerwolf.mimo.concurrent.ChannelFuture;

public interface Channel {

	void register(RunLoop runLoop, ChannelFuture future);

	void connect(SocketAddress localAddress, SocketAddress remoteAddress);

	void write(Object obj);

	void writeAndFlush(Object obj);

	void flush();

	ChannelProcessorChain chain();

	void writeInner(Object obj, boolean flush);

	UnderLayer underLayer();

	interface UnderLayer {

		void read();

		Channel accept() throws IOException;

		void connect() throws IOException;
		
		void close() throws IOException;
		
		// RunLoop runLoop();

	}
}
