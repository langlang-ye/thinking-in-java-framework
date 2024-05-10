package com.langlang.test;

import java.util.Map;

import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import com.langlang.bean.Blue;
import com.langlang.bean.Color;
import com.langlang.bean.ColorFactoryBean;
import com.langlang.bean.Person;
import com.langlang.bean.RainBow;
import com.langlang.config.MainConfig;
import com.langlang.config.MainConfig2;

public class IOCTest {
	
	AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig2.class);
	
	@Test
	public void testImport() {
		printBeans(applicationContext);
		Blue bean = applicationContext.getBean(Blue.class);
		System.out.println(bean);
		
//		Object b1 = applicationContext.getBean("colorFactoryBean");
//		Color c1 = applicationContext.getBean(Color.class);
//		ColorFactoryBean b2 = applicationContext.getBean(ColorFactoryBean.class);
//		Object b4 = applicationContext.getBean("&colorFactoryBean");
//		
//		System.out.println(b1);
//		System.out.println(c1);
//		System.out.println(b1 == c1); // true  都是color 对象
//		
//		System.out.println(b4);
//		System.out.println(b2);
//		System.out.println(b2 == b4); // true 都是ColorFactoryBean 对象
		
		// 工厂Bean 获取的是调用getObject 创建的对象
		Object bean2 = applicationContext.getBean("colorFactoryBean");
		Object bean3 = applicationContext.getBean("colorFactoryBean");
		System.out.println("bean的类型：" + bean2.getClass());
		System.out.println(bean2 == bean3);
		
		Object bean4 = applicationContext.getBean("&colorFactoryBean");
		System.out.println(bean4.getClass());
		
	}
	
	private void printBeans(AnnotationConfigApplicationContext applicationContext) {
		String[] definitionNames = applicationContext.getBeanDefinitionNames();
		for (String name : definitionNames) {
			System.out.println(name);
		}
	}
	
	
	@Test
	public void test03() {
		ConfigurableEnvironment environment = applicationContext.getEnvironment();
		//动态获取环境变量的值；Windows 10
		String property = environment.getProperty("os.name");
		System.out.println(property);
		
		// getBeanNamesForType():  只是获取bean 名字, 不是获取bean 不会触发延迟创建bean 对象
		String[] namesForType = applicationContext.getBeanNamesForType(Person.class);
		for (String name : namesForType) {
			System.out.println(name);
		}
		Map<String, Person> persons = applicationContext.getBeansOfType(Person.class);
		System.out.println(persons);	// bean 的id 为key, value 是bean 的值
		
		Person person = persons.get("莉丝");
		System.out.println(person);
		
			
	}
	
	
	@Test
	public void test02() {
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig2.class);
		/*String[] definitionNames = applicationContext.getBeanDefinitionNames();
		for (String name : definitionNames) {
			System.out.println(name);
		}
		
		Object bean = applicationContext.getBean("person");
		Object bean2 = applicationContext.getBean("person");
		System.out.println(bean == bean2);*/
		
		/*System.out.println("ioc容器创建完成....");
		Object bean = applicationContext.getBean("person");
		Object bean2 = applicationContext.getBean("person");
		System.out.println(bean == bean2);*/
		
		
//		System.out.println("ioc容器创建完成....");
//		Object bean = applicationContext.getBean("person");
//		Object bean2 = applicationContext.getBean("person");
//		System.out.println(bean == bean2);
		
	}
	
	
	
	@SuppressWarnings("resource")
	@Test
	public void test01() {
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig.class);
		String[] definitionNames = applicationContext.getBeanDefinitionNames();
		for (String name : definitionNames) {
			System.out.println(name);
		}
		
	}

}
