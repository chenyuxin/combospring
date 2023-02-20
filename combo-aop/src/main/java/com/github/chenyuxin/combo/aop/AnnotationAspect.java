package com.github.chenyuxin.combo.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AnnotationAspect {
	
	@Autowired ApplicationContext applicationContext;
	
	/**
	 * 切点 通过@ComboAop注解的方法
	 */
	@Pointcut("@annotation(com.github.chenyuxin.combo.aop.ComboAop)")
	private void pointCutComboAop(){}
	
	@SuppressWarnings("unchecked")
	@Around("@annotation(comboAop)")
	public <R> Object process(ProceedingJoinPoint point,ComboAop comboAop) throws Throwable {
		ComboAopDoSomeThing<R> comboAopDoSomeThing;
		R beforeResult;
		try {
			comboAopDoSomeThing = applicationContext.getBean(comboAop.cuterClass());//TODO 多个ComboAopDoSomeThing的功能添加
			beforeResult = comboAopDoSomeThing.before(point, comboAop);
		} catch (Throwable e) {
			throw e;
		}
//    		System.out.println("ComboAop welcome");
//          System.out.println("ComboAop 切入者："+ comboAop.cuterClass().getSimpleName());
//          System.out.println("ComboAop 调用类：" + point.getSignature().getDeclaringTypeName());
//          System.out.println("ComboAop 调用类名" + point.getSignature().getDeclaringType().getSimpleName());
    	try {
			return point.proceed();//调用目标方法
		} catch (Throwable e) {
			comboAopDoSomeThing.throwable(point, comboAop, beforeResult,e);
			//System.out.println("ComboAop error:" + e.getMessage());
			throw e;
		} finally {
			comboAopDoSomeThing.after(point, comboAop, beforeResult);
		}
    	
    	
	}
	
	
	

}
