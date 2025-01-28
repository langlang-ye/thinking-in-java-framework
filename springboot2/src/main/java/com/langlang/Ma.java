package com.langlang;

import com.langlang.config.PropertyConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class Ma {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(Ma.class, args);
        PropertyConfig propertyConfig = applicationContext.getBean("propertyConfig", PropertyConfig.class);
        System.out.println(propertyConfig);


    }
}
