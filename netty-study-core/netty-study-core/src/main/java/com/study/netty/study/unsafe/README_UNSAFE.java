package com.study.netty.study.unsafe;

public class README_UNSAFE {

	/**
	 * Unsafe是Channel的内部类
	 * 
	 * 《netty权威指南》主要讲解了三个子类：
	 * AbstractUnsafe
	 * AbstractNioUnsafe
	 * NioByteUnsafe
	 * 
	 */
	
	/**
	 * ChannelOutboundBuffer 这个类就是环形数组，大概
	 */
	
	/**
	 * AbstractUnsafe
	 * 
	 * register
	 * 
	 * ensureOpen方法判断当前Channel是否打开，如果没有打开则无法注册，直接返回
	 * 
	 */
	
	/**
	 * AbstractUnsafe
	 * 
	 * close(final ChannelPromise promise)
	 * 
	 * 在链路关闭之前需要首先判断是否处于刷新状态，如果处于刷新状态说明还有消息尚未发送出去，需要等到所有消息发送完成再关闭链路，因此，将关闭
	 * 操作封装城Runnable稍后再执行。
	 * 
	 * 如果链路没有处于刷新状态，需要从closeFuture中判断关闭操作是否完成，如果已经完成，不需要重复关闭链路，设置ChannelPromise的操作
	 * 结果围成功并返回。
	 * 
	 * 执行关闭操作，将消息发送缓冲数组设置为空，通知JVM进行内存回收。调用抽象方法doClose关闭链路。如果关闭操作成功，设置ChannelPromise结果
	 * 为成功。如果操作失败，则设置异常对象到ChannelPromise中。
	 * 
	 * 调用ChannelOutboundBuffer的close方法释放缓冲区的消息，随后构造链路关闭通知Runnable放到NioEventLoop中执行。
	 * 
	 * 最后调用deregister方法，将Channel从多路复用器上取消注册。
	 * 
	 */
	
	/**
	 * AbstractUnsafe
	 * 
	 * write(Object msg, ChannelPromise promise)
	 * 
	 * write方法实际上将消息添加到环形发送数组中，并不是真正的写Channel。
	 * 如果Channel没有处于激活状态，说明TCP链路还没有真正建立成功，当前Channel存在以下两种状态：
	 * 1、Channel打开，但是TCP链路尚未建立成功:NOT_YET_CONNECTED_EXCEPTION
	 * 2、Channel已经关闭:CLOSED_CHANNEL_EXCEPTION
	 * 对链路状态进行判断，给ChannelPromise设置对应的异常，然后调用ReferenceCountUtil的release方法释放发送的msg对象
	 * 
	 * 如果链路状态正常，则将发送的msg合promise放入发送缓冲区中（环形数组）。
	 * 
	 */
	
	/**
	 * AbstractUnsafe
	 * 
	 * flush
	 * 
	 * flush方法负责将发送缓冲区中待发送的消息全部写入到Channel中，并发送给通信对方。
	 * 
	 * 首先将发送环形数组的unflushed指针修改为tail，标识本次要发送消息的缓冲区范围，然后调用flush0进行发送，由于flush0代码非常简单，
	 * 看不明白的话可以结合上面的close和write方法一起看看。
	 * 
	 * 
	 */
	
}
