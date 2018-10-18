package com.mimo.test;

import com.mimo.test.processor.TestServerChannelInbound;
import com.mimo.test.processor.TestServerChannelOutbound;
import com.thinkerwolf.mimo.bootstrap.ServerBootstrap;
import com.thinkerwolf.mimo.channel.RunLoopGroup;
import com.thinkerwolf.mimo.channel.nio.NioRunLoopGroup;
import com.thinkerwolf.mimo.channel.nio.NioServerSocketChannel;

public class ServerBootstrapTest {

	public static void start() {
		ServerBootstrap bootstrap = new ServerBootstrap();
		RunLoopGroup bossGroup = new NioRunLoopGroup(2);
		RunLoopGroup workerGroup = new NioRunLoopGroup(10);
		bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).channelInitialize(channel -> {
			channel.chain().addLast("inbound", new TestServerChannelInbound());
			channel.chain().addLast("outbound", new TestServerChannelOutbound());
		});
		bootstrap.bind(8088);
	}

	public static void main(String[] args) {
		start();
	}
}
