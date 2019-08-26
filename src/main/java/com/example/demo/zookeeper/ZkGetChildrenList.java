package com.example.demo.zookeeper;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

/**
 * 获取子节点
 */
public class ZkGetChildrenList implements Watcher {
	
	private static final String ZK_SERVER_PATH = "192.168.1.235:2181";
	private static final int SESSION_TIME_OUT = 5000;
	private static ZooKeeper zk = null;
	private static CountDownLatch countDown = new CountDownLatch(1);
	
	public ZkGetChildrenList() {
	}
	
	public ZkGetChildrenList(String connectString) {
		try {
			zk = new ZooKeeper(connectString, SESSION_TIME_OUT, new ZkGetChildrenList());
			System.out.println("开始连接zookeeper服务器...");
			System.out.println("状态：" + zk.getState());
			countDown.await();
			System.out.println("状态：" + zk.getState());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void process(WatchedEvent event) {
		try {
			if (event.getState() == KeeperState.SyncConnected) {
				if (event.getType() == EventType.None && null == event.getPath()) {
					countDown.countDown();
				} else if(event.getType() == EventType.NodeChildrenChanged) {
					System.out.println("NodeChildrenChanged");
					List<String> childrenList = zk.getChildren(event.getPath(), true);
					for (String node : childrenList) {
						System.out.println(node);
					}
				} else if(event.getType() == EventType.NodeCreated) {
					System.out.println("NodeCreated");
				} else if(event.getType() == EventType.NodeDeleted) {
					System.out.println("NodeDeleted");
				} else if(event.getType() == EventType.NodeDataChanged) {
					System.out.println("NodeDataChanged");
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		ZkGetChildrenList zkServer = new ZkGetChildrenList(ZK_SERVER_PATH);
		
		// 同步  获取某节点下的所有子节点
//		List<String> childrenList = zk.getChildren("/zephyr", true);
//		for (String node : childrenList) {
//			System.out.println(node);
//		}
		
		// 异步调用
		String ctx = "{'callback':'ChildrenCallback'}";
//		zk.getChildren("/zephyr", true, new ChildrenCallBack(), ctx);
		zk.getChildren("/zephyr", true, new Children2CallBack(), ctx);
		
		Thread.sleep(Integer.MAX_VALUE);
	}

}
