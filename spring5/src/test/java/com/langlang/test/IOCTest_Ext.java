package com.langlang.test;

import org.junit.Test;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.event.ApplicationContextEvent;

import com.langlang.ext.ExtConfig;

import java.util.Arrays;

public class IOCTest_Ext {

	@Test
	public void test01() {
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(ExtConfig.class);

		applicationContext.publishEvent(new ApplicationEvent(new String("我发布的时间")) {
		});
		
//		applicationContext.publishEvent(new ApplicationContextEvent(applicationContext) {
//		});

		String[] names = applicationContext.getBeanDefinitionNames();
		System.out.println(Arrays.asList(names));


		applicationContext.close();
		
	}

	
}
