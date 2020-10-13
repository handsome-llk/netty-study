package com.study.netty.study.bytebuf.nio.bytebuffer;

public class README_ByteBuffer {

	/**
	 * 记录下两个nio ByteBuffer常用的两个操作：flip、clear
	 * 
	 * flip一般用在写数据时需要转换为读数据使用
	 * clear将缓冲区清空，一般用在重新写缓冲区
	 * 
	 * 
	 * 这里用最常用的3个变量来说明,position,limit,capacity
	 * 
	 * position代表指针的位置
	 * 
	 * 最开始limit是等于capacity的。在往缓冲区写入数据时，position记录写入数据的最终索引。
	 * 此时要从写数据转为读数据，则需要调用flip方法。
	 * 
	 * public final Buffer flip() {
			limit = position;
			position = 0;
			mark = -1;
			return this;
		}
	 * 
	 * 可以看出来flip方法将指针位置归0，limit变为写入数据的最终索引位。position不能超过limit，保证读取数据的准确性。
	 * 
	 * 当要从读数据转为写数据时，调用clear方法
	 * 
	 * public final Buffer clear()
		{
		    position = 0; //重置当前读写位置
		    limit = capacity; 
		    mark = -1;  //取消标记
		    return this;
		}
	 * 
	 * 
	 * emmm，稍微看看还是蛮好理解的
	 * 
	 * 
	 */
	
}
