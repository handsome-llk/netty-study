package com.study.netty.bytebuf;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class README_ByteBuf {

	/**
	 * 相比于nio的ByteBuffer的指针说明
	 * 
	 * netty的ByteBuf使用readerIndex和writerIndex来表示读指针和写指针。
	 * 
	 * readerIndex不能超过writerIndex。readerIndex到writerIndex之间的内容是可读取的。writerIndex到capacity
	 * 之间的内容是可写入的。
	 * 0到readerIndex之间的内容被视为可discard（丢弃）的，调用discardReadBytes方法，可以释放0到readerIndex之间的空间，
	 * 此时writerIndex = writerIndex - readerIndex, readerIndex = 0。
	 * 
	 * clear方法则将状态置0。writerIndex 和 readerIndex都归为0。
	 * 
	 */
	
	/**
	 * 相比于nio的ByteBuffer的put操作，写入数据的说明
	 * 
	 * ByteBuffer的put操作是不能动态扩容的，因此使用put操作时往往需要判断内存是否足够，不足的话需要创建新的ByteBuffer.
	 * 
	 * if (this.buffer.remaining() < needSize) {
	 * 		int toBeExtSize = needSize > 128 ? needSize : 128;
	 * 		ByteBuffer tmpBuffer = ByteBuffer.allocate(this.buffer.capacity() + toBeExtSize);
	 * 		this.buffer.flip();
	 * 		tmpBuffer.put(this.buffer);
	 * 		this.buffer = tmpBuffer;
	 * }
	 * 
	 * 每次put操作都需要校验，则造成了代码冗余。因此netty的write操作对此进行了封装，可以动态扩展空间
	 * 
	 * 
	 */
	
	
	/**
	 * mark、rest
	 * 
	 * ByteBuffer mark会将position被分到mark，调用rest会将mark还原至position。
	 * 如果调用rest时，mark<0则会报错。
	 * 
	 * 
	 * ByteBuf中也有相关操作
	 * markReaderIndex、resetReaderIndex、markWriterIndex、resetWriterIndex
	 * 备份readerIndex的变量是markedReaderIndex，备份writerIndex的变量是markedWriterIndex
	 * 
	 * 
	 */
	
	/**
	 * byteBuf的复制
	 * 
	 * 有三种方法
	 * duplicate、slice、slice(int index, int length)
	 * copy、copy(int index, int length)
	 * 
	 * duplicate和slice复制的内容和原内容指向同一份内容
	 * copy复制的内容和原内容是相互独立的
	 * 
	 * duplicate和slice的区别：
	 * duplicate复制的整个原始byteBuf，而slice复制的是position到limit之间的内容
	 * 
	 */
	
	/**
	 * byteBuf转换为ByteBuffer
	 * 
	 * nioBuffer、nioBuffer(int index, int length)
	 * 
	 */
	

	/**
	 * byteBuf随机读写get、set
	 * 
	 * 按我的理解，随机读写就是可以指定索引读写，即往原有内容中间插入新内容
	 * 但是值得注意的是，随机读写会进行索引合法性检查，需要注意，不然会报错哦
	 * 
	 */
	
	/**
	 * 从内存分配角度看，byteBuf分为两类
	 * 
	 * 堆内存(HeapByteBuf)和直接内存(DirectByteBuf)
	 * 
	 * 堆内存字节缓冲区：特点是内存的分配和回收速度快，可以被jvm自动回收；缺点就是如果进行socket的i/o读写，需要额外做一次内存复制，将
	 * 堆内存对应的缓冲区复制到内核Channel中，性能会有一定程度的下降。
	 * 
	 * 直接内存字节缓冲区：非堆内存，它在对外进行内存分配，相比于堆内存，它的分配和回收速度会慢一些，但是将它写入或者从socket channel中
	 * 读取时，由于少一次内存复制，速度比堆内存快
	 * 
	 * 经验表明，buteBuf的最佳实践是在i/o通信线程的读写缓冲区使用DirectByteBuf，后端业务消息的编解码模块使用HeapByteBuf，这样
	 * 组合可以达到性能最优。
	 * 
	 * 从内存回收角度看，byteBuf也分为两类
	 * 
	 * 基于对象池的ByteBuf和普通的ByteBuf
	 * 
	 */
	
	/**
	 * 这里想说一下netty源码里的一些参数，好好看的话其实很容易理解他们的意思
	 * 比如minNewCapacity,最小的新的空间等等，所以不要停止思考
	 * 
	 */
	
	/**
	 * ByteBuf中的一些接口是跟具体子类实现相关的，不同的子类功能是不同的
	 * 
	 * isDirect方法（是否是基于直接内存实现）：如果是基于堆内存实现的ByteBuf，它返回false
	 * 
	 * hasArray方法：由于UnpooledHeapByteBuf基于字节数组实现，所以它的返回值是true
	 * 
	 * array方法：由于UnpooledHeapByteBuf基于字节数组实现，所以它的返回值是内部的字节数组成员变量。在调用array方法前，
	 * 一般会调用hasArray方法进行判断
	 * 
	 */
	
	/**
	 * PooledByteBuf内存池原理，没看懂，后面有时间再研究研究。lilktodo
	 * 
	 */
	
	/**
	 * 
	 * netty权威指南讲解的byteBuf部分主要涉及
	 * ByteBuf -> AbstractByteBuf -> AbstractReferenceCountedByteBuf
	 * 
	 * AbstractReferenceCountedByteBuf -> UnpooledHeapByteBuf
	 * 
	 * AbstractReferenceCountedByteBuf -> PooledByteBuf
	 * 
	 * AbstractReferenceCountedByteBuf -> PooledByteBuf -> PooledDirectByteBuf
	 * 
	 * 以及一些相关辅助类：
	 * ByteBufHolder、ByteBufAllocator、CompositeByteBuf、ByteBufUtil
	 * 
	 * 简单介绍一下：
	 * ByteBufHolder：对ByteBuf的封装，它包含了一个ByteBuf，并且可以灵活的搭配其他方法进行扩展
	 * 
	 * ByteBufAllocator：字节缓冲区分配器，按照netty的缓冲区实现不同，共有两种不同的分配器
	 * 
	 * CompositeByteBuf：将多个ByteBuf的实例组装到一起，形成一个统一的视图。（具体没明白干啥用的，用到了再说）
	 * 
	 * ByteBufUtil：提供了一系列静态方法用于操作ByteBuf对象。最有用的方法是对字符串的编码和解码。
	 * 
	 */
	
}
