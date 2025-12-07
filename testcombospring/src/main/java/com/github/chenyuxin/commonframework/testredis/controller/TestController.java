package com.github.chenyuxin.commonframework.testredis.controller;


import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.chenyuxin.commonframework.daojpa.config.DaoConfResource;
import com.github.chenyuxin.commonframework.daojpa.intf.CommonDao;

import jakarta.servlet.http.HttpServletRequest;


@Controller
public class TestController {
	
	@Autowired DaoConfResource daoConfResource;
	
	@Autowired CommonDao commonDao;
	
	@RequestMapping("/test1")
	public String test1(HttpServletRequest request) {
		System.out.println("ShowSql:" + daoConfResource.isShowSql());
		request.setAttribute("ShowSql", "ShowSql:" + daoConfResource.isShowSql());
		commonDao.selectBaseObj("select sysdate from dual", Date.class);
		return "index";
	}
	
	@RequestMapping("/test3")
	public String test3(HttpServletRequest request) {
		System.out.println("ShowSql:" + daoConfResource.isShowSql());
		request.setAttribute("ShowSql", "ShowSql:" + daoConfResource.isShowSql());
		commonDao.selectBaseObj("select sysdate from dual", Date.class);
		return "test3";
	}
	
	@RequestMapping("/test2")
	public String test2() {
		commonDao.selectBaseObj("select sysdate from dual", Date.class);
		return "index-html";
	}
	
	

}
