package com.study.netty.study.channelHandler;

/**
 * ChannelHandler源码讲解
 * @author lilk
 *
 */
public class README_ChannelHandler {

	/**
	 * ChannelHandler类似于Servlet的Filter过滤器，负责对I/O事件或者I/O操作进行拦截和处理，它可以选择性地拦截和处理自己感兴趣的事件，
	 * 也可以透传和终止事件的传递。
	 * 
	 * 基于ChannelHandler接口，用户可以方便地进行业务逻辑定制，例如打印日志、统一封装异常信息、性能统计和消息编解码等。
	 * 
	 * ChannelHandler支持注解，目前支持的注解有两种。
	 * Sharable：多个ChannelPipeline共用同一个ChannelHandler
	 * Skip：被Skip注解的方法不会被调用，直接被忽略
	 * 
	 */
	
	/**
	 * ByteToMessageDecoder功能说明
	 * 利用I/O进行网络编程时，往往需要将读取到的字节数组或者字节缓冲区解码为业务可以使用的POJO对象。为了方便业务将ByteBuf解码城业务POJO对象，Netty提供了
	 * ByteToMessageDecoder抽象工具解码类。
	 * 
	 * 用户的解码器继承ByteToMessageDecoder，只需要实现void decode(ChannelHandlerContext ct, ByteBuf in, List<Object> out)
	 * 抽象方法即可完成ByteBuf到POJO对象的解码。
	 * 
	 * 由于ByteToMessageDecoder并没有考虑TCP粘包和组包等场景，读半包需要用户解码器自己负责处理（？底层处理的不是读半包吗TODO LILK）。正因为如此，
	 * 对于大多数场景不会直接继承ByteToMessageDecoder，而是继承另外一些更高级的解码器来屏蔽半包的处理。
	 * 
	 */
	
	/**
	 * MessageToMessageDecoder功能说明
	 * MessageToMessageDecoder实际上是Netty的二次解码器，它的职责是将一个对象二次解码为其他对象。
	 * 
	 * 为什么称它为二次解码器呢？我们知道，从SocketChannel读取到的TCP数据报是ByteBuffer，实际就是字节数组，我们首先需要将ByteBuffer缓冲区中
	 * 的数据报读取出来，并将其解码为Java对象；然后对Java对象根据某些规则做二次解码，将其解码为另一个POJO对象。因为MessageToMessageDecoder在
	 * ByteToMessageDecoder之后，所以称之为二次解码器。
	 * 
	 * 二次解码器在实际的商业项目中非常有用，以HTTP+XML协议栈为例，第一次解码往往是将字节数组解码成HttpRequest对象，然后对HttpRequest消息中的
	 * 消息体字符串进行二次解码，将XML格式的字符串解码为POJO对象，这就用到了二次解码器。
	 * 
	 * 事实上，做一个超级复杂的解码器将多个解码器组合成一个大而全的MessageToMessageDecoder解码器似乎也能解决多次解码的问题，但是采用这种方式的代码
	 * 可维护性会非常差。例如，如果我们打算在HTTP+XML协议栈中增加一个打印码流的功能，即首次解码获取HttpRequest对象之后打印XML格式的码流。如果采用
	 * 多个解码器组合，在中间插入一个打印消息体的Handler即可，不需要修改原有的代码；如果做一个大而全的解码器，就需要在解码的方法中增加打印码流的代码，
	 * 可扩展性和可维护性都会变差。
	 * 
	 * 用户的解码器只需要是实现void decode(ChannelHandlerContext ctx, I msg, List<Object> out)抽象方法即可，由于它是将一个POJO
	 * 解码为另一个POJO，所以一般不会涉及到半包的处理，相对于ByteToMessageDecoder更加简单些。
	 * 
	 */
	
	/**
	 * LengthFieldBasedFrameDecoder
	 * 
	 * 在编解码章节我们讲过TCP的粘包导致解码的时候需要考虑如何处理半包的问题，前面介绍了Netty提供的半包解码器LineBasedFrameDecoder
	 * 和DelimiterBasedFrameDecoder。现在进行第三种最通用的半包解码器——LengthFieldBasedFrameDecoder。
	 * 
	 * 如何区分一个整包消息，通常有如下4种做法。
	 * 1、固定长度，例如每120个字节代表一个整包消息，不足的前面补零。解码器在处理这类定长消息的时候比较简单，每次读到指定长度的字节后再进行解码。
	 * 2、通过回车换行符区分消息，例如FTP协议。这类区分消息的方式多用于文本协议。
	 * 3、通过分隔符区分整包消息。
	 * 4、通过指定长度来标识整包消息。
	 * 
	 * 如果消息是通过长度进行区分的，LengthFieldBAsedFrameDecoder都可以自动处理粘包和半包问题，只需要传入正确的参数，即可轻松搞定“读半包”问题
	 * 
	 * 这个类的参数蛮有意思的，这里说明一下：（参考https://www.jianshu.com/p/a0a51fd79f62）
	 * 1、lengthFieldOffset 
	 * 表示长度域的偏移量。按我的理解就是代表长度域的字节信息在字节数组索引第几位
	 * 2、lengthFieldLength 
	 * 表示长度域的长度。按我的理解就是长度域这个信息在字节数组中占的位数，即这个长度域信息长几个字节
	 * 3、lengthAdjustment
	 * 表示包体长度调整的大小
	 * 4、initialBytesToStrip
	 * 表示netty拿到一个完整的数据包之后向业务解码器传递之前，应该跳过多少字节
	 * 
	 * 拿一个比较详细的例子来说：
	 * 左边为完整的数据包，希望传到应用层时有效的数据报为右边这个。此时设置参数如下：
	 * (16 bytes)                                         (13 bytes)
	 * +------+--------+------+----------------+          +------+----------------+
	 * | HDR1 | Length | HDR2 | Actual Content | -------> | HDR2 | Actual Content |
	 * | 0xCA | 0x000C | 0xFE | "HELLO,WORLD"  |          | 0xFE | "HELLO,WORLD"  |
	 * +------+--------+------+----------------+          +------+----------------+
	 * 
	 * 这里HDR1长度为1，Length长度为2
	 * 
	 * 所以
	 * 长度域偏移量lengthFieldOffset为1
	 * 长度域长度lengthFieldLength为2
	 * 因为解码后需要包含HDR2长度，而消息体体长度为0x000C(12)，所以需要调成消息体长度加1。即lengthAdjustment为1
	 * 解码后，需要跳过HDR1和Length字段，这两个字段之后为3，所以initialBytesToStrip为3。
	 * 
	 * 但是有些协议中，lengthFieldLength的长度可能包含消息头，此时需要使用lengthAdjustment为负数来准确定位消息体长度。
	 * 
	 * 在pipeline中增加LengthFieldBasedFrameDecoder解码器，指定正确的参数组合，它可以将Netty的ByteBuf解码成单个的整包消息，
	 * 后面的业务解码器拿到的就是个完整的数据报，正常进行解码即可，不再需要额外考虑"读半包"问题，方便了业务消息的解码。
	 * 
	 */
	
	/**
	 * MessageToByteEncoder功能说明
	 * 
	 * MessageToByteEncoder负责将POJO对象编码成ByteBuf，用户的编码其继承MessageToByteEncoder,实现
	 * void encoder(ChannelHandlerContext ctx, I msg, ByteBuf out)接口。
	 * 
	 */
	
}









