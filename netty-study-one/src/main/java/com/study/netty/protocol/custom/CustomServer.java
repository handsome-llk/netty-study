package com.study.netty.protocol.custom;

import com.study.netty.custom.handler.common.NettyConstant;
import com.study.netty.custom.handler.decoder.NettyMessageDecoder;
import com.study.netty.custom.handler.encoder.NettyMessageEncoder;
import com.study.netty.custom.handler.handshake.LoginAuthRespHandler;
import com.study.netty.custom.handler.heartbeat.HeartBeatRespHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class CustomServer {

	public void bind() throws InterruptedException {
		// 配置服务端的nio线程组
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		ServerBootstrap b = new ServerBootstrap();
		b.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel.class)
			.option(ChannelOption.SO_BACKLOG, 100)
			.handler(new LoggingHandler(LogLevel.INFO))
			.childHandler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline()
						.addLast("nettyMessageDecoder", new NettyMessageDecoder(1024 * 1024, 4, 4))
						.addLast("nettyMessageEncoder", new NettyMessageEncoder())
						.addLast("readTimeoutHandler", new ReadTimeoutHandler(50))
						.addLast("loginAuthRespHandler", new LoginAuthRespHandler())
						.addLast("heartBeatHandler", new HeartBeatRespHandler());
				}
				
			});
		
		// 绑定端口，同步等待成功
		b.bind(NettyConstant.REMOTEIP, NettyConstant.PORT).sync();
		System.out.println("Netty server start ok : " 
				+ (NettyConstant.REMOTEIP + " : " + NettyConstant.PORT));
	}
	
}
