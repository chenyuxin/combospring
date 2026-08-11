package com.github.chenyuxin.commonframework.daojpa.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.chenyuxin.commonframework.daojpa.common.DaoConst;
import com.github.chenyuxin.commonframework.daojpa.config.DaoResource;
import com.github.chenyuxin.commonframework.daojpa.intf.ComboJpa;
import com.github.chenyuxin.commonframework.daojpa.jparun.JpaRunner;

import jakarta.persistence.EntityManager;

@Service("remove" + DaoConst.JPA_RUNNER_SUFFIX)
public class ComboJpaRemoveImpl implements ComboJpa {
	
	private DaoResource daoResource;
	
	public ComboJpaRemoveImpl(DaoResource daoResource) {
		this.daoResource = daoResource;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public String run(JpaRunner jpaRunner) {
		EntityManager entityManager = daoResource.moreEntityManager(jpaRunner.getDataSourceName());
		Object entity = jpaRunner.getEntity();
		entityManager.remove(entity);
		return DaoConst.delObj_SUCCESS_MESSAGE;
	}

}
