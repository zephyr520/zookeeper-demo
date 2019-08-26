package com.example.demo.countdown;

import java.util.concurrent.CountDownLatch;

public class StationOfGuangZhou extends DangerCenter {

	public StationOfGuangZhou(CountDownLatch countDown) {
		super(countDown, "广州调度站");
	}

	@Override
	public void check() {
		System.out.println("正在检查 [" + this.getStation() + "]...");

		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("检查 [" + this.getStation() + "] 完毕，可以发车~");
	}

}
