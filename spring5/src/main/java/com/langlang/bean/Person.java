package com.langlang.bean;

import org.springframework.beans.factory.annotation.Value;

public class Person {
	
	//使用@Value赋值；
	//1、基本数值
	//2、可以写SpEL； #{}
	//3、可以写${}；取出配置文件【properties】中的值（在运行环境变量里面的值）

	@Value("张三")
	private String name;
	@Value("#{20-4}")
	private Integer age;
	
	@Value("${person.nickName}")
	private String nickName;
	
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	
	
	
	public Person(String name, Integer age) {
		super();
		/* if (name.equals("lisi")) {
			throw new RuntimeException("重复异常");
		} */
		this.name = name;
		this.age = age;
	}
	

	public Person() {
		super();
	}
	
	
	@Override
	public String toString() {
		return "Person [name=" + name + ", age=" + age + ", nickName=" + nickName + "]";
	}
	
	
	

}
