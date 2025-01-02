package com.langlang.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 *
 */
@ConfigurationProperties(prefix = PropertyConfig.PROPERTY_PREFIX)
@Configuration
public class PropertyConfig extends Properties {

    /**
     * todo PropertyConfig 创建过程中使用的 RootBeanDefinition 对象 mbd,
     * mbd.getTargetType()  返回值 class com.langlang.config.PropertyConfig$$EnhancerBySpringCGLIB$$450f1dd8
     * mbd.getTargetType().getSuperclass() 返回值 class com.langlang.config.PropertyConfig
     * XXController XXService 的 RootBeanDefinition 对比一下 ?
     *
     *
     *
     */
    public static final String PROPERTY_PREFIX = "lang";

    private String firstName;
    private String lastName;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
