package com.study.netty.channel;

public class README_Channel {

	/**
	 * netty权威指南Channel源码分析的类：
	 * 
	 * 主要针对NioSocketChannel和NioServerSocketChannel展开，这是两个最主要的类。
	 * 
	 * AbstractChannel
	 * AbstractNioChannel
	 * AbstractNioByteChannel 
	 * AbstractNioMessageChannel
	 * AbstractNioMessageServerChannel
	 * NioServerSocketChannel
	 * NioSocketChannel
	 * 
	 */
	
	/**
	 * netty应该算是nio框架，它将nio包装的更好使用
	 * 
	 * 将Channel注册到EventLoop时，需要指定监听的网络操作为来表示Channel对哪几类网络事件感兴趣，具体的定义如下：
	 * java.nio.channels.SelectKey下
	 * OP_READ = 1 << 0; 读操作位
	 * OP_WRITE = 1 << 2; 写操作位
	 * OP_CONNECT = 1 << 3; 客户端连接服务端操作位
	 * OP_ACCEPT = 1 << 4; 服务端接收客户端连接操作位
	 * 
	 * 而在AbstractNioChannel 中doRegister()方法中，将Channel注册到EventLoop时注册的是0，说明对任何事件都不感兴趣，仅仅完成注册操作
	 * 
	 */
	
	/**
	 * AbstractNioChannel核心api：
	 * doRegister() -> 将channel注册到eventLoop上
	 * doBeginRead() -> 按我的理解，就是在感兴趣的操作位中加入读操作位
	 * 
	 */
	
	/**
	 * AbstractNioByteChannel
	 * 
	 * 其中有个方法clearOpWrite()，这个方法应该是清除读操作的意思
	 * 其中有个位运算符~，是将所有位置0变成1，1变成0。（反码：整数反码等于原码，负数反码是除最高位外，其余位置0变成1，1变成0）
	 * 知道这个就知道clearOpWrite()是如何清除读操作位的
	 * 
	 * 核心方法doWrite
	 * 如果SelectionKey的OP_WRITE操作位被设置，多路复用器会不断轮询对应的Channel用于处理没有发送完成的半包消息，
	 * 直到清除SelectionKey的OP_WRITE操作位。因此设置了OP_WRITE操作位后，就不需要启动独立的Runnable来负责发送半包消息了。
	 * 如果没有设置OP_WRITE操作位，需要启动独立的Runnable，将其加入到EventLoop中执行，由Runnable负责半包消息的发送，它的实现
	 * 很简单，就是调用flush()方法来发送缓冲数组中的消息。
	 * 
	 */
	
	/**
	 * AbstractNioMessageChannel
	 * 
	 * 只有一个方法：doWrite
	 * 
	 */
	
	/**
	 * AbstractNioMessageServerChannel
	 * 
	 * 这个实现很简单，它定义了一个EventLoopGroup类型的childGoup，用于给新接入的客户端NioSocketChannel分配EventLoop。
	 * 每当服务端介入一个新的客户端连接NioSocketChannel时，都会调用childEventLoopGroup方法获取EventLoopGroup线程组，用于
	 * 给NioSocketChannel分配Reactor线程EventLoop。
	 * 
	 */
	
}
