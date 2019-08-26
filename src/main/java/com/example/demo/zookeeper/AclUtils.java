package com.example.demo.zookeeper;

import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

public class AclUtils {

	public static String getDigestUserPwd(String id) throws Exception {
		return DigestAuthenticationProvider.generateDigest(id);
	}
	
	public static void main(String[] args) throws Exception {
		String id = "zephyr:123456";
		System.out.println(getDigestUserPwd(id));
	}
}
