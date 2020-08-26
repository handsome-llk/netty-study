package com.study.netty.protocol.http.xml.example.comm;

import java.net.InetSocketAddress;

import com.study.netty.protocol.http.xml.example.handler.request.HttpXmlRequestDecoder;
import com.study.netty.protocol.http.xml.example.handler.response.HttpXmlResponseEncoder;
import com.study.netty.protocol.http.xml.example.pojo.Order;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

public class HttpXmlServer {

	public void run(final int port) throws InterruptedException {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast("http-decoder", new HttpRequestDecoder());
						ch.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65536));
						ch.pipeline().addLast("xml-decoder", new HttpXmlRequestDecoder(Order.class, true));
						ch.pipeline().addLast("http-encoder", new HttpResponseEncoder());
						ch.pipeline().addLast("xml-encoder", new HttpXmlResponseEncoder());
						ch.pipeline().addLast("xmlServerHandler", new HttpXmlServerHandler());
					}
					
				});
			
			ChannelFuture future = b.bind(new InetSocketAddress(port)).sync();
			System.out.println("http 订单服务器启动，网址是 ： " + "http://localhost:" + port);
			future.channel().closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
	
}
