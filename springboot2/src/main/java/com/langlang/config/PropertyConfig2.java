package com.langlang.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 *
 */
@ConfigurationProperties(prefix = PropertyConfig2.PROPERTY_PREFIX)
@Configuration
public class PropertyConfig2 {

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
     *  ConfigurationProperties 注解配置属性的生效的时机分析
     *  @see PropertyConfig 解析
     *  在没有继承 Properties, 这里只看与前面不一样的地方
     *  来到 Binder#bindObject 方法
     *  private <T> Object bindObject(ConfigurationPropertyName name, Bindable<T> target, BindHandler handler,
     * 		Context context, boolean allowRecursiveBinding) {
     * 	ConfigurationProperty property = findProperty(name, target, context);
     * 	if (property == null && context.depth != 0 && containsNoDescendantOf(context.getSources(), name)) {
     * 		return null;
     * 	    }
     * 	AggregateBinder<?> aggregateBinder = getAggregateBinder(target, context);
     * 	if (aggregateBinder != null) {
     * 		return bindAggregate(name, target, handler, context, aggregateBinder);
     *    }
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
     * 进入 bindDataObject 方法
     * private Object bindDataObject(ConfigurationPropertyName name, Bindable<?> target, BindHandler handler,
     * 		Context context, boolean allowRecursiveBinding) {
     * 	if (isUnbindableBean(name, target, context)) {
     * 		return null;
     * 	    }
     * 	Class<?> type = target.getType().resolve(Object.class);
     * 	if (!allowRecursiveBinding && context.isBindingDataObject(type)) {
     * 		return null;
     *    }
     *    // lambda 表达式创建一个 DataObjectPropertyBinder 对象, 调用 bindProperty 方法时,
     * 	DataObjectPropertyBinder propertyBinder = (propertyName, propertyTarget) -> bind(name.append(propertyName),
     * 			propertyTarget, handler, context, false, false);
     * 	return context.withDataObject(type, () -> {
     * 	this.dataObjectBinders: Collections#UnmodifiableRandomAccessList 长度为 2, 1. ValueObjectBinder 2. JavaBeanBinder
     * 		for (DataObjectBinder dataObjectBinder : this.dataObjectBinders) {
     * 			Object instance = dataObjectBinder.bind(name, target, context, propertyBinder);
     * 			if (instance != null) {
     * 				return instance;
     *            }
     *        }
     * 		return null;
     *    });
     * }
     *  ValueObjectBinder 对象调用bind 方法返回了null
     *  Object instance = dataObjectBinder.bind(name, target, context, propertyBinder);
     *  来看 JavaBeanBinder 的调用
     *  public <T> T bind(ConfigurationPropertyName name, Bindable<T> target, Context context,
     * 			DataObjectPropertyBinder propertyBinder) {
     * 	    boolean hasKnownBindableProperties = target.getValue() != null && hasKnownBindableProperties(name, context); // true
     * 	    Bean<T> bean = Bean.get(target, hasKnownBindableProperties);
     * 	        if (bean == null) {
     * 	        	return null;
     * 	      }
     * 	    后面会分析 beanSupplier
     *  	BeanSupplier<T> beanSupplier = bean.getSupplier(target);
     * 	    boolean bound = bind(propertyBinder, bean, beanSupplier, context);
     * 	    return (bound ? beanSupplier.get() : null);
     *  }
     * 进入 bind 方法
     * private <T> boolean bind(DataObjectPropertyBinder propertyBinder, Bean<T> bean, BeanSupplier<T> beanSupplier,
     * 			Context context) {
     * 		boolean bound = false;
     * 	bean.getProperties().values(): LinkedHashMap$LinkedValues 有三个JavaBeanBinder.BeanProperty 对象
     * 	bean-factory, first-name, last-name
     * 		for (BeanProperty beanProperty : bean.getProperties().values()) {
     * 	 通过 |= 赋值, 一旦有一次 bind 方法返回是true, bound 的结果都是 true
     * 			bound |= bind(beanSupplier, propertyBinder, beanProperty);
     * 			context.clearConfigurationProperty();
     * 		        }
     * 		return bound;
     * 	}
     * 	查看 bind 方法
     * 		private <T> boolean bind(BeanSupplier<T> beanSupplier, DataObjectPropertyBinder propertyBinder,
     * 			BeanProperty property) {
     * 		String propertyName = property.getName(); // bean-factory
     * 		ResolvableType type = property.getType();
     * 		Supplier<Object> value = property.getValue(beanSupplier);
     * 		Annotation[] annotations = property.getAnnotations();
     * 	跟进	bindProperty, 进入 bind 方法
     * 		Object bound = propertyBinder.bindProperty(propertyName,
     * 				Bindable.of(type).withSuppliedValue(value).withAnnotations(annotations));
     * 		if (bound == null) { propertyName 是 bean-factory 时候, 返回了null, 经过判断返回 false
     * 			return false;
     * 		 }
     * 		if (property.isSettable()) {
     * 	        // bound 配置项的值已经经过convert 处理了
     * 			property.setValue(beanSupplier, bound);
     *        }
     * 		else if (value == null || !bound.equals(value.get())) {
     * 			throw new IllegalStateException("No setter found for property: " + property.getName());
     *        }
     * 		return true; // 最终返回 true 表示数据绑定成功
     * 	}
     *
     * 	private <T> T bind(ConfigurationPropertyName name, Bindable<T> target, BindHandler handler, Context context,
     * 			boolean allowRecursiveBinding, boolean create) {
     * 		try {
     * 			Bindable<T> replacementTarget = handler.onStart(name, target, context);
     * 			if (replacementTarget == null) {
     * 				return handleBindResult(name, target, handler, context, null, create);
     * 			            }
     * 			target = replacementTarget;
     * 			Object bound = bindObject(name, target, handler, context, allowRecursiveBinding);
     * 			return handleBindResult(name, target, handler, context, bound, create);
     * 			}
     * 		catch (Exception ex) {
     * 			return handleBindError(name, target, handler, context, ex);
     *        }
     *    }
     *    再次进入 bindObject 方法
     *    private <T> Object bindObject(ConfigurationPropertyName name, Bindable<T> target, BindHandler handler,
     * 			Context context, boolean allowRecursiveBinding) {
     * 		ConfigurationProperty property = findProperty(name, target, context); // propery 是null
     * 		name 拼接后 lang-t.bean-factory containsNoDescendantOf 方法返回了 true, 方法返回了 null
     * 		if (property == null && context.depth != 0 && containsNoDescendantOf(context.getSources(), name)) {
     * 			return null;
     * 		 }
     * 		AggregateBinder<?> aggregateBinder = getAggregateBinder(target, context);
     * 		if (aggregateBinder != null) {
     * 			return bindAggregate(name, target, handler, context, aggregateBinder);
     *        }
     * 		if (property != null) {
     * 			try {
     * 		// 	lang-t.first-name 进入 bindProperty 方法 这个方法之前解析过, 返回配置项的值
     * 				return bindProperty(target, context, property);
     *            }
     * 			catch (ConverterNotFoundException ex) {
     * 				// We might still be able to bind it using the recursive binders
     * 				Object instance = bindDataObject(name, target, handler, context, allowRecursiveBinding);
     * 				if (instance != null) {
     * 					return instance;
     *                }
     * 				throw ex;
     *            }
     *        }
     * 		return bindDataObject(name, target, handler, context, allowRecursiveBinding);
     * 		}
     *
     * 	private <T> T handleBindResult(ConfigurationPropertyName name, Bindable<T> target, BindHandler handler,
     * 			Context context, Object result, boolean create) throws Exception {
     * 		if (result != null) {
     * 			result = handler.onSuccess(name, target, context, result);
     * 			result = context.getConverter().convert(result, target);
     * 		        }
     * 		if (result == null && create) {
     * 			result = create(target, context);
     * 			result = handler.onCreate(name, target, context, result);
     * 			result = context.getConverter().convert(result, target);
     * 			Assert.state(result != null, () -> "Unable to create instance for " + target.getType());
     *        }
     * 		handler.onFinish(name, target, context, result);
     * 		return context.getConverter().convert(result, target);
     * 		}
     * 	查看 convert	调用重载的 convert 方法
     * 	<T> T convert(Object source, ResolvableType targetType, Annotation... targetAnnotations) {
     * 		if (source == null) {
     * 			return null;
     * 		        }
     * 		return (T) convert(source, TypeDescriptor.forObject(source),
     * 				new ResolvableTypeDescriptor(targetType, targetAnnotations));
     * 		}
     *
     * 	通过反射给属性设置值, 获取到 bean 实例,  this.setter.invoke(instance.get(), value) 调用来到 PropertyConfig2 setXXX 方法
     * 	void setValue(Supplier<?> instance, Object value) {
     * 			try {
     * 				this.setter.setAccessible(true);
     * 				this.setter.invoke(instance.get(), value);
     * 			    }
     * 			catch (Exception ex) {
     * 				throw new IllegalStateException("Unable to set value for property " + this.name, ex);
     *            }
     *            }
     *    ***********************************************************
     *    方法返回 Bean.cached, Bean 只会创建一次, cached 会被缓存起来, 后面再次获取的时候直接返回
     *    Bean<T> bean = Bean.get(target, hasKnownBindableProperties);
     *     	static <T> Bean<T> get(Bindable<T> bindable, boolean canCallGetValue) {
     * 			ResolvableType type = bindable.getType();
     * 			Class<?> resolvedType = type.resolve(Object.class);
     * 			Supplier<T> value = bindable.getValue();
     * 			T instance = null;
     * 			if (canCallGetValue && value != null) {
     * 				instance = value.get();
     * 				resolvedType = (instance != null) ? instance.getClass() : resolvedType;
     * 			            }
     * 			if (instance == null && !isInstantiable(resolvedType)) {
     * 				return null;
     *            }
     * 			Bean<?> bean = Bean.cached;
     * 			if (bean == null || !bean.isOfType(type, resolvedType)) {
     * 				bean = new Bean<>(type, resolvedType);
     * 				cached = bean;
     *          }
     * 			return (Bean<T>) bean;
     * 		}
     * 	// 查看创建Bean 方法
     * 	Bean(ResolvableType type, Class<?> resolvedType) {
     * 			this.type = type;
     * 			this.resolvedType = resolvedType;
     * 			addProperties(resolvedType);
     * 	}
     * 	查看 	addProperties(resolvedType)
     * 	private void addProperties(Class<?> type) {
     * 	        // 通过while 循环添加当前类声明的方法和属性, 后面获取type 的父类并再次赋值给type
     * 	        // 然后判断type 不是null 并且不是Object类型时候, 再次进行上面的操作, 否则跳出循环
     * 			while (type != null && !Object.class.equals(type)) {
     * 				Method[] declaredMethods = getSorted(type, Class::getDeclaredMethods, Method::getName);
     * 				Field[] declaredFields = getSorted(type, Class::getDeclaredFields, Field::getName);
     * 				addProperties(declaredMethods, declaredFields);
     * 				type = type.getSuperclass();
     * 			   }
     * 	}
     * 	获取类声明的方法, 并按照方法名字对应的字符串的Unicode值进行的字典排序, 属性也是一样的操作
     * 	private <S, E> E[] getSorted(S source, Function<S, E[]> elements, Function<E, String> name) {
     * 			E[] result = elements.apply(source);
     * 			Arrays.sort(result, Comparator.comparing(name));
     * 			return result;
     * 	}
     *
     * 		protected void addProperties(Method[] declaredMethods, Field[] declaredFields) {
     * 			for (int i = 0; i < declaredMethods.length; i++) {
     * 				if (!isCandidate(declaredMethods[i])) {
     * 			根据候选条件的方法返回值取反后, 把对应下标的method 对象置为null 后面 addMethodIfPossible 方法里有判断
     * 					declaredMethods[i] = null;
     * 				}
     * 			}
     * 			for (Method method : declaredMethods) {
     * 		        对于 Boolean 类型的属性对应的 isXXX 方法 最后也是添加到 BeanProperty 的 getter 属性
     * 				addMethodIfPossible(method, "is", 0, BeanProperty::addGetter);
     *            }
     * 			for (Method method : declaredMethods) {
     * 				addMethodIfPossible(method, "get", 0, BeanProperty::addGetter);
     *            }
     * 			for (Method method : declaredMethods) {
     * 				addMethodIfPossible(method, "set", 1, BeanProperty::addSetter);
     *            }
     * 			for (Field field : declaredFields) {
     * 				addField(field);
     *            }
     *        }
     *  看一下是否是候选方法的实现
     *  private boolean isCandidate(Method method) {
     * 			int modifiers = method.getModifiers();
     * 		    // 依次判断方法不能是私有的 受保护的 抽象的 静态的, 方法不能是桥接方法, 方法的声明类不能是Object类型 也不能是Class 类型, 方法名不能包含$
     * 			return !Modifier.isPrivate(modifiers) && !Modifier.isProtected(modifiers) && !Modifier.isAbstract(modifiers)
     * 					&& !Modifier.isStatic(modifiers) && !method.isBridge()
     * 					&& !Object.class.equals(method.getDeclaringClass())
     * 					&& !Class.class.equals(method.getDeclaringClass()) && method.getName().indexOf('$') == -1;
     * 	}
     * 	给 BeanProperty 对象添加方法
     * 	private void addMethodIfPossible(Method method, String prefix, int parameterCount,
     * 				BiConsumer<BeanProperty, Method> consumer) {
     * 			判断条件: 方法不能是null, 并且方法的参数要和预期添加方法[set 只有一个参数 get和is 没有参数]的方法参数数量一样
     * 		            方法名字要是以前缀字符串开始的, 并且方法名字的长度大于前缀字符串的长度
     * 			if (method != null && method.getParameterCount() == parameterCount && method.getName().startsWith(prefix)
     * 					&& method.getName().length() > prefix.length()) {
     *
     * 				String propertyName = Introspector.decapitalize(method.getName().substring(prefix.length()));
     * 			    this.properties 是一个 LinkedHashMap<String, BeanProperty>
     * 			    key: propertyName 属性名字   value: BeanProperty
     * 			    accept 接受 BeanProperty 对象, Method 对象  调用对应的 addGetter 或者 addSetter
     * 				consumer.accept(this.properties.computeIfAbsent(propertyName, this::getBeanProperty), method);
     * 			    }
     * 			}
     * 	    方法需要的参数name, 在 HashMap中的 computeIfAbsent 方法中
     * 	      V v = mappingFunction.apply(key); 刚好把参数传入, 返回 BeanProperty 对象
     *      private BeanProperty getBeanProperty(String name) {
     * 			return new BeanProperty(name, this.type);
     * 		 }
     *
     * 		void addGetter(Method getter) {
     * 			if (this.getter == null || isBetterGetter(getter)) {
     * 				this.getter = getter;
     * 			      }
     * 		}
     *
     * 		 void addSetter(Method setter) {
     * 			if (this.setter == null || isBetterSetter(setter)) {
     * 				this.setter = setter;
     * 			  }
     * 		 }
     * 		前面先处理 isXXX getXXX setXXX 方法, 对应的属性名的 BeanProperty 已经存储在 this.properties 里面了
     *  	private void addField(Field field) {
     * 			BeanProperty property = this.properties.get(field.getName());
     * 			if (property != null) {
     * 				property.addField(field);
     * 			  }
     * 		}
     *      void addField(Field field) {
     * 			if (this.field == null) {
     * 				this.field = field;
     * 			 }
     * 		}
     *  BeanProperty(String name, ResolvableType declaringClassType) {
     * 			this.name = DataObjectPropertyName.toDashedForm(name);
     * 			this.declaringClassType = declaringClassType;
     * 	}
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
     *  todo  super extends 用法
     *      public static <T, U extends Comparable<? super U>> Comparator<T> comparing(
     *             Function<? super T, ? extends U> keyExtractor)
     *     {
     *         Objects.requireNonNull(keyExtractor);
     *         return (Comparator<T> & Serializable)
     *             (c1, c2) -> keyExtractor.apply(c1).compareTo(keyExtractor.apply(c2));
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
     */
    public static final String PROPERTY_PREFIX = "lang-t";

    private String firstName;
    private String lastName;

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        System.out.println("setAdult: " + adult);
        this.adult = adult;
    }

    private boolean adult;


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
