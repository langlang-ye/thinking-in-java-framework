package com.langlang.di;

import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class DiConfigTest {

    @Test
    public void test() {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(DiConfig.class);
        AddressService addressService = context.getBean("addressService", AddressService.class);
        System.out.println(addressService);
        context.close();
    }
}
