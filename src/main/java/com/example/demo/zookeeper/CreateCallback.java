package com.example.demo.zookeeper;

import org.apache.zookeeper.AsyncCallback.StringCallback;

public class CreateCallback implements StringCallback {

	@Override
	public void processResult(int rc, String path, Object ctx, String name) {
		System.out.println("创建节点：" + path);
		System.out.println((String) ctx);
	}

}
