package com.example.demo;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class HaspMapDeadLoopTest extends Thread {

	private static AtomicInteger atomic = new AtomicInteger(0);
	private static Map<Integer, Integer> map = new HashMap<>();
	@Override
	public void run() {
		Integer value = atomic.get();
		while(value < 100000) {
			map.put(value, value);
			atomic.incrementAndGet();
		}
	}
	
	public static void main(String[] args) {
		HaspMapDeadLoopTest hm1 = new HaspMapDeadLoopTest();
		HaspMapDeadLoopTest hm2 = new HaspMapDeadLoopTest();
		HaspMapDeadLoopTest hm3 = new HaspMapDeadLoopTest();
		HaspMapDeadLoopTest hm4 = new HaspMapDeadLoopTest();
		HaspMapDeadLoopTest hm5 = new HaspMapDeadLoopTest();
		hm1.start(); 		
		hm2.start();
		hm3.start();
		hm4.start();
		hm5.start();
	}
}
