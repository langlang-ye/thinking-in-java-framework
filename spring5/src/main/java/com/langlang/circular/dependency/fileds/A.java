package com.langlang.circular.dependency.fileds;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class A {

    // Field injection is not recommended
    @Autowired
    private B b;

}
