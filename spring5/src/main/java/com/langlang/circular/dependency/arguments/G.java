package com.langlang.circular.dependency.arguments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class G {
    private H h;

    // @Autowired  // 标记方法可以注入
    public void setH(@Autowired H h) { // 标记到set 方法无效
        this.h = h;
    }
}
