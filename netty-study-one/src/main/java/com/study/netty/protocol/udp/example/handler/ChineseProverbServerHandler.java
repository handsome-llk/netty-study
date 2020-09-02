package com.study.netty.protocol.udp.example.handler;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

public class ChineseProverbServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {

	private static final String[] DICTIONARY = {"只要功夫深，铁棒磨成针。",
			"旧时王谢堂前燕，飞入寻常百姓家",
			"洛阳亲友如相问，一片冰心在玉壶",
			"一寸光阴一寸金，寸金难买寸光阴",
			"老骥伏枥，志在千里。烈士暮年，壮心不已！"};
	
	private AtomicInteger counter = new AtomicInteger();
	
	private String nextQuote() {
//		int quoteId = ThreadLocalRandom.current().nextInt(DICTIONARY.length);
		int index = counter.incrementAndGet() % DICTIONARY.length;
		return DICTIONARY[index];
	}
	
	@Override
	protected void messageReceived(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
		String req = packet.content().toString(CharsetUtil.UTF_8);
		System.out.println(req);
		if ("谚语字典查询?".equals(req)) {
			ctx.writeAndFlush(new DatagramPacket(
					Unpooled.copiedBuffer("谚语查询结果:" + nextQuote(), CharsetUtil.UTF_8),
					packet.sender()));
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		ctx.close();
		cause.printStackTrace();
	}

}