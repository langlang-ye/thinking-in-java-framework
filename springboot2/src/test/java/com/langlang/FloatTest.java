package com.langlang;

public class FloatTest {

    public static void main(String[] args) {

        float f = new Float("000092204142622");
        System.out.println(f);  //损失精度 9.2204147E10

        double d = new Double("000092204142622");
        System.out.println(d);  //应该使用double 转 9.2204142622E10

    }
}
