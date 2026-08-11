package com.github.chenyuxin.commonframework.testjpa.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.chenyuxin.commonframework.daojpa.config.DaoConfResource;
import com.github.chenyuxin.commonframework.daojpa.jparun.Jpa;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class TestJpaController {
	
	public TestJpaController() {
		
	}
	
	@RequestMapping("/testJpa1")
	public ResponseEntity<?> testJpa1(HttpServletRequest request) {
		System.out.println("ShowSql:" + DaoConfResource.showSql);
		request.setAttribute("ShowSql", "ShowSql:" + DaoConfResource.showSql);
		//commonDao.selectBaseObj("select sysdate from dual", Date.class);
		String result = Jpa.save(null).run();
		return ResponseEntity.ok(result);
	}

}
