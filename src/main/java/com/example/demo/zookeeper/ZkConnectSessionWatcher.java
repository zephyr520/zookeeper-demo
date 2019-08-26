package com.example.demo.zookeeper;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.data.Stat;

/**
 * zk会话(session)重连
 */
public class ZkConnectSessionWatcher implements Watcher {

	private static CountDownLatch countDown = new CountDownLatch(1);
	private static Stat stat = new Stat();
	private static final String ZK_SERVER_PATH = "192.168.1.235:2181";
	private static ZooKeeper zk = null;
	private static int SESSION_TIME_OUT = 5000;

	@Override
	public void process(WatchedEvent event) {
		// zk连接成功通知事件
		if (event.getState() == KeeperState.SyncConnected) {
			if (EventType.None == event.getType() && null == event.getPath()) {
				countDown.countDown();
			} else if (event.getType() == EventType.NodeDataChanged) {
				try {
					System.out.println("配置已修改，新值为：" + new String(zk.getData(event.getPath(), true, stat)));
				} catch (KeeperException | InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) throws Exception {
		String path = "/zephyr";
		zk = new ZooKeeper(ZK_SERVER_PATH, SESSION_TIME_OUT, new ZkConnectSessionWatcher());
		
		long sessionId = zk.getSessionId();
		byte[] sessionPasswd = zk.getSessionPasswd();
		
		System.out.println("客户端开始连接zookeeper服务器......");
		System.out.println("连接状态：" + zk.getState());
		
		// 等待zookeeper连接成功的通知
		countDown.await();
		
		System.out.println("连接状态：" + zk.getState());
		
		System.out.println("zephyr的节点信息：" + new String(zk.getData(path, true, stat)));
		
		System.out.println("----------------------------- ");
		System.out.println("zookeeper session会话重连......");
		zk = new ZooKeeper(ZK_SERVER_PATH, SESSION_TIME_OUT, new ZkConnectSessionWatcher(), sessionId, sessionPasswd);
		System.out.println("会话重连后的状态：" + zk.getState());
		Thread.sleep(30000);
		System.out.println("会话重连后的状态：" + zk.getState());
		System.out.println("会话重连后的zephyr的节点信息：" + new String(zk.getData(path, true, stat)));
	}

}
