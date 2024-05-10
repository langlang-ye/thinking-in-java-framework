package com.langlang.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.langlang.bean.Person;


// @PropertySource & @PropertySources 可重复注解, 参见  @ComponentScan & @ComponentScans
//使用@PropertySource读取外部配置文件中的k/v保存到运行的环境变量中;加载完外部的配置文件以后使用${}取出配置文件的值
@PropertySource(value={"classpath:person.properties"})
//@PropertySource(value={"classpath:/person.properties"})
//@PropertySource(value={"person.properties"})
@Configuration
@ComponentScan
public class MainConfigOfPropertyValues {
	
	@Bean
	public Person person(){
		return new Person();
	}

}
