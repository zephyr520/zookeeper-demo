package com.spring.annotation.demo.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class MyBeanPostProcessor implements BeanPostProcessor {
	/**
	 * 初始化方法调用之前执行此方法
	 */
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		System.out.println("执行MyBeanPostProcessor的postProcessBeforeInitialization方法" + beanName + " >>> " + bean);
		return bean;
	}

	/**
	 * 初始化方法调用完成之后，执行此方法
	 */
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		System.out.println("执行MyBeanPostProcessor的postProcessAfterInitialization方法" + beanName + " >>> " + bean);
		return bean;
	}

}
