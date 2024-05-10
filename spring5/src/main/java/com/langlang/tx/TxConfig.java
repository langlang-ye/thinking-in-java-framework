package com.langlang.tx;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.util.StringUtils;

import com.mchange.v2.c3p0.ComboPooledDataSource;


/**
 * 声明式事务：
 * 
 * 环境搭建：
 * 1、导入相关依赖
 * 		数据源、数据库驱动、Spring-jdbc模块
 * 2、配置数据源、JdbcTemplate（Spring提供的简化数据库操作的工具）操作数据
 * 3、给方法上标注 @Transactional 表示当前方法是一个事务方法；
 * 4、 @EnableTransactionManagement 开启基于注解的事务管理功能；
 * 		@EnableXXX
 * 5、配置事务管理器来控制事务;
 * 		@Bean
 * 		public PlatformTransactionManager transactionManager()
 * 
 * 
 * 原理：
 * 1）、@EnableTransactionManagement
 * 			利用TransactionManagementConfigurationSelector给容器中会导入组件
 * 			导入两个组件
 * 			AutoProxyRegistrar
 * 			ProxyTransactionManagementConfiguration
 * 2）、AutoProxyRegistrar：
 * 			给容器中注册一个 InfrastructureAdvisorAutoProxyCreator 组件；
 * 			InfrastructureAdvisorAutoProxyCreator：？
 * 			利用后置处理器机制在对象创建以后，包装对象，返回一个代理对象（增强器），代理对象执行方法利用拦截器链进行调用；
 * 
 * 3）、ProxyTransactionManagementConfiguration 做了什么？
 * 			1、给容器中注册事务增强器；
 * 				1）、事务增强器要用事务注解的信息，AnnotationTransactionAttributeSource 解析事务注解  比如 @Transactional(XXX) 包含注解以及注解里面的属性
 * 				2）、事务拦截器：
 * 					TransactionInterceptor；保存了事务属性信息，事务管理器；
 * 					它是一个 MethodInterceptor；
 * 					在目标方法执行的时候；
 * 						执行拦截器链；
 * 						事务拦截器：
 * 							1）、先获取事务相关的属性
 * 							2）、再获取PlatformTransactionManager，如果事先没有添加指定任何 transactionmanger
 * 								最终会从容器中按照类型获取一个PlatformTransactionManager；
 * 								todo: qualifier transactionManagerBeanName  在哪里配置的 
								String qualifier = txAttr.getQualifier();
										if (StringUtils.hasText(qualifier)) {
											return determineQualifiedTransactionManager(qualifier);
										}
										else if (StringUtils.hasText(this.transactionManagerBeanName)) {
											return determineQualifiedTransactionManager(this.transactionManagerBeanName);
										}
										else {
											PlatformTransactionManager defaultTransactionManager = getTransactionManager();
											if (defaultTransactionManager == null) {
												defaultTransactionManager = this.transactionManagerCache.get(DEFAULT_TRANSACTION_MANAGER_KEY);
												if (defaultTransactionManager == null) {
													defaultTransactionManager = this.beanFactory.getBean(PlatformTransactionManager.class);
													this.transactionManagerCache.putIfAbsent(
															DEFAULT_TRANSACTION_MANAGER_KEY, defaultTransactionManager);
												}
											}
											return defaultTransactionManager;
										}
 * 	
 * 							3）、执行目标方法   
 * 								  TODO invokeWithinTransaction 走源码调试一下
 * 								如果异常，获取到事务管理器，利用事务管理回滚操作；
 * 								如果正常，利用事务管理器，提交事务
 * 			
 */

@EnableTransactionManagement()
@ComponentScan("com.langlang.tx")
@Configuration
public class TxConfig {

	// 数据源
	@Bean
	public DataSource dataSource() throws Exception {
		
		ComboPooledDataSource dataSource = new ComboPooledDataSource();
		dataSource.setUser("root");
		dataSource.setPassword("root");
		dataSource.setDriverClass("com.mysql.jdbc.Driver");
		dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/test?useSSL=false");
		
		System.out.println(dataSource);
		return dataSource;
	}
	
	@Bean
	public JdbcTemplate jdbcTemplate() throws Exception { // todo: 能不能在方法上 利用 @Autowired  
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource());
		return jdbcTemplate;
	}
	
	/*@Bean
	public PlatformTransactionManager transactionManager() throws Exception {
		return new DataSourceTransactionManager(dataSource());
	}*/
	
	
	// 直接在方法参数传入也可以, 
	@Bean
	public PlatformTransactionManager transactionManager(DataSource dataSource) throws Exception {
		
		System.out.println(dataSource);
		return new DataSourceTransactionManager(dataSource);
	}
}
