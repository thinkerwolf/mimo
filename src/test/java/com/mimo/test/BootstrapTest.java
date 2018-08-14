package com.mimo.test;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import com.mimo.bootstrap.Bootstrap;
import com.mimo.channel.Channel;
import com.mimo.channel.RunLoopGroup;
import com.mimo.channel.nio.NioRunLoopGroup;
import com.mimo.channel.nio.NioSocketChannel;
import com.mimo.concurrent.ChannelFuture;
import com.mimo.test.processor.TestClientChannelInbound;
import com.mimo.test.processor.TestClientChannelOutbound;
import com.mimo.util.NetUtils;

public class BootstrapTest {

	public static void start() throws InterruptedException {
		Bootstrap bootstrap = new Bootstrap();
		RunLoopGroup group = new NioRunLoopGroup(2);
		bootstrap.channel(NioSocketChannel.class);
		bootstrap.channelInitialize(channel -> {
            channel.chain().addLast("inbound", new TestClientChannelInbound());
            channel.chain().addLast("outbound", new TestClientChannelOutbound());
        });
		bootstrap.group(group);
		ChannelFuture future = bootstrap
				.connect(new InetSocketAddress(NetUtils.getLocalhostAddress().getHostAddress(), 8088));
		future.sync();
		Channel channel = future.channel();
		channel.writeAndFlush("cccccccccc*********&&&&&&&&&)");
		channel.writeAndFlush("$FJIJIP))(I)NJLKNIHO");
		// Thread.sleep(1000);
	}

	public static void start(String host, int port, String text) throws InterruptedException {
		Bootstrap bootstrap = new Bootstrap();
		RunLoopGroup group = new NioRunLoopGroup(2);
		bootstrap.channel(NioSocketChannel.class);
		bootstrap.group(group);
		ChannelFuture future = bootstrap.connect(host, port);
		future.sync();
		Channel channel = future.channel();
		ByteBuffer buf = ByteBuffer.wrap(text.getBytes());
		channel.writeAndFlush(buf);
	}

	public static void main(String[] args) {
		try {
			start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
