package com.github.chenyuxin.combo.testspringboot.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.chenyuxin.combo.dao.intf.ComboDao;
import com.github.chenyuxin.combo.dao.resource.DaoConfResource;

import jakarta.servlet.http.HttpServletRequest;


@Controller
public class TestController {
	
	@Autowired DaoConfResource daoConfResource;
	
	@Autowired ComboDao comboDao;
	
	@RequestMapping("/test1")
	public String test1(HttpServletRequest request) {
		System.out.println("ShowSql:" + daoConfResource.isShowSql());
		request.setAttribute("ShowSql", "ShowSql:" + daoConfResource.isShowSql());
		return "index";
	}
	
	@RequestMapping("/test2")
	public String test2() {
		return "index-html";
	}
	
	

}
