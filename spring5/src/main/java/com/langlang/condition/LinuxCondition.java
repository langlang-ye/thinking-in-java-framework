package com.langlang.condition;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.core.env.Environment;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;
import org.springframework.core.type.StandardMethodMetadata;
import org.springframework.util.MultiValueMap;

import com.langlang.bean.Person;

//判断是否linux系统
public class LinuxCondition implements Condition{
	/**
	 * ConditionContext：判断条件能使用的上下文（环境）
	 * AnnotatedTypeMetadata：注释信息, 添加该注解(LinuxCondition)的类的元数据信息 所在方法的元信息
	 */
	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		// TODO 是否linux系统
		//1、能获取到ioc使用的beanfactory, 创建对象以及bean装配的工厂
		
		ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
		System.out.println(beanFactory);
		
		
		//2、获取类加载器
		ClassLoader classLoader = context.getClassLoader();
		
		//3、获取当前环境信息, 包括运行时的信息, 环境变量 虚拟机的信息
		Environment environment = context.getEnvironment();
		
		//4、获取到bean定义的注册类, 所有bean的定义都在这个类里注册, 可以查有没有哪个bean的定义, 也可以注册一个bean定义 等 
		BeanDefinitionRegistry registry = context.getRegistry();
		
		String property = environment.getProperty("os.name");
		// 可以判断容器中的bean注册情况，也可以给容器中注册bean
		boolean containsBeanDefinition = registry.containsBeanDefinition("person");
		if(containsBeanDefinition) {
			RootBeanDefinition beanDefinition = new RootBeanDefinition(Person.class);
			beanDefinition.setNonPublicAccessAllowed(true);
//			MutablePropertyValues propertyValues = new MutablePropertyValues();
//			propertyValues.add("name", "lisi");
//			propertyValues.add("age", 5);
//			beanDefinition.setPropertyValues(propertyValues); // 为注册的bean 设置属性值
			
//			MutablePropertyValues propertyValues = new MutablePropertyValues();
//			propertyValues.addPropertyValue(new PropertyValue("name", "new lisi"));
//			propertyValues.addPropertyValue(new PropertyValue("age", 3));
//			beanDefinition.setPropertyValues(propertyValues);
			
			beanDefinition.setAttribute("aa", "new---> lisi"); // 设置 beanDefinition 属性？ 
			beanDefinition.setAttribute("bb", 1);

//			Object attribute = beanDefinition.getAttribute("name");
//			System.out.println(attribute);
//			
//			String[] attributeNames = beanDefinition.attributeNames();
//			System.out.println("" + Arrays.asList(attributeNames));
			
			
			
//			registry.registerBeanDefinition("莉丝", beanDefinition);
		}
		
		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++");
		Class<? extends AnnotatedTypeMetadata> classType = metadata.getClass();
		System.out.println(classType);  // StandardMethodMetadata
	
		
		/* StandardMethodMetadata standardMethodMetadata = ((StandardMethodMetadata) metadata);
		System.out.println(standardMethodMetadata.getDeclaringClassName()); //方法所在的类 com.langlang.config.MainConfig2
		System.out.println(standardMethodMetadata.getMethodName()); // 方法名 person02
		System.out.println(standardMethodMetadata.getReturnTypeName()); // // 返回值 Person
		
		Method method = standardMethodMetadata.getIntrospectedMethod();
		System.out.println(method); // public com.langlang.bean.Person com.langlang.config.MainConfig2.person02()
		
		Annotation[] annotations = method.getAnnotations();
		System.out.println(Arrays.asList(annotations)); // 获取方法上标注的所有的注解
		
		MultiValueMap<String, Object> allAnnotationAttributes = standardMethodMetadata.getAllAnnotationAttributes("org.springframework.context.annotation.Bean");
		System.out.println(allAnnotationAttributes);
		
		System.out.println(annotations[1].annotationType().getCanonicalName());
		Map<String, Object> annotationAttributes = standardMethodMetadata.getAnnotationAttributes(annotations[2].annotationType().getCanonicalName());
		System.out.println(annotationAttributes); */
		
		System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++");
		
		
		if (property.contains("linux")) {
			return true;
		}
		
		return false;
	}

}
