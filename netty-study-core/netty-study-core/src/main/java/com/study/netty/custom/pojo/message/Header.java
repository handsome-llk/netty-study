package com.study.netty.custom.pojo.message;

import java.util.HashMap;
import java.util.Map;

public class Header {

	/**
	 * netty消息的检验码，它由三部分组成
	 * 1、0xabef:固定值，表明该消息时netty协议消息，2个字节
	 * 2、主版本号：1~255，1个字节
	 * 3、次版本号：1~255，1个字节
	 * crcCode = 0xabef + 主版本号 + 次版本号
	 */
	private int crcCode = 0xabef0101;
	// 消息长度
	private int length;
	// 会话id
	private long sessionID;
	// 消息类型
	private byte type;
	// 消息优先级
	private byte priority;
	// 附件
	private Map<String, Object> attachment = new HashMap<>();
	
	public final int getCrcCode() {
		return crcCode;
	}
	public final void setCrcCode(int crcCode) {
		this.crcCode = crcCode;
	}
	public final int getLength() {
		return length;
	}
	public final void setLength(int length) {
		this.length = length;
	}
	public final long getSessionID() {
		return sessionID;
	}
	public final void setSessionID(long sessionID) {
		this.sessionID = sessionID;
	}
	public final byte getType() {
		return type;
	}
	public final void setType(byte type) {
		this.type = type;
	}
	public final byte getPriority() {
		return priority;
	}
	public final void setPriority(byte priority) {
		this.priority = priority;
	}
	public final Map<String, Object> getAttachment() {
		return attachment;
	}
	public final void setAttachment(Map<String, Object> attachment) {
		this.attachment = attachment;
	}
	
	@Override
	public String toString() {
		return "Header [crcCode=" + crcCode + ", length=" + length + ", sessionID=" + sessionID + ", type=" + type
				+ ", priority=" + priority + ", attachment=" + attachment + "]";
	}
	
	
	
}
