package com.langlang.analyze;

// {@link org.springframework.boot.context.properties.source.SpringConfigurationPropertySources}
// @see org.springframework.boot.context.properties.source.SpringConfigurationPropertySources

/**
 * SpringConfigurationPropertySources 类修饰符是缺省的, @see @link  idea 会提示错误
 * SpringConfigurationPropertySources implements Iterable
 *
 * org.springframework.boot.context.properties.bind.Binder#findProperty(ConfigurationPropertyName name, Bindable <T> target,
 * 			Context context) {
 * 		for (ConfigurationPropertySource source : context.getSources()) {	//  context.getSources() 是SpringConfigurationPropertySources 的
 * 	对象, 有两个属性 sources, cache
 * 	}
 * 	来到 iterator 方法
 * 	return new SourcesIterator(this.sources.iterator(), this::adapt);
 * 	再看 SourcesIterator 构造方法
 *  SourcesIterator(Iterator<PropertySource<?>> iterator,
 * 				Function<PropertySource<?>, ConfigurationPropertySource> adapter) {
 * 			this.iterators = new ArrayDeque<>(4);
 * 			this.iterators.push(iterator);
 * 			this.adapter = adapter;
 * 	}
 * 	使用 iterator 遍历核心方法 hasNext next
 * 	两个方法都调用了 fetchNext() 来获取下一个, hasNext 根据 fetchNext 返回的值是否是null, 返回true|false,
 * 	next 将 fetchNext 返回的值返回, 把this.next 重置为 null
 *
 * 		private ConfigurationPropertySource fetchNext() {
 * 			if (this.next == null) {
 * 				if (this.iterators.isEmpty()) {
 * 					return null; // this.next 是null, 并且队列里的Iterator 也是null
 * 				                }
 * 				if (!this.iterators.peek().hasNext()) { // 查看队列头部的元素, 看它是否有个下一个, 不移除元素
 * 					this.iterators.pop(); // 没有, 移除队列头部元素
 * 					return fetchNext();
 *                }
 * 				PropertySource<?> candidate = this.iterators.peek().next();
 * 				if (candidate.getSource() instanceof ConfigurableEnvironment) {
 * 			        //获取的 candidate 是ConfigurableEnvironment 类型, push 到队列中
 * 					push((ConfigurableEnvironment) candidate.getSource());
 * 					return fetchNext();
 *                }
 * 				if (isIgnored(candidate)) {
 * 					return fetchNext();
 *                }
 *
 * 				this.next = this.adapter.apply(candidate);
 * 				}
 *
 * 			return this.next;
 * 		}
 *      private void push(ConfigurableEnvironment environment) {
 * 			this.iterators.push(environment.getPropertySources().iterator());
 * 		}
 *
 * 将 PropertySource 转化成 ConfigurationPropertySource 对象, cache 是一个 ConcurrentReferenceHashMap
 * 先从缓存中取, 如果没有再通过 SpringConfigurationPropertySource.from(source) 获取, 然后存在缓存中, 避免了重复解析
 * 	private ConfigurationPropertySource adapt(PropertySource<?> source) {
 * 		ConfigurationPropertySource result = this.cache.get(source);
 * 		// Most PropertySources test equality only using the source name, so we need to
 * 		// check the actual source hasn't also changed.
 * 		if (result != null && result.getUnderlyingSource() == source) {
 * 			return result;
 * 		        }
 * 		result = SpringConfigurationPropertySource.from(source);
 * 		if (source instanceof OriginLookup) {
 * 			result = result.withPrefix(((OriginLookup<?>) source).getPrefix());
 *        }
 * 		this.cache.put(source, result);
 * 		return result;
 * 		}
 *
 *
 */
public class SpringConfigurationPropertySourcesAnalyze {



}
