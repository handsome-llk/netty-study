package com.study.netty.study.question;

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
	 * 4、如何理解同步异步、阻塞非阻塞
	 * https://www.cnblogs.com/felixzh/p/10345929.html 这个讲的还可以。这里简单说下
	 * 当我们发起一个io请求的时候，系统完成io分为了两步：一是内核准备数据报；二是将数据从内核拷贝到用户空间。
	 * 而我们说的阻塞非阻塞是针对第一步，同步异步是针对第二步。 
	 * 
	 * 5、zero copy
	 * http://www.360doc.com/content/19/0528/13/99071_838741319.shtml 这个基础讲的挺详细的，但是mmap相关的没讲清楚。
	 * 这里简单说下：
	 * 传统io(normalIo.png)
	 * 1、调用read(),上下文切换(context switch)到内核，DMA把磁盘数据复制到内核的缓存空间
	 * 2、read()返回，上下文切换到用户进程，CPU把数据复制到用户的缓存空间
	 * 3、write() 上下文切换到内核，CPU把数据复制到内核socket缓存,write返回，上下文切换的进程
	 * 4、DMP把socket缓存数据复制到网卡缓存上
	 * 经过4次上下文切换，4次数据拷贝，在数据量比较大时，性能比sendfile方式低下
	 * 
	 * 然后比较重要的还有sendfile(sendfile.png)和mmap
	 * 
	 * mmap大致就是在用户内存和内核之间开辟了一块共享内存来减少copy次数
	 * 
	 * 
	 * 
	 * 
	 */
	
}
