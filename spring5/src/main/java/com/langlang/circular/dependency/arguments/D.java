package com.langlang.circular.dependency.arguments;

import org.springframework.beans.factory.annotation.Autowired;

public class D {

    private C c;

    public void setC(@Autowired C c) {
        this.c = c;
    }
}
