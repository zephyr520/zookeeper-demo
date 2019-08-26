package com.example.demo.zookeeper;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * zookeeper检查节点是否存在
 */
public class ZkNodeExistVerify implements Watcher {
	
	private static CountDownLatch countDown = new CountDownLatch(1);
	private static Stat stat = new Stat();
	private static final String ZK_SERVER_PATH = "192.168.1.235:2181";
	private static final int SESSION_TIME_OUT = 5000;
	private static ZooKeeper zk = null;
	
	public ZkNodeExistVerify() {}
	
	public ZkNodeExistVerify(String connectString) {
		try {
			zk = new ZooKeeper(connectString, SESSION_TIME_OUT, new ZkNodeExistVerify());
			System.out.println("开始连接zookeeper服务器......");
			System.out.println("状态：" + zk.getState());
			// 等待客户端连接上zookeeper服务器
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
				} else if (event.getType() == EventType.NodeCreated){
					
					System.out.println("创建节点：" + event.getPath());
					byte[] byteData = zk.getData(event.getPath(), true, stat);
					String data = new String(byteData);
					System.out.println("创建的节点信息：" + data);
					
				} else if(event.getType() == EventType.NodeDeleted) {
					
					System.out.println("删除节点：" + event.getPath());
					
				} else if(event.getType() == EventType.NodeDataChanged) {
					
					System.out.println("修改节点");
					byte[] byteData = zk.getData(event.getPath(), true, stat);
					String data = new String(byteData);
					System.out.println("修改的节点信息：" + data);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws Exception {
		ZkNodeExistVerify zkServer = new ZkNodeExistVerify(ZK_SERVER_PATH);
		
		Stat stat = zk.exists("/zephyr-test", true);
		if (stat == null) {
			System.out.println("节点不存在...");
		} else {
			System.out.println("节点版本号：" + stat.getVersion());
		}
		
		Thread.sleep(Integer.MAX_VALUE);
	}

}
