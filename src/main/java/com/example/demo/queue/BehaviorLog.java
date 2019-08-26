package com.example.demo.queue;

public class BehaviorLog {

	private Integer userId;

	public BehaviorLog(Integer userId) {
		this.userId = userId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "BehaviorLog [userId=" + userId + "]";
	}
}
