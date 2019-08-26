package com.example.demo.zookeeper;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * Zookeeper连接
 */
public class ZkConnect implements Watcher {
	
	private static CountDownLatch countDown = new CountDownLatch(1);
	private static Stat stat = new Stat();
	private static final String ZK_SERVER_PATH = "192.168.1.235:2181";
//	private static final String ZK_SERVER_PATH = "192.168.1.235:2181,192.168.1.235:2182,192.168.1.235:2183";
	private static ZooKeeper zk = null;
	private static int SESSION_TIME_OUT = 5000;

	public static void main(String[] args) throws Exception {
		String path = "/zephyr";
		// 连接zookeeper并且注册一个默认的监听器
		/***
		 * 客户端和zk服务器连接是一个异步的过程，当连接成功后，客户端会收到一个watch通知
		 * Zookeeper参数：
		 * connectString：连接服务器的IP字符串
		 * 		比如："192.168.1.235:2181,192.168.1.235:2182,192.168.1.235:2183"
		 * 		可以是一个ip，也可以是多个ip，一个ip代表单机，多个ip代表集群，也可以在ip后加路径
		 * sessionTimeout：超时时间，心跳收不到了，那就超时
		 * watcher: 通知事件，如果有对应的事件触发，则会收到一个通知，如果不需要，那就设置为null
		 * canBeReadOnly: 可读，当这个物理机节点断开后，还是可以读到数据，只是不能写，此时数据被读取到的可能是旧数据
		 * 				此处建议设置为false，不推荐使用
		 * sessionId: 会话ID
		 * sessionPasswd: 会话密码 ，当会话丢失后，可以根据sessionId和sessionPasswd重新获取会话
		 */
		zk = new ZooKeeper(ZK_SERVER_PATH, SESSION_TIME_OUT, new ZkConnect());
		
		System.out.println("客户端开始连接zookeeper服务器......");
		System.out.println("连接状态：" + zk.getState());
		
		// 等待zookeeper连接成功的通知
		countDown.await();
		
		System.out.println("连接状态：" + zk.getState());
		
		System.out.println("zephyr的节点信息：" + new String(zk.getData(path, true, stat)));
		
		Thread.sleep(Integer.MAX_VALUE);
	}

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

}
