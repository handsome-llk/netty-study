package com.study.netty.protocol.custom.readme;

public class README {

	/**
	 * Netty协议栈承载了业务内部各模块之间的消息交互和服务调用，它的主要功能如下：
	 * 1、基于netty的nio通信框架，提供高性能的异步通信能力
	 * 2、提供消息的编解码框架，可以实现pojo的序列化和反序列化
	 * 3、提供基于ip地址的白名单接入认证机制
	 * 4、链路的有效性校验机制
	 * 5、链路的断连重连机制
	 * 
	 * 
	 * 
	 * 可靠性设计：
	 * 
	 * 在凌晨等业务低谷时段，如果发生网络闪断、连接被Hang住等网络问题时，由于没有业务消息，应用进程很难发现。到了白天业务高峰期时，
	 * 会发生大量的网络通信失败，严重的会导致一段时间内无法处理业务消息。为了解决这个问题，在网络空闲时采纳心跳机制来检测链路的互通性，
	 * 一旦发现网络故障，立即关闭链路，主动重连。
	 * 
	 * 这里主要说明下心跳机制，因为在很多地方看到过，当时也不太理解：
	 * 1、当网络处于空闲状态持续时间达到T（连续周期T没有读写消息）时，客户端主动发送Ping心跳消息给服务端
	 * 2、如果在下一个周期T到来时客户端没有收到对方发送的Pong心跳应答消息或者读取到服务端发送的其他业务消息，则心跳失败计数器加1
	 * 3、每当客户端接收到服务器的业务消息或者Pong应答消息，将心跳失败计数器清零；当连续N次没有接收到服务端的Pong消息或者业务消息，
	 * 		则关闭链路，间隔INTERVAL时间后发起重连操作
	 * 4、服务端网络空闲状态持续时间达到T后，服务端将心跳失败计数器加1；只要接收到客户端发送的Ping消息或者其他业务消息，计数器清零
	 * 5、服务端连续N次没有接收到客户端的Ping消息或者其他业务消息，则关闭链路，释放资源，等待客户端重连
	 * 
	 * 
	 * 
	 * 
	 */
	
}