package com.github.chenyuxin.combo.base.type.other;

/**
 * 与Parse相反，反解析
 * @param <T>
 * @param <E>
 */
public interface Escape<T,E> {
	
	/**
	 * 反解析为对应的类型
	 * @param <E>
	 * @param value
	 * @return
	 */
	T escape(E value);

}