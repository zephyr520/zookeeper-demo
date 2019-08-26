package com.example.demo;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.aspect.RateLimiterApsect;
import com.example.demo.config.DistributedLock;
import com.example.demo.queue.BehaviorLog;
import com.example.demo.queue.BehaviorLogQueue;
import com.google.common.util.concurrent.RateLimiter;

@RestController
@SpringBootApplication
public class SpringbootApplication {
	
	@Autowired
	DistributedLock distributedLock;

	public static void main(String[] args) {
		SpringApplication.run(SpringbootApplication.class, args);
	}
	
	RateLimiter limiter = RateLimiter.create(1.0); 
	
	@GetMapping("/gift/lottery")
	public String lottery(@RequestParam(value = "username", required = false) String userName) {
		System.out.println("等待的时间：" + limiter.acquire());
		return "hello, world!";
	}
	
	@RateLimiterApsect
	@GetMapping("/test")
	public String test() {
		return JSONObject.toJSONString("{\"code\":1001, \"msg\":\"success\"}");
	}
	
	@GetMapping("/curator1")
	public boolean curator1() {
		boolean flag = Boolean.FALSE;
		distributedLock.acquireLock("order");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            flag = distributedLock.releaseLock("order");
        }
        flag = distributedLock.releaseLock("order");
        return flag;
	}
	
	@GetMapping("/curator2")
	public boolean curator2() {
		boolean flag = Boolean.FALSE;
		distributedLock.acquireLock("order");
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
            flag = distributedLock.releaseLock("order");
        }
        flag = distributedLock.releaseLock("order");
        return flag;
	}
	
	@GetMapping("/addLog")
	public Boolean addLog() {
		return BehaviorLogQueue.getInstance().push(new BehaviorLog(new Random().nextInt(100000000)));
	}
}
