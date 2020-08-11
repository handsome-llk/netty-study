package com.study.netty.netty.protobuf.test;

import java.util.ArrayList;
import java.util.List;

import com.google.protobuf.InvalidProtocolBufferException;
import com.study.netty.netty.protobuf.SubscribeReqProto;

public class TestSubscribeReqProto {

	public static byte[] encode(SubscribeReqProto.SubscribeReq req) {
		return req.toByteArray();
	}
	
	public static SubscribeReqProto.SubscribeReq decode(byte[] body) throws InvalidProtocolBufferException {
		return SubscribeReqProto.SubscribeReq.parseFrom(body);
	}
	
	public static SubscribeReqProto.SubscribeReq createSubscribeReq() {
		SubscribeReqProto.SubscribeReq.Builder builder = SubscribeReqProto.SubscribeReq.newBuilder();
		builder.setSubReqID(1);
		builder.setUserName("Lilinfeng");
		builder.setProductName("Netty Book");
		List<String> address = new ArrayList<>();
		address.add("NanJing YuHuaTai");
		address.add("BeiJing LiuLiChang");
		address.add("ShenZhen HongShuLin");
		builder.addAllAddress(address);
		return builder.build();
	}
	
}
