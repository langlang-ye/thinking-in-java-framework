package com.langlang.analyze;

import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySources;

/**
 * @see MutablePropertySources
 * @see PropertySources
 *
 */
public class MutablePropertySourcesAnalyze {

    /**
     MutablePropertySources implements PropertySources
        public interface PropertySources extends Iterable<PropertySource<?>> {
     类中属性:
     private final List<PropertySource<?>> propertySourceList = new CopyOnWriteArrayList<>();

     public MutablePropertySources(PropertySources propertySources) {
     this(); // 调用默认的无参构造方法
     for (PropertySource<?> propertySource : propertySources) {
     addLast(propertySource);  // 通过遍历把 PropertySource 加入到集合中, addLast 里有去重
     }

     iterator stream  size toString removeIfPresent 处理方式类似
     public Iterator<PropertySource<?>> iterator() {
     return this.propertySourceList.iterator(); // 遍历直接调用集合的iterator 方法
     }
     contains get 处理方式类似
     public boolean contains(String name) {
     for (PropertySource<?> propertySource : this.propertySourceList) {
     if (propertySource.getName().equals(name)) {
     return true;
     }
     }
     return false;
     }

     addFirst addLast addAtIndex 处理方式类似 都有去重
     public void addFirst(PropertySource<?> propertySource) {
     synchronized (this.propertySourceList) {
     removeIfPresent(propertySource);
     this.propertySourceList.add(0, propertySource);
     }
     }
     addBefore addAfter 指定位置添加, assertLegalRelativeAddition 不能根据自身位置添加
     public void addBefore(String relativePropertySourceName, PropertySource<?> propertySource) {
     assertLegalRelativeAddition(relativePropertySourceName, propertySource);
     synchronized (this.propertySourceList) {
     removeIfPresent(propertySource);
     int index = assertPresentAndGetIndex(relativePropertySourceName);
     addAtIndex(index, propertySource);
     }
     }
     返回指定 PropertySource 优先级
     public int precedenceOf(PropertySource<?> propertySource) {
     return this.propertySourceList.indexOf(propertySource);
     }

     // 根据名字删除
     public PropertySource<?> remove(String name) {
     synchronized (this.propertySourceList) {
     int index = this.propertySourceList.indexOf(PropertySource.named(name));
     return (index != -1 ? this.propertySourceList.remove(index) : null);
     }
     }
     根据名字替换
     public void replace(String name, PropertySource<?> propertySource) {
     synchronized (this.propertySourceList) {
     int index = assertPresentAndGetIndex(name);
     this.propertySourceList.set(index, propertySource);
     }
     }
     如果存在返回它的下标
     private int assertPresentAndGetIndex(String name) {
     int index = this.propertySourceList.indexOf(PropertySource.named(name));
     if (index == -1) {
     throw new IllegalArgumentException("PropertySource named '" + name + "' does not exist");
     }
     return index;
     }

     }


     */

}
