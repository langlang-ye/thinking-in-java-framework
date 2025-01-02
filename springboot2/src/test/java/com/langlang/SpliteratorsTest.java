package com.langlang;

import java.util.ArrayList;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;

public class SpliteratorsTest {

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("aa");
        list.add("bb");
        list.add("cc");
        list.add("dd");
        list.add("ee");
        list.add("ff");

        Spliterator<String> spliterator = Spliterators.spliterator(list, 0);
        System.out.println(spliterator);

    }
}
