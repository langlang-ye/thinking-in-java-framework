package com.langlang.test;

import com.langlang.bean.Yellow;
import com.langlang.config.MainConfigOfProfile;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import javax.sql.DataSource;

public class IOCTest_Profile {

    //1、使用命令行动态参数: 在虚拟机参数位置加载 -Dspring.profiles.active=test
    // eclipse: run as--> run configurations 里面设置
    // idea: 选中要配置的方法 Edit Configurations 进行设置
    //2、代码的方式激活某种环境；
    @Test
    public void test01() {
        // 带参数的方法:   new AnnotationConfigApplicationContext(MainConfigOfProfile.class);
        //        this();
        //        this.register(componentClasses);
        //        this.refresh();  将三步骤拆分开来, 可以做一些配置
        //AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(MainConfigOfProfile.class);

        //1、创建一个applicationContext
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();

        //2、设置需要激活的环境
        applicationContext.getEnvironment().setActiveProfiles("dev");

        //3、注册主配置类
        applicationContext.register(MainConfigOfProfile.class);

        //4、启动刷新容器
        applicationContext.refresh();

        String[] namesForType = applicationContext.getBeanNamesForType(DataSource.class);
        for (String string : namesForType) {
            System.out.println(string);
        }


        Yellow bean = applicationContext.getBean(Yellow.class);
        System.out.println(bean);

        applicationContext.close();
    }

}
