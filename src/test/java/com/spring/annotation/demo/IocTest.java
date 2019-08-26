package com.spring.annotation.demo;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import com.spring.annotation.demo.bean.Blue;
import com.spring.annotation.demo.bean.Person;
import com.spring.annotation.demo.config.MainConfigOfLifeCycle;

public class IocTest {
	
	private ApplicationContext context = null;

	@Test
	public void test() {
		context = new AnnotationConfigApplicationContext("com.spring");
		String[] beans = context.getBeanNamesForType(Person.class);
		for (String bean : beans) {
			System.out.println(bean);
		}
	}
	
	@Test
	public void printImport() {
		context = new AnnotationConfigApplicationContext("com.spring");
		String[] definitionNames = context.getBeanDefinitionNames();
		for (String definitionName : definitionNames) {
			System.out.println(definitionName);
		}
		Blue blue = context.getBean(Blue.class);
		System.out.println(blue);
		Object bean = context.getBean("colorFactoryBean");
		Object bean2 = context.getBean("colorFactoryBean");
		System.out.println("bean的类型：" + bean.getClass());
		System.out.println(bean == bean2);
		//默认获取到的是工厂bean调用的getObject创建的对象
		// 要获取工厂bean本身，就需要在ID前面加一个&
		Object bean3 = context.getBean("&colorFactoryBean");
		System.out.println("bean的类型：" + bean3.getClass());
	}
	
	@Test
	public void testIocLifeCycle() {
		context = new AnnotationConfigApplicationContext(MainConfigOfLifeCycle.class);
		System.out.println("容器初始化完成");
		((AbstractApplicationContext) context).close();
	}
}
