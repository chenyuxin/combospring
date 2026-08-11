package com.github.chenyuxin.commonframework.daojpa.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.chenyuxin.commonframework.daojpa.config.DaoResource;
import com.github.chenyuxin.commonframework.daojpa.intf.ComboJpa;
import com.github.chenyuxin.commonframework.daojpa.jparun.JpaRunner;

import jakarta.persistence.EntityManager;

@Service("mergeAll")
public class ComboJpaMergeAllImpl implements ComboJpa {
	
	private DaoResource daoResource;
	
	public ComboJpaMergeAllImpl(DaoResource daoResource) {
		this.daoResource = daoResource;
	}

	@Transactional
	@SuppressWarnings("unchecked")
	@Override
	public Object run(JpaRunner jpaRunner) {
		EntityManager entityManager = daoResource.moreEntityManager(jpaRunner.getDataSourceName());
		Collection<?> entityList = (Collection<?>) jpaRunner.getEntity();
		List<Object> mergedResults = new ArrayList<>();
		for (Object item : entityList) {
			mergedResults.add(entityManager.merge(item));
		}
		return mergedResults;
	}

}
