package com.example.demo.zookeeper;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Stat;

/**
 * Zookeeper节点的操作
 */
public class ZkNodeOperate implements Watcher {

	private static final String ZK_SERVER_PATH = "192.168.1.235:2181";
	private static final int SESSION_TIME_OUT = 5000;
	private ZooKeeper zk = null;
	private volatile boolean connected;
	private CountDownLatch countDown = new CountDownLatch(1);
	
	public ZkNodeOperate() {
	}

	public ZkNodeOperate(String connectString) {
		try {
			System.out.println("客户端开始连接zookeeper服务器......");
			zk = new ZooKeeper(connectString, SESSION_TIME_OUT, this, false);
			countDown.await();
			connected = true;
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
		// zk连接成功通知事件
		if (event.getState() == KeeperState.SyncConnected) {
			if (EventType.None == event.getType() && null == event.getPath()) {
				System.out.println("zookeeper服务器连接成功......");
				countDown.countDown();
			} else if (event.getType() == EventType.NodeDataChanged) {
				System.out.println("节点信息已修改");
			}
		}
	}
	
	public boolean isConnected() {
        return connected;
    }

	public static void main(String[] args) throws Exception {
		ZkNodeOperate zkServer = new ZkNodeOperate(ZK_SERVER_PATH);
		
		// 创建节点
		zkServer.createZKNode("/testnode", "testnode".getBytes(), Ids.OPEN_ACL_UNSAFE);
		
		// 获取节点信息
		Stat stat = zkServer.getZk().setData("/testnode", "123".getBytes(), 0);
		System.out.println("当前节点的版本号：" + stat.getVersion());
		
		// 删除节点信息(同步)
//		zkServer.getZk().delete("/testnode", stat.getVersion());
		
		// 删除节点信息(异步)
		String ctx = "{'delete':'success'}";
		zkServer.getZk().delete("/testnode", stat.getVersion(), new DeleteCallback(), ctx);
		Thread.sleep(2000);
	}
	
	/**
	 * @description 创建zk节点
	 * @param path 节点
	 * @param data 节点存储的数据
	 * @param acls 节点访问权限
	 */
	public void createZKNode(String path, byte[] data, List<ACL> acls) {
		String result = "";
		try {
			/***
			 * 	同步或者异步创建节点，都不支持子节点的递归创建，异步有一个callback函数
			 * 	参数：
			 * 	path: 创建的路径
			 * 	data： 存储数据的byte[]
			 * 	acl: 控制权限策略
			 * 			Ids.OPEN_ACL_UNSAFE --> world:anyone:cdrwa
			 * 			Ids.CREATOR_ALL_ACL --> auth:user:password:cdrwa
			 * 	createMode: 节点类型，是一个枚举
			 * 			PERSISTENT：持久节点
			 * 			PERSISTENT_SEQUENTIAL：持久顺序节点
			 * 			EPHEMERAL：临时节点
			 * 			EPHEMERAL_SEQUENTIAL：临时顺序节点
			 */
			result = zk.create(path, data, acls, CreateMode.PERSISTENT);
			
			// 异步创建节点
//			String ctx = "{'create':'success'}";
//			zk.create(path, data, acls, CreateMode.PERSISTENT, new CreateCallback(), ctx);
			System.out.println("创建节点：\t" + result + "\t成功...");
			
			Thread.sleep(2000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ZooKeeper getZk() {
		return zk;
	}

	public void setZk(ZooKeeper zk) {
		this.zk = zk;
	}
}
