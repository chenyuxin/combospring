package com.github.chenyuxin.commonframework.base.converter;

/**
 *提供解析方法 
 * @param <T>
 * @param <E>
 */
public interface Parse<T, E> {
	
	/**
	 * 解析为对应的类型
	 * @param value
	 * @return
	 */
	T parse(E value);
	
	/**
	 * 解析为对应的类型
	 * @param value
	 * @param formatString 自定义解析格式
	 * @return
	T parse(Object value,String formatString);
	 */

}
