package com.study.netty.study.eventLoop;

/**
 * eventLoop源码讲解
 * @author lilk
 *
 */
public class README_eventLoop {

	/**
	 * Reactor线程模式
	 * 
	 * Reactor单线程模式、Reactor多线程模式、Reactor主从多线程模式。
	 * 
	 * Reactor单线程模式，其I/O操作使用的是异步非阻塞I/O，所有的I/O操作都不会导致阻塞。
	 * 但是如果用户一多，数据一多，解码编码等效率跟不上，就会导致数据积压。
	 * 
	 * Reactor多线程模式，其与单线程模式的区别是多了一组NIO线程来处理IO操作。其模型就是用一个NIO线程监听连接请求。然后解码编码等IO操作
	 * 使用新加的一组NIO线程来处理。但是在个别场景还会有问题，就是连接操作本身的安全认证也是很耗性能的，所以在请求过多的情况下也可能导致
	 * 性能不足的问题
	 * 
	 * 主从Reactor多线程模型，其与多线程的最大区别就是又加了一组NIO线程池来处理连接请求。
	 * 
	 */
	
	/**
	 * 最佳实践，Netty的多线程编程最佳实践如下：
	 * 1、创建两个NioEventLoopGroup，用于逻辑隔离NIO Acceptor和NIO I/O线程
	 * 2、尽量不要在ChannelHandler中启动用户线程（解码后用于将POJO消息派发到后端业务线程的除外）
	 * 3、解码要放在NIO线程调用的解码Handler中进行，不要切换到用户线程中完成消息的解码
	 * 4、如果业务逻辑操作非常简单，没有复杂的业务逻辑计算，没有可能会导致线程被阻塞的磁盘操作、数据库操作、网路操作等，可以直接在NIO线程上完成业务逻辑编排，
	 * 不需要切换到用户线程
	 * 5、如果业务逻辑处理复杂，不要在NIO线程上完成，建议将解码后的POJO消息封装成Task，派发到业务线程池中由业务线程执行，以保证NIO线程尽快被释放，处理
	 * 其他的I/O操作。
	 * 
	 * 推荐的线程数量计算公式有以下两种：
	 * 1、线程数量 = (线程总时间 / 瓶颈资源时间) * 瓶颈资源的线程并行数
	 * 2、QPS = 1000 / 线程总时间 * 线程数
	 * 
	 */


