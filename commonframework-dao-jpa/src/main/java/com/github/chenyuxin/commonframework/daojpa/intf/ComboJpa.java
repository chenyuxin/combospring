package com.github.chenyuxin.commonframework.daojpa.intf;

import com.github.chenyuxin.commonframework.daojpa.jparun.JpaRunner;

/**
 * JPA执行的方法
 */
public interface ComboJpa {
	
	/**
	 * 执行
	 * @param <T>
	 * @param jpaRunner
	 * @return
	 */
	public <T> T run(JpaRunner jpaRunner);

}
