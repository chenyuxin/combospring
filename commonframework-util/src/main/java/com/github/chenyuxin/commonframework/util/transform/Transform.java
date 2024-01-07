package com.github.chenyuxin.commonframework.util.transform;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * 值域转换等转换<br>
 * 通过枚举类型做值域转换
 */
@Target(value={FIELD})
@Retention(value=RUNTIME)
public @interface Transform {
	
	/**
	 * 枚举类型转换<br>
	 * 枚举需要实现getValue和getName方法
	 * @return
	 */
	Class<? extends Enum<?>> enumType();
	
	/**
	 * 默认不开启<br>
	 * 当不能转换或者转换出错时开启默认值替换，
	 */
	boolean enableDefaultValue() default false;
	
	/**
	 * 当enableDefaultValue开启时,当不能转换或者转换出错时开启默认值替换
	 * @return
	 */
	String defaultValue() default "";
	
}
