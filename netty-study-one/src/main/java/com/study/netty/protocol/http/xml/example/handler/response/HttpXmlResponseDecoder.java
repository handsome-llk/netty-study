package com.study.netty.protocol.http.xml.example.handler.response;

import java.util.List;

import org.jibx.runtime.JiBXException;

import com.study.netty.protocol.http.xml.example.handler.AbstractHttpXmlDecoder;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;

public class HttpXmlResponseDecoder extends AbstractHttpXmlDecoder<DefaultFullHttpResponse> {

	public HttpXmlResponseDecoder(Class<?> clazz) {
		this(clazz, false);
	}
	
	public HttpXmlResponseDecoder(Class<?> clazz, boolean isPrintlog) {
		super(clazz, isPrintlog);
	}
	
	@Override
	protected void decode(ChannelHandlerContext ctx, DefaultFullHttpResponse msg, List<Object> out) throws JiBXException {
		HttpXmlResponse resHttpXmlResponse = new HttpXmlResponse(msg, decode0(ctx, msg.content()));
		out.add(resHttpXmlResponse);
	}

}
