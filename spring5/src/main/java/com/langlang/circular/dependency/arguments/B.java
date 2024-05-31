package com.langlang.circular.dependency.arguments;

public class B {

    private A a;

    public B(A a) {
        this.a = a;
    }
}
