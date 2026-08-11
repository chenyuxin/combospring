package com.github.chenyuxin.commonframework.testjpa.service.impl;

import org.springframework.stereotype.Service;

//import com.github.chenyuxin.commonframework.daojpa.common.DaoConst;
//import com.github.chenyuxin.commonframework.daojpa.config.DaoResource;
import com.github.chenyuxin.commonframework.daojpa.jparun.Jpa;
import com.github.chenyuxin.commonframework.testjpa.service.intf.TestJpaService;

//import jakarta.persistence.EntityManager;

@Service
public class TestJpaServiceImpl implements TestJpaService {
	
//	private final DaoResource daoResource;
//	
//	private final EntityManager entityManager;
//	
//	public TestJpaServiceImpl(DaoResource daoResource) {
//		this.daoResource = daoResource;
//		this.entityManager = daoResource.moreEntityManager(DaoConst.defaultDataSourceName);
//	}

	@Override
	public String save() {
		Jpa.save(null).run();
		// TODO Auto-generated method stub
		return null;
	}

}
