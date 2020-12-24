package com.study.netty.study;

/**
 * 记录一些问题
 * @author lilk
 *
 */
public class Questions {

	/**
	 * 1、 事件是如何一步步向下传递的
	 * 
	 * 2、传递的话需要手动调用firexxx等方法吗
	 * 我认为就是使用ctx.fireChannelRead(msg);方法向下传递msg的。感兴趣的类型判断这个，好像是我之前没有考虑。很多handler都是有对msg类
	 * 进行过滤的
	 * 
	 * 3、底层读写的时候解决了读半包的问题，那粘包呢？
	 * 读半包简单说明下，就是底层在读的时候，发现Channel中的数据全部刚好读完，那可能表示数据还有些没有全部读进来，此时给Channel设置再读标志放回pipelin中，
	 * 等待下一次读取，知道所有数据读完。
	 * 粘包的问题貌似是由ByteToMessageDecoder的子类实现的
	 * 
	 */
	
}
