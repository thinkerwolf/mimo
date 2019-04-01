package com.thinkerwolf.mimo.test;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import com.thinkerwolf.mimo.bootstrap.Bootstrap;
import com.thinkerwolf.mimo.channel.Channel;
import com.thinkerwolf.mimo.channel.RunLoopGroup;
import com.thinkerwolf.mimo.channel.nio.NioRunLoopGroup;
import com.thinkerwolf.mimo.channel.nio.NioSocketChannel;
import com.thinkerwolf.mimo.concurrent.ChannelFuture;
import com.thinkerwolf.mimo.test.processor.TestClientChannelInbound;
import com.thinkerwolf.mimo.test.processor.TestClientChannelOutbound;
import com.thinkerwolf.mimo.util.NetUtils;

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
		
		for (int i = 0; i < 100; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						BootstrapTest.start();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
		

	}

}
