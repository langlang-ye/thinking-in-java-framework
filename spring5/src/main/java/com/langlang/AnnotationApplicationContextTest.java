package com.langlang;

import com.langlang.bean.Person;
import com.langlang.config.MainConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class AnnotationApplicationContextTest {
    public static void main(String[] args) {

        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfig.class);

        Object person = applicationContext.getBean("person");
        System.out.println(person);

        // Spring 容器中定义的所有 JavaBean 的名称
		String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
		for (String name : beanDefinitionNames) {
			System.out.println(name);
		}

        String[] beanNamesForType = applicationContext.getBeanNamesForType(Person.class);
        for (String name : beanNamesForType) {
            System.out.println(name);
        }


    }


}