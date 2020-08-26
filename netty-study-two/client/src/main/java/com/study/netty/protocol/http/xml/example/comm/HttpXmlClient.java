package com.study.netty.protocol.http.xml.example.comm;

import java.net.InetSocketAddress;

import com.study.netty.protocol.http.xml.example.handler.request.HttpXmlRequestEncoder;
import com.study.netty.protocol.http.xml.example.handler.response.HttpXmlResponseDecoder;
import com.study.netty.protocol.http.xml.example.pojo.Order;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseDecoder;

public class HttpXmlClient {

	public void connect(int port, String host) throws InterruptedException {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group).channel(NioSocketChannel.class)
				.option(ChannelOption.TCP_NODELAY, true)
				.handler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						// 将二进制码流解码成为http的应答消息
						ch.pipeline().addLast("http-decoder", new HttpResponseDecoder());
						// 将1个http请求消息的多个部分合并成一条完整的http消息
						ch.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65536));
						// XML 解码器
						ch.pipeline().addLast("xml-decoder", new HttpXmlResponseDecoder(Order.class, true));
						ch.pipeline().addLast("http-encoder", new HttpRequestEncoder());
						ch.pipeline().addLast("xml-encoder", new HttpXmlRequestEncoder());
						ch.pipeline().addLast("xmlClientHandler", new HttpXmlClientHandle());
					}
					
				});
			
			ChannelFuture f = b.connect(new InetSocketAddress(port)).sync();
			
			f.channel().closeFuture().sync();
		
		} finally {
			group.shutdownGracefully();
		} 
	}
	
}
