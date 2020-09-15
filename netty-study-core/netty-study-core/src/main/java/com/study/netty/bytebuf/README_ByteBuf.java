package com.study.netty.bytebuf;

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
	
}
