package com.example.demo.aspect;

import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.google.common.util.concurrent.RateLimiter;

@Aspect
@Scope
@Component
public class RateLimiterAop {

	@Autowired
	private HttpServletResponse response;

	// 比如说，我这里设置"并发数"为5
	private RateLimiter rateLimiter = RateLimiter.create(10.0);

	@Pointcut("@annotation(com.example.demo.aspect.RateLimiterApsect)")
	public void serviceLimit() {
	}

	@Around("serviceLimit()")
	public Object around(ProceedingJoinPoint joinPoint) {
		Boolean flag = rateLimiter.tryAcquire();
		Object obj = null;
		try {
			if (flag) {
				obj = joinPoint.proceed();
			} else {
				String result = JSONObject.toJSONString("{\"code\":100, \"msg\":\"failure\"}");
				output(response, result);
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		System.out.println("flag = " + flag + ", obj = " + obj);
		return obj;
	}
	
	public void output(HttpServletResponse response, String msg) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            outputStream.write(msg.getBytes("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            outputStream.flush();
            outputStream.close();
        }
    }
}
