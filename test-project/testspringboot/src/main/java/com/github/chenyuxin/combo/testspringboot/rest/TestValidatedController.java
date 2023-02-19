package com.github.chenyuxin.combo.testspringboot.rest;

import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.chenyuxin.combo.testspringboot.testdto.EmpUser;
import com.github.chenyuxin.combo.validator.ValidUtil;

@RestController
@Validated
public class TestValidatedController {
	
	@RequestMapping("info1")
	public String info1(@Validated @RequestBody EmpUser empUser) {
		return empUser.getName();
	}
	
	@RequestMapping("info2")
	public String info2(@Validated @RequestBody EmpUser empUser,BindingResult result) {
		if(	result.hasErrors() ) {
			result.getFieldErrors().forEach(e -> {
				System.out.println(e.getCode());
				System.out.println(e.getField());
				System.out.println(e.getDefaultMessage());
			});
			
			return result.getAllErrors().toString();
		}
		System.out.println(empUser);
		return empUser.getName();
	}
	
	@RequestMapping("info3")
	public String info3(@RequestBody EmpUser empUser) {
		ValidUtil.validate(empUser);
		return empUser.getName();
	}

}