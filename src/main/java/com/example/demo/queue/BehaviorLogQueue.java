package com.example.demo.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 	行为日志队列
 */
public class BehaviorLogQueue {

	public static final int QUEUE_MAX_SIZE = 1000;
	
	private volatile static BehaviorLogQueue behaviorLogQueue = null;
	
	// 阻塞队列
	private BlockingQueue<BehaviorLog> blockingQueue = new LinkedBlockingQueue<>(QUEUE_MAX_SIZE);
	
	private BehaviorLogQueue() {}
	
	public static BehaviorLogQueue getInstance() {
		if (behaviorLogQueue == null) {
			synchronized (BehaviorLogQueue.class) {
				if (behaviorLogQueue == null) {
					behaviorLogQueue = new BehaviorLogQueue();
				}
			}
		}
		return behaviorLogQueue;
	}
	
	/**
	 * @desc 消息入队列， 队列满了，就抛出异常，不阻塞
	 * @param behaviorLog
	 * @return
	 */
	public boolean push(BehaviorLog behaviorLog) {
		return this.blockingQueue.add(behaviorLog);
	}
	
	/**
	 * @desc 消息出队列
	 * @return
	 */
	public BehaviorLog poll() {
		BehaviorLog result = null;
		try {
			result = this.blockingQueue.take();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public int size() {
		return this.blockingQueue.size();
	}
}
