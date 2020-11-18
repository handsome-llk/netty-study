package com.study.netty.study.unsafe;

import java.util.ArrayList;
import java.util.List;

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
	
	
	/**
	 * AbstractNioUnsafe
	 * 
	 * finishConnect
	 * 
	 * 首先缓存连接状态，当前返回false，然后执行doFinishConnect方法判断连接结果。
	 * doFinishConnect通过SocketChannel的finishConnect方法判断连接结果，执行该方法返回三种可能结果。
	 * 1、连接成功返回true
	 * 2、连接失败返回false
	 * 3、发生链路被关闭、链路中断等异常，连接失败
	 * 
	 * 只要连接失败，就抛出Error()，由调用方执行句柄关闭等资源释放操作，如果返回成功，则执行fullfillConnectPromise方法，它
	 * 负责将SocketChannel修改为监听读操作位，用来监听网络的读事件。
	 * 
	 * 最后finally中对连接超时进行判断：如果连接超时时仍然没有接收到服务端的ACK应答消息，则由定时任务关闭客户端连接，将SocketChannel从
	 * Reactor线程的多路复用器上摘除，释放资源。
	 * 
	 */
	
	
	/**
	 * NioByteUnsafe
	 * 
	 * read
	 * 
	 * 首先，获取NioSocketChannel的SocketChannelConfig，它主要用于设置客户端连接TCP参数。
	 * 
	 * 继续看allocHandle的初始化。如果是首次调用，从SocketChannelConfig的RecvByteBufAllocator中创建Handle。下面我们对
	 * RecvByteBufAllocator进行简单地代码分析：RecvByteBufAllocator默认有两种实现，分别是AdaptiveRecvByteBufAllocator和
	 * FixedRecvByteBufAllocator。由于FixedRecvByteBufAllocator地实现比较简单，我们重点分析AdaptiveRecvByteBufAllocator
	 * 地实现。
	 * 
	 * 顾名思义，AdaptiveRecvByteBufAllocator指的是缓冲区大小可以动态调整的ByteBuf分配器。
	 * 
	 * 首先看AdaptiveRecvByteBufAllocator定义的常量。
	 * 最小缓冲区长度64字节、初始容量1024字节、最大容量65536字节。
	 * 以及两个动态调整容量时的步进参数：扩张的步进索引为4、收缩的步进索引为1.
	 * 
	 * 最后，定义了长度的向量表SIZE_TABLE并初始化它。
	 * 
	 * 向量数组的每个值都对应一个Buffer容量，当容量小于512的时候，由于缓冲区已经比较小，需要降低步进值，容量每次下调的幅度要小些；当大于512时，说明
	 * 需要解码的消息码流比较大，这时采用调大步进幅度的方式减少动态扩张的频率，所以它采用512的倍数进行扩展。
	 * 
	 * 接下来分析AdaptiveRecvByteBufAllocator的方法
	 * 
	 * 方法getSizeTableIndex(final int size)，采用了二分法。
	 * 
	 * 然后看看它的内部静态类HandleImpl。
	 * 
	 * 首先看下它的成员变量。它有5个成员变量，分别是：对应向量表的最小索引、最大索引、当前索引、下一次预分配的Buffer大小和是否立即执行容量收缩操作。
	 * 
	 * 接下来重点分析它的record(int actualReadBytes)方法，当NioSocketChannel执行完读操作后，会计算获得本次轮询读取的总字节数，它就是
	 * 参数actualReadBytes(ctrl + shift + g查看调用处可以了解)，执行record方法，根据实际读取的字节数对ByteBuf进行动态伸缩和扩张。
	 * 
	 * 同构源码可以看出，AdaptiveRecvByteBufAllocator就是根据本次读取的实际字节数对下次接收缓冲区的容量进行动态调整。
	 * 
	 * 使用动态缓冲区分配器的有点如下：
	 * 1、Netty作为一个通用的NIO框架，并不对用户的应用场景进行假设，可以使用它做流媒体传输，也可以用它做聊天工具。不同的应用场景，传输的码流大小
	 * 千差万别，无论初始化分配的是32k还是1m，都会随着应用场景的变化而变得不适应。因此，Netty根据上次实际读取的码流大小对下次的接收Buffer缓冲区进行预测
	 * 和调整，能够最大限度地满足不同行业的应用场景
	 * 2、性能更高，容量过大会导致内存占用开开销增加，后续的Buffer处理性能会下降；容量过小时需要频繁地内存扩张来接收大的请求消息，同样会导致性能下降。
	 * 3、更节约内存。加入通常情况下请求消息平均值为1m左右，接收缓冲区大小为1.2m；突然某个客户发送了一个10m的流媒体附件，接收缓冲区扩张为10m以接纳该附件，
	 * 如果缓冲区不能收缩，每次缓冲区创建都会分配10m的内存，但是后续所有的消息都是1m左右，这样会导致内存的浪费，如果并发客户端过多，可能会导致内存溢出，
	 * 最终宕机。
	 * 
	 * 首先通过接收缓冲区分配器的Handler计算获得下次预分配的缓冲区容量byteBufCapacity，紧接着根据缓冲区容量进行缓冲区分配，Netty的缓冲区种类很多，此处
	 * 重点介绍的是消息的读取，因此对缓冲区不展开说明。
	 * 
	 * 接收缓冲区ByteBuf分配完成后，进行消息的异步读取(doReadBytes(ByteBuf byteBuf)).这是个抽象方法，具体实现再NioSocketChannel中。
	 * 进入NioSocketChannel的doReadByted中，其中javaChannel()返回的是SocketChannel，byteBuf.writableBytes返回本次可读的最大长度。
	 * 继续看具体实现，在AbstractByteBuf的writeBytes.然后对setBytes展开分析。
	 * 
	 * 拿UnpooledHeapByteBuf的setBytes来说。
	 * 
	 * 由于SocketChannel的read方法参数是Java NIO的ByteBuffer，所以，需要先将Netty的ByteBuf转换成JDK的ByteBuffer，随后调用ByteBuffer
	 * 的clear方法对指针进行重置用于新消息的读取，随后将position指针指到初始读index，读取的上限设置为index+读取的长度。最后调用read方法将
	 * SocketChannel中就绪的码流读取到ByteBuffer中，完成消息的读取，返回读取的字节数。
	 * 
	 * 完成消息的异步读取后，需要对本次读取的字节数进行判断，有以下三种可能：
	 * 1、返回0，表示没有就绪的消息可读
	 * 2、返回值大于0，读到了消息
	 * 3、返回值-1，表示发生了I/O异常，读取失败
	 * 
	 * 下面继续看Netty的后续处理，首先对读取的字节数进行判断，如果等于或者小于0，表示没有就绪的消息可读或者发生了I/O异常，此时需要释放接收缓冲区；如果读取的字节数
	 * 小于0，则需要将close状态位置位，用于关闭连接，释放句柄资源。置位完成之后退出循环。
	 * 
	 * 完成一次异步读之后，就会触发一次ChannelRead事件(fireChannelRead，表示消息传递至下一个处理器)，这里要特别提醒大家的是：完成一次读操作，并不意味着读到了一条完整的消息，因为TCP底层存在组包和粘包，
	 * 所以，一次读操作可能包含多条消息，也可能是一条不完整的消息。因此不要把它跟读取的消息个数等同起来。在没有做任何处理的情况下，以ChannelRead的触发次数做
	 * 计数器来进行性能分析和统计，是完全错误的。当然，如果你使用了半包解码器或者处理了半包，就能够实现一次ChannelRead对应一条完整的消息。
	 * 
	 * 触发和完成ChannelRead事件调用之后，将接收缓冲区释放。
	 * 
	 * 因为一次读操作未必能过够完成TCP缓冲区的全部读取工作，所以，读操作在循环体中进行，每次读取操作完成之后，会对读取的字节数进行累加。
	 * 
	 * 在累加之前，需要对长度上限做保护，如果累计读取的字节数已经发生溢出，则将读取到的字节数设置为整型的最大值，然后退出循环。原因是本次循环已经读取过多的字节，需要退出，
	 * 否则会影响后面排队的Task任务和写操作的执行。如果没有溢出，则执行累加操作。
	 * 
	 * 最后，对本次读取的字节数进行判断，如果小于缓冲区可写的容量，说明TCP缓冲区已经没有就绪的字节可读，读取操作已经完成，需要退出循环。如果仍然有未读的消息，则继续执行
	 * 读操作。连续的读操作会阻塞排在后面的任务队列中待执行的Task，以及写操作，所以，要对连续读操作做上限控制，默认值为16次，无论TCP缓冲区有多少码流需要读取，只要连续
	 * 16次没有读完，都需要强制退出，等待下次selector轮询周期再执行。
	 * 
	 * 完成多路复用器本轮读操作之后，触发ChannelReadComplete事件，随后调用接收缓冲区容量分配器的Hanlder的记录方法，将本次读取的总字节数传入到record()方法
	 * 中进行缓冲区的动态分配，为下一次读取选取更加合适的缓冲区容量。
	 * 
	 * 上面我们提到，如果读到的返回值为-1，表明发生了I/O异常，需要关闭连接，释放资源。
	 * 
	 * 至此，请求消息的异步读取源码我们已经分析完成。
	 * 
	 */
	
}














