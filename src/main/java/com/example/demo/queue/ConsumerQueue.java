package com.example.demo.queue;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConsumerQueue {

	@Autowired
	private BehaviorLogService behaviorLogService;
	
	@PostConstruct
    public void startrtThread() {
        ExecutorService e = Executors.newFixedThreadPool(2);//两个大小的固定线程池
        e.submit(new PollBehaviorLog(behaviorLogService));
        e.submit(new PollBehaviorLog(behaviorLogService));
    }

    class PollBehaviorLog implements Runnable {
    	BehaviorLogService behaviorLogService;

        public PollBehaviorLog(BehaviorLogService behaviorLogService) {
            this.behaviorLogService = behaviorLogService;
        }

        @Override
        public void run() {
            while (true) {
                try {
                	BehaviorLog behaviorLog = BehaviorLogQueue.getInstance().poll();
                    if(behaviorLog != null){
                    	behaviorLogService.saveBehaviorLog(behaviorLog);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
