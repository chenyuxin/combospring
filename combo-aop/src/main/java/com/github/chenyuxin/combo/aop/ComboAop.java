package com.github.chenyuxin.combo.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 通用AOP
 * @author chenyuxin
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ComboAop {
	
	/**
	 * 切入者类型
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	Class<? extends ComboAopDoSomeThing> cuterClass();
	
	/**
	 * 特性配置CommonAopFeatures
	 * @return
	 */
	Class<? extends ComboAopFeatures>[] commonAopFeatures() default {};


}
