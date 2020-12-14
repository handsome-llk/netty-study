package com.study.netty.study.bytebuf.test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

public class AtomicTest {

	/**
	 * 这个是用来递增某个对象中的某个成员变量用的
	 */
	private static AtomicIntegerFieldUpdater<AtomicTest> update 
		= AtomicIntegerFieldUpdater.newUpdater(AtomicTest.class, "b");
	
	private volatile int a = 0;
	private volatile int b = 0;	
	
	private static final int length = 30;
	private static final CountDownLatch countDownLatch = new CountDownLatch(length);
	
	
	public static void main(String[] args) throws InterruptedException {
		final AtomicTest num = new AtomicTest();
		for (int i = 0; i < length; i++) {
			new Thread(() -> {
				update.compareAndSet(num, num.a, num.a + 1);
				countDownLatch.countDown();
			}).start(); 
		}
		
		countDownLatch.await();
		System.out.println(num.a);
		System.out.println(num.b);
		
	}
}
