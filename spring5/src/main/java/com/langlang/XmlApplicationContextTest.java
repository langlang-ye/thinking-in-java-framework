package com.langlang;

import org.springframework.beans.factory.support.BeanDefinitionReader;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * applicationContext.getBean("duck")
 *  调用doGetBean(name, null, null, false) {
 *      final String beanName = transformedBeanName(name);
 *      Object sharedInstance = getSingleton(beanName); // 在 getSingleton 中, 通过3层缓存的map 不断获取目标bean; 第一次获取肯定没有 返回 null
 *
 *      // if 走 else 逻辑,  首先检查 bean 是不是正在被创建
 *      if (isPrototypeCurrentlyInCreation(beanName)) {	throw new BeanCurrentlyInCreationException(beanName);}
 *      BeanFactory parentBeanFactory = getParentBeanFactory();  // getParentBeanFactory 方法返回null
 *
 *      if (!typeCheckOnly) {
 * 			markBeanAsCreated(beanName); // 标记当前bean 正在被创建
 * 		}
 *
 *      final RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
 *
 *
 *      checkMergedBeanDefinition(mbd, beanName, args)  进入方法中 if (mbd.isAbstract()) {  throw new BeanIsAbstractException(beanName); } // bean 不能是抽象类
 *       String[] dependsOn = mbd.getDependsOn(); // 获取依赖的bean, 返回一个String 数组
 *          // 如果存在依赖的bean, 通过for循环 get(bean) [递归调用] 获取到依赖的bean
 *
 *          // 创建单例 bean
 * 			if (mbd.isSingleton()) {
 * 					sharedInstance = getSingleton(beanName, () -> {   // 创建单例的bean
 * 						try {
 * 							return createBean(beanName, mbd, args);
 * 						    }
 * 						catch (BeansException ex) {
 * 							destroySingleton(beanName); // 创建有异常, 还有销毁bean 的操作
 * 							throw ex;
 *                        }
 *                        });
 * 						bean = getObjectForBeanInstance(sharedInstance, name, beanName, mbd);
 * 				}
 * 			// 判断 scope 是 prototype 或者其他的类型 分别执行对应的逻辑
 *          //    requiredType 是null, if 判断为 false
 *	        return (T) bean;  //返回对应的bean 实例
 *	    }
 *
 *      getMergedLocalBeanDefinition(beanName){
 *      	RootBeanDefinition mbd = this.mergedBeanDefinitions.get(beanName);  // null,  先从 map 中获取
 *          if (mbd != null && !mbd.stale) {
 * 			return mbd;
 * 		    }
 * 		     // 否则调用
 * 		    return getMergedBeanDefinition(beanName, getBeanDefinition(beanName));
 *
 *      }
 *      getBeanDefinition(String beanName) throws NoSuchBeanDefinitionException {
 * 		BeanDefinition bd = this.beanDefinitionMap.get(beanName); // 从map 中获取 bean 的定义信息, 返回的是 org.springframework.beans.factory.support.GenericBeanDefinition
 * 		return bd;
 * 		}
 *
 * 	    getMergedBeanDefinition(beanName, bd, null) {
 *        synchronized (this.mergedBeanDefinitions) {   方法体里面是一个 synchronized 同步代码块
 *        // 再次尝试获取 bean 定义
 * 			if (containingBd == null) {
 * 				mbd = this.mergedBeanDefinitions.get(beanName);
 * 			}
 *
 *           if (mbd == null || mbd.stale) {
 * 				previous = mbd;
 * 				if (bd.getParentName() == null) {
 * 					// Use copy of given root bean definition.
 * 					if (bd instanceof RootBeanDefinition) {
 * 						mbd = ((RootBeanDefinition) bd).cloneBeanDefinition();
 * 					                    }
 * 					else {
 * 						mbd = new RootBeanDefinition(bd); // 传入 GenericBeanDefinition 的对象
 *                    }
 *
 *                  }
 *                  // 设置 bean 的默认scope 为 singleton
 * 				    if (!StringUtils.hasLength(mbd.getScope())) {
 * 					mbd.setScope(SCOPE_SINGLETON);
 * 				     }
 *
 * 				  // 把 bean 定义信息暂时缓存到map, 后面可能仍会重新合并，以便进行元数据更改
 * 				if (containingBd == null && isCacheBeanMetadata()) {
 * 					this.mergedBeanDefinitions.put(beanName, mbd);
 * 				 }
 * 				 return mbd;
 * 		       }
 *
 *         }
 * 	    }
 *	    transformedBeanName(name){
 *        canonicalName(BeanFactoryUtils.transformedBeanName(name))
 *               BeanFactoryUtils.transformedBeanName(name) 方法里:  如果是 name.startsWith("&) 通过一个 do-while 不
 *               断去掉开头的&, 返回 beanName
 *               canonicalName() 方法中: 通过一个 do-while { this.aliasMap.get(canonicalName); }  从别名的map 中不断获取bean 名字, 也就
 *              可以套娃, 最后返回 canonicalName
 *     }
 *
 *      获取单例 bean
 * 		getSingleton(String beanName, ObjectFactory<?> singletonFactory) { // singletonFactory 传入一个 Lambda 表达式
 * 	           // 执行断言判断 beanName 不能为空
 *          synchronized (this.singletonObjects)   { // 整个方法的逻辑 都是同步代码中
 * 			Object singletonObject = this.singletonObjects.get(beanName);
 * 			if (singletonObject == null) {
 * 				if (this.singletonsCurrentlyInDestruction) { // 判断当这个工厂的单例被破坏时，不允许创建单例 bean
 * 					throw new BeanCreationNotAllowedException(beanName,
 * 							"Singleton bean creation not allowed while singletons of this factory are in destruction " +
 * 							"(Do not request a bean from a BeanFactory in a destroy method implementation!)");
 * 				     }
 *
 * 				beforeSingletonCreation(beanName); // 回调方法  在创建中检查排除
 * 				boolean newSingleton = false; // 标记 bean 是否创建成功
 * 				boolean recordSuppressedExceptions = (this.suppressedExceptions == null); // true
 * 				if (recordSuppressedExceptions) { // 记录异常信息
 * 					this.suppressedExceptions = new LinkedHashSet<>();
 *                }
 * 				try {
 * 					singletonObject = singletonFactory.getObject(); // 调用回到  return createBean(beanName, mbd, args);
 * 					newSingleton = true; // 标记 bean 创建成功
 *                }
 * 				catch (IllegalStateException ex) {
 * 					singletonObject = this.singletonObjects.get(beanName);
 * 					if (singletonObject == null) {
 * 						throw ex;
 *                    }
 *                }
 *
 *
 * 				finally {
 * 					if (recordSuppressedExceptions) {
 * 						this.suppressedExceptions = null;
 *                    }
 * 					afterSingletonCreation(beanName); // 回调方法, 检查在创建中检查排除
 *                }
 * 				if (newSingleton) {
 * 			        // private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256); // k: beanName, v: bean 实例
 *                  // private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<>(16);  //  k: beanName, v: ObjectFactory
 *                  // private final Map<String, Object> earlySingletonObjects = new HashMap<>(16);   // k: beanName, v: 早期的 bean 实例
 *                  //	private final Set<String> registeredSingletons = new LinkedHashSet<>(256); // 按照注册顺序保存 bean 名称
 * 			        // 在同步代码块中, 把创建 bean 添加到缓存 singletonObjects  中, 在 singletonFactories earlySingletonObjects 删除对应beanName的缓存
 * 			        // 在registeredSingletons 添加注册bean 的名字
 * 					addSingleton(beanName, singletonObject);
 *                }
 *                }
 * 			return singletonObj;
 * 		}
 * 		}
 *
 *      bean创建流程
 *      createBean(beanName, mbd, args) {
 *          // 确保bean类在这一点上被实际解析，并且如果是动态解析的类，则单独使用bean定义, 其不能存储在共享合并bean定义中
 *          Class<?> resolvedClass = resolveBeanClass(mbd, beanName);
 *
 *          mbdToUse.prepareMethodOverrides(); // 准备方法的覆盖 hasMethodOverrides() 判断如果没有 什么都不做
 *
 *          // Give BeanPostProcessors a chance to return a proxy instead of the target bean instance.
 * 			Object bean = resolveBeforeInstantiation(beanName, mbdToUse); // 给 BeanPostProcessors 一个返回代理对象的机会, 而不是目标bean实例
 * 			if (bean != null) { // bean 是 null
 * 				return bean;
 * 			}
 * 	        Object beanInstance = doCreateBean(beanName, mbdToUse, args);
 *          return beanInstance;
 *      }
 *
 *      resolveBeanClass(mbd, beanName) {
 *	        if (mbd.hasBeanClass()) { // return (this.beanClass instanceof Class); // 返回false, beanClass 类型是 Object 此时存储的是字符串 表示一个类的全类名
 * 				return mbd.getBeanClass();
 * 			 }
 * 			if (System.getSecurityManager() != null) {  // 默认null; 系统安全管理器  后面也会多次遇到
 * 				return AccessController.doPrivileged((PrivilegedExceptionAction<Class<?>>) () ->
 * 					doResolveBeanClass(mbd, typesToMatch), getAccessControlContext());
 *            }
 * 			else {
 * 				return doResolveBeanClass(mbd, typesToMatch); // 进入这个方法
 *            }
 *      }
 *      doResolveBeanClass(mbd, typesToMatch) {
 *           // 忽略掉if 是false 没执行的业务逻辑
 *          String className = mbd.getBeanClassName(); // 获得 bean 的全类名
 *          Object evaluated = evaluateBeanDefinitionString(className, mbd); // 进入这个方法里面  if (this.beanExpressionResolver == null) { // 表达式解析策略是null 直接返回	 return value; }
 *          // 忽略掉if 是false 没执行的业务逻辑
 *          return mbd.resolveBeanClass(beanClassLoader);
 *      }
 *      getBeanClassName() { // beanClass: Object 类型, 有两层含义: 如果是字符串, 表示一个类的全类名, 如果是 Class 对象 则表示是一个 Class 对象
 * 		        Object beanClassObject = this.beanClass;
 * 		        if (beanClassObject instanceof Class) {
 * 		        	return ((Class<?>) beanClassObject).getName();
 * 		        }
 * 		        else {
 * 		        	return (String) beanClassObject;
 *               }
 *      }
 *      resolveBeanClass(beanClassLoader) {
 *          String className = getBeanClassName();
 * 		    if (className == null) {
 * 			    return null;
 * 		    }
 * 		    Class<?> resolvedClass = ClassUtils.forName(className, classLoader); // spring 提供的工具类, 内部调用的是 return Class.forName(name, false, clToUse);
 * 		    this.beanClass = resolvedClass; // 将 beanClass 的值设置为解析后的 Class 对象, 方便后续后续获取使用
 * 		    return resolvedClass;
 *      }
 *
 *      resolveBeforeInstantiation(String beanName, RootBeanDefinition mbd) {  // 给 BeanPostProcessors 一个返回代理对象的机会, 而不是目标bean实例
 * 		Object bean = null;
 * 		if (!Boolean.FALSE.equals(mbd.beforeInstantiationResolved)) {
 * 			// Make sure bean class is actually resolved at this point.
 * 			if (!mbd.isSynthetic() && hasInstantiationAwareBeanPostProcessors()) { // hasInstantiationAwareBeanPostProcessors() false
 * 				Class<?> targetType = determineTargetType(beanName, mbd);
 * 				if (targetType != null) {
 * 					bean = applyBeanPostProcessorsBeforeInstantiation(targetType, beanName);
 * 					if (bean != null) {
 * 						bean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
 * 					                    }                          }
 * 			}
 * 			mbd.beforeInstantiationResolved = (bean != n ); // bean 是null 设置false
 * 		}
 * 		return   bean;
 * 	}
 *
 *
 * 	bean创建流程:
 * 	doCreateBean(beanName, mbdToUse, args) {
 * 	    BeanWrapper instanceWrapper = null;
 *      if (mbd.isSingleton()) {
 * 			instanceWrapper = this.factoryBeanInstanceCache.remove(beanName); // map的size =0
 * 		}
 *
 * 	   if (instanceWrapper == null) {
 * 	        // 此处返回 BeanWrapper 对象, 包装着的bean实例, 只是通过构造方法创建了对象, 对象的属性值都是默认值. 并没有进行设置属性值等初始化操作.
 * 			instanceWrapper = createBeanInstance(beanName, mbd, args); // 这个方法创建 bean 对象, 并返回了 BeanWrapper 对象
 * 		 }
 * 		 final Object bean = instanceWrapper.getWrappedInstance(); // 获取到 bean 对象
 * 		Class<?> beanType = instanceWrapper.getWrappedClass(); // 获取到 bean 对象对应的Class 对象
 * 		if (beanType != NullBean.class) {
 * 			mbd.resolvedTargetType = beanType; // 设置解析的目标类型
 * 		    }
 * 		// Allow post-processors to modify the merged bean definition.  允许后处理器修改合并的 Bean 定义
 * 		synchronized (mbd.postProcessingLock) { // final Object postProcessingLock = new Object(); Object 充当锁对象
 * 			if (!mbd.postProcessed) {
 * 				try {
 * 			        // 	进入 applyMergedBeanDefinitionPostProcessors 方法, 通过 getBeanPostProcessors() 获取所有 BeanPostProcessor, 返回一个size为0 的集合
 * 					applyMergedBeanDefinitionPostProcessors(mbd, beanType, beanName);
 * 				    }
 * 				catch (Throwable ex) {
 * 					throw new BeanCreationException(mbd.getResourceDescription(), beanName,
 * 							"Post-processing of merged bean definition failed", ex);
 *                }
 * 				mbd.postProcessed = true;
 * 				}
 * 		}
 * 	    // Eagerly cache singletons to be able to resolve circular references
 * 		// even when triggered by lifecycle interfaces like BeanFactoryAware.
 * 	    // 急切地缓存单例，以便能够解析循环引用  即使由 BeanFactoryAware 等生命周期接口触发
 * 		boolean earlySingletonExposure = (mbd.isSingleton() && this.allowCircularReferences &&
 * 				isSingletonCurrentlyInCreation(beanName));  // true
 * 		if (earlySingletonExposure) {
 * 			if (logger.isTraceEnabled()) {
 * 				logger.trace("Eagerly caching bean '" + beanName +
 * 						"' to allow for resolving potential circular references");
 * 			            }
 * 			addSingletonFactory(beanName, () -> getEarlyBeanReference(beanName, mbd, bean));
 * 			}
 *
 * 	      // Initialize the bean instance.  初始化 bean 实例
 * 		Object exposedObject = bean;
 * 		try {
 * 			populateBean(beanName, mbd, instanceWrapper); // 设置 bean 实例的属性, 调用 setXXX 方法  todo 后续总结
 * 			exposedObject = initializeBean(beanName, exposedObject, mbd); // 重要方法 参见 {@link com.langlang.config.MainConfigOfLifeCycle}
 * 		 }
 *     if (earlySingletonExposure) { // true
 * 			Object earlySingletonReference = getSingleton(beanName, false); // null
 * 			if (earlySingletonReference != null) {}
 *
 *      try { // 注册为 DisposableBean, 当bean 销毁的时候调用回调方法
 * 			registerDisposableBeanIfNecessary(beanName, bean, mbd); // todo bean实现接口
 * 		}
 *      return exposedObject;
 *
 * 	}
 * 	// 添加到单例工厂的缓存中
 * 	addSingletonFactory(String beanName, ObjectFactory<?> singletonFactory) {
 * 		Assert.notNull(singletonFactory, "Singleton factory must not be null");
 * 		synchronized (this.singletonObjects) {
 * 			if (!this.singletonObjects.containsKey(beanName)) {
 * 				this.singletonFactories.put(beanName, singletonFactory); // singletonFactory 传入的是一个 Lambda 表达式
 * 				this.earlySingletonObjects.remove(beanName);
 * 				this.registeredSingletons.add(beanName);
 * 			            }
 * 			  }
 * 	}
 *
 *  bean创建流程:
 *  protected BeanWrapper createBeanInstance(String beanName, RootBeanDefinition mbd, @Nullable Object[] args) {
 * 		// Make sure bean class is actually resolved at this point.
 * 		Class<?> beanClass = resolveBeanClass(mbd, beanName); // 确保bean类在这一点上被实际解析, 可以得到实际的 Class 对象
 *
 *      // beanClass 是不是pubic, 非public 是否允许访问;
 * 		if (beanClass != null && !Modifier.isPublic(beanClass.getModifiers()) && !mbd.isNonPublicAccessAllowed()) {
 * 			throw new BeanCreationException(mbd.getResourceDescription(), beanName,
 * 					"Bean class isn't public, and non-public access not allowed: " + beanClass.getName());
 * 				}
 * 		Supplier<?> instanceSupplier = mbd.getInstanceSupplier(); // null
 * 		if (instanceSupplier != null) {
 * 			return obtainFromSupplier(instanceSupplier, beanName);
 * 		 }
 * 		 if (mbd.getFactoryMethodName() != null) { // null
 * 			return instantiateUsingFactoryMethod(beanName, mbd, args);
 * 		        }
 *
 * 		// Shortcut when re-creating the same bean...
 * 		boolean resolved = false;
 * 		boolean autowireNecessary = false;
 * 		if (args == null) {
 * 			synchronized (mbd.constructorArgumentLock) { //  final Object constructorArgumentLock = new Object(); 锁对象是 Object
 * 				if (mbd.resolvedConstructorOrFactoryMethod != null) { // null
 * 					resolved = true;
 * 					autowireNecessary = mbd.constructorArgumentsResolved;
 * 				                }            		        }
 * 		}
 * 	    // Candidate constructors for autowiring?
 * 	    // 候选的构造方法
 * 		Constructor<?>[] ctors = determineConstructorsFromBeanPostProcessors(beanClass, beanName); // 进入方法  hasInstantiationAwareBeanPostProcessors() false if 判断失败, 直接返回null
 * 		if (ctors != null || mbd.getResolvedAutowireMode() == AUTOWIRE_CONSTRUCTOR ||
 * 				mbd.hasConstructorArgumentValues() || !ObjectUtils.isEmpty(args)) {
 * 			return autowireConstructor(beanName, mbd, ctors, args);
 * 		        }
 * 		 // Preferred constructors for default construction?
 * 		ctors = mbd.getPreferredConstructors(); // 默认的首选构造方法, 进入方法, 默认实现 直接返回 null
 * 		if (ctors != null) {
 * 			return autowireConstructor(beanName, mbd, ctors, null);
 * 		        }
 * 		// No special handling: simply use no-arg constructor.
 * 		return instantiateBean(beanName, mbd); //  最后默认使用 无参构造方法
 * 	 }
 *
 * 	 bean创建流程:
 * 	 protected BeanWrapper instantiateBean(final String beanName, final RootBeanDefinition mbd) {
 *      final BeanFactory parent = this; this 是DefaultListableBeanFactory 对象
 *      if (System.getSecurityManager() != null) { 默认是 null
 * 			// 忽略掉 }
 * 			else {
 * 				beanInstance = getInstantiationStrategy().instantiate(mbd, beanName, parent);
 *            }
 *          BeanWrapper bw = new BeanWrapperImpl(beanInstance); 把 beanInstance 封装成 BeanWrapper 对象
 * 			initBeanWrapper(bw); // 初始化 BeanWrapper
 * 			return bw;
 * 	 }
 *
 * 	 bean创建流程:
 * 	 instantiate(mbd, beanName, parent) {
 * 	 // Don't override the class with CGLIB if no overrides.
 * 		if (!bd.hasMethodOverrides()) { // 没有重写 进入
 * 			Constructor<?> constructorToUse;
 * 			synchronized (bd.constructorArgumentLock) {  Object 充当锁对象
 * 				constructorToUse = (Constructor<?>) bd.resolvedConstructorOrFactoryMethod; // null
 * 				if (constructorToUse == null) {
 * 					final Class<?> clazz = bd.getBeanClass();
 * 					if (clazz.isInterface()) {  // 接口不能被实例化
 * 						throw new BeanInstantiationException(clazz, "Specified class is an interface");
 * 					                    }
 * 					try {
 * 						if (System.getSecurityManager() != null) {
 * 							constructorToUse = AccessController.doPrivileged(
 * 									(PrivilegedExceptionAction<Constructor<?>>) clazz::getDeclaredConstructor);
 *                        }
 * 						else {
 * 							constructorToUse = clazz.getDeclaredConstructor(); // 通过反射获取到默认的构造器对象
 *                        }
 * 						bd.resolvedConstructorOrFactoryMethod = constructorToUse;
 *                    }
 * 					catch (Throwable ex) {
 * 						throw new BeanInstantiationException(clazz, "No default constructor found", ex);
 *                    }                *            }
 * 			}
 * 			return BeanUtils.instantiateClass(constructorTo);
 * 		}
 * 		else { // 如果有重写, 一定要生成 CGLIB 的子类
 * 			// Must generate CGLIB subclass.
 * 			return instantiateWithMethodInjection(bd, beanName, owner);
 * 		}
 * 	 }
 *
 * 	 bean创建流程:
 * 	  BeanUtils.instantiateClass(constructorToUse) {
 * 	  try {
 * 			ReflectionUtils.makeAccessible(ctor); // 设置暴力访问
 * 			if (KotlinDetector.isKotlinReflectPresent() && KotlinDetector.isKotlinType(ctor.getDeclaringClass())) {
 * 				return KotlinDelegate.instantiateClass(ctor, args);
 * 			            }
 * 			else { // 获取构造方法的参数列表, 传入的参数列表长度不能比 构造方法的参数列表长度长
 * 				Class<?>[] parameterTypes = ctor.getParameterTypes();
 * 				Assert.isTrue(args.length <= parameterTypes.length, "Can't specify more arguments than constructor parameters");
 * 				Object[] argsWithDefaultValues = new Object[args.length];
 * 				for (int i = 0 ; i < args.length; i++) {
 * 					if (args[i] == null) {
 * 						Class<?> parameterType = parameterTypes[i];
 * 					    // 如果值是 null, 处理基础数据类型的默认值
 * 						argsWithDefaultValues[i] = (parameterType.isPrimitive() ? DEFAULT_TYPE_VALUES.get(parameterType) : null);
 *                    }
 * 					else {
 * 						argsWithDefaultValues[i] = args[i];
 *                    }
 *                }
 * 				return ctor.newInstance(argsWithDefaultValues); // Java 反射创建对象
 *            }
 *          }
 * 	  }
 *
 *
 */
public class XmlApplicationContextTest {

    public static void main(String[] args) {

        Resource resource = new ClassPathResource("applicationContext.xml");

        DefaultListableBeanFactory defaultListableBeanFactory = new DefaultListableBeanFactory();

        BeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(defaultListableBeanFactory);

        beanDefinitionReader.loadBeanDefinitions(resource);

        Object cat = defaultListableBeanFactory.getBean("tom");
        System.out.println(cat);
    }

}
