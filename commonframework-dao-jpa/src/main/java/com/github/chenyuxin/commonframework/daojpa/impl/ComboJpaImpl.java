package com.github.chenyuxin.commonframework.daojpa.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.chenyuxin.commonframework.daojpa.config.DaoResource;
import com.github.chenyuxin.commonframework.daojpa.intf.ComboJpa;
import com.github.chenyuxin.commonframework.daojpa.jparun.JpaRunner;

@Service
public class ComboJpaImpl implements ComboJpa {
	
	private DaoResource daoResource;
	
	public ComboJpaImpl(DaoResource daoResource) {
		this.daoResource = daoResource;
	}

	@Transactional
	@Override
	public <T> T run(JpaRunner jpaRunner) {
		return jpaRunner.getJpaRunType().run(daoResource, jpaRunner);
	}

}
