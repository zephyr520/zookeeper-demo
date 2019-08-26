package com.example.demo.zookeeper;

import org.apache.zookeeper.AsyncCallback.VoidCallback;

public class DeleteCallback implements VoidCallback {

	@Override
	public void processResult(int rc, String path, Object ctx) {
		System.out.println("删除节点：" + path);
		System.out.println((String) ctx);
	}

}
