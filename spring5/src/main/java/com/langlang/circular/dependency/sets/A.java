package com.langlang.circular.dependency.sets;


import org.springframework.beans.factory.annotation.Autowired;

/**
 * 创建bean 的分支:
 * /org/springframework/beans/factory/support/AbstractAutowireCapableBeanFactory.java:1177
 * if (mbd.getFactoryMethodName() != null) {
 * 			return instantiateUsingFactoryMethod(beanName, mbd, args);
 * 	 }
 */
public class A {

    private B b;

    @Autowired
    public void setB(B b) {
        this.b = b;
    }
}
