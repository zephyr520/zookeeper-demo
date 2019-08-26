package com.example.demo.zookeeper;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * zookeeper服务器上的节点数据的获取
 */
public class ZkNodeDataGet implements Watcher {
	
	private static CountDownLatch countDown = new CountDownLatch(1);
	private static Stat stat = new Stat();
	private static final String ZK_SERVER_PATH = "192.168.1.235:2181";
	private static final int SESSION_TIME_OUT = 5000;
	private static ZooKeeper zk = null;
	
	public ZkNodeDataGet() {
	}
	
	public ZkNodeDataGet(String connectString) {
		try {
			zk = new ZooKeeper(connectString, SESSION_TIME_OUT, new ZkNodeDataGet());
			System.out.println("客户端开始连接zookeeper服务器......");
			System.out.println("状态:" + zk.getState());
			countDown.await();
			System.out.println("状态:" + zk.getState());
		} catch (Exception e) {
			e.printStackTrace();
			if (zk != null) {
				try {
					zk.close();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	@Override
	public void process(WatchedEvent event) {
		try {
			if (event.getState() == KeeperState.SyncConnected) {
				if (event.getType() == EventType.None && event.getPath() == null) {
					countDown.countDown();
				} else if (event.getType() == EventType.NodeDataChanged) {
					byte[] byteData = zk.getData(event.getPath(), true, stat);
					String data = new String(byteData);
					System.out.println("更改后的值:" + data);
					System.out.println("版本号变化dversion：" + stat.getVersion());
				} else if(event.getType() == EventType.NodeCreated) {
					
				} else if(event.getType() == EventType.NodeChildrenChanged) {
					
				} else if(event.getType() == EventType.NodeDeleted) {
					
				} 
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		ZkNodeDataGet zkServer = new ZkNodeDataGet(ZK_SERVER_PATH);
		
		byte[] byteData = zk.getData("/zephyr", true, stat);
		String data = new String(byteData);
		System.out.println("/zephyr节点的当前数据是：" + data);
		
		Thread.sleep(Integer.MAX_VALUE);
	}
}
