package com.example.demo;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.common.util.concurrent.RateLimiter;

public class RateLimiterTest {

	public static void main(String[] args) {
		String start = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		// 这里的1表示每秒允许处理的量为1个
		RateLimiter limiter = RateLimiter.create(1.0);
		for (int i=1; i<=10; i++) {
			double waitTime = limiter.acquire(i);
			System.out.println("cutTime = " + System.currentTimeMillis() + " call execute : " + i + " waitTime: " + waitTime);
		}
		String end = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		System.out.println("start time : " + start);
		System.out.println("end time : " + end);
	}

}
