package com.langlang.config;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.langlang.bean.Car;

/**
 * bean的生命周期：
 * 		bean创建---初始化----销毁的过程
 * 容器管理bean的生命周期；
 * 我们可以自定义初始化和销毁方法；容器在bean进行到当前生命周期的时候来调用我们自定义的初始化和销毁方法
 * 
 * 构造（对象创建）
 * 		单实例：在容器启动的时候创建对象
 * 		多实例：在每次获取的时候创建对象
 * 
 * BeanPostProcessor.postProcessBeforeInitialization
 * 初始化：
 * 		对象创建完成，并赋值(类的属性赋值)好，调用初始化方法。。。
 * BeanPostProcessor.postProcessAfterInitialization
 * 销毁：
 * 		单实例：容器关闭的时候
 * 		多实例：容器不会管理这个bean；容器不会调用销毁方法；
 * 
 * applyBeanPostProcessorsBeforeInitialization: 初始化之前执行
 * 遍历得到容器中所有的BeanPostProcessor；挨个执行beforeInitialization，
 * 一但返回null，跳出for循环，不会执行后面的BeanPostProcessor.postProcessorsBeforeInitialization
 * applyBeanPostProcessorsAfterInitialization 执行逻辑类似
 *
 * 
 * BeanPostProcessor原理
 * populateBean(beanName, mbd, instanceWrapper);给bean进行属性赋值
 * initializeBean
 * {
 * invokeAwareMethods(beanName, bean);  执行各种 aware 接口的回调方法
 * applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName); 初始化之前执行
 * invokeInitMethods(beanName, wrappedBean, mbd);执行自定义初始化
 * applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName); 初始化之后执行
 *}
 * 
 * 
 * 
 * 1）、指定初始化和销毁方法；
 * 		通过@Bean指定 init-method 和 destroy-method；
 * 2）、通过让Bean实现 InitializingBean（定义初始化逻辑），
 * 				DisposableBean（定义销毁逻辑）;
 * 3）、可以使用JSR250； jdk 自带的接口，   需要把项目JRE 调整到1.7以上
 * 		@PostConstruct：在bean创建完成并且属性赋值完成；来执行初始化方法
 * 		@PreDestroy：在容器销毁bean之前通知我们进行清理工作
 * 4）、BeanPostProcessor【interface】：bean的后置处理器；
 * 		在bean初始化前后进行一些处理工作；
 * 		postProcessBeforeInitialization:在初始化之前工作
 * 		postProcessAfterInitialization:在初始化之后工作
 * 
 * Spring底层对 BeanPostProcessor 的使用；
 * 		bean赋值，注入其他组件，@Autowired，生命周期[如: @PostConstruct @PreDestroy ]注解功能，@Async,xxx BeanPostProcessor;
 * 
 *
 */
@ComponentScan("com.langlang.bean")
@Configuration
public class MainConfigOfLifeCycle {
	
//	@Scope("prototype")
	@Bean(initMethod="init", destroyMethod="destroy") //在配置数据源的时候, 初始化方法赋值属性等, 销毁方法释放链接等
	public Car car() {
		// Car car = new Car();
		// car.setName("ll");
		// return car;
		 return new Car();
	}
	
}
