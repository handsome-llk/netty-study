package com.study.netty.study.unsafe;

public class README_UNSAFE_METHOD {

	/**
	 * unsafe相关方法说明：
	 * 
	 * 1、invoker()
	 * 返回默认使用的ChannelHandlerInvoker
	 * 
	 * 2、localAddress()
	 * 返回本地绑定的Socket地址
	 * 
	 * 3、remoteAddress()
	 * 返回通信对端的Socket地址
	 * 
	 * 4、register(ChannelPromise promise)
	 * 注册Channel到多路复用器上，一旦注册操作完成，通知ChannelFuture
	 * 
	 * 5、bind(SocketAddress localAddress, ChannelPromise promise)
	 * 绑定指定的本地地址localAddress到当前的Channel上，一旦完成，通知ChannelFuture
	 * 
	 * 6、connect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise)
	 * 绑定本地的localAddress之后，连接服务端，一旦操作完成，通知ChannelFuture
	 * 
	 * 7、disconnect(ChannelPromise promise)
	 * 断开Channel的连接，一旦完成，通知ChannelFuture
	 * 
	 * 8、close(ChannelPromise promise)
	 * 关闭Channel的连接，一旦完成，通知ChannelFuture
	 * 
	 * 9、closeForciby()
	 * 强制立即关闭连接
	 * 
	 * 10、beginRead()
	 * 设置网络操作位为读，用于读取消息
	 * 
	 * 11、write(Object msg, ChannelPromise)
	 * 发送消息，一旦完成，通知ChannelFuture
	 * 
	 * 12、flush()
	 * 将发送缓冲数组中的消息写入到Channel中
	 * 
	 * 13、voidPromise()
	 * 返回一个特殊的可重用和传递的ChannelPromise，它不用于操作成功或者失败的通知器，仅仅作为一个容器被使用
	 * 
	 * 14、outboundBuffer()
	 * 返回消息发送缓冲区
	 * 
	 * 
	 */
	
}
