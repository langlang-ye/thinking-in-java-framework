package com.langlang.test;

import com.langlang.aop.MathCalculator;
import com.langlang.bean.Boss;
import com.langlang.bean.Car;
import com.langlang.bean.Color;
import com.langlang.config.MainConfigOfAOP;
import com.langlang.config.MainConifgOfAutowired;
import com.langlang.service.BookService;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.SpringVersion;

public class IOCTest_AOP {

	@Test
	public void test01() {
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfigOfAOP.class);

		//1. 直接创建对象的方式
//		MathCalculator mathCalculator = new MathCalculator();
//		mathCalculator.div(1, 1);

		MathCalculator mathCalculator = applicationContext.getBean(MathCalculator.class);
		mathCalculator.div(1, 0);


		
		applicationContext.close();
		
	}
	

	
}
