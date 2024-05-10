package com.langlang.test;


import com.langlang.aop.MathCalculator;
import org.aspectj.lang.annotation.Aspect;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.langlang.config.MainConfig2;
import com.langlang.config.MainConifgOfAutowired;
import org.springframework.core.SpringVersion;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.Arrays;

public class MyTest {

	@Test
	public void te() {
		
		
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig2.class);
		
		System.out.println("ioc容器创建完成....");
		Object bean = applicationContext.getBean("person");
		Object bean2 = applicationContext.getBean("person");
		System.out.println(bean == bean2);
		
		String[] definitionNames = applicationContext.getBeanDefinitionNames();
		for (String name : definitionNames) {
			System.out.println(name);
			
		}
		Object bean3 = applicationContext.getBean("mainConfig2");
		System.out.println(bean3);
		MainConfig2 bean4 = applicationContext.getBean(MainConfig2.class);
		System.out.println(bean3 == bean4); // true
	}
	
	@Test
	public void defaultBeans() throws Exception {
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConifgOfAutowired.class);
		
		String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
		for (String name : beanDefinitionNames) {
			System.out.println(name + "   " + applicationContext.getBean(name));
			
		}

	}


	@Test
	public void testAspect() {
		String version = SpringVersion.getVersion();
		System.out.println(version);

		Aspect annotation = AnnotationUtils.findAnnotation(MathCalculator.class, Aspect.class);

		System.out.println(annotation);


	}


	@Test
	public void testInit() {
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();

		String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();

		System.out.println(Arrays.asList(beanDefinitionNames));

		System.out.println(beanDefinitionNames.length);

	}


}
