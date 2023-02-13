package com.github.chenyuxin.combo.dao.resource.anotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.github.chenyuxin.combo.base.constant.StringPool;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
public @interface Table {
	/**
	 * (Optional) The name of the table.
	 * <p/>
	 * Defaults to the entity name.
	 * 
	 * 数据库表名
	 */
	String name();
	
	/**
	 * 表中文名，或用于调用接口名
	 */
	String cName() default StringPool.BLANK;
	
}	

