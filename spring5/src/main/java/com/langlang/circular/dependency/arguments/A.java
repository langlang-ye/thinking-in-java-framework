package com.langlang.circular.dependency.arguments;


public class A {

    private B b;

    public A(B b) {
        this.b = b;
    }
}
