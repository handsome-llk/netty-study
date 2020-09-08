package com.study.netty;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import com.study.netty.handle.AsyncTimeClientHandler;
import com.study.netty.handle.TimeClientHandle;
import com.study.netty.netty.delimiter.DelimiterClient;
import com.study.netty.netty.fixedlength.FixedLengthClient;
import com.study.netty.netty.jboss_marshalling.MarshallingSubReqClient;
import com.study.netty.netty.netty.TimeClient;
import com.study.netty.netty.protobuf.ProtoSubClient;
import com.study.netty.netty.serializable.SubReqClient;
import com.study.netty.protocol.custom.CustomClient;
import com.study.netty.protocol.http.xml.example.comm.HttpXmlClient;
import com.study.netty.protocol.udp.ChineseProverbClient;

public class MyMain {

	public static void main(String[] args) throws InterruptedException {
		int port = 8080;
		String host = "127.0.0.1";
		if (args != null && args.length > 0) {
			try {
				port = Integer.valueOf(args[0]);
			} catch(NumberFormatException e) {
				// ����Ĭ��ֵ
			}
		}
		// IOMain(args);
		// NIOMain(args);
		// AIOMain(args);
		// NettyMain(port);
		// DelimiterMain(port, host);
		// FixedLengthMain(port, host);
		// SubSerializableMain(port, host);
		// ProtoSubMain(port, host);
		// MarshallingMain(port, host);
		// HttpXmlMain(port, host);
		// UdpMain(port);
		CustomMain(port, host);
	}
	
	/**
	 * 自定义协议栈
	 * @param port
	 * @param host
	 * @throws InterruptedException 
	 */
	private static void CustomMain(int port, String host) throws InterruptedException {
		new CustomClient().connect(port, host);
	}
	
	/**
	 * udp协议
	 * @param port
	 * @throws InterruptedException
	 */
	private static void UdpMain(int port) throws InterruptedException {
		new ChineseProverbClient().run(port);
	}
	
	/**
	 * http协议，使用xml传输数据
	 * @param port
	 * @param host
	 * @throws InterruptedException
	 */
	private static void HttpXmlMain(int port, String host) throws InterruptedException {
		new HttpXmlClient().connect(port, host);
	}
	
	private static void MarshallingMain(int port, String host) throws InterruptedException {
		new MarshallingSubReqClient().connect(port, host);
	}
	
	private static void ProtoSubMain(int port, String host) throws InterruptedException {
		new ProtoSubClient().connect(port, host);
	}
	
	private static void SubSerializableMain(int port, String host) throws InterruptedException {
		new SubReqClient().connect(port, host);
	}
	
	private static void FixedLengthMain(int port, String host) throws InterruptedException {
		new FixedLengthClient().connect(port, host);
	}
	
	private static void DelimiterMain(int port, String host) throws InterruptedException {
		new DelimiterClient().connect(port, host);
	}
	
	private static void NettyMain(int port) throws InterruptedException {
		new TimeClient().connect(port, "127.0.0.1");
	}

	private static void AIOMain(int port) {
		new Thread(new AsyncTimeClientHandler("127.0.0.1", port), "AIO-AsyncTimeClientHandler-001").start();
	}
	
	private static void NIOMain(int port) {
		new Thread(new TimeClientHandle("127.0.0.1", port), "TimeClient-001").start();
	}
	
	private static void IOMain(int port) {
		Socket socket = null;
		BufferedReader in = null;
		PrintWriter out = null;
		try {
			socket = new Socket("127.0.0.1", port);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out = new PrintWriter(socket.getOutputStream(), true);
			TimeUnit.SECONDS.sleep(10);
			out.println("QUERY TIME ORDER");
			System.out.println("Send order 2 server succeed.");
			String resp = in.readLine();
			System.out.println("Now is : " + resp);
		} catch (Exception e) {
			// ����Ҫ����
		} finally {
			if (out != null) {
				out.close();
				out = null;
			}
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				in = null;
			}
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				socket = null;
			} 
		}
	}
	
}
