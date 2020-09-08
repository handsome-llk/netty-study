package com.study.netty.custom.handler.encoder;

import java.io.IOException;

import org.jboss.marshalling.Marshaller;

import com.study.netty.custom.handler.channel.buffer.ChannelBufferByteOutput;
import com.study.netty.custom.handler.factory.MarshallingCodeCFactory;

import io.netty.buffer.ByteBuf;

public class MarshallingEncoder {

	private static final byte[] LENGTH_PLACEHOLDER = new byte[4];
	Marshaller marshaller;
	
	public MarshallingEncoder() throws IOException {
		marshaller = MarshallingCodeCFactory.buildMarshalling();
	}
	
	protected void encode(Object msg, ByteBuf out) throws IOException {
		try {
			int lengthPos = out.writerIndex();
			out.writeBytes(LENGTH_PLACEHOLDER);
			ChannelBufferByteOutput output = new ChannelBufferByteOutput(out);
			marshaller.start(output);
			marshaller.writeObject(msg);
			marshaller.finish();
			out.setInt(lengthPos, out.writerIndex() - lengthPos - 4);
		} finally {
			marshaller.close();
		}
	}
	
}
