package com.langlang.other;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan("com.langlang.other")
@Configuration
public class OtherCon {

    @Bean
    public Star star() {

        return new Star();
    }


}
