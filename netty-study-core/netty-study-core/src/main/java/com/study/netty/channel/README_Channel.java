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
	 * 从发送消息环形数组ChannelOutboundBuffer弹出一条消息，判断该消息是否为空，如果为空，说明消息发送数组中所有待发送的消息都已经发送完成，
	 * 清除半包标识，然后退出循环。
	 * 接着判断msg是否是ByteBuf是否是ByteBuf类型，如果是，则进行强制类型转换，将其转换成ByteBuf类型，判断当前消息的科读字节数是否为0，如果
	 * 为0，说明该消息不可读，需要丢弃。从环形发送数组中删除该消息，继续循环处理其他的消息。
	 * 声明消息发送相关的成员变量，包括：写半包标识、消息是否全部发送标识、发送的总消息字节数。
	 * 这些局部变量创建完成之后，对循环发送次数进行判断，如果为-1，则从Channel配置对象中获取循环发送次数。循环发送次数是指当一次发送没有完成时(写半包)，
	 * 继续循环发送的次数。设置写半包最大循环次数的原因时当循环发送的时候，I/O线程会一直尝试进行写操作，此时I/O线程无法处理其他的I/O操作，例如读新的
	 * 消息或者执行定时人物和NioTask等，如果网络I/O阻塞或者对方接收消息太慢，可能会导致线程假死。
	 * 调用doWriteBytes进行消息发送。如果本次发送的字节数为0，说明发送TCP缓冲区已满，发生了ZERO_WINDOW。此时再次发送仍然可能出现写0字节，
	 * 空循环会占用CPU的资源，导致I/O线程无法处理其他I/O操作，所以将写半包标识setOpWrite设置为true，退出循环，释放I/O线程。
	 * 如果发送的字节数大于0，则对发送总数进行计数。判断当前消息是否已经发送成功（缓冲区没有可读字节），如果发送成功则设置done为true，退出当前循环。
	 * 消息发送操作完成之后调用ChannelOutboundBuffer更新发送进度信息，然后对发送结果进行判断。如果发送成功，则将已经发送的消息从发送数组中
	 * 删除；否则调用incompleteWrite方法，设置写半包标识，启动刷新线程继续发送之前没有发送完成的半包消息（写半包）。
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
	
	/**
	 * NioServerSocketChannel
	 * 
	 * doReadMessages
	 * 对于NioServerSocketChannel，它的读取操作就是接收客户端的连接，创建NioSocketChannel对象
	 */
	
	/**
	 * NioSocketChannel
	 * 
	 * doConnect
	 * 
	 * doWrite
	 * 
	 */
}
