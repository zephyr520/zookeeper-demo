package com.example.demo.zookeeper.curator;

import java.util.List;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent.Type;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.retry.RetryNTimes;

public class ZkCuratorOperate {
	
	private static final String ZK_SERVER_PATH = "192.168.1.235:2181";
	private CuratorFramework client = null;
	private static RetryPolicy retryPolicy = null;
	
	public ZkCuratorOperate() {
		/**
		 * curator连接zookeeper的重试策略：ExponentialBackoffRetry
		 * 	baseSleepTimeMs： 初始sleep的时间
		 * 	maxRetries： 最大重试次数
		 * 	maxSleepMs： 最大重试时间
		 */
//		retryPolicy = new ExponentialBackoffRetry(1000, 5);
		
		/**
		 *	curator连接zookeeper的重试策略： RetryNTimes
		 *	n： 重试次数
		 *  sleepMsBetweenRetries：每次重试的间隔时间
		 */
		retryPolicy = new RetryNTimes(3, 5000);
		
		client = CuratorFrameworkFactory.builder().namespace("curator-namespace")
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

	public static void main(String[] args) throws Exception {
		ZkCuratorOperate cto = new ZkCuratorOperate();
		boolean isZkCuratorStated = cto.getClient().isStarted();
		System.out.println("当前客户的状态：" + (isZkCuratorStated ? "连接" : "关闭"));
		
		// 创建节点
		String nodePath = "/super/zephyr";
//		byte[] data = "zephyrtest".getBytes();
//		cto.getClient().create().creatingParentsIfNeeded()
//				.withMode(CreateMode.PERSISTENT)
//				.withACL(Ids.OPEN_ACL_UNSAFE)
//				.forPath(nodePath, data);
		
		// 更新节点数据
//		byte[] data = "spiderman".getBytes();
//		cto.getClient().setData().withVersion(0).forPath(nodePath, data);
		
		// 删除节点
//		cto.getClient().delete()
//				.guaranteed()
//				.deletingChildrenIfNeeded()
//				.withVersion(1)
//				.forPath(nodePath);
		
		// 读取节点数据
//		Stat stat = new Stat();
//		byte[] getData = cto.getClient().getData().storingStatIn(stat).forPath(nodePath);
//		System.out.println("当前节点的版本号：" + stat.getVersion());
//		System.out.println("当前节点的数据：" + new String(getData));
		
		// 读取子节点
//		List<String> childNodes = cto.getClient().getChildren().forPath(nodePath);
//		for (String childNode : childNodes) {
//			System.out.println(nodePath + "下的子节点有：" + childNode);
//		}
		
		// 判断节点是否存在,如果不存在则为空
//		Stat stat = cto.getClient().checkExists().forPath(nodePath + "/c");
//		if (stat == null) {
//			System.out.println("该节点不存在");
//		} else {
//			System.out.println("节点信息是：" + stat);
//		}
		
		// watcher 事件  当使用usingWatcher的时候，监听只会触发一次，监听完毕后就销毁
//		cto.client.getData().usingWatcher(new MyWatcher()).forPath(nodePath);
		
		
		// 给节点添加watcher事件
		// NodeCache: 监听数据节点的变更，会触发事件
//		final NodeCache nodeCache = new NodeCache(cto.getClient(), nodePath);
		// buildInitial : if set true表示初始化的时候获取node的值并且缓存
//		nodeCache.start(true);
//		ChildData childData = nodeCache.getCurrentData();
//		if (childData != null) {
//			System.out.println("节点初始化数据为：" + new String(childData.getData()));
//		} else {
//			System.out.println("节点初始化数据为空...");
//		}
		// 添加监听
//		nodeCache.getListenable().addListener(new NodeCacheListener() {
//			
//			@Override
//			public void nodeChanged() throws Exception {
//				if (nodeCache.getCurrentData() == null) {
//					System.out.println("缓存中的数据为空");
//					return ;
//				}
//				String data = new String(nodeCache.getCurrentData().getData());
//				System.out.println("节点路径：" + nodeCache.getCurrentData().getPath() + "数据：" + data);
//			}
//		});
		
		
		// 为子节点添加watcher
		String childNodePath = nodePath;
		// cacheData: 设置缓存节点的数据状态
		final PathChildrenCache childCache = new PathChildrenCache(cto.getClient(), childNodePath, true);
		/**
		 * StartMode: 初始化方式
		 * POST_INITIALIZED_EVENT：异步初始化，初始化之后会触发事件，会触INITIALIZED的执行
		 * NORMAL：异步初始化，会触发对现有节点和新节点的事件
		 * BUILD_INITIAL_CACHE：同步初始化
		 */
		childCache.start(StartMode.POST_INITIALIZED_EVENT);
		List<ChildData> childDatas = childCache.getCurrentData();
		System.out.println("当前数据节点的子节点数据列表：");
		for(ChildData childData : childDatas) {
			System.out.println(childNodePath + "的子节点是：" + childData.getPath() + "，子节点对应的数据是：" + new String(childData.getData()));
		}
		// 添加监听
		childCache.getListenable().addListener(new PathChildrenCacheListener() {
			
			@Override
			public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
				if (event.getType().equals(Type.INITIALIZED)) {
					System.out.println("子节点初始化ok...");
				} else if(event.getType().equals(Type.CHILD_ADDED)) {
					System.out.println("添加子节点：" + event.getData().getPath());
					System.out.println("添加子节点数据：" + new String(event.getData().getData()));
				} else if(event.getType().equals(Type.CHILD_UPDATED)) {
					System.out.println("修改子节点路径：" + event.getData().getPath());
					System.out.println("修改子节点数据：" + new String(event.getData().getData()));
				} else if(event.getType().equals(Type.CHILD_REMOVED)) {
					System.out.println("删除子节点：" + event.getData().getPath());
				}
			}
		});
		
		Thread.sleep(Integer.MAX_VALUE);
		
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
