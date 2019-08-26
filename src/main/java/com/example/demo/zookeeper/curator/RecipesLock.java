package com.example.demo.zookeeper.curator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.RetryNTimes;

public class RecipesLock {

	private static final String ZK_SERVER_PATH = "192.168.1.235:2181";
	private CuratorFramework client = null;
	private static RetryPolicy retryPolicy = null;
	
	public RecipesLock() {
		/**
		 *	curator连接zookeeper的重试策略： RetryNTimes
		 *	n： 重试次数
		 *  sleepMsBetweenRetries：每次重试的间隔时间
		 */
		retryPolicy = new RetryNTimes(3, 5000);
		
		client = CuratorFrameworkFactory.builder()
				.connectString(ZK_SERVER_PATH)
				.retryPolicy(retryPolicy)
				.sessionTimeoutMs(60 * 1000)
				.connectionTimeoutMs(30 * 1000)
				.build();
		
		client.start();
	}
	
	public void closeZkClient() {
		if (client != null) {
			client.close();
		}
	}
	
	public static void main(String[] args) {
		RecipesLock recipesLock = new RecipesLock();
		String path = "/curator_recipes_lock";
		InterProcessMutex lock = new InterProcessMutex(recipesLock.getClient(), path);
		final CountDownLatch countDown = new CountDownLatch(1);
		for (int i=0; i<30; i++) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					try {
						countDown.await();
						lock.acquire();
						String orderNo = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
						System.out.println("订单号：" + orderNo);
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						try {
							lock.release();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}).start();
		}
		countDown.countDown();
		recipesLock.closeZkClient();
	}

	public CuratorFramework getClient() {
		return client;
	}

	public void setClient(CuratorFramework client) {
		this.client = client;
	}
}
