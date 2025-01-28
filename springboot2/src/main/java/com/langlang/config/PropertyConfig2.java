package com.langlang.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 *
 */
@ConfigurationProperties(prefix = PropertyConfig2.PROPERTY_PREFIX)
@Configuration
public class PropertyConfig2{

    /**
     * todo PropertyConfig 创建过程中使用的 RootBeanDefinition 对象 mbd,
     * mbd.getTargetType()  返回值 class com.langlang.config.PropertyConfig$$EnhancerBySpringCGLIB$$450f1dd8
     * mbd.getTargetType().getSuperclass() 返回值 class com.langlang.config.PropertyConfig
     * XXController XXService 的 RootBeanDefinition 对比一下 ?
     *
     * Controller  这里设置了 targetType
     * org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory#determineTargetType(java.lang.String, org.springframework.beans.factory.support.RootBeanDefinition, java.lang.Class[])
     *
     * todo ObjectUtils
     *
     *
     *
     *
     * org.springframework.boot.context.properties.source.SpringIterableConfigurationPropertySource
     *      #getConfigurationProperty(org.springframework.boot.context.properties.source.ConfigurationPropertyName)
     *
     * if (name == null) {
     * 			return null;
     * 		        }
     * 		ConfigurationProperty configurationProperty = super.getConfigurationProperty(name);
     * 		if (configurationProperty != null) {
     * 			return configurationProperty;
     *        }
     * 		for (String candidate : getMappings().getMapped(name)) {  // 获取值的细节
     * 			Object value = getPropertySource().getProperty(candidate);
     * 			if (value != null) {
     * 				Origin origin = PropertySourceOrigin.get(getPropertySource(), candidate);
     * 				return ConfigurationProperty.of(this, name, value, origin);
     *            }
     *        }
     * 		return null;
     *
     *
     *
     *
     */
    public static final String PROPERTY_PREFIX = "lang-t";

    private String firstName;
    private String lastName;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFirstName(String firstName) {
        System.out.println("setFirstName: " + firstName);
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        System.out.println("setLastName: " + lastName);
        this.lastName = lastName;
    }
}
