package com.langlang.test;

import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.langlang.bean.Boss;
import com.langlang.bean.Car;
import com.langlang.bean.Color;
import com.langlang.config.MainConifgOfAutowired;
import com.langlang.dao.BookDao;
import com.langlang.service.BookService;

public class IOCTest_Autowired {


	@Test
	public void test01() {
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConifgOfAutowired.class);
		BookService bookService = applicationContext.getBean(BookService.class);
		System.out.println(bookService);
		
//		BookDao bean = applicationContext.getBean(BookDao.class);
//		System.out.println(bean);
		
		
		Boss boss = applicationContext.getBean(Boss.class);
		System.out.println(boss);
		Car car = applicationContext.getBean(Car.class);
		System.out.println(car);
		
		Color color = applicationContext.getBean(Color.class);
		System.out.println(color);
		
		System.out.println(applicationContext);
		
		applicationContext.close();
	}
	
	
	private void printBeans(AnnotationConfigApplicationContext applicationContext) {
		String[] definitionNames = applicationContext.getBeanDefinitionNames();
		for (String name : definitionNames) {
			System.out.println(name);
		}
	}
	
}
