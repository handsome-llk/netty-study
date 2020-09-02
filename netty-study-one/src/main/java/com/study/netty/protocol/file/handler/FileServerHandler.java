package com.study.netty.protocol.file.handler;

import java.io.File;
import java.io.RandomAccessFile;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.DefaultFileRegion;
import io.netty.channel.FileRegion;
import io.netty.channel.SimpleChannelInboundHandler;

public class FileServerHandler extends SimpleChannelInboundHandler<String> {

	// line.separator 是回车
	private static final String CR = System.getProperty("line.separator");
	
	@Override
	protected void messageReceived(ChannelHandlerContext ctx, String msg) throws Exception {
		File file = new File(msg);
		if (file.exists()) {
			if (!file.isFile()) {
				ctx.writeAndFlush("Not a file : " + file + CR);
				System.out.print(CR);
				System.out.println("a");
				return;
			}
			
			ctx.writeAndFlush(file + " " + file.length() + CR);
			RandomAccessFile randomAccessFile = new RandomAccessFile(msg, "r");
			FileRegion region = new DefaultFileRegion(randomAccessFile.getChannel(), 0, randomAccessFile.length());
			ctx.write(region);
			ctx.writeAndFlush(CR);
			randomAccessFile.close();
		
		} else {
			ctx.writeAndFlush("File not found : " + file + CR);
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}
}
