package com.study.netty.protocol.http.xml.example.handler.response;

import static io.netty.handler.codec.http.HttpHeaders.setContentLength;
import static io.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import java.util.List;

import com.study.netty.protocol.http.xml.example.handler.AbstractHttpXmlEncoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;

public class HttpXmlResponseEncoder extends AbstractHttpXmlEncoder<HttpXmlResponse> {

	@Override
	protected void encode(ChannelHandlerContext ctx, HttpXmlResponse msg, List<Object> out) throws Exception {
		ByteBuf body = encode0(ctx, msg.getResult());
		FullHttpResponse response = msg.getHttpResponse();
		if (response == null) {
			response = new DefaultFullHttpResponse(HTTP_1_1, OK, body);
		} else {
			response = new DefaultFullHttpResponse(msg.getHttpResponse().getProtocolVersion(), 
					msg.getHttpResponse().getStatus(), 
					body);
		}
		response.headers().set(CONTENT_TYPE, "text/xml");
		setContentLength(response, body.readableBytes());
		out.add(response);
	}

}
