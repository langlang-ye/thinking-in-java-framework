package com.langlang.bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//默认加在ioc容器中的组件，容器启动会调用无参构造器创建对象，再进行初始化赋值等操作
@Component
public class Boss {

	private Car car;
	
	private Dog dog;
	
	//构造器要用的组件，都是从容器中获取
	@Autowired	// TODO @Autowired 在构造器 或是方法参数位置 省略问题
	public Boss( Car car) { // @Autowired
		this.car = car;
		System.out.println("Boss...有参构造器");
	}
	
	public Boss(){
		System.out.println("************无参构造方法*************");
	}


	public Car getCar() {
		return car;
	}


//	@Autowired
	// 标注在方法，Spring容器创建当前对象，就会调用方法，完成赋值；
	// 方法使用的参数，自定义类型的值从ioc容器中获取
	public void setCar(@Autowired Car car) { // TODO setCar 参数位置的 @Autowired 省略是否生效 对比不同版本的区别
		this.car = car;
	}

	@Override
	public String toString() {
		return "Boss [car=" + car + "]";
	}

}
