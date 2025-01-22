package com.langlang.analyze;

import org.springframework.boot.context.properties.source.ConfigurationPropertyState;

/**
 * org.springframework.boot.context.properties.source.SpringIterableConfigurationPropertySource
 * 类的声明 class SpringIterableConfigurationPropertySource extends SpringConfigurationPropertySource
 * 	implements IterableConfigurationPropertySource, CachingConfigurationPropertySource
 * @see org.springframework.boot.context.properties.source.MapConfigurationPropertySource
 *  调用路径:
 *  @see ConfigurationPropertyState search 方法
 *
 *  for (T item : source) { ... }
 *  source 是 MapConfigurationPropertySource 对象,
 *  public Iterator<ConfigurationPropertyName> iterator() {
 * 		return this.delegate.iterator();
 * 	    }
 *   this.delegate 是 SpringIterableConfigurationPropertySource 对象
 *   public Iterator<ConfigurationPropertyName> iterator() {
 * 		return new ConfigurationPropertyNamesIterator(getConfigurationPropertyNames());
 * 	}
 * 	 getConfigurationPropertyNames()  返回 ConfigurationPropertyName[]
 *
 * 	private final ConfigurationPropertyName[] names;
 * 	private int index = 0;
 * 	ConfigurationPropertyNamesIterator(ConfigurationPropertyName[] names) {
 * 		this.names = names;
 * 	}
 * 	public boolean hasNext() {
 * 		skipNulls();
 * 		return this.index < this.names.length;
 *  }
 *  public ConfigurationPropertyName next() {
 * 		skipNulls();
 * 		if (this.index >= this.names.length) {
 * 			throw new NoSuchElementException();
 * 		  }
 * 		return this.names[this.index++];
 * 	}
 *
 *
 */
public class SpringIterableConfigurationPropertySourceAnalyze {
}
