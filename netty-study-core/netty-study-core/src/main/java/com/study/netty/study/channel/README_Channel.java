package com.study.netty.study.channel;

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
	
	
	/**
	 * 对NioSocketChannel doWrite进行源码解读
	 * 
	 * 获取待发送的ByteBuf个数，如果小于等于1，则调用父类AbstractNioByteChannel的doWrite方法，操作完成之后退出。
	 * 
	 * 在批量发送缓冲区的消息之前，先对一系列的局部变量进行赋值，首先，获取需要发送的ByteBuffer数组个数nioBufferCnt，然后，从ChannelOutboundBuffer
	 * 中获取需要发送的总字节数，从NioSocketChannel中获取NIO的SocketChannel，将是否发送完成标识设置为false，将是否有写半包标识设置为false。
	 * 
	 * 就像循环读一样，我们需要对一次Selector轮询的写操作次数进行上限控制，因为如果TCP的发送缓冲区满，TCP处于KEEP-ALIVE状态，消息会无法发送出去，如果不对
	 * 上限进行控制，就会长时间地处于发送状态，Reactor线程无法及时读取其他消息和执行排队的Task。所以，我们必须对循环次数上限做控制。
	 * 
	 * 调用NIO SocketChannel的write方法，它有三个参数：第一个时需要发送的ByteBuffer数组，第二个是数组的偏移量，第三个参数是发送的ByteBuffer个数。
	 * 返回值是写入SocketChannel的字节个数。
	 * 
	 * 下面对写入的字节进行判断，如果为0，说明TCP发送缓冲区已满，很可能无法再写进去，因此从循环中跳出，同时将写半包标识设置为true，用于向多路复用器注册写操作位，
	 * 告诉多路复用器有没发完的半包消息，需要轮询出就绪的SocketChannel继续发送。
	 * 
	 * 发送操作完成后进行两个计算：需要发送的字节数要减去已经发送的字节数；发送的字节总数+已经发送的字节数。更新完这两个变量后，判断缓冲区中所有的消息是否已经发送
	 * 完成，如果是，则把发送完成标识设置为true同时退出循环。如果没有发送完成，则继续循环。从循环发送中退出之后，首先对发送完成标识done进行判断，如果发送完成，
	 * 则循环释放已经发送的消息。环形数组的发送缓冲区释放完成后，取消半包标识，告诉多路复用器消息已经全部发送完成。
	 * 
	 * 当缓冲区中的消息没有发送完成，甚至某个ByteBuffer只发送了几个字节，出现了所谓的“写半包”时，该怎么办？
	 * 
	 * （这里首先说明一下，writtenBytes是发送出去的所有消息的总字节数。而该循环是一个一个处理ByteBuf的。而且是一个ByteBuf维护一对读写索引）
	 * 
	 * 首先，循环遍历发送缓冲区，对消息的发送结果进行判断，下面具体展开进行说明。
	 * 
	 * 1、从ChannelOutboundBuffer弹出第一条发送的ByteBuf，然后获取该ByteBuf的读索引和可读字节数。
	 * 
	 * 2、对可读字节数和发送的总字节数进行比较，如果发送的字节数大于可读的字节数，说明当前的ByteBuf已经被完全发送出去，更新ChannelOutboundBuffer的发送
	 * 进度信息，将已经发送的ByteBuf删除，释放相关资源。最后，发送的字节数要减去第一条发送的字节数，得到后续消息发送的总字节数，然后继续循环判断第二条消息、第三条
	 * 消息。。。
	 * 
	 * 3、如果可读的消息大于已经发送的总字节数，说明这条消息没有被完整地发送出去，仅仅发送了部分数据报，也就是出现了所谓的“写半包”问题。此时，需要更新可读的索引为
	 * 当前索引+已经发送的总字节数，然后更新ChannelOutboundBuffer的发送进度信息，退出循环。
	 * 
	 * 4、如果可读字节数等于已经发送的总字节数，则说明最后一次发送的消息是个整包消息，没有剩余的半包消息待发送。更新发送进度信息，将最后一条已发送的消息从缓冲区中
	 * 删除，最后退出循环。
	 * 
	 * 
	 * 循环发送操作完成之后，更新SocketChannel的操作位为OP_WRITE，由多路复用器在下一次轮询中触发SocketChannel，继续处理没有发送完成的半包消息。
	 * 
	 */
}















