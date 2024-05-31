package com.langlang.circular.dependency.constructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class B {

    public B() {
    }

    @Autowired
    public B(A a) {}
}
