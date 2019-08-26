package com.example.demo.zookeeper;

import java.util.List;

import org.apache.zookeeper.AsyncCallback.ChildrenCallback;

public class ChildrenCallBack implements ChildrenCallback {

	@Override
	public void processResult(int rc, String path, Object ctx, List<String> children) {
		for (String node : children) {
			System.out.println(node);
		}
		System.out.println((String) ctx);
	}

}
