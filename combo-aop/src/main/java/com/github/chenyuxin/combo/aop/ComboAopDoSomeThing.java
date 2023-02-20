package com.github.chenyuxin.combo.aop;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 通用SpringAop执行,实现此接口注入spring管理，然后自定义规则逻辑。
 * @param <R>
 */
public interface ComboAopDoSomeThing<R> {
	
	/**
	 * 进程方法执行前，执行本切口规则
	 * @param point 切点（截获的进程、方法）
	 * @param commonAop 注解的配置信息
	 * @return
	 */
	R before(ProceedingJoinPoint point,ComboAop comboAop);
	
	/**
	 * 进程方法执行成功完成后，执行本切口规则
	 * @param point 切点（截获的进程、方法）
	 * @param commonAop 注解的配置信息
	 * @param beforeResult before方法的执行结果
	 * @return
	 */
	void after(ProceedingJoinPoint point,ComboAop comboAop, R beforeResult);
	
	/**
	 * 进程方法抛出执行异常时，执行本切口规则
	 * @param point 切点（截获的进程、方法）
	 * @param commonAop 注解的配置信息
	 * @param beforeResult 
	 * @param e 异常
	 * @return
	 */
	void throwable(ProceedingJoinPoint point,ComboAop comboAop,R beforeResult, Throwable e);

}
