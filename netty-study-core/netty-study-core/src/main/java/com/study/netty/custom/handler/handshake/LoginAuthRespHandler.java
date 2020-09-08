package com.study.netty.custom.handler.handshake;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.study.netty.custom.handler.enums.MessageType;
import com.study.netty.custom.pojo.message.Header;
import com.study.netty.custom.pojo.message.NettyMessage;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class LoginAuthRespHandler extends ChannelHandlerAdapter {

	private Map<String, Boolean> nodeCheck = new ConcurrentHashMap<String, Boolean>();
	
	// 可以将127.0.0.1去掉试试
	private String[] whitekList = {"127.0.0.1", "10.8.10.72"};

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		NettyMessage message = (NettyMessage) msg;
		if (message.getHeader() != null &&
				message.getHeader().getType() == MessageType.LOGIN_REQ.value()) {
			String nodeIndex = ctx.channel().remoteAddress().toString();
			NettyMessage loginResp = null;
			// 重复登陆，拒绝
			if (nodeCheck.containsKey(nodeIndex)) {
				loginResp = buildResponse((byte) -1);
			} else {
				InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
				String ip = address.getAddress().getHostAddress();
				boolean isOK = false;
				for (String wip : whitekList) {
					if (wip.equals(ip)) {
						isOK = true;
						break;
					}
				}
				loginResp = isOK ? buildResponse((byte) 0) : buildResponse((byte) -1);
				if (isOK) {
					nodeCheck.put(nodeIndex, true);
				}
			}
				
			System.out.println("The login response is : " 
					+ loginResp 
					+ " body [" 
					+ loginResp.getBody() 
					+ "]");
			ctx.writeAndFlush(loginResp);
		} else {
			ctx.fireChannelRead(msg);
		}
	}
	
	
	private NettyMessage buildResponse(byte result) {
		NettyMessage message = new NettyMessage();
		Header header = new Header();
		header.setType(MessageType.LOGIN_RESP.value());
		message.setHeader(header);
		message.setBody(result);
		return message;
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		// 删除缓存
		nodeCheck.remove(ctx.channel().remoteAddress().toString());
		ctx.close();
		ctx.fireExceptionCaught(cause);
	}
	
}
