package com.study.netty.netty.echo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class EchoServerHandler extends ChannelHandlerAdapter {

	int counter = 0;
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		String body = (String) msg;
		System.out.println("This is " + ++counter + " times receive client : [" + body + "]");
//		body += "已阅$_";
		ByteBuf echo = Unpooled.copiedBuffer((body + "已阅1$_").getBytes());
		ctx.writeAndFlush(echo);
		
		echo = Unpooled.copiedBuffer((body + "已阅2$_").getBytes());
		ctx.writeAndFlush(echo);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
	
	
}
