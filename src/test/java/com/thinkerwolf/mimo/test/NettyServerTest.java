package com.thinkerwolf.mimo.test;

import java.nio.charset.Charset;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * Netty 服务器
 * 
 * @author wukai
 *
 */
public class NettyServerTest {

	public static void start(int port) throws Exception {
		ServerBootstrap sb = new ServerBootstrap();
		NioEventLoopGroup bg = new NioEventLoopGroup(2);
		NioEventLoopGroup wg = new NioEventLoopGroup(3);

		sb.group(bg, wg).channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {

			@Override
			protected void initChannel(SocketChannel ch) throws Exception {
				ChannelPipeline pipeline = ch.pipeline();
				pipeline.addLast("decoder", new StringDecoder(Charset.forName("UTF-8")));
				pipeline.addLast("encode", new StringEncoder(Charset.forName("UTF-8")));
				pipeline.addLast("handler", new SimpleChannelInboundHandler<String>() {
					@Override
					protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
						System.out.println("messageReceived : " + msg);
						ctx.channel().writeAndFlush("423212123");
					}
				});

			}
		});

		sb.bind(port);

	}

	public static void main(String[] args) {
		try {
			start(8088);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
