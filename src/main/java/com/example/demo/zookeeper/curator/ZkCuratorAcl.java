package com.example.demo.zookeeper.curator;

import java.util.ArrayList;
import java.util.List;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.ZooDefs.Perms;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.data.Stat;

import com.example.demo.zookeeper.AclUtils;

public class ZkCuratorAcl {
	
	private static final String ZK_SERVER_PATH = "192.168.1.235:2181";
	private CuratorFramework client = null;
	private static RetryPolicy retryPolicy = null;
	
	public ZkCuratorAcl() {
		retryPolicy = new RetryNTimes(3, 5000);
		
		client = CuratorFrameworkFactory.builder().authorization("digest", "zephyr:123456".getBytes())
				.connectionTimeoutMs(30 * 1000)
				.sessionTimeoutMs(60 * 1000)
				.connectString(ZK_SERVER_PATH).retryPolicy(retryPolicy)
				.namespace("curator-acl").build();
		
		client.start();
	}
	
	public void closeZkClient() {
		if (client != null) {
			client.close();
		}
	}

	public static void main(String[] args) throws Exception {
		ZkCuratorAcl cto = new ZkCuratorAcl();
		
		boolean isZkCuratorStarted = cto.getClient().isStarted();
		System.out.println("当前客户的状态：" + (isZkCuratorStarted ? "连接中" : "已关闭"));
		
		String nodePath = "/acl/father/child/sub";
		List<ACL> acls = new ArrayList<ACL>();
		Id id1 = new Id("digest", AclUtils.getDigestUserPwd("zephyr:123456"));
		Id id2 = new Id("digest", AclUtils.getDigestUserPwd("zephyr:123456"));
		acls.add(new ACL(Perms.ALL, id1));
		acls.add(new ACL(Perms.READ, id2));
		acls.add(new ACL(Perms.DELETE | Perms.CREATE, id2));
		
		// 创建节点
//		byte[] data = "spiderman".getBytes();
//		cto.getClient().create().creatingParentsIfNeeded()
//				.withMode(CreateMode.PERSISTENT)
//				.withACL(acls)
//				.forPath(nodePath, data);
		
		// 给指定节点设置ACl权限
//		cto.getClient().setACL().withACL(acls).forPath("/curatorNode");
		
		// 更新节点数据
//		byte[] data = "batMan".getBytes();
//		cto.getClient().setData().withVersion(0).forPath(nodePath, data);
		
		// 获取节点数据
		Stat stat = new Stat();
		byte[] data = cto.getClient().getData().storingStatIn(stat).forPath(nodePath);
		System.out.println("节点数据：" + new String(data));
		System.out.println("节点的版本号：" + stat.getVersion());
		
		// 删除节点
		cto.getClient().delete()
					.guaranteed()
					.deletingChildrenIfNeeded()
					.withVersion(stat.getVersion())
					.forPath(nodePath);
		
		cto.closeZkClient();
		
		boolean isZkCuratorStarted2 = cto.getClient().isStarted();
		System.out.println("当前客户的状态：" + (isZkCuratorStarted2 ? "连接中" : "已关闭"));
	}

	public CuratorFramework getClient() {
		return client;
	}

	public void setClient(CuratorFramework client) {
		this.client = client;
	}

}
