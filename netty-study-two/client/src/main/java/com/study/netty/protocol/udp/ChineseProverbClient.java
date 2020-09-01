package com.study.netty.protocol.udp;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;

public class ChineseProverbClient {

	public void run(int port) throws InterruptedException {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group)
				.channel(NioDatagramChannel.class)
				.option(ChannelOption.SO_BROADCAST, true)
				.handler(new ChineseProverbClientHandler());
			
			Channel ch = b.bind(0).sync().channel();
			
			// 由于不需要和服务端建立链路，UDP Channel创建完成之后，客户端就要主动发送广播消息；TCP客户端是
			// 在客户端和服务端链路建立成功之后有客户端的业务handler发送消息。给我的感觉是tcp有3次握手，而udp只有一次的样子
			ch.writeAndFlush(
					new DatagramPacket(Unpooled.copiedBuffer("谚语字典查询?", CharsetUtil.UTF_8),
					new InetSocketAddress("255.255.255.255", port))).sync();
			
			if (!ch.closeFuture().await(15000)) {
				System.out.println("查询超时");
			}
			
		} finally {
			group.shutdownGracefully();
		}
	}
	
}
