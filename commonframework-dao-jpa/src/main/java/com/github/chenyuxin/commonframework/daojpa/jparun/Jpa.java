package com.github.chenyuxin.commonframework.daojpa.jparun;

import org.springframework.stereotype.Component;

import com.github.chenyuxin.commonframework.daojpa.intf.ComboJpa;

/**
 * Jpa运行器
 */
@Component
public class Jpa {
	
	private static ComboJpa comboJpa;
	
	public Jpa(ComboJpa comboJpa) {
		Jpa.comboJpa = comboJpa;
	}
	
	/**
	 * 保存或更新
	 * @param entity
	 * @return
	 */
	public static JpaRunner save(Object entity) {
		JpaRunner jpaRunner = new JpaRunner(JpaRunType.merge, entity);
		return jpaRunner;
	}

	/**
	 * 执行
	 * @param <T>
	 * @param jpaRunner
	 * @return
	 */
	protected static <T> T run(JpaRunner jpaRunner) {
		return comboJpa.run(jpaRunner);
	}

}
