package com.study.netty.study.channelPipeline;

import static io.netty.channel.ChannelHandlerInvokerUtil.invokeChannelReadNow;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipelineException;

/**
 * ChannelPipeline源码分析类
 * @author lilk
 *
 */
public class README_ChannelPipeline {

	/**
	 * 《netty权威指南》
	 * ChannelPipeline
	 * 
	 * 一个消息被ChannelPipeline的ChannelHandler链拦截和处理的过程，其中消息的读取和发送处理全流程描述如下：
	 * 1、 底层的SocketChannel read() 方法读取ByteBuf，触发ChannelRead事件，由I/O线程NioEventLoop调用ChannelPipeline的
	 * fireChannelRead(Object msg)方法，将消息(ByteBuf)传输到ChannelPipeline中。
	 * 2、消息一次被HandHandler、ChannelHandler1、ChannelHandler2。。。。。。TailHandler拦截和处理，在这个过程中，任何ChannelHandler
	 * 都可以中断当前的流程，结束消息的传递；
	 * 3、调用ChannelHandlerContext的write方法发送消息，消息从TailHandler开始，途经ChannelHandlerN。。。。。。ChannelHandler1、
	 * HeadHandler，最终被添加到消息发送缓冲区中等待刷新和发送，在此过程中也可以中断消息的传递，例如当编码失败时，就需要中断流程，构造异常的future返回。
	 * 
	 * 、
	 * Netty中的事件分为inbound事件和outbound事件。inbound事件通常由I/O线程触发，例如TCP链路建立事件、链路关闭事件、读事件、异常通知事件等。它对应上述
	 * 第二步。
	 * 触发inbound事件的方法如下：
	 * 1、ChannelHandlerContext.fireChannelRegistered()：Channel注册事件
	 * 2、ChannelHandlerContext.fireChannelActive()：TCP链路建立成功，Channel激活事件
	 * 3、ChannelHandlerContext.fireChannelRead(Object)：读事件
	 * 4、ChannelHandlerContext.fireChannelREadComplete()：读操作完成通知事件
	 * 5、ChannelHandlerContext.fireExceptionCaught(Throwable)：异常通知事件
	 * 6、ChannelHandlerContext.fireUserEventTriggered(Object)：用户自定义事件
	 * 7、ChannelHandlerContext.fireChannelWritabilityChanged()：Channel的可写状态变化通知事件
	 * 8、ChannelHandlerContext.fireChanneInactive()：TCP连接关闭，链路不可用通知事件
	 * 
	 * Outbound事件通常是由用户主动发起的网络I/O操作，例如用户发起的连接操作、绑定操作、消息发送等操作。它对应上述第三步。
	 * 触发outbound事件的方法如下：
	 * 1、ChannelHandlerContext.bind(SocketAddress, ChannelPromise)：绑定本地地址事件
	 * 2、ChannelHandlerContext.connect(SocketAddress, SocketAddress, ChannelPromise)：连接服务端事件
	 * 3、ChannelHandlerContext.write(Object, ChannelPromise)：发送事件
	 * 4、ChannelHandlerContext.flush()：刷新事件
	 * 5、ChannelHandlerContext.read()：读事件
	 * 6、ChannelHandlerContext.disconnect(ChannelPromise)：断开连接事件
	 * 7、ChannelHandlerContext。close(ChannelPromise)：关闭当前Channel事件
	 * 
	 * 
	 */
	
	/**
	 * addBefore -> addBefore -> checkMultiplicity
	 * 中有一个判断
	 * 
	 * ChannelHandlerAdapter h = (ChannelHandlerAdapter) handler;
	 * if (!h.isSharable() && h.added) {
     *     throw new ChannelPipelineException(
     *              h.getClass().getName() +
     *              " is not a @Sharable handler, so can't be added or removed multiple times.");
     *  }
     *  
     *  表示该ChannelHandlerAdapter不能被共享，且已经被添加
	 * 
	 */
	
	/**
	 * ChannelPipeline inbound事件
	 * 
	 * 当发生某个I/O事件的时候，例如链路建立、链路关闭、读取操作完成等，都会产生一个事件，事件在pipeline中得到传播和处理，它是事件处理的总入口。由于
	 * 网络I/O相关的事件有限，因此Netty对这些事件进行了统一抽象，Netty自身和用户的ChannelHandler会对感兴趣的事件进行拦截和处理。
	 * 
	 * pipeline中以fireXXX命名的方法都是从IO线程刘翔用户业务Handler的inbound事件，它们的实现因功能而异，但是处理步骤类似，总结如下：
	 * 1、调用HandHandler对应的fireXXX方法；
	 * 2、执行事件相关的逻辑操作。
	 * 
	 * 以fireChannelActive方法为例，调用head.fireChannelActive()之后，判断当前的Channel配置是否自动读取，如果为真则调用Channel的read方法。
	 * 
	 * 这里说说我看到的一些代码。DefaultChannelHandlerContext里的fireChannelRead(Object msg)
	 * @Override
	 *  public ChannelHandlerContext fireChannelRead(Object msg) {
	 *      DefaultChannelHandlerContext next = findContextInbound(MASK_CHANNEL_READ);
	 *      next.invoker.invokeChannelRead(next, msg);
	 *      return this;
	 *  }
	 * 按我的理解，从addBefore里源码可以看出ChannelHandlerContext会保存自己的上一个处理器和下一个处理器(如果有的话)。
	 * 
	 * findContextInbound(MASK_CHANNEL_READ); -> ctx = ctx.next;懂了吧
	 * MASK_CHANNEL_READ 这就是一个标志，代表这感兴趣的处理器。
	 * ctx.skipFlags代表想要跳过的操作。
	 * 因此
	 * do {
     *     ctx = ctx.next;
     * } while ((ctx.skipFlags & mask) != 0);
     * 会找到最近的一个不跳过的ctx
	 * 
	 * 所以这段代码就是找到自己的下一个处理器，并继续channelRead(很久没用可能忘了，这个方法是连接成功后会调用的方法，继承类ChannelHandlerAdapter
	 * 后可以重写)的操作
	 * next.invoker.invokeChannelRead(next, msg); 点进去可以看到 -> invokeChannelReadNow(ctx, msg);
	 * -> ctx.handler().channelRead(ctx, msg);
	 */
	
	/**
	 * ChannelPipeline outbound事件
	 * 
	 * 由用户线程或者代码发起的I/O操作被称为outbound事件，事实上inbound和outbound是Netty自身根据事件在pipeline中的流向抽象出来的术语，在其他NIO框架
	 * 中并没有这个概念。
	 * 
	 * pipeline本身并不直接进行I/O操作，在前面对Channel和Unsafe的介绍中我们知道最终都是由Unsafe和Channel来实现真正的I/O操作的。pipeline负责将I/O
	 * 事件通过TailHandler进行调度和传播，最终调用Unsafe的I/O方法进行I/O操作。
	 * 
	 */
	
}

































