package com.study.netty.netty.fixedlength;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class FixedLengthClientHandler extends ChannelHandlerAdapter {

	private int counter;
	static String FIXED_LENGTH_REQ = "Hi, Lilinfeng. Welcom to Netty.";
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		for (int i = 0; i < 10; i++) {
			if (FIXED_LENGTH_REQ.length() < 200) {
				while (FIXED_LENGTH_REQ.length() < 200) {
					FIXED_LENGTH_REQ += " ";
				}
			}
			ctx.writeAndFlush(Unpooled.copiedBuffer(FIXED_LENGTH_REQ.getBytes()));
		}
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		String body = (String) msg;
		System.out.println("This is " + ++counter + " times receive server : [" + body + "]");
		System.out.println(body.length());
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
