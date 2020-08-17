package com.study.netty.netty.jboss_marshalling;

import com.study.netty.netty.serializable.SubscribeReq;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class MarshallingSubReqClientHandler extends ChannelHandlerAdapter {

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		for (int i = 0; i < 10; i++) {
			ctx.write(subReq(i));
		}
		ctx.flush();
	}
	
	private SubscribeReq subReq(int i) {
		SubscribeReq req = new SubscribeReq();
		req.setAddress("南京市江宁区方山国家地质公园");
		req.setPhoneNumber("13812345678");
		req.setProductName("Netty Book For Marshalling");
		req.setSubReqID(i);
		if (i % 3 == 0) {
			req.setUserName("李林峰");
		} else {
			req.setUserName("Lilinfeng");
		}
		return req;
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		System.out.println("Receive server response : [" + msg + "]");
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		System.out.println("complete");
		ctx.flush();
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
	
}
