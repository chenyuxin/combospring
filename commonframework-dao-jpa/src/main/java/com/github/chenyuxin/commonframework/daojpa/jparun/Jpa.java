package com.github.chenyuxin.commonframework.daojpa.jparun;

import java.util.Collection;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.github.chenyuxin.commonframework.daojpa.intf.ComboJpa;

/**
 * Jpa运行器
 */
@Component
public class Jpa {
	
	
	private static ApplicationContext applicationContext;
	
	public Jpa(ApplicationContext applicationContext) {
		Jpa.applicationContext = applicationContext;
	}
	
	/**
	 * 保存或更新
	 * @param entity
	 * @return
	 */
	public static JpaRunner save(Object entity) {
		JpaRunner jpaRunner = new JpaRunner(entity instanceof Collection ? JpaRunType.mergeAll : JpaRunType.merge, entity);
		return jpaRunner;
	}
	
	/**
	 * 删除
	 * @param entity
	 * @return
	 */
	public static JpaRunner remove(Object entity) {
		JpaRunner jpaRunner = new JpaRunner(entity instanceof Collection ? JpaRunType.removeAll : JpaRunType.remove, entity);
		return jpaRunner;
	}

	/**
	 * 执行
	 * @param <T>
	 * @param jpaRunner
	 * @return
	 */
	protected static <T> T run(JpaRunner jpaRunner) {
		ComboJpa comboJpa = (ComboJpa) applicationContext.getBean(jpaRunner.getJpaRunType().name());
		return comboJpa.run(jpaRunner);
	}

}
