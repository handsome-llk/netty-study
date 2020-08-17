package com.study.netty.netty.jboss_marshalling;

public class README {
	/**
	 * 我这里出现了一个问题：就是客户端连上服务器后，服务器channelRead没有执行。
	 * 我尝试将channel.pipeline中的marshalling序列化编解码接口换成netty的序列化编解码接口后，服务器channelRead成功执行。
	 * 初步排查发现jboss marshalling的序列化编解码接口写的有问题
	 *
	 * 
	 * 很气，没找出问题，这个例子目前还是不能正常执行channelRead。应该是编解码那一块有问题
	 * 
	 * 
	 */
}
