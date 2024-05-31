package com.langlang.circular.dependency.arguments;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class H {

    private G g;

    public void setG(@Autowired G g) {
        this.g = g;
    }
}
