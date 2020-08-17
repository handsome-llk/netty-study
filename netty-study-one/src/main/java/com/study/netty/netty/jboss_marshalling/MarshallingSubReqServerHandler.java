package com.study.netty.netty.jboss_marshalling;

import com.study.netty.netty.serializable.SubscribeReq;
import com.study.netty.netty.serializable.SubscribeResp;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class MarshallingSubReqServerHandler extends ChannelHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		SubscribeReq req = (SubscribeReq) msg;
		if ("Lilinfeng".equalsIgnoreCase(req.getUserName())) {
			System.out.println("Service accipt client subscribe req : [" + req.toString() + "]");
			ctx.writeAndFlush(resp(req.getSubReqID()));
		} else {
			System.out.println("error user");
		}
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		System.out.println("complete");
	}
	
	private SubscribeResp resp(int subReqID) {
		SubscribeResp resp = new SubscribeResp();
		resp.setSubReqID(subReqID);
		resp.setRespCode(0);
		resp.setDesc("Netty book order succeed, 3 days later, sent to the designated address");
		return resp;
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
	
}
