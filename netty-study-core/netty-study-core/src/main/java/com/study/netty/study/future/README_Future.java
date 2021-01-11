package com.study.netty.study.future;

public class README_Future {

    /**
     * 关于ChannelFuture讲解
     *
     * ChannelFuture有两种状态：uncompleted和completed。当开始一个I/O操作时，一个新的
     * ChannelFuture被创建，此时它处于uncompleted状态--非失败、非成功、非取消，因为I/O操作
     * 此时还没有完成。一旦I/O操作完成，ChannelFuture将会被设置成completed，它的结果有如下三种
     * 可能：
     * 1、操作成功
     * 2、操作失败
     * 3、操作被取消
     * 代码域状态图如下：
     * uncompleted:
     * isDone() = false
     * isSuccess() = false
     * isCancelled() = false
     * cause() = null
     *
     * completed successfully
     * isDone() = true
     * isSuccess() = true
     *
     * completed with failure
     * isDone() = true
     * cause() = non-null
     *
     * completed by cancellation
     * isDone() = true
     * isCancelled() = true
     *
     * Netty 强烈建议直接通过添加监听器的方式获取I/O操作结果，或者进行后续的相关操作。ChannelFuture
     * 可以同时添加一个或者多个GenericFutureListener，也可以通过remove方法删除
     * GenericFutureListener。当I/O操作完成之后，I/O线程会回调ChannelFuture中
     * GenericFutureListener的operationComplete方法，并发ChannelFuture对象当作方法的入参。如果
     * 用户需要做上下文相关的操作，需要将上下位信息保存到对应的ChannelFuture中。
     *
     * 推荐通过GenericFutureListener代替ChannelFuture的get等方法的原因时：当我们进行异步I/O操作时，
     * 完成的时间是无法预测的，如果不设置超时时间，它会导致调用线程长时间被阻塞，甚至挂起。而设置超时时间，
     * 时间又无法精确预测。利用异步通知机制回调GenericFutureListener是最佳的解决方案，它的性能最优。
     *
     * 需要注意的是：不要再ChannelHandler中调用ChannelFuture的await()方法，这会导致死锁。原因是
     * 发起I/O操作之后，由I/O线程负责异步通知发起I/O操作的用户线程，如果I/O线程和用户线程是同一个线程，
     * 就会导致I/O线程等待自己通知操作完成，这就导致了死锁，这跟经典的两个线程互等待死锁不同，属于自己
     * 把自己挂死。
     *
     * 异步I/O操作有两类超时：一个是TCP层面的I/O超时，另一个是业务逻辑层面的操作超时。两者没有必然的
     * 联系，但是通常情况下业务逻辑超时时间应该大于I/O超时时间，它们两者是包含的关系。
     *
     * 相关代码举例：
     * // GOOD
     * Bootstrap b = ...;
     * // Configure the connect timeout option
     * b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);
     * ChannelFuture f = b.connect(...);
     * f.awaitUninterruptibly();
     *
     * // Now we are sure the future is completed
     * assert f.isDone();
     *
     * if (f.isCancelled()) {
     *     // Connection attempt cancelled by user
     * } else if (!f.isSuccess()) {
     *      f.cause().printStackTrace();
     * } else {
     *     // Connection established successfully
     * }
     *
     * ChannelFuture超时时间配置如下：
     * Bootstrap b = ...;
     * ChannelFuture f = b.connect(...);
     * f.awaitUninterruptibly(10, TimeUnit.SECONDS);
     * if (f.isCancelled()) {
     *     // Connection attempt cancelled by user
     * } else if (!f.isSuccess()) {
     *     // You might get a NullPointerException here because the future
     *     // might not be completed yet.
     *     f.cause().printStackTrace();
     * } else {
     *     // Connection established successfully
     * }
     *
     * 需要指出的是：ChannelFuture超时并不代表I/O超时，这意味这ChannelFuture超时后，如果没有关闭
     * 连接资源，随后连接依旧可能会成功，这会导致严重的问题。所以通常情况下，必须要考虑究竟是设置I/O超
     * 时还是ChannelFuture超时。
     *
     */

}
