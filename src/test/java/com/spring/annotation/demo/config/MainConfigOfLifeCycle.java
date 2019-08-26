package com.spring.annotation.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.spring.annotation.demo.bean.Car;
import com.spring.annotation.demo.bean.Cat;
import com.spring.annotation.demo.bean.MyBeanPostProcessor;
import com.spring.annotation.demo.bean.Tiger;

/**
 * bean的生命周期配置
 * 		bean的创建----初始化----销毁的过程
 * bean的生命周期有spring容器管理
 *	构造
 *		单实例：在容器启动的时候创建对象
 *		多实例：在每次获取的时候创建对象
 *	初始化
 *		对象创建完成，并赋值好，调用初始化方法
 *	销毁
 *		单实例：容器关闭的时候销毁
 *		多实例：容器不会管理这个bean，容器不会调用销毁方法
 * 
 * 
 * 		1、指定初始化和销毁方法(xml配置文件中，init-method="" 和destory-method="")
 * 		2、注解的方式 @Bean(initMethod = "init", destroyMethod = "destory")
 * 		3、实现InitializingBean定义初始化逻辑，实现DisposableBean进行销毁
 * 		4、@PostConstruct:在bean创建完成并且属性赋值完成，来执行初始化
 * 		   @PreDestory：在bean将要被移除之前，会调用
 * 		5、BeanPostProcessor:bean的后置处理器，在bean初始化前后执行一些处理工作
 * @author Administrator
 *
 */
@ComponentScan(basePackageClasses = {Cat.class})
@Configuration
public class MainConfigOfLifeCycle {

//	@Scope("prototype")
	@Bean(initMethod = "init", destroyMethod = "destory")
	public Car car() {
		return new Car();
	}
	
	@Bean
	public Tiger tiger() {
		return new Tiger();
	}
	
	@Bean
	public MyBeanPostProcessor myBeanPostProcessor() {
		return new MyBeanPostProcessor();
	}
}
