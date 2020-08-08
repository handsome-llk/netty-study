package com.study.netty.netty.fixedlength;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class FixedLengthServerHandler extends ChannelHandlerAdapter {

		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) {
			String body = (String) msg;
			System.out.println("Receive client : [" + body + "]");
			System.out.println(body.length());
			ctx.writeAndFlush(Unpooled.copiedBuffer(body.getBytes()));
		}
		
		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
			cause.printStackTrace();
			ctx.close();
		}
	
}