	/**
	 * NioEventLoop 的 run
	 *
	 * 首先需要将wakenUp还原为false，并将之前的wake up 状态保存到oldWakenUp变量中。通过hasTask()方法判断当前的消息队列
	 * 中是否有消息尚未处理，如果有则调用selectNow()方法立即进行一次select操作，看是否有准备就绪的Channel需要处理。
	 *
	 * Selector的selectNow方法会立即触发Selector的选择操作，如果有准备就绪的Channel，则返回就绪Channel的集合(我感觉是key？)，
	 * 否则返回0.选择完成之后，再次判断用户是否调用了Selector的wakeup方法，如果调用，则执行selector.wakeup()操作。
	 *
	 * 下面继续说run方法。
	 * 如果消息队列中没有消息需要处理，则执行select()方法，由Selector多路复用器轮询，看是否有准备就绪的Channel。
	 *
	 * 进入select()方法。
	 * 取当前系统的纳秒时间，调用delayNanos()方法计算获得NioEventLoop中定时任务的触发时间。计算下一个将要触发的定时任务的剩余超时时间，将
	 * 它转换成毫秒，为超时时间增加0.5毫秒的调整值。对剩余的超时时间进行判断，如果需要立即执行或者已经超时，则调用selector.selectNow()进行
	 * 轮询操作，将selectCnt设置为1，并退出当前循环。
	 *
	 * 将定时任务剩余的超时时间作为参数进行select操作，每完成一次select操作，对select计数器selectCnt加1.select操作完成之后，需要对结果进行
	 * 判断，如果存在下列任意一种情况，则退出当前循环：
	 * 1、有Channel处于就绪状态，selectedKeys不为0，说明有读写时间需要处理；
	 * 2、oldWakenUp为true
	 * 3、系统或者用户调用了wakeup操作，唤醒当前的多路复用器
	 * 4、消息队列中有新的任务需要处理
	 *
	 * 如果本次Selector的轮询结果为空，也没有wakeup操作或是新的消息需要处理，则说明是个空轮询，有可能触发了JDK的epoll bug，
	 * 它会导致Selector的空轮询，使I/O线程一直处于100%状态。截至到当前最新的JDK7版本，该bug仍然没有被完全修复，所以Netty需要
	 * 对该bug进行规避和修正。该bug的修复策略如下：
	 * 1、对Selector的select操作周期进行统计
	 * 2、每完成一次空的select操作进行一次计数
	 * 3、在某个周期内如果连续发生N次空轮询，说明触发了JDK NIO的epoll()死循环bug
	 * 检测到Selector处于死循环后，需要通过重建Selector的方式让系统恢复正常。
	 *
	 * 下面看看rebuildSelector的逻辑：
	 * 首先通过inEventLoop()方法判断是否是其他线程发起的rebuildSelector，如果由其他线程发起，为了避免多线程并发操作Selector和
	 * 其他资源，需要将rebuildSelector封装城Task，放到NioEventLoop的消息队列中，由NioEventLoop线程负责调用，这样就避免了多
	 * 线程并发操作导致的线程安全问题。
	 *
	 * 通过调用openSelector方法创建并打开新的Selector，通过循环，将原Selector上注册的SocketChannel从旧的Selector上去注册，
	 * 重新注册到新的Selector上，并将老的Selector关闭。通过销毁旧的、有问题的多路复用器，使用新建的Selector，就可以解决空轮询
	 * Selector导致的I/O线程CPU占用100%的问题。
	 *
	 * 回到run方法，如果轮询到了处于就绪状态的SocketChannel，则需要处理网络I/O事件。由于默认未开启selectedKeys优化功能，所以
	 * 会进入到processSelectedKeysPlain分支执行。
	 *
	 * 对SelectionKey进行保护性判断，如果为空则返回。获取SelectionKey的迭代器进行循环操作，通过迭代器获取SelectionKey和
	 * SocketChannel的附件对象，将已经选择的选择键从迭代器中删除，防止下次被重复选择和处理。
	 *
	 * 对SocketChannel的附件类型进行判读，如果是AbstractNioChannel类型，说明它是NioServerSocketChannel或者
	 * NioSocketChannel，需要进行I/O读写相关的操作；如果它是NioTask，则对其进行类型转换，调用processSelectedKey进行处理。
	 * 由于Netty自身没实现NioTask接口，所以通常情况下系统不会执行该分支，除非用户自行注册该Task到多路复用器。
	 *
	 * 进入到方法processSelectedKey中。
	 * 首先从NioServerSocketChannel或者NioSocketChannel中获取其内部类Unsafe，判断当前选择键是否可用，如果不可用，则调用
	 * unsafe的close方法，释放连接资源。如果是读或者连接操作，则调用Unsafe的read方法。此处Unsafe的实现是个多态。对于
	 * NioServerSocketChannel，它的读操作就是接收客户端的TCP连接(AbstractNioMessageChannel doReadMessages)；
	 * 对于NioSocketChannel，它的读操作就是从SocketChannel中读取ByteBuffer(AbstractNioByteChannel doReadBytes)。
	 *
	 * 如果网络操作位为写，则说明由半包消息尚未发送完成，需要继续调用flush方法进行发送。如果网络操作位为连接状态，则需要对连接结果
	 * 进行判读。需要注意的是，在进行finishConnect判断之前，需要将网络操作位进行修改，注销吊SelectionKey.OP_CONNECT;
	 *
	 * 回到run方法。处理完I/O事件之后，NioEventLoop需要执行非I/O操作的系统Task和定时任务。由于NioEventLoop需要同时处理I/O
	 * 事件和非I/O任务，为了保证两者都能得到足够的CPU时间被执行，Netty提供了I/O比例共用户定制。如果I/O操作多于定时任务和Task，则
	 * 可以将I/O比例调大，反之则调小，默认值为50%;Task的执行时间根据本次I/O操作的执行时间计算得来。
	 *
	 * 首先从定时任务消息队列中弹出消息进行处理，如果消息队列为空，则退出循环。根据当前的时间戳进行判断，如果该定时任务已经或者处于
	 * 超时状态，则将其加入到执行Task Queue中，同时从延时队列中删除。定时任务如果没有超时，说明本轮循环不需要处理，直接退出即可。
	 *
	 * 执行Task Queue中原有的任务和从延时队列中复制的已经超时或者正处于超时状态的定时任务。由于获取系统纳秒时间是个耗时的操作，每次
	 * 循环都获取当前系统纳秒时间进行超时判断会降低性能。为了提升性能，没执行63次判断一次，如果当前系统时间已经到了分配给非I/O操作的
	 * 超时时间，则退出循环。这是为了防止由于非I/O任务过多导致I/O操作被长时间阻塞。
	 *
	 * 最后，判断系统是否进入优雅停机状态，如果处于关闭状态，则需要调用closeAll方法，释放资源，并让NioEventLoop线程退出循环，结束
	 * 运行。
	 *
	 *
	 */

	
}
