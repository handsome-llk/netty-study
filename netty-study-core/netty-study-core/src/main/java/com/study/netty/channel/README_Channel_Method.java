package com.study.netty.channel;

public class README_Channel_Method {

	/**
	 * Channel一些方法介绍
	 * 
	 * 1、Channel read()
	 * lilktodo
	 * 
	 * 2、ChannelFuture write(Object msg)
	 * 请求将当前的msg通过ChannelPipeline写入到目标Channel中。注意，write操作只是将消息存入到消息发送环形数组中，并没有真正被
	 * 发送，只有调用flush操作才会被写入到Channel中，发送给对方
	 * 
	 * 3、ChannelFuture write(Object msb, ChannelPromise promise)
	 * 功能与write(Object msg)相同，但是携带了ChannelPromise参数负责设置写入操作的结果
	 * 
	 * 4、ChannelFuture writeAndFlush(Object msg, ChannelPromise promise)
	 * 与方法3相似，不同之处在于它会将消息写入到Channel中发送，等价于单独调用write和flush操作的组合
	 * 
	 * 5、ChannelFuture writeAndFlush(Object msg)
	 * 功能等同于方法4
	 * 
	 * 6、Channel flush()
	 * 将之前写入到发送环形数组中的消息全部写入到目标Channel中，发送给通信对方
	 * 
	 * 7、ChannelFuture close(ChannelPromise promise)
	 * 主动关闭当前连接，通过ChannelPromise设置操作结果并进行结果通知，无论操作是否成功，都可以通过ChannelPromise获取操作结果。该操作
	 * 会级联触发ChannelPipeline中所有ChannelHandler的ChannelHandler.close(ChannelHandlerContext,ChannelPromise)
	 * 事件
	 * 
	 * 8、ChannelFuture disconnect(ChannelPromise promise)
	 * 请求断开与远程通信对端的连接并使用ChannelPromise来获取操作结果的通知消息。该方法会级联触发
	 * ChanndlHandler.disconnect(ChannelHandlerContext, ChannelPromise)事件
	 * 
	 * 9、ChannelFuture connect(SocketAddress remoteAddress)
	 * 客户端使用指定的服务端地址remoteAddress发起连接请求，如果连接因为应答超时而失败，ChannelFuture中的操作结果就是ConnectTimeoutException
	 * 异常，如果连接被拒绝，操作结果为ConnectException。该方法会级联触发
	 * ChannelHandler.connect(ChannelHandlerContext, SocketAddress, SocketAddress, ChannelPromise)事件
	 * 
	 * 10、ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress)
	 * 与方法9功能类似，唯一不同的就是先绑定指定的本地地址localAddress，然后再连接服务端
	 * 
	 * 11、ChannelFuture connect(SocketAddress remoteAddress, ChannelPromise promise)
	 * 与方法9功能类似，唯一不同的是携带了ChannelPromise参数用于写入操作结果
	 * 
	 * 12、connect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise)
	 * 与方法11功能类似，唯一不同的就是绑定了本地地址
	 * 
	 * 13、ChannelFuture bind(SocketAddress localAddress)
	 * 绑定指定的本地Socket地址localAddress，该方法会级联触发
	 * ChannelHandler.bind(ChannelHandlerContext, SocketAddress, ChannelPromise)事件
	 * 
	 * 14、ChannelFuture bind(SocketAddress localAddress, ChannelPromise promise)
	 * 与方法13功能类似，多携带了一个ChannelPromise用于写入操作结果
	 * 
	 * 15、ChanndlFuture config()
	 * 获取当前Channel的配置信息，例如CONNECT_TIMEOUT_MILLIS;
	 * 
	 * 16、boolean isOpen()
	 * 判断当前Channel是否已经打开
	 * 
	 * 17、boolean isRegistered()
	 * 判断当前Channel是否已经注册到EventLoop上
	 * 
	 * 18、boolean isActive()
	 * 判断当前Channel是否已经处于激活状态
	 * 
	 * 19、ChannelMetadata metadata()
	 * 获取当前Channel的元数据描述信息，包括TCP参数配置等
	 * 
	 * 20、SocketAddress localAddress()
	 * 获取当前Channel的本地绑定地址
	 * 
	 * 21、SocketAddress remoteAddress()
	 * 获取当前Channel同行的远程Socket地址
	 * 
	 */
	
	/**
	 * 其他一些常用的api说明
	 * 
	 * 1、eventLoop()
	 * Channel需要注册到EventLoop的多路复用器上，用于处理i/o事件，通过eventLoop()方法可以获取到Channel注册的EventLoop。
	 * EventLoop本质上就是处理网络读写事件的Reactor线程。在netty中，它不仅仅用来处理网络事件，也可以用来执行定时任务和自定义Nio Task
	 * 等任务
	 * 
	 * 2、metadata()
	 * 当创建Socket的时候需要指定TCP参数，例如接收和发送的TCP缓冲区大小，TCP的超时事件，是否重用地址等等。在netty中，每个Channel对应
	 * 一个物理连接，每个连接都有自己的TCP参数配置。所以，Channel会聚合一个ChannelMetadata用来对TCP参数提供元数据描述信息，通过
	 * metadata()方法就可以获取当前Channel的TCP参数设置
	 * 
	 * 3、parent()
	 * 对于服务器Channel而言，它的父Channel为空；对于客户端Channel，它的父Channel就是创建它的ServerSocketChannel
	 * 
	 * 4、id()
	 * 它返回ChannelId对象，ChannelId是Channel的唯一标识
	 * 
	 * 
	 */
	
}
