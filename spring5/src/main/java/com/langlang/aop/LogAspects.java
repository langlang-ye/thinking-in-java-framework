package com.langlang.aop;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

/**
 * 切面类
 *
 * @Aspect： 告诉Spring当前类是一个切面类
 *
 */
@Aspect
public class LogAspects {

    //抽取公共的切入点表达式
    //1、本类引用
    //2、其他的切面引用
    @Pointcut("execution(public int com.langlang.aop.MathCalculator.*(..))")
    public void pointCut(){} // 方法名字任意的 配合通知注解使用



//    @Before("public int com.langlang.aop.MathCalculator.div(int, int)") // 方法名字 * 代表任意方法,  参数 .. 两个点 代表任意参数
//    @Before("public int com.langlang.aop.MathCalculator.*(..)")
//    @Before("pointCut()")
    @Before("execution(* com.langlang.aop.MathCalculator.div(int, int))") // * 表示返回值可以任意类型 // TODO 学习 execution 表达式 语法
    public void logStart(JoinPoint joinpoint){ // joinpoint.getSignature() 获取的就是方法的签名
    	Object[] args = joinpoint.getArgs(); // 传递给目标方法的参数
    	
        System.out.println(""  + joinpoint.getSignature().getName() + "运行... 参数列表是: { " + Arrays.asList(args) +"}");
        

    }

    @After("com.langlang.aop.LogAspects.pointCut()")  // 其它切面使用 其他类使用
    public void logEnd(JoinPoint joinPoint){
        System.out.println("" + joinPoint.getSignature().getName() + "结束...");
    }
    
  //JoinPoint一定要出现在参数列表的第一位
    @AfterReturning(value="pointCut()", returning="result")
    public void logReturn(JoinPoint joinPoint, Object result){
        System.out.println("" + joinPoint.getSignature().getName() +"正常返回... 运行结果是: {"+ result +"}");


    }
    @AfterThrowing(value="pointCut()", throwing="exception")
    public void logException(JoinPoint joinPoint, Exception exception){
        System.out.println("" + joinPoint.getSignature().getName() + "异常... 异常信息是: {" + exception +"}");
    }



}
