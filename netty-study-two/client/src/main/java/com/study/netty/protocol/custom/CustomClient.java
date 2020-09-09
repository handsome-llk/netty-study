package com.study.netty.protocol.custom;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.study.netty.custom.handler.common.NettyConstant;
import com.study.netty.custom.handler.decoder.NettyMessageDecoder;
import com.study.netty.custom.handler.encoder.NettyMessageEncoder;
import com.study.netty.custom.handler.handshake.LoginAuthReqHandler;
import com.study.netty.custom.handler.heartbeat.HeartBeatReqHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

public class CustomClient {

	private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
	EventLoopGroup group = new NioEventLoopGroup();
	
	public void connect() throws InterruptedException {
		try {
			Bootstrap b = new Bootstrap();
			b.group(group)
				.channel(NioSocketChannel.class)
				.option(ChannelOption.TCP_NODELAY, true)
				.handler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline()
							.addLast("MessageDecoder", new NettyMessageDecoder(1024 * 1024, 4, 4))
							.addLast("MessageEncoder", new NettyMessageEncoder())
							.addLast("readTimeoutHandler", new ReadTimeoutHandler(10))
							.addLast("LoginAuthHandler", new LoginAuthReqHandler())
							.addLast("HeartBeatHandler", new HeartBeatReqHandler());
					}
					
				});
			
			// 发起异步连接操作
			ChannelFuture future = b.connect(
					new InetSocketAddress(NettyConstant.REMOTEIP, NettyConstant.PORT),
					new InetSocketAddress(NettyConstant.LOCALIP, NettyConstant.LOCAL_PORT)).sync();
			
			future.channel().closeFuture().sync();
		} finally {
			executor.execute(new Runnable() {

				@Override
				public void run() {
					try {
						TimeUnit.SECONDS.sleep(5);
						try {
							// 发起重连操作
							connect();
						} catch (Exception e) {
							e.printStackTrace();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
			});
		}
	}
	
}
