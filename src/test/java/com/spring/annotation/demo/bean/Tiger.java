package com.spring.annotation.demo.bean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

public class Tiger {

	public Tiger() {
		System.out.println("Tiger constructor......");
	}
	
	@PostConstruct
	public void init() {
		System.out.println("Tiger @PostConstruct.......");
	}
	
	@PreDestroy
	public void destory() {
		System.out.println("Tiger @PreDestroy......");
	}
}
