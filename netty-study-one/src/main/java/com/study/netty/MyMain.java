package com.study.netty;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

import com.google.protobuf.InvalidProtocolBufferException;
import com.study.netty.handler.AsyncTimeServerHandler;
import com.study.netty.handler.MultiplexerTimeServer;
import com.study.netty.handler.TimeServerHandler;
import com.study.netty.netty.delimiter.DelimiterServer;
import com.study.netty.netty.fixedlength.FixedLengthServer;
import com.study.netty.netty.jboss_marshalling.MarshallingSubReqServer;
import com.study.netty.netty.netty.TimeServer;
import com.study.netty.netty.protobuf.SubscribeReqProto;
import com.study.netty.netty.protobuf.handler.ProtoSubReqServer;
import com.study.netty.netty.protobuf.test.TestSubscribeReqProto;
import com.study.netty.netty.serializable.SubReqServer;
import com.study.netty.protocol.custom.CustomServer;
import com.study.netty.protocol.file.handler.FileServer;
import com.study.netty.protocol.http.file.HttpFileServer;
import com.study.netty.protocol.http.xml.example.comm.HttpXmlServer;
import com.study.netty.protocol.udp.example.handler.ChineseProverbServer;
import com.study.netty.protocol.websocket.example.handler.WebSocketServer;
import com.study.netty.serializable.UserInfo;

public class MyMain {
	
	public static void main(String[] args) throws Exception {
		int port = 1234;
		if (args != null && args.length > 0) {
			try {
				port = Integer.valueOf(args[0]);
			} catch (NumberFormatException e) {
				// nothing
			}
		}
		// IOMain(args);
		// NIOMain(args);
		// AIOMain(args);
		// NettyMain(port);
		// DelimiterMain(port);
		// FixedLengthMain(port);
		// SerializableMain();
		// Serializable2Main();
		// SubSerializableMain(port);
		// ProtoSerializableMain();
		// ProtoMain(port);
		// MarshallingMain(port);
		// HttpMain(port);
		// HttpXmlMain(port);
		// WebSocketMain(port);
		// UdpMain(port);
		// FileMain(port);
		CustomMain();
	}
	
	/**
	 * 自定义协议栈
	 * @param port
	 * @throws InterruptedException 
	 */
	private static void CustomMain() throws InterruptedException {
		new CustomServer().bind();
	}
	
	/**
	 * 文件传输
	 * @param port
	 * @throws InterruptedException 
	 */
	private static void FileMain(int port) throws InterruptedException {
		new FileServer().run(port);
	}
	
	/**
	 * udp协议
	 * @param port
	 * @throws InterruptedException
	 */
	private static void UdpMain(int port) throws InterruptedException {
		new ChineseProverbServer().run(port);
	}
	
	/**
	 * webSocket协议
	 * @param port
	 * @throws InterruptedException
	 */
	private static void WebSocketMain(int port) throws InterruptedException {
		new WebSocketServer().run(port);
	}
	
	/**
	 * http协议，xml示例
	 * @param port
	 * @throws InterruptedException
	 */
	private static void HttpXmlMain(int port) throws InterruptedException {
		new HttpXmlServer().run(port);
	}
	
	/**
	 * 文件服务器示例
	 * @param port
	 * @throws InterruptedException
	 */
	private static void HttpMain(int port) throws InterruptedException {
		String url = HttpFileServer.DEFAULT_URL;
		new HttpFileServer().run(port, url);
	}
	
	private static void MarshallingMain(int port) throws InterruptedException {
		new MarshallingSubReqServer().bind(port);
	}
	
	private static void ProtoMain(int port) throws InterruptedException {
		new ProtoSubReqServer().bind(port);
	}
	
	private static void ProtoSerializableMain() throws InvalidProtocolBufferException {
		SubscribeReqProto.SubscribeReq req = TestSubscribeReqProto.createSubscribeReq();
		System.out.println("Before encode : " + req.toString());
		SubscribeReqProto.SubscribeReq req2 = TestSubscribeReqProto.decode(TestSubscribeReqProto.encode(req));
		System.out.println("After decode : " + req.toString());
		System.out.println("Asert equal : --> " + req2.equals(req));
	}
	
	private static void SubSerializableMain(int port) throws InterruptedException {
		new SubReqServer().bind(port);
	}
	
	private static void Serializable2Main() throws IOException {
		UserInfo info = new UserInfo();
		info.buildUserID(100).buildUserName("Welcome to Netty");
		int loop = 1000000;
		ByteArrayOutputStream bos = null;
		ObjectOutputStream os = null;
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < loop; i++) {
			bos = new ByteArrayOutputStream();
			os = new ObjectOutputStream(bos);
			os.writeObject(info);
			os.flush();
			os.close();
			byte[] b = bos.toByteArray();
			bos.close();
		}
		
		long endTime = System.currentTimeMillis();
		System.out.println("The jdk serializable cost time is : " + (endTime - startTime) + " ms");
		
		System.out.println("--------------------------------------------------------------------");
		
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		startTime = System.currentTimeMillis();
		for (int i = 0; i < loop; i++) {
			byte[] b = info.codeC(buffer);
		}
		endTime = System.currentTimeMillis();
		System.out.println("The byte array serializable cost time is : " + (endTime - startTime) + " ms");
	}
	
	private static void SerializableMain() throws IOException {
		UserInfo info = new UserInfo();
		info.buildUserID(100).buildUserName("Welcome to Netty");
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		ObjectOutputStream os = new ObjectOutputStream(bos);
		os.writeObject(info);
		os.flush();
		os.close();
		byte[] b = bos.toByteArray();
		System.out.println("The jdk serializable length is : " + b.length);
		bos.close();
		System.out.println("----------------------------------------------");
		System.out.println("The byte array serializable length is : " + info.codeC().length);
	}
	
	private static void FixedLengthMain(int port) throws Exception {
		new FixedLengthServer().bind(port);
	}
	
	private static void DelimiterMain(int port) throws Exception {
		new DelimiterServer().bind(port);
	}
	
	private static void NettyMain(int port) throws InterruptedException  {
		new TimeServer().bind(port);
	}

	private static void AIOMain(int port) throws IOException {
		AsyncTimeServerHandler timeServer = new AsyncTimeServerHandler(port);
		new Thread(timeServer, "AIO-AsyncTimeServerHandler-001").start();
	}
	
	private static void NIOMain(int port) throws IOException {
		MultiplexerTimeServer timeServer = new MultiplexerTimeServer(port);
		new Thread(timeServer, "NIO-MultiplexerTimeServer-001").start();
	}
	
	private static void IOMain(int port) throws IOException {
		ServerSocket server = null;
		try {
			server = new ServerSocket(port);
			System.out.println("The time server is start in port : " + port);
			Socket socket = null;
			while (true) {
				socket = server.accept();
				new Thread(new TimeServerHandler(socket)).start();
			}
		} finally {
			if (server != null) {
				System.out.println("The time server close");
				server.close();
				server = null;
			}
		}
	}
	
}
