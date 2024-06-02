package com.langlang.di;

import com.langlang.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *  Autowired: 优先按照type在上下文中查找匹配的bean, 如果存在多个同类型的bean, 则按照name进行匹配, 如果名字匹配不到, idea 提示报错
 *             可以配合Primary Qualifier 注解来指定需要导入的bean,
 *             可以设置 required 属性, 默认true 表示一定要注入成功, false 允许为空
 *  Resource: 如果同时指定了name和type，则从Spring上下文中找到唯一匹配的bean进行装配，找不到则抛出异常。
 *            如果指定了name，则从上下文中查找名称（id）匹配的bean进行装配，找不到则抛出异常。
 *            如果指定了type，则从上下文中找到类型匹配的唯一bean进行装配，找不到或是找到多个，都会抛出异常。
 *            如果既没有指定name，又没有指定type，则默认按照byName方式进行装配；如果没有匹配，按照byType进行装配。
 *            可以配合Primary Qualifier 注解来指定需要导入的bean,
 *
 *
 *
 */
@Service
public class AddressService {

    // @Autowired(required = false)  // required = false, 允许为空, 组件没有注入进来, 使用的时候NPE 要有其他的补救措施
    // private DB db;  //当DB, 仅有一个实现类的时候, 没问题; 如果有两个或者更多的实现类注入的bean, idea 编辑器直接报错并给出了修改提示

    /* @Autowired
    @Qualifier("oracleDB")
    private DB db; */

    /**
     * Autowired 注解生效的分析:
     * populateBean 方法中
     *  for (BeanPostProcessor bp : getBeanPostProcessors()) { // AutowiredAnnotationBeanPostProcessor#postProcessProperties 方法
     * 				if (bp instanceof InstantiationAwareBeanPostProcessor) {
     * 					InstantiationAwareBeanPostProcessor ibp = (InstantiationAwareBeanPostProcessor) bp;
     * 					PropertyValues pvsToUse = ibp.postProcessProperties(pvs, bw.getWrappedInstance(), beanName);
     * 				// 忽略后续逻辑
     * 				}
     * 				}
     * 	 postProcessProperties --> 	inject --> inject --> resolveDependency --> doResolveDependency
     * 	 doResolveDependency {
     *      Map<String, Object> matchingBeans = findAutowireCandidates(beanName, type, descriptor);
     *  后续处理, 对同类型的多个bean 进行过滤, 如果 autowiredBeanName 是null, 判断
     * 	if (matchingBeans.size() > 1) {
     * 			autowiredBeanName = determineAutowireCandidate(matchingBeans, descriptor);
     * 			if (autowiredBeanName == null) {
     * 					if (isRequired(descriptor) || !indicatesMultipleBeans(type)) {
     * 						return descriptor.resolveNotUnique(descriptor.getResolvableType(), matchingBeans);  // 报错
     * 					                    }
     * 					else {
     * 						return null;
     *                    }
     *              }
     *              	instanceCandidate = matchingBeans.get(autowiredBeanName); // 获取到需要注入组件的 class 对象
     *
     *              	if (instanceCandidate instanceof Class) {
     * 				    instanceCandidate = descriptor.resolveCandidate(autowiredBeanName, type, this);
     * 				    resolveCandidate 方法里: 	return beanFactory.getBean(beanName); 根据bean名从 ioc 容器获取bean
     * 			        }
     * 			}
     * 		}
     * 	findAutowireCandidates(beanName, type, descriptor) {
     * 	        // 获取到候选的bean 名
     * 	    	String[] candidateNames = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(
     * 				this, requiredType, true, descriptor.isEager());
     *
     *          Map<String, Object> result = new LinkedHashMap<>(candidateNames.length);
     * 			for (String candidate : candidateNames) {
     * 			if (!isSelfReference(beanName, candidate) && isAutowireCandidate(candidate, descriptor)) {
     * 				addCandidateEntry(result, candidate, descriptor, requiredType);
     * 			            }
     * 			     }
     *        return result;
     * 	}
     * 	isAutowireCandidate(candidate, descriptor) -- > isAutowireCandidate --> isAutowireCandidate --> isAutowireCandidate
     * 	        --> QualifierAnnotationAutowireCandidateResolver#isAutowireCandidate 里面的 isAutowireCandidate
     *
     * 	  isAutowireCandidate(BeanDefinitionHolder bdHolder, DependencyDescriptor descriptor) {
     * 		boolean match = super.isAutowireCandidate(bdHolder, descriptor);
     * 		if (match) {
     * 			match = checkQualifiers(bdHolder, descriptor.getAnnotations());  // 里面对 Qualifier 注解进行匹配
     * 			// 忽略后续逻辑
     * 		}
     * 	 return match;
     * }
     * addCandidateEntry(result, candidate, descriptor, requiredType) {
     *     // 忽略 else 之前的逻辑
     *     candidates.put(candidateName, getType(candidateName));
     *     getType 调用 AbstractBeanFactory#getType  方法
     * }
     *
     *
     *
     * 	determineAutowireCandidate(matchingBeans, descriptor) { 具体过滤的逻辑
     * 	    Class<?> requiredType = descriptor.getDependencyType();
     * 	    // 通过 Primary 进行筛选, 查看这个 determinePrimaryCandidate 方法, 如果有两个同类型的组件都标注了 Primary, 程序报错: NoUniqueBeanDefinitionException
     * 		String primaryCandidate = determinePrimaryCandidate(candidates, requiredType);
     * 		if (primaryCandidate != null) {
     * 			return primaryCandidate;
     * 		        }
     * 		// 通过 Priority 优先级排序筛选
     * 	    // 查看 determineHighestPriorityCandidate, 遍历找到值最小的, 如果当前的值和之前筛选的最小值相等, 程序报错 : NoUniqueBeanDefinitionException
     * 		String priorityCandidate = determineHighestPriorityCandidate(candidates, requiredType);
     * 		if (priorityCandidate != null) {
     * 			return priorityCandidate;
     * 		 }
     *      // 最后根据名字进行匹配
     *      for (Map.Entry<String, Object> entry : candidates.entrySet()) {
     * 			String candidateName = entry.getKey();
     * 			Object beanInstance = entry.getValue();
     * 			if ((beanInstance != null && this.resolvableDependencies.containsValue(beanInstance)) ||
     * 					matchesBeanName(candidateName, descriptor.getDependencyName())) {
     * 				return candidateName;
     * 			            }
     * 			         }
     * 		return null;  // 上面的筛选都没有合适的, 返回null
     *   }
     * 	    matchesBeanName(candidateName, descriptor.getDependencyName()) : 根据 bean 名字匹配
     */
    @Autowired
    // @Qualifier("oracleDB")
    private DB db;

    /*  @Resource
    private DB db;    */    // 虽然idea 没有报错, 运行的报错: NoUniqueBeanDefinitionException

    /* @Resource(name = "mysqlDB")
    @Qualifier("oracleDB")
    private DB db;   */


}
