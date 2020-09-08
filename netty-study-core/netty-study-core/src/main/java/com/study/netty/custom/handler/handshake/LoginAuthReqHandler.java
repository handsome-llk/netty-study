package com.study.netty.custom.handler.handshake;

import com.study.netty.custom.handler.enums.MessageType;
import com.study.netty.custom.pojo.message.Header;
import com.study.netty.custom.pojo.message.NettyMessage;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class LoginAuthReqHandler extends ChannelHandlerAdapter {

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		ctx.writeAndFlush(buildLoginReq());
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		NettyMessage message = (NettyMessage) msg;
		// 如果是握手应答消息，需要判断是否认证成功
		if (message.getHeader() != null && message.getHeader().getType() == MessageType.LOGIN_RESP.value()) {
			byte loginResult = (byte) message.getBody();
			if (loginResult != (byte) 0) {
				// 握手失败，关闭连接
				ctx.close();
			} else {
				System.out.println("Login is ok : " + message);
				ctx.fireChannelRead(msg);
			}
		} else {
			ctx.fireChannelRead(msg);
		}
	}
	
	private NettyMessage buildLoginReq() {
		NettyMessage message = new NettyMessage();
		Header header = new Header();
		header.setType(MessageType.LOGIN_REQ.value());
		message.setHeader(header);
		return message;
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		ctx.fireExceptionCaught(cause);
	}
	
}
