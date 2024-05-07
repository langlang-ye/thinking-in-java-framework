package com.langlang;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;


public class ConvertTypeTest {


    /**
     * 集合和数组的转换
     */
    @Test
    public void collectionToArray() {

        // 下面三种写法都是 声明了一个长度为 0 的 Object 数组, 用于类型转换 T[] a
        Object[] obj1 = new Object[]{};
        Object[] obj2 = new Object[0];
        Object[] obj3 = {};

        Set<String> set = new LinkedHashSet<>();
        set.add("aa");
        set.add("bb");
        set.add("cc");
        set.add("dd");

//        String[] array = set.toArray(new String[]{});
        String[] array = set.toArray(new String[0]); // spring 源码中
        Arrays.stream(array).forEach(System.out::println);

    }

    /**
     * {@link ArrayUtils } Apache Commons Lang3 包中的工具类
     * 内部原理: 使用循环将基本类型数组中的元素给复制到一个新的对象数组中
     */
    @Test
    public void IntegerTest() {

        // int[] --> Integer[]
//        int[] arr = {1, 2, 3, 4, 5, 6, 8, 0};
//        Integer[] integers = ArrayUtils.toObject(arr);
        // Integer[] --> int[]
//        int[] ints = ArrayUtils.toPrimitive(integers);


        // int[] --> Integer[]
        int[] arr = {1, 2, 3, 4, 5};
        Integer[] integers = Arrays.stream(arr).boxed().toArray(Integer[]::new);
        // Integer[] --> int[]
        int[] ints = Arrays.stream(integers).mapToInt(Integer::valueOf).toArray();

        // LongStream和DoubleStream 用法类似
    }


    @Test
    public void nullConvertType() {
        Object obj = null;

        // null 可以进行强制类型转换
        MantisShrimp type = (MantisShrimp) obj;
        System.out.println(type);

        // idea 选中System.out.println(type) 输出语句 Evaluate expression 回车 会在控制台进行输出

        // null 使用 instanceof 判断类型
        if (obj instanceof MantisShrimp) {
            System.out.println(true);
        } else {
            System.out.println(false);
        }

    }
}
