package com.spring.annotation.demo.bean;

import org.springframework.beans.factory.FactoryBean;

/**
 * @desc 创建一个Spring定义的FactoryBean
 * @author Administrator
 *
 */
public class ColorFactoryBean implements FactoryBean<Color> {

	/**
	 * @desc 返回一个Color对象，这个对象会添加到容器中
	 */
	@Override
	public Color getObject() throws Exception {
		System.out.println("ColorFactoryBean is execute");
		return new Color();
	}

	@Override
	public Class<?> getObjectType() {
		return Color.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

}
