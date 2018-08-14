package com.mimo.test;

import com.mimo.bootstrap.ServerBootstrap;
import com.mimo.channel.RunLoopGroup;
import com.mimo.channel.nio.NioRunLoopGroup;
import com.mimo.channel.nio.NioServerSocketChannel;
import com.mimo.test.processor.TestServerChannelInbound;
import com.mimo.test.processor.TestServerChannelOutbound;

public class ServerBootstrapTest {

	public static void start() {
		ServerBootstrap bootstrap = new ServerBootstrap();
		RunLoopGroup group = new NioRunLoopGroup();
		bootstrap.group(group).channel(NioServerSocketChannel.class).channelInitialize(channel -> {
			channel.chain().addLast("inbound", new TestServerChannelInbound());
			channel.chain().addLast("outbound", new TestServerChannelOutbound());
		});
		bootstrap.bind(8088);
	}

	public static void main(String[] args) {
		start();
	}
}
