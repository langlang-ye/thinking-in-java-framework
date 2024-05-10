package com.langlang.test;

import org.junit.Test;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import com.langlang.bean.Person;
import com.langlang.config.MainConfigOfPropertyValues;

public class IOCTest_PropertyValue {
	
	AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfigOfPropertyValues.class);
	
	@Test
	public void test01() {
		printBeans(applicationContext);
		System.out.println("===============");
		
		Person person = (Person) applicationContext.getBean("person");
		System.out.println(person);
		
		ConfigurableEnvironment environment = applicationContext.getEnvironment();
		String property = environment.getProperty("person.nickName"); // 配置文件里面的k/v 默认加载到环境变量中了
		System.out.println(property);
		applicationContext.close();
	}
	
	
	private void printBeans(AnnotationConfigApplicationContext applicationContext) {
		String[] definitionNames = applicationContext.getBeanDefinitionNames();
		for (String name : definitionNames) {
			System.out.println(name);
		}
	}
	
	
	@Test
	public void test02() {
		Resource resource = new ClassPathResource("beans.xml");
		GenericXmlApplicationContext applicationContext = new GenericXmlApplicationContext(resource);
		
		System.out.println("容器创建完成...");
		
		Person person = (Person) applicationContext.getBean("person");
		System.out.println(person);

		ConfigurableEnvironment environment = applicationContext.getEnvironment();
		String property = environment.getProperty("person.nickName");
		System.out.println(property);

		applicationContext.close();
	}
	

	@Test
	public void test03() {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("beans.xml");
		
		System.out.println("容器创建完成...");
		
		Person person = (Person) applicationContext.getBean("person");
		System.out.println(person);
		Environment environment = applicationContext.getEnvironment();
		String property = environment.getProperty("person.nickName");
		System.out.println(property);
		
		((AbstractApplicationContext) applicationContext).close();
	}
	
	@Test
	public void test04() {
		DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
		Resource resource = resourceLoader.getResource("beans.xml");
//		Resource resource = new ClassPathResource("beans.xml"); // resource 两种方式都可以
		
		
		DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
		XmlBeanDefinitionReader xmlBeanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
		xmlBeanDefinitionReader.loadBeanDefinitions(resource); // 不支持 el 表达式 ${person.nickName}
			
	
		System.out.println("容器创建完成...");
		
		Person person = (Person) beanFactory.getBean("person");
		System.out.println(person);
		
		// 报错: org.springframework.beans.factory.support.DefaultListableBeanFactory cannot be cast to org.springframework.context.ConfigurableApplicationContext
//		((ConfigurableApplicationContext)beanFactory).close(); 
		// getEnvironment()方法没有
		
	}
}
