package com.study.netty;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.study.netty.handler.AsyncTimeServerHandler;
import com.study.netty.handler.MultiplexerTimeServer;
import com.study.netty.handler.TimeServerHandler;
import com.study.netty.netty.delimiter.DelimiterServer;
import com.study.netty.netty.fixedlength.FixedLengthServer;
import com.study.netty.netty.netty.TimeServer;

public class MyMain {
	
	public static void main(String[] args) throws Exception {
		int port = 8080;
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
		FixedLengthMain(port);
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
