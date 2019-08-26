package com.example.demo.countdown;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CheckStartUp {

	private static List<DangerCenter> stationList;
	private static CountDownLatch countDown;
	private static final int STATION_SIZE = 3;

	public static boolean checkAllStation() throws Exception {
		// 初始化3个调度站
		countDown = new CountDownLatch(STATION_SIZE);
		// 把所有调度站添加到list中
		stationList = new ArrayList<>();
		stationList.add(new StationOfShenZhen(countDown));
		stationList.add(new StationOfGuangZhou(countDown));
		stationList.add(new StationOfDongGuan(countDown));

		ExecutorService es = Executors.newFixedThreadPool(stationList.size());
		for (DangerCenter dc : stationList) {
			es.execute(dc);
		}
		// 等待所有线程执行完成
		countDown.await();
		es.shutdown();
		for (DangerCenter dc : stationList) {
			if (!dc.isOk()) {
				return false;
			}
		}
		return true;
	}

	public static void main(String[] args) throws Exception {
		boolean checkResult = checkAllStation();
		System.out.println("监控中心针对所有危化品调度站点的检查结果为：" + checkResult);
	}

}
