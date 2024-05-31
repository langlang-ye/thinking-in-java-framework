package com.langlang.circular.dependency.sets;

import org.springframework.beans.factory.annotation.Autowired;

public class B {

    private A a;

    @Autowired
    public void setA(A a) {
        this.a = a;
    }
}
