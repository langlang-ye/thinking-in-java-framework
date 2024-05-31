package com.langlang.circular.dependency.arguments;

import org.springframework.beans.factory.annotation.Autowired;

public class E {

    private F f;

    public void setF(@Autowired F f) {
        this.f = f;
    }
}
