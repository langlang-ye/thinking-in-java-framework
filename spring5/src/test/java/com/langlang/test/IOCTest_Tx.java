package com.langlang.test;


import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.langlang.tx.TxConfig;
import com.langlang.tx.UserService;

public class IOCTest_Tx {

	@Test
	public void test01() {
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(TxConfig.class);
		
		DataSource bean = applicationContext.getBean(DataSource.class);
		System.out.println(bean);
		

		UserService userService = applicationContext.getBean(UserService.class);
		userService.insertUser();
		
		applicationContext.close();
		
	}
	

	
}
