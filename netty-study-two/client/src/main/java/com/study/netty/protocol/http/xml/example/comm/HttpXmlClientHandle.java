package com.study.netty.protocol.http.xml.example.comm;

import com.study.netty.json.JsonUtil;
import com.study.netty.protocol.http.xml.example.handler.request.HttpXmlRequest;
import com.study.netty.protocol.http.xml.example.handler.response.HttpXmlResponse;
import com.study.netty.protocol.http.xml.example.pojo.Order;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class HttpXmlClientHandle extends SimpleChannelInboundHandler<HttpXmlResponse> {

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		HttpXmlRequest request = new HttpXmlRequest(null, OrderFactory.create(123));
		ctx.writeAndFlush(request);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
	
	@Override
	protected void messageReceived(ChannelHandlerContext ctx, HttpXmlResponse msg) throws Exception {
		System.out.println("The clien receive response of http header is : "
				+ msg.getHttpResponse().headers().names());
		System.out.println("The client receive response of http body is : "
				+ JsonUtil.getInstance().getJsonByObject(msg.getResult()));
	}

}
