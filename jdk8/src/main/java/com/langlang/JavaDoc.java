package com.langlang;

import java.util.ArrayList;
import java.util.Date;

/**
 * {@link ArrayList} 前面带有注释仍然可以跳转(command+鼠标左键 实现跳转)
 * {@link  [package.]<class>#[method||field]}
 * 主要用法如上，完整路径为【包名.类名#方法名或者字段名】
 * {@link ArrayList#size()}
 * {@link MantisShrimp#name } 私有的属性值或者私有方法会报红, 但不影响程序的运行
 * {@link java.util.Date} {@link Date ) // 类使用 import 导入后, 可以省略包名. 如果是跳转当前类可以省略包名和类名，直接使用
 * {@link ConvertTypeTest } 在同一包下，包名可以省略
 * {@link #test()}}  如果是跳转当前类可以省略包名和类名，直接使用
 * <p>
 * {@link <a href="https://www.baidu.com">这是一个网址</a>}  跳转到网址
 *
 * @see [package.]<class>#[method||field]  跳转到类、方法、字段
 * @see ArrayList   前面不可以带有注释， 否则不能跳转
 * @see ArrayList#size()
 * @see MantisShrimp#name
 *   @see <a href="https://www.baidu.com">这是一个网址</a>
 */
public class JavaDoc {

    /**
     * <a href="https://www.baidu.com">这是一个网址</a>
     * http://www.baidu.com
     */
    void test() {

    }
}
