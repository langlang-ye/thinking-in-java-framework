package com.langlang.bean;

import org.springframework.stereotype.Component;

@Component
public class Car {
	
	/*private String name;
	
	public void setName(String name) {
		
		System.out.println("car... setName...");
		this.name = name;
	}
	
	public String getName() {
		return name;
	}*/
	

	public Car() {
		System.out.println("car constructor...");
	}
	
	public void init() {
		System.out.println("car...init...");
	}
	
	public void destroy() {
		System.out.println("car...destroy...");
	}
}
