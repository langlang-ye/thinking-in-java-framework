package com.langlang.circular.dependency.arguments;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


/**
 * A B 的构造方法存在循环依赖, 且三层缓存无法解决 和构造方法的例子一样
 * C D 工厂方法创建bean构成了循环依赖, 且三层缓存无法解决
 * E F 对象可以创建成功, 但是set方法 没有使用, @Autowired 没有起作用
 */
@ComponentScan("com.langlang.circular.dependency.arguments")
@Configuration
public class ArgumentConfig {

    // @Bean
    public A a(B b) {
        return new A(b);
    }

    // @Bean
    public B b(A a) {
        return new B(a);
    }

    // @Bean 注解, 标注的方法会作为工厂bean 方法, 创建指定的bean, 方法里的参数是从容器里获取自动注入(@Autowired 可以忽略)
    //@Bean
    public C c(D d) {
        C c = new C();
        c.setD(d);
        return c;
    }

    // @Bean
    public D d(C c) {
        D d = new D();
        d.setC(c);
        return d;
    }

    // @Bean
    public E e() {
        return new E();
    }

    // @Bean
    public F f() {
        return new F();
    }

    @Bean
    public K k(H h, G g) {
        K k = new K();
        h.setG(g);
        g.setH(h); // h g 相互依赖

        k.setH(h);
        k.setG(g);
        return k;
    }

}
