package com.spring.annotation.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import com.spring.annotation.demo.bean.ColorFactoryBean;
import com.spring.annotation.demo.bean.LinuxCondition;
import com.spring.annotation.demo.bean.MyImportSelector;
import com.spring.annotation.demo.bean.Person;
import com.spring.annotation.demo.bean.WindowsCondition;

@Configuration
@Import({MyImportSelector.class})
public class MainConfig {

	@Bean("billGates")
	@Conditional(WindowsCondition.class)
	public Person person01() {
		return new Person("Bill Gates", 80);
	}
	
	@Bean("linus")
	@Conditional(LinuxCondition.class)
	public Person person02() {
		return new Person("Linus", 75);
	}
	
	@Bean
	public ColorFactoryBean colorFactoryBean() {
		return new ColorFactoryBean();
	}
}
