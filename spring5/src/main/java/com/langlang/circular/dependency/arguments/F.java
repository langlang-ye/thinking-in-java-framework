package com.langlang.circular.dependency.arguments;

import org.springframework.beans.factory.annotation.Autowired;

public class F {

    private E e;

    public void setE(@Autowired E e) { // 方法  is never used,
        this.e = e;
    }
}
