package com.study.netty.study.promise;

public class README_Promise {

    /**
     * Promise相关介绍
     * Promise继承了Future，是可写的Future，Future自身并没有写操作相关的接口，Netty通过Promise对Future进行
     * 扩展，用于设置I/O操作的结果。
     *
     * Netty发起I/O操作的时候，会创建一个新的Promise对象，例如调用ChannelHandlerContext的
     * write(Object object)方法时，会创建一个新的ChannelPromise(newPromise()方法)。
     *
     * 当I/O操作发生异常或者完成时，设置Promise的结果。
     *
     */

    /**
     * DefaultPromise
     * 下面看比较重要的setSuccess方法的实现
     *
     * 首先判断当前Promise的操作结果是否已经被设置，如果已经被设置，则不允许重复设置，返回设置失败。
     * 由于可能存在I/O线程和用户线程同时操作Promise，所以设置操作结果的时候需要加锁保护，防止并发
     * 操作。对操作结果是否被设置进行二次判断（为了提升并发性能的二次判断），如果已经被设置，则返回
     * 操作失败。
     *
     * 对操作结果result进行判断，如果为空，说明仅仅需要notify在等待的业务线程，不包含具体的业务逻辑
     * 对象。因此，将result设置为系统默认的SUCCESS。如果操作结果非空，将结果设置为result。
     *
     * 如果又正在等待异步I/O操作的用户线程或者其他系统线程，则调用notifyAll方法唤醒所有正在等待的
     * 线程。注意，notify和wait方法都必须在同步块内使用。
     *
     */

}
