package com.langlang.controller;

import com.langlang.config.PayConfig;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController implements ApplicationContextAware {

    private ApplicationContext applicationContext;



    @GetMapping("/hello")
    public String sayHello() {
        System.out.println("hello 111111111111111111 start ");
        PayConfig payConfig = applicationContext.getBean("payConfig", PayConfig.class);

        System.out.println(payConfig.getVersion());
        System.out.println("hello 111111111111111111 mind ");

        System.out.println(payConfig.getPayId());
        System.out.println(payConfig.getUrl());
        System.out.println("hello 111111111111111111 end ");

        return "Hello World";
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
