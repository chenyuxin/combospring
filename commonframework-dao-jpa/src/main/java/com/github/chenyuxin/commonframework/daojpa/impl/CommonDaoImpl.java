package com.github.chenyuxin.commonframework.daojpa.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;

import com.github.chenyuxin.commonframework.daojpa.common.TableType;
import com.github.chenyuxin.commonframework.daojpa.intf.CommonDao;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class CommonDaoImpl implements CommonDao {
	
	@Autowired JpaRepositoryImplementation<?, ?> jparepository;

	@Override
	public boolean isTable(TableType tableType, Object... daoOptions) {
		// TODO Auto-generated method stub
		return false;
	}

}
