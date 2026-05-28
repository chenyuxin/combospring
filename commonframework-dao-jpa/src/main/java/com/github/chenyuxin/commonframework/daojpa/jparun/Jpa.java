package com.github.chenyuxin.commonframework.daojpa.jparun;

/**
 * Jpa运行器
 */
public class Jpa {
	
	public static <T> JpaRunner<T> save(T entity) {
		JpaRunner<T> jpaRunner = new JpaRunner<>(JpaRunType.save, entity);
		return jpaRunner;
	}

	/**
	 * 执行
	 * @param <T>
	 * @param jpaRunner
	 * @return
	 */
	public static <T> String run(JpaRunner<T> jpaRunner) {
		// TODO Auto-generated method stub
		return null;
	}

}
