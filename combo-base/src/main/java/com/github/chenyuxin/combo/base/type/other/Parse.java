package com.github.chenyuxin.combo.base.type.other;

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

}
