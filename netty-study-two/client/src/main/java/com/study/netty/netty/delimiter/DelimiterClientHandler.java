package com.study.netty.netty.delimiter;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class DelimiterClientHandler extends ChannelHandlerAdapter {

	private int counter;
	
	static final String ECHO_REQ = "Hi, Lilinfeng. Welcom to Netty.$_";
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		for (int i = 0; i < 10; i++) {
			ctx.writeAndFlush(Unpooled.copiedBuffer(ECHO_REQ.getBytes()));
		}
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		System.out.println("This is " + ++counter + " times receive server : [" + msg + "]");
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		ctx.flush();
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
	
}