package com.langlang.circular.dependency;

import com.langlang.circular.dependency.arguments.ArgumentConfig;
import com.langlang.circular.dependency.fileds.FiledConfig;
import com.langlang.circular.dependency.constructor.ConstructorConfig;
import com.langlang.circular.dependency.sets.SetConfig;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * spring 循环依赖&Autowired注入的三方法:
 *  1. field注入: 项目中最常用的, 最常见的
 *  缺点: 对于IOC容器之外的环境, 除了使用反射来提供它需要的依赖之外，无法复用该实现类. 存在潜在的隐患, 调用有可能出现空指针(NPE)异常
 *  2. 构造器注入: 在Spring4.x版本中推荐的注入方式就是这种，相较于上面的field注入方式而言，就显得有点难看，特别是当注入的依赖很多（5个以上）的时候，就会明显的发现代码显得很臃肿。 spring文档:
 *  The Spring team generally advocates constructor injection as it enables one to implement application components as immutable objects and to ensure that required dependencies are not null. Furthermore constructor-injected components are always returned to client (calling) code in a fully initialized state.
 *  简单说: 构造器注入能够保证注入的组件不可变，并且确保需要的依赖不为空。此外，构造器注入的依赖总是能够在返回客户端（组件）代码的时候保证完全初始化的状态。
 *  3. setter注入: 在 Spring3.x 刚推出的时候，推荐使用注入的就是这种，现在也基本没看到过这种注解方式，写起来麻烦，当初推荐Spring自然也有他的道理，这里我们引用一下Spring当时的原话:
 *  The Spring team generally advocates setter injection, because large numbers of constructor arguments can get unwieldy, especially when properties are optional. Setter methods also make objects of that class amenable to reconfiguration or re-injection later. Management through JMX MBeans is a compelling use case.
 *  Some purists favor constructor-based injection. Supplying all object dependencies means that the object is always returned to client (calling) code in a totally initialized state. The disadvantage is that the object becomes less amenable to reconfiguration and re-injection.
 *  简单说: 构造器注入参数太多了，显得很笨重，另外setter的方式能用让类在之后重新配置或者重新注入。
 *  构造器注入:
 *      1. 依赖不可变: 组件声明使用 final 修饰
 *      2. 依赖不为空: 构造器传入所需要的组件参数, 一定不能为空.
 *      3. 完全初始化的状态: 构造器所需要的组件参数肯定是完全初始化的状态的, 至少是 addSingletonFactory 方法早期暴露出来的单例bean
 */
public class CircularTest {

    @Test
    public void setConfigTest() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SetConfig.class);
        context.close();
    }

    @Test
    public void filedConfigTest() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(FiledConfig.class);
        context.close();
    }

    @Test
    public void constructorConfigTest() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ConstructorConfig.class);
        context.close();
    }

    @Test
    public void argumentConfigTest() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ArgumentConfig.class);
        context.close();
    }


}
