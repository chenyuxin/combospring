package com.github.chenyuxin.commonframework.daojpa.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.chenyuxin.commonframework.daojpa.config.DaoResource;
import com.github.chenyuxin.commonframework.daojpa.intf.ComboJpa;
import com.github.chenyuxin.commonframework.daojpa.jparun.JpaRunner;

import jakarta.persistence.EntityManager;

@Service("merge")
public class ComboJpaMergeImpl implements ComboJpa {
	
	private DaoResource daoResource;
	
	public ComboJpaMergeImpl(DaoResource daoResource) {
		this.daoResource = daoResource;
	}

	@Transactional
	@Override
	public <T> T run(JpaRunner jpaRunner) {
		EntityManager entityManager = daoResource.moreEntityManager(jpaRunner.getDataSourceName());
		@SuppressWarnings("unchecked")
		T entity = (T) jpaRunner.getEntity();
		return entityManager.merge(entity);
	}

}
