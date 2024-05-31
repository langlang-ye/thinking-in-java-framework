package com.langlang.circular.dependency.arguments;

import org.springframework.beans.factory.annotation.Autowired;

public class C {

    private D d;

    public void setD(@Autowired D d) {
        this.d = d;
    }
}
