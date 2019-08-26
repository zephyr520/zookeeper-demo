package com.example.demo.zookeeper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Perms;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;

/**
 *  Zookeeper 操作节点并设置acl权限
 */
public class ZkNodeAcl implements Watcher {
	
	private static final String ZK_SERVER_PATH = "192.168.1.235:2181";
	private static final int SESSION_TIME_OUT = 5000;
	private static CountDownLatch countDown = new CountDownLatch(1);
	private static ZooKeeper zk = null;
	
	public ZkNodeAcl() {}
	
	public ZkNodeAcl(String connectString) {
		try {
			zk = new ZooKeeper(connectString, SESSION_TIME_OUT, new ZkNodeAcl());
			System.out.println("zk连接状态：" + zk.getState());
			countDown.await();
			System.out.println("zk连接状态：" + zk.getState());
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

	public static void main(String[] args) throws Exception {
		ZkNodeAcl zkNodeAcl = new ZkNodeAcl(ZK_SERVER_PATH);
		
		// 任何人都可以访问
//		zkNodeAcl.createZKNode("/zephyr-acl", "aclTest".getBytes(), Ids.OPEN_ACL_UNSAFE);
		
		// 自定义用户认证访问
//		List<ACL> acls = new ArrayList<ACL>();
//		Id id1 = new Id("digest", AclUtils.getDigestUserPwd("zephyr:123456"));
//		Id id2 = new Id("digest", AclUtils.getDigestUserPwd("zephyr:123456"));
//		acls.add(new ACL(Perms.ALL, id1));
//		acls.add(new ACL(Perms.READ, id2));
//		acls.add(new ACL(Perms.DELETE | Perms.CREATE, id2));
		
//		zkNodeAcl.createZKNode("/zephyr-acl/testdigest", "testdigest".getBytes(), acls);
		
		// 注册过的用户必须通过addAuthInfo才能操作节点，linux命令是：addauth
//		zk.addAuthInfo("digest", "zephyr:123456".getBytes()); // 类似登录
//		zkNodeAcl.createZKNode("/zephyr-acl/testdigest/childtest", "childtest".getBytes(), Ids.CREATOR_ALL_ACL);
//		Stat stat = new Stat();
//		byte[] data = zk.getData("/zephyr-acl/testdigest", false, stat);
//		System.out.println("获取节点数据：" + new String(data));
//		zk.setData("/zephyr-acl/testdigest", "digest123".getBytes(), 0);
		
		// ip白名单的acl
		List<ACL> acls = new ArrayList<ACL>();
		Id ipId = new Id("ip", "192.168.1.45");
		acls.add(new ACL(Perms.ALL, ipId));
		zkNodeAcl.createZKNode("/zephyr-acl/iptest", "iptest".getBytes(), acls);
		
		// 验证ip是否有权限
		Stat stat = new Stat();
		byte[] data = zk.getData("/zephyr-acl/iptest", false, stat);
		System.out.println("原始IP节点数据：" + new String(data));
		System.out.println("版本号：" + stat.getVersion());
//		zk.setData("/zephyr-acl/iptest", "now".getBytes(), stat.getVersion());
	}

	@Override
	public void process(WatchedEvent event) {
		try {
			if (event.getState() == KeeperState.SyncConnected) {
				if (event.getType() == EventType.None && null == event.getPath()) {
					countDown.countDown();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
