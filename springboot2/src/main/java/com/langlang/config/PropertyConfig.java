package com.langlang.config;

import com.langlang.analyze.SpringConfigurationPropertySourcesAnalyze;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * todo Duplicated prefix  ? public static final String PROPERTY_PREFIX_T = "lang";  不能有两个??
 *
 * 类声明:  class FilteredIterableConfigurationPropertiesSource extends FilteredConfigurationPropertiesSource
 * 		implements IterableConfigurationPropertySource
 * @see java.util.Properties
 * @see java.util.Hashtable
 * Properties 继承了 Hashtable, Hashtable 实现了 map 接口
 *
 *
 *
 */
@ConfigurationProperties(prefix = PropertyConfig.PROPERTY_PREFIX)
@Configuration
public class PropertyConfig extends Properties {

    /**
     * ConfigurationProperties 注解配置属性的生效的时机分析
     * PropertyConfig bean 在 doCreateBean 方法中, 再进入 initializeBean 方法中, 在 applyBeanPostProcessorsBeforeInitialization 方法中遍历的BeanPostProcessor,
     * ConfigurationPropertiesBindingPostProcessor#postProcessBeforeInitialization
     * public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
     *      看看 ConfigurationPropertiesBean#get 方法 返回 ConfigurationPropertiesBean#get 对象
     *      bind(ConfigurationPropertiesBean.get(this.applicationContext, bean, beanName));
     *      return bean;
     * }
     *  public static ConfigurationPropertiesBean get(ApplicationContext applicationContext, Object bean, String beanName) {
     * 		Method factoryMethod = findFactoryMethod(applicationContext, beanName);
     * 		return create(beanName, bean, bean.getClass(), factoryMethod); 再看create 方法
     * 	}
     * 	private static ConfigurationPropertiesBean create(String name, Object instance, Class<?> type, Method factory) {
     * 	ConfigurationProperties annotation = findAnnotation(instance, type, factory, ConfigurationProperties.class);
     * 	if (annotation == null) {
     * 		return null;
     * 	    }
     * 	Validated validated = findAnnotation(instance, type, factory, Validated.class);
     * 	Annotation[] annotations = (validated != null) ? new Annotation[] { annotation, validated }
     * 			: new Annotation[] { annotation };
     * 	ResolvableType bindType = (factory != null) ? ResolvableType.forMethodReturnType(factory)
     * 			: ResolvableType.forClass(type);
     * 	Bindable<Object> bindTarget = Bindable.of(bindType).withAnnotations(annotations);
     * 	if (instance != null) {  // instance 就是当前创建的 bean 实例 propertyConfig 看这个 withExistingValue 方法
     * 		bindTarget = bindTarget.withExistingValue(instance);
     *    }
     *   创建 ConfigurationPropertiesBean 对象
     * 	return new ConfigurationPropertiesBean(name, instance, annotation, bindTarget);
     * }
     * public Bindable<T> withExistingValue(T existingValue) {
     * 		Assert.isTrue(
     * 				existingValue == null || this.type.isArray() || this.boxedType.resolve().isInstance(existingValue),
     * 				() -> "ExistingValue must be an instance of " + this.type);
     * 		value 是 Supplier 函数 返回当前创建的bean 实例 后面会有用到
     * 		Supplier<T> value = (existingValue != null) ? () -> existingValue : null;
     * 		return new Bindable<>(this.type, this.boxedType, value, this.annotations, this.bindRestrictions);
     * 	    }
     *  再看 bind
     *  this.binder.bind(bean); 此时的 bean 是 ConfigurationPropertiesBean 对象 进入 bind 方法
     *  BindResult<?> bind(ConfigurationPropertiesBean propertiesBean) {
     *      asBindTarget 方法 直接返回了  return this.bindTarget;  很像是 getXXX 方法
     * 		Bindable<?> target = propertiesBean.asBindTarget();
     * 		ConfigurationProperties annotation = propertiesBean.getAnnotation();
     * 		BindHandler bindHandler = getBindHandler(target, annotation);
     * 		return getBinder().bind(annotation.prefix(), target, bindHandler);
     * 	 }
     * 进入 bind 方法 ...来到 Binder#bind
     * private <T> T bind(ConfigurationPropertyName name, Bindable<T> target, BindHandler handler, Context context,
     * 			boolean allowRecursiveBinding, boolean create) {
     * 		try {
     * 			Bindable<T> replacementTarget = handler.onStart(name, target, context);
     * 			if (replacementTarget == null) {
     * 				return handleBindResult(name, target, handler, context, null, create);
     * 			}
     * 			target = replacementTarget;
     * 			Object bound = bindObject(name, target, handler, context, allowRecursiveBinding);
     * 			return handleBindResult(name, target, handler, context, bound, create);
     * 			} catch (Exception ex) {
     * 			return handleBindError(name, target, handler, context, ex);
     *        }
     *    }
     *  看一下 onStart 方法  表达式为 false 返回了 target
     *  public <T> Bindable<T> onStart(ConfigurationPropertyName name, Bindable<T> target, BindContext context) {
     * 			return isConfigurationProperties(target.getType().resolve())
     * 					? target.withBindRestrictions(BindRestriction.NO_DIRECT_PROPERTY) : target;
     * 		        }
     * .... 来到 Binder#bindObject 方法  第一次调用
     * private <T> Object bindObject(ConfigurationPropertyName name, Bindable<T> target, BindHandler handler,
     * 			Context context, boolean allowRecursiveBinding) {
     * 	 @see SpringConfigurationPropertySourcesAnalyze  有 findProperty 方法实现细节
     * 	ConfigurationProperty property = findProperty(name, target, context);
     * 	if (property == null && context.depth != 0 && containsNoDescendantOf(context.getSources(), name)) {
     * 		return null;
     * 	}
     * 	AggregateBinder<?> aggregateBinder = getAggregateBinder(target, context); // 返回 MapBinder 对象
     * 	if (aggregateBinder != null) {
     * 		return bindAggregate(name, target, handler, context, aggregateBinder); 进入这个方法
     *  }
     * 	if (property != null) {
     * 		try {
     * 			return bindProperty(target, context, property);
     *        }
     * 		catch (ConverterNotFoundException ex) {
     * 			// We might still be able to bind it using the recursive binders
     * 			Object instance = bindDataObject(name, target, handler, context, allowRecursiveBinding);
     * 			if (instance != null) {
     * 				return instance;
     *            }
     * 			throw ex;
     *        }
     *    }
     * 	return bindDataObject(name, target, handler, context, allowRecursiveBinding);
     * }
     *
     * 进入 bindAggregate 方法
     * private <T> Object bindAggregate(ConfigurationPropertyName name, Bindable<T> target, BindHandler handler,
     * 			Context context, AggregateBinder<?> aggregateBinder) {
     * 	创建 AggregateElementBinder 接口的匿名对象, 接口有两个方法, 2个参数的bind 方法是 default 提供了默认实现,
     * 	默认实现的方法调用三个参数的bind 方法, 后续会用到
     * 		AggregateElementBinder elementBinder = (itemName, itemTarget, source) -> {
     * 			boolean allowRecursiveBinding = aggregateBinder.isAllowRecursiveBinding(source);
     * 			Supplier<?> supplier = () -> bind(itemName, itemTarget, handler, context, allowRecursiveBinding, false);
     * 			return context.withSource(source, supplier);
     * 		        };
     * 		return context.withIncreasedDepth(() -> aggregateBinder.bind(name, target, elementBinder));    * 	}
     * 	withIncreasedDepth(Supplier<T> supplier) 方法里 supplier.get()  来到 aggregateBinder.bind(name, target, elementBinder)
     * 	aggregateBinder 是MapBinder 对象, 它继承了AggregateBinder 抽象类, 没有重写方法bind 方法
     * 	来到 AggregateBinder#bind
     * 	final Object bind(ConfigurationPropertyName name, Bindable<?> target, AggregateElementBinder elementBinder) {
     * 		Object result = bindAggregate(name, target, elementBinder); 方法返回一个 LinkedHashMap 对象
     * 		Supplier<?> value = target.getValue(); Supplier 函数接口 返回当前的bean 实例
     * 		if (result == null || value == null) {
     * 			return result;
     * 		        }
     * 		return merge((Supplier<T>) value, (T) result);     	}
     * 	进入 bindAggregate 方法
     * 	protected Object bindAggregate(ConfigurationPropertyName name, Bindable<?> target,
     * 			AggregateElementBinder elementBinder) {
     * 	创建了一个 LinkedHashMap 对象, 最后返回
     * 		Map<Object, Object> map = CollectionFactory
     * 				.createMap((target.getValue() != null) ? Map.class : target.getType().resolve(Object.class), 0);
     * 		Bindable<?> resolvedTarget = resolveTarget(target);
     * 		boolean hasDescendants = hasDescendants(name);
     * 	 getContext().getSources() 是 SpringConfigurationPropertySources 对象 遍历
     *   @see SpringConfigurationPropertySourcesAnalyze 有这个遍历的分析
     * 		for (ConfigurationPropertySource source : getContext().getSources()) {
     * 			if (!ConfigurationPropertyName.EMPTY.equals(name)) {
     * 				ConfigurationProperty property = source.getConfigurationProperty(name);
     * 				if (property != null && !hasDescendants) {
     * 					getContext().setConfigurationProperty(property);
     * 					return getContext().getConverter().convert(property.getValue(), target);
     * 				                }
     * 				source = source.filter(name::isAncestorOf);
     * 				}
     * 	source 是 org.springframework.boot.context.properties.source.FilteredIterableConfigurationPropertiesSource 类对象
     *  这个对象有一个 source 属性, 展开看到 propertySource, 展开 这里面两个重要的属性
     *  name: Config resource 'class path resource [application.yaml]' via location 'optional:classpath:/'
     *  或者 Config resource 'class path resource [application.properties]' via location 'optional:classpath:/'
     *  source: Collections$UnmodifiableMap 对象 保存的是 key 是配置项的名字, value OriginTrackedValue$OriginTrackedCharSequence
     * 			new EntryBinder(name, resolvedTarget, elementBinder).bindEntries(source, map);
     *        }
     * 		return map.isEmpty() ? null : map;
     * }
     * 进入 bindEntries 方法, computeIfAbsent 方法给map 添加数据, 第一次肯定key 不存在
     * void bindEntries(ConfigurationPropertySource source, Map<Object, Object> map) {
     * 			if (source instanceof IterableConfigurationPropertySource) {
     *
     * 	source 是 FilteredIterableConfigurationPropertiesSource 对象, 它的 iterator 方法来自 IterableConfigurationPropertySource 接口提供的默认实现
     * 	default Iterator<ConfigurationPropertyName> iterator() {  return stream().iterator(); }
     * 	stream 方法是一个接口, 来到 FilteredIterableConfigurationPropertiesSource 实现的方法
     * 	public Stream<ConfigurationPropertyName> stream() {
     * 		return getSource().stream().filter(getFilter());
     * 	    }
     * 	protected Predicate<ConfigurationPropertyName> getFilter() { return this.filter; }  filter 过滤
     * 	来看 getSource() 方法
     * 	protected IterableConfigurationPropertySource getSource() { return (IterableConfigurationPropertySource) super.getSource();}
     * 	super 调用父类的 FilteredConfigurationPropertiesSource 的方法
     * 	protected ConfigurationPropertySource getSource() { return this.source; }  this.source 是 SpringIterableConfigurationPropertySource 类型的
     * 	返回后来看 SpringIterableConfigurationPropertySource#stream 方法, 方法返回一个 stream 流
     * 	public Stream<ConfigurationPropertyName> stream() {
     * 		ConfigurationPropertyName[] names = getConfigurationPropertyNames();
     * 		return Arrays.stream(names).filter(Objects::nonNull);
     * 	}
     *
     * 	for (ConfigurationPropertyName name : (IterableConfigurationPropertySource) source) {
     * 					Bindable<?> valueBindable = getValueBindable(name);
     * 					ConfigurationPropertyName entryName = getEntryName(source, name);
     * 					Object key = getContext().getConverter().convert(getKeyName(entryName), this.keyType);
     * 					map.computeIfAbsent(key, (k) -> this.elementBinder.bind(entryName, valueBindable));
     * 				             }
     * 				   }
     *        }
     *  this.elementBinder.bind(entryName, valueBindable) 先来到接口的默认方法, 再到重载的方法, 再到 lambda 表达式重写的方法
     *  来到 context.withSource(source, supplier)  if (source == null) { return supplier.get(); }
     *  来到  () -> bind(itemName, itemTarget, handler, context, allowRecursiveBinding, false)
     *  private <T> T bind(ConfigurationPropertyName name, Bindable<T> target, BindHandler handler, Context context,
     * 			boolean allowRecursiveBinding, boolean create) {
     * 		try {
     * 			Bindable<T> replacementTarget = handler.onStart(name, target, context);
     * 			if (replacementTarget == null) {
     * 				return handleBindResult(name, target, handler, context, null, create);
     * 			            }
     * 			target = replacementTarget;
     * 			Object bound = bindObject(name, target, handler, context, allowRecursiveBinding);
     * 			return handleBindResult(name, target, handler, context, bound, create); // 把结果进行类型转换
     * 			} catch (Exception ex) {
     * 			return handleBindError(name, target, handler, context, ex);
     *        }
     *    }
     *
     *  Object bound = bindObject(name, target, handler, context, allowRecursiveBinding);
     *  进入 bindObject 方法 第二次调用
     *  private <T> Object bindObject(ConfigurationPropertyName name, Bindable<T> target, BindHandler handler,
     * 			Context context, boolean allowRecursiveBinding) {
     * 		property 是 ConfigurationProperty 对象 属性name: ConfigurationPropertyName(它的string 存储的配置项的key ) 类型 , value: Object 类型存储的配置的值
     * 		ConfigurationProperty property = findProperty(name, target, context);
     * 	    if (property == null && context.depth != 0 && containsNoDescendantOf(context.getSources(), name)) {
     * 			return null;
     * 		}
     * 	   AggregateBinder<?> aggregateBinder = getAggregateBinder(target, context);
     * 		if (aggregateBinder != null) {
     * 			return bindAggregate(name, target, handler, context, aggregateBinder);
     * 		}
     * 		if (property != null) {
     * 			try {
     * 				return bindProperty(target, context, property);   后面有分析 property 如何获取的
     * 			    }
     * 			    catch (ConverterNotFoundException ex) {
     * 				// We might still be able to bind it using the recursive binders
     * 				Object instance = bindDataObject(name, target, handler, context, allowRecursiveBinding);
     * 				if (instance != null) {
     * 					return instance;
     * 				                }
     * 				throw ex;
     * 				}
     * 			}
     * 		return bindDataObject(name, target, handler, context, allowRecursiveBinding);
     *    }
     *    这个方法获取到了配置的值, 并且替换占位符号, 进行类型转换, 最后结果返回.
     *    private <T> Object bindProperty(Bindable<T> target, Context context, ConfigurationProperty property) {
     * 		context.setConfigurationProperty(property);
     * 		Object result = property.getValue();
     * 		result = this.placeholdersResolver.resolvePlaceholders(result);
     * 		result = context.getConverter().convert(result, target);
     * 		return result;
     * 	 }
     *
     *  最后看一眼
     *  protected Map<Object, Object> merge(Supplier<Map<Object, Object>> existing, Map<Object, Object> additional) {
     *      getExistingIfPossible {return existing.get();  有异常返回 null  }
     *      Map<Object, Object> existingMap = getExistingIfPossible(existing);
     * 		if (existingMap == null) {  // 返回当前创建 bean 实例
     * 			return additional;
     * 		        }
     * 		try {
     * 	        // 当前创建的bean 继承了 Properties, putAll 把linkedHashMap 里面的数据都添加到了 当前bean 实例中
     * 			existingMap.putAll(additional);
     * 		   // 把当前bean 实例 复制一份返回
     * 		   // 在调试计算表达式 (copyIfPossible(existingMap)).equals( existingMap ) 返回 true; (copyIfPossible(existingMap)) == existingMap 返回false
     * 			return copyIfPossible(existingMap);
     *        }
     * 		catch (UnsupportedOperationException ex) {
     * 			Map<Object, Object> result = createNewMap(additional.getClass(), existingMap);
     * 			result.putAll(additional);
     * 			return result;
     *        }
     *   }
     *
     *
     *  再来看看  ConfigurationProperty property = findProperty(name, target, context);
     *  private <T> ConfigurationProperty findProperty(ConfigurationPropertyName name, Bindable<T> target,
     * 		Context context) {
     * 	if (name.isEmpty() || target.hasBindRestriction(BindRestriction.NO_DIRECT_PROPERTY)) {
     * 		return null;
     * 	}
     * 	for (ConfigurationPropertySource source : context.getSources()) {  // 这里的遍历细节可以看 SpringConfigurationPropertySourcesAnalyze
     * 	source 是 SpringIterableConfigurationPropertySource 类型
     * 	    ((SpringIterableConfigurationPropertySource) source).propertySource 是 OriginTrackedMapPropertySource 类型
     * 	    propertySource 属性继承自父类的  SpringConfigurationPropertySource
     * 	                                    private final PropertySource<?> propertySource;
     * 		ConfigurationProperty property = source.getConfigurationProperty(name);
     * 		if (property != null) {
     * 			return property;
     *        }
     *    }
     * 	return null;
     * }
     * 查看 source.getConfigurationProperty(name)
     *  public ConfigurationProperty getConfigurationProperty(ConfigurationPropertyName name) {
     * 	if (name == null) {
     * 		return null;
     * 	 }
     * 	 调用父类的 SpringConfigurationPropertySource#getConfigurationProperty 方法
     * 	ConfigurationProperty configurationProperty = super.getConfigurationProperty(name);
     * 	// configurationProperty 返回了 null
     * 	if (configurationProperty != null) {
     * 		return configurationProperty;
     *    }
     * 	for (String candidate : getMappings().getMapped(name)) {
     * 	// 这两个方法之前有分析过, 返回 OriginTrackedValue.OriginTrackedCharSequence
     * 		Object value = getPropertySource().getProperty(candidate);
     * 		if (value != null) {
     * 			Origin origin = PropertySourceOrigin.get(getPropertySource(), candidate);
     * 			return ConfigurationProperty.of(this, name, value, origin);
     *        }
     *    }
     * 	return null;
     *  }
     *  public ConfigurationProperty getConfigurationProperty(ConfigurationPropertyName name) {
     * 	if (name == null) {
     * 		return null;
     * 	 }
     * 	 this.mappers 是一个 PropertyMapper 类型的数组, 长度为1, 存储的是 PropertyMapper 实现类 DefaultPropertyMapper
     * 	for (PropertyMapper mapper : this.mappers) {
     * 		try {
     * 			for (String candidate : mapper.map(name)) { 看map 方法
     * 				Object value = getPropertySource().getProperty(candidate);
     * 				if (value != null) {
     * 					Origin origin = PropertySourceOrigin.get(this.propertySource, candidate);
     * 					return ConfigurationProperty.of(this, name, value, origin);
     *                }
     *            }
     *        }
     * 		catch (Exception ex) {
     *        }
     *    }
     * 	return null;
     * }
     * 来看 DefaultPropertyMapper#map 方法
     *  public List<String> map(ConfigurationPropertyName configurationPropertyName) {
     * 	// Use a local copy in case another thread changes things  --> 使用本地副本，以防其他线程更改内容
     * 	last 是 DefaultPropertyMapper.LastMapping 对象
     * 	LastMapping<ConfigurationPropertyName, List<String>> last = this.lastMappedConfigurationPropertyName;
     * 	看一下 isFrom 方法, 直接返回 return ObjectUtils.nullSafeEquals(from, this.from); == 判断出来同一个对象 结果是 true
     * 	if (last != null && last.isFrom(configurationPropertyName)) {
     * 		return last.getMapping(); 返回Collections$SingletonList 对象, 配置文件里配置的 key, 比如: lang.lastname
     *  }
     * 	String convertedName = configurationPropertyName.toString();
     * 	List<String> mapping = Collections.singletonList(convertedName);
     * 	this.lastMappedConfigurationPropertyName = new LastMapping<>(configurationPropertyName, mapping);
     * 	return mapping;
     * }
     *
     *  再回来看 getPropertySourceo() { return (EnumerablePropertySource<?>) super.getPropertySource(); }
     *  来到父类 SpringConfigurationPropertySource#getPropertySource() {
     *      	return this.propertySource; 返回的是 OriginTrackedMapPropertySource
     *  }
     *  再回来看 OriginTrackedMapPropertySource#getProperty(String name) {
     * 		Object value = super.getProperty(name);
     * 		if (value instanceof OriginTrackedValue) {
     * 	 如果 value 是 OriginTrackedValue 对象,强制转换成 OriginTrackedValue 对象 getValue 方法
     * 			return ((OriginTrackedValue) value).getValue();
     * 		  }
     * 		return value; 返回 null
     * 	}
     * 	看父类 MapPropertySource#getProperty(String name) {
     * 	OriginTrackedMapPropertySource的source 属性 ObjectUtils#UnmodifiableMap 里面存储的是一个配置项的k-v 键值对
     * 		return this.source.get(name);  注意key的大小写, 这里返回nuLl
     * 	}
     *  来看看 SpringIterableConfigurationPropertySource#getMappings() { return this.cache.get(this::createMappings, this::updateMappings); }
     *  this.cache 是 SoftReferenceConfigurationPropertyCache 类型 进入 get 方法
     *  T get(Supplier<T> factory, UnaryOperator<T> refreshAction) {
     * 		T value = getValue();  value 是 SpringIterableConfigurationPropertySource#Mappings 对象
     * 		if (value == null) {
     * 			value = refreshAction.apply(factory.get());
     * 			setValue(value);
     * 		        }
     * 		else if (hasExpired()) {  方法返回 false
     * 			value = refreshAction.apply(value);
     * 			setValue(value);
     *        }
     * 		if (!this.neverExpire) { neverExpire 是true 取反后 false
     * 			this.lastAccessed = now();
     *        }
     * 		return value;
     * 		}
     *  查看 SoftReferenceConfigurationPropertyCache#getValue 方法
     *  protected T getValue() {return this.value.get();  this.value 是SoftReference 对象  }
     *  接下来查看 SoftReference#get 方法 强制进入下一步
     *   public T get() {
     *         T o = super.get();
     *         if (o != null && this.timestamp != clock)
     *             this.timestamp = clock;
     *         return o;
     *     }
     *  调用 Reference 父类的 get 方法
     *  public T get() {
     *     return this.referent;  返回  SpringIterableConfigurationPropertySource#Mappings 对象
     *  }
     *  继续来看 SpringIterableConfigurationPropertySource#Mappings getMapped
     *  Set<String> getMapped(ConfigurationPropertyName configurationPropertyName) { configurationPropertyName string 属性值: lang.firstname
     *  this.mappings LinkedHashMap
     *  k: ConfigurationPropertyName 类型, value: HashSet<String>
     *      如: pay.version-pay.version; lang.firstname-lang.firstName
     * 		return this.mappings.getOrDefault(configurationPropertyName, Collections.emptySet()); 返回结果: HashSet 存储字符串 lang.firstName
     * 	  }
     *
     *
     * 	   PropertySourceOrigin.get(getPropertySource(), candidate);  getPropertySource() 返回 OriginTrackedMapPropertySource 对象
     * 	   public static Origin get(PropertySource<?> propertySource, String name) {
     * 		Origin origin = OriginLookup.getOrigin(propertySource, name);
     * 	    origin 非空 返回
     * 		return (origin != null) ? origin : new PropertySourceOrigin(propertySource, name);
     * 	   }
     *
     * 	   static <K> Origin getOrigin(Object source, K key) {
     * 		if (!(source instanceof OriginLookup)) { true 取反后false
     * 			return null;
     * 		        }
     * 		try {
     * 	        看它
     * 			return ((OriginLookup<K>) source).getOrigin(key);
     *        }
     * 		catch (Throwable ex) {
     * 			return null;
     *        }
     *    }
     *   public Origin getOrigin(String name) {
     * 		Object value = super.getProperty(name);  前面有分析过
     * 	    value 是 OriginTrackedValue.OriginTrackedCharSequence  对象
     * 		if (value instanceof OriginTrackedValue) {
     * 	      Origin 对象: resource: "class path resource [application.yaml]"
     * 	        location: "9:14"
     * 			return ((OriginTrackedValue) value).getOrigin();
     * 		}
     * 		return null;
     * 	}
     *     ConfigurationProperty.of(this, name, value, origin) {
     *         	if (value == null) {
     * 			return null;
     * 		        }
     * 		    调用构造方法最后返回 ConfigurationProperty 对象, 不为null 前面的循环判断后会返回
     * 		    这就是最终 Binder#bindObject(ConfigurationPropertyName, Bindable, BindHandler, Binder.Context, boolean) 第一行
     * 		    ConfigurationProperty property = findProperty(name, target, context) 代码的 返回结果
     * 	    	return new ConfigurationProperty(source, name, value, origin);
     *
     *     }
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
     *
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
        System.out.println("setFirstName: " + firstName);
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        System.out.println("setLastName: " + lastName);
        this.lastName = lastName;
    }
}
