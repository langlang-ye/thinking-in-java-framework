package com.langlang.circular.dependency.constructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * todo 同时多个构造方法, 会使用哪一个 ?
 */
@Component
public class A {

    public A() {
    }

    @Autowired  // 只有一个构造方法的时候, 也可以省略该注解
    public A(B b){}  // 方法参数B 加不加 @Autowired 都一样
}
