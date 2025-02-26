package com.langlang.test;

import com.langlang.bean.Pig;

public class ArrayTest {

    public static void main(String[] args) {

        Pig pig = new Pig();
        Pig[] ps = new Pig[2];
        ps[0] = pig;
        ps[1] = pig;

        // 报错: Java 编译器在编译阶段进行了类型检查
        // System.out.println(ps instanceof Pig);
        Object os = ps;
        System.out.println(os instanceof Pig);  // false 实际类型是 Pig[]
        System.out.println(ps instanceof Pig[]); // true

        System.out.println(ps instanceof Object[]);//  true
        System.out.println(ps.getClass()); // class [Lcom.langlang.bean.Pig;
        System.out.println(ps.getClass().isArray()); //true

        // 获取数组具体的类型
        System.out.println(ps.getClass().getComponentType()); // class com.langlang.bean.Pig

    }

}
