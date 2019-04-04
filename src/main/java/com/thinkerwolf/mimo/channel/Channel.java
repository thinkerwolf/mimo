package com.thinkerwolf.mimo.channel;

import java.io.IOException;
import java.net.SocketAddress;

public interface Channel {

	void register(RunLoop runLoop, ChannelPromise promise);

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

		void register(RunLoop runLoop) throws IOException;

		// RunLoop runLoop();

	}
}
