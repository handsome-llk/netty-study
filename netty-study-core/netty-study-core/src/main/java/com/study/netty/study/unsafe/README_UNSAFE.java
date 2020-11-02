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
	 * 接下来说说doWrite(ChannelOutboundBuffer in)，拿 NioSocketChannel 的 doWrite 来举例
	 * 
	 * 首先计算需要发送的消息个数(unflushed - flush)，如果只有1个消息需要发送，则调用父类的写操作，我们分析AbstractNioByteChannel的
	 * doWrite()方法。
	 * 
	 * 因为只有一条消息需要发送，所以直接从ChannelOutboundBuffer中获取当前需要发送的消息。
	 * 
	 * 进入current，首先获取需要发送的消息，如果消息为ByteBuf且它分配的是JDK的非堆内存，则直接返回。继续分析AbstractNioByteChannel doWrite，
	 * 对返回的消息进行判断，如果为空，说明消息已经发送完成并被回收，然后执行清空OP_WRITE操作位的clearOpWrite方法。
	 * 
	 * 继续向下分析，如果需要发送的ByteBuf已经没有可写的字节了，则说明已经发送完成，将该消息从环形队列中删除，然后继续循环。
	 * 
	 * 下面分析下ChannelOutboundBuffer的remove方法。
	 * 
	 * 首先判断环形队列中是否有需要发送的消息，如果没有，则直接返回，如果非空，则首先获取Entry，然后对其进行资源释放，同时对需要发送的索引flushed进行
	 * 更新。所有操作执行完之后，调用decrementPendingOutboundBytes减去已经发送的字节数，该方法跟incrementPendingOutboundBytes类似，
	 * 会进行发送低水位的判断和事件通知，此时不再赘述。
	 * 
	 * 继续对AbstractNioByteChannel oWrite进行进行分析。
	 * 
	 * 首先将半包标识设置位false，从DefaultSocketChannelConfig中获取循环发送的次数，进行循环发送，对发送方法doWriteBytes展开分析。
	 * ByteBuf()的readBytes()方法的功能是将当前ByteBuf中的可写字节数组写入到指定的Channel中。方法的第一个参数是Channel，此处就是
	 * SocketChannel，第二个参数是写入的字节数组长度，它等于ByteBuf的可读字节数，返回值是写入的字节数。由于我们将SocketChannel设置为异步非阻塞
	 * 模式，所以写操作不会阻塞。
	 * 
	 * 从写操作中返回，需要对写入的字节数进行判断，如果为0，说明TCP发送缓冲区已满，不能继续再向里面写入消息，因此，将写半包标识设置为true，然后退出循环，执行
	 * 后续排队的其他任务或者读操作，等待下一次selector的轮询继续触发写操作。
	 * 
	 * 对写入的字节数进行累加，判断当前的ByteBuf中是否还有没有发送的字节，如果没有可发送的字节，则将done设置为true，退出循环。
	 * 
	 * 从循环发送状态退出后，首先根据实际发送的字节数更新发送进度，实际就是发送的字节数和需要发送的字节数的一个比值。执行完进度更新后，判断本轮循环是否将需要
	 * 发送的消息全部发送完成，如果发送完成则将该消息从循环队列中删除；否则，设置多路复用器的OP_WRITE操作位，用于通知Reactor线程还有半包消息需要继续发送。
	 * 
	 */
	
	
	/**
	 * AbstractNioUnsafe
	 * 
	 * AbstractNioUnsafe是AbstractUnsafe类的NIO实现，它主要实现了connect、finishConnect等方法。
	 * 
	 */
	
	/**
	 * AbstractNioUnsafe
	 * 
	 * connect
	 * 
	 * 首先获取当前的连接状态进行缓存，然后发起连接操作doConnect。
	 * 这里需要指出的是，SocketChannel执行connect()操作有三种可能的结果。
	 * 1、连接成功，返回true
	 * 2、暂时没有连接上，服务端没有返回ACK应答，连接结果不确定，返回false
	 * 3、连接失败，直接抛出I/O异常
	 * 
	 * 如果是第2种结果，需要将NioSocketChannel中的selectionKey设置为OP_CONNECT，监听连接应答消息。
	 * 
	 * 异步连接返回之后，需要判断连接结果，如果连接成功，则触发ChannelActive
	 * 事件(fullfillConnectPromise(ChannelPromise promise, boolean wasActive)),下面对ChannelActive事件处理不在进行详细说明，
	 * 它最终会将NioSocketChannel中的selectionKey设置为SelectionKey.OP_READ,用于监听网络读操作位。
	 * 
	 * doConnect如果是第二种结果，后面的操作有两个目的：
	 * 1、根据连接超时时间设置定时任务，超时时间到之后触发校验，如果发现连接并没有完成，则关闭连接句柄，释放资源，设置异常堆栈并发起去注册。
	 * 2、设置连接结果监听器，如果接收到连接完成通知则判断连接是否被取消，如果被取消则关闭连接句柄，释放资源，发起取消注册操作。
	 * 
	 */
	
	
	
	
	
	
	
	
	
	
	
}
