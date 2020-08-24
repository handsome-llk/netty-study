package com.study.netty.protocol.http.file;

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
import io.netty.handler.stream.ChunkedWriteHandler;

public class HttpFileServer {

//	public static final String DEFAULT_URL = "/src/com/phei/netty/";
	public static final String DEFAULT_URL = "/src/main/java/com/study/netty";
	
	public void run(final int port, final String url) throws InterruptedException {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.childHandler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						// http 请求消息解码器
						ch.pipeline().addLast("http-decoder", new HttpRequestDecoder());
						// 将多个消息转换成单一的FullHttpRequest或FullHttpResponse。http解码器在每个http消息中会生成多个消息对象
						ch.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65536));
						// http 响应消息编码器
						ch.pipeline().addLast("http-ecnoder", new HttpResponseEncoder());
						// 支持异步发送大的码流（例如大的文件传输），但不占用过多的内存，防止发生java内存溢出错误
						ch.pipeline().addLast("http-chunked", new ChunkedWriteHandler());
						ch.pipeline().addLast("fileServerHandler", new HttpFileServerHandler(url));
					}
					
				});
			
			String host = "127.0.0.1";
//			String host = "10.8.3.152";
//			String host = "192.168.1.102";
			ChannelFuture f = b.bind(host, port).sync();
			System.out.println("HTTP 文件目录服务器启动，网址是 : " + host + ":" + port + url);
			
			f.channel().closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
	}
	
}
