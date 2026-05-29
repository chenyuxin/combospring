package com.github.chenyuxin.commonframework.daojpa.jparun;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.github.chenyuxin.commonframework.daojpa.common.DaoConst;
import com.github.chenyuxin.commonframework.daojpa.config.DaoResource;

import jakarta.persistence.EntityManager;

public enum JpaRunType {

	find {

		@Override
		public <T> T run(DaoResource daoResource, JpaRunner jpaRunner) {
			EntityManager entityManager = daoResource.moreEntityManager(jpaRunner.getDataSourceName());
			@SuppressWarnings("unchecked")
			Class<T> clazz = (Class<T>) jpaRunner.getEntityClass();

			// TODO Auto-generated method stub
			return entityManager.find(clazz, "");
		}

	},

	remove {

		@SuppressWarnings("unchecked")
		@Override
		public String run(DaoResource daoResource, JpaRunner jpaRunner) {
			EntityManager entityManager = daoResource.moreEntityManager(jpaRunner.getDataSourceName());
			Object entity = jpaRunner.getEntity();
			if (entity instanceof Collection<?> entityCollection) {
				if (entityCollection.isEmpty()) {
					return DaoConst.delObj_SUCCESS_MESSAGE;
				}
				for (Object item : entityCollection) {
					entityManager.remove(item);
				}
				return DaoConst.delObj_SUCCESS_MESSAGE;
			} else {
				entityManager.remove(entity);
				return DaoConst.delObj_SUCCESS_MESSAGE;
			}
		}

	},

	merge {

		@SuppressWarnings("unchecked")
		@Override
		public Object run(DaoResource daoResource, JpaRunner jpaRunner) {
			EntityManager entityManager = daoResource.moreEntityManager(jpaRunner.getDataSourceName());
			Object entity = jpaRunner.getEntity();
			if (entity instanceof Collection<?> entityCollection) {
				List<Object> mergedResults = new ArrayList<>();
				for (Object item : entityCollection) {
					mergedResults.add(entityManager.merge(item));
				}
				return mergedResults;
			} else {
				return entityManager.merge(entity);
			}
		}

	},

	;

	public abstract <T> T run(DaoResource daoResource, JpaRunner jpaRunner);

}
