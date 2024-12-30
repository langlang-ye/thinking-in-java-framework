package com.langlang.controller;

import com.langlang.service.HelloService;
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
        System.out.println("BayController start");

        helloService.tt();
        System.out.println("BayController end");
        return "Hello World";
    }


    @GetMapping("/bay")
    public String bay() {
        System.out.println("bay 22222222222222222222 start ");
        Integer id = 1001;

        String name = "tom";
        System.out.println("bay 22222222222222222222 mind ");

        System.out.println("say bay");
        System.out.println("bay 22222222222222222222 end ");
        return "bay";
    }
}
