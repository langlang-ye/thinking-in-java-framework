package com.langlang.controller;

import com.langlang.config.PropertyConfig;
import com.langlang.service.HelloService;
import org.springframework.beans.PropertyAccessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BayController {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    HelloService helloService;

    @GetMapping("/bayHello")
    public String bayHello() {
        System.out.println(applicationContext);
        return "Hello World";
    }


    @GetMapping("/bay")
    public String bay() {
        PropertyConfig propertyConfig = applicationContext.getBean("propertyConfig", PropertyConfig.class);
        System.out.println(propertyConfig.getFirstName());
        System.out.println(propertyConfig.getLastName());
        return "bay";
    }
}
