package com.langlang.config;

import java.lang.annotation.Repeatable;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.validation.ValidationUtils;

import com.langlang.bean.Person;
import com.langlang.service.BookService;

/*
 配置中可以配置数组的，一个值 k=v 多个值写成 k = {v1, v2} 例如：
 classes = Controller.class
 classes = {Controller.class,Service.class}
 */

//配置类==配置文件
@Configuration	//告诉spring这是一个配置类, @Configuration 注解包含有  @Component 
/*@ComponentScan(value= "com.langlang", excludeFilters = {
		@Filter(type = FilterType.ANNOTATION, classes = {Controller.class})
})*/
/*@ComponentScan(value= "com.langlang", includeFilters = {
		@Filter(type = FilterType.ANNOTATION, classes = {Controller.class})
}, useDefaultFilters = false) 
 use-default-filters = false 禁用默认的扫描规则；默认的扫描规则就是扫描所有的，只有禁用这个规则后，只包含的配置才可以生效 
@ComponentScan // @Repeatable 它是一个可重复注解，如果使用jdk8 可以多写几个
*/
// @ComponentScans value 是 @ComponentScan 数组， 可以配置多个
@ComponentScans(
		value = {
				@ComponentScan(value= "com.langlang", includeFilters = {
					/*	@Filter(type = FilterType.ANNOTATION, classes = {Controller.class}),
						@Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {BookService.class}),*/
						@Filter(type = FilterType.CUSTOM, classes = {MyFilterType.class})
				}, useDefaultFilters = false)
		}
		)
// 			@Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {BookService.class})
//FilterType.ANNOTATION 按照注解
//FilterType.ASSIGNABLE_TYPE：按照给定的类型；
//FilterType.ASPECTJ：使用ASPECTJ表达式
//FilterType.REGEX：使用正则指定
//FilterType.CUSTOM：使用自定义规则

public class MainConfig {
	
	//给容器中注册一个bean; 类型为返回值的类型; id 默认是用方法名作为id
	@Bean("person")	// 默认的value 属性就是 bean 的name; 设置value 属性会覆盖掉方法名的id 
	public Person person01(){
		return new Person("lisi", 20);
		
	}

}
