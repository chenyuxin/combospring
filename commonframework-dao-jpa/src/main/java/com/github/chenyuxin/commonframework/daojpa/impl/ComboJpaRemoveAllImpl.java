package com.github.chenyuxin.commonframework.daojpa.impl;

import java.util.Collection;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.chenyuxin.commonframework.daojpa.common.DaoConst;
import com.github.chenyuxin.commonframework.daojpa.config.DaoResource;
import com.github.chenyuxin.commonframework.daojpa.intf.ComboJpa;
import com.github.chenyuxin.commonframework.daojpa.jparun.JpaRunner;

import jakarta.persistence.EntityManager;

@Service("removeAll")
public class ComboJpaRemoveAllImpl implements ComboJpa {
	
	private DaoResource daoResource;
	
	public ComboJpaRemoveAllImpl(DaoResource daoResource) {
		this.daoResource = daoResource;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public String run(JpaRunner jpaRunner) {
		EntityManager entityManager = daoResource.moreEntityManager(jpaRunner.getDataSourceName());
		Collection<?> entityList = (Collection<?>) jpaRunner.getEntity();
		if (entityList.isEmpty()) {
			return DaoConst.delObj_SUCCESS_MESSAGE;
		}
		for (Object item : entityList) {
			entityManager.remove(item);
		}
		return DaoConst.delObj_SUCCESS_MESSAGE;
	}

}
