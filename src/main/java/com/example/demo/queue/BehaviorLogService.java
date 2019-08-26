package com.example.demo.queue;

import org.springframework.stereotype.Service;

@Service
public class BehaviorLogService {

	public void saveBehaviorLog(BehaviorLog behaviorLog) {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println(behaviorLog);
	}
}
