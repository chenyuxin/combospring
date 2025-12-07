package com.github.chenyuxin.commonframework.testredis.testdto;

import java.util.Date;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class EmpUser {
	@NotBlank(message = "姓名不能为空")
	private String name;
	@NotNull
	@Max(value=999,message = "年龄范围不正确")
	@Min(value=1,message = "年龄范围不正确")
	private Integer age;
	@Email
	private String email;
	
	private Date brithday;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Date getBrithday() {
		return brithday;
	}
	public void setBrithday(Date brithday) {
		this.brithday = brithday;
	}
	@Override
	public String toString() {
		return "EmpUser [name=" + name + ", age=" + age + ", email=" + email + ", brithday=" + brithday + "]";
	}
	
	
}