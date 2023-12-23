package com.github.chenyuxin.commonframework.validator;

import java.util.Set;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidationException;
import jakarta.validation.Validator;

/**
 * 验证工具类 <br> 
 * @author chenyuxin
 * 
 * <br><B>常用的对象验证注解:<B>
@Null  被注释的元素必须为null
@NotNull  被注释的元素不能为null
@AssertTrue  被注释的元素必须为true
@AssertFalse  被注释的元素必须为false
@Min(value)  被注释的元素必须是一个数字，其值必须大于等于指定的最小值
@Max(value)  被注释的元素必须是一个数字，其值必须小于等于指定的最大值
@DecimalMin(value)  被注释的元素必须是一个数字，其值必须大于等于指定的最小值
@DecimalMax(value)  被注释的元素必须是一个数字，其值必须小于等于指定的最大值
@Size(max,min)  被注释的元素的大小必须在指定的范围内。
@Digits(integer,fraction)  被注释的元素必须是一个数字，其值必须在可接受的范围内
@Past  被注释的元素必须是一个过去的日期
@Future  被注释的元素必须是一个将来的日期
@Pattern(value) 被注释的元素必须符合指定的正则表达式。
@Email 被注释的元素必须是电子邮件地址
@Length 被注释的字符串的大小必须在指定的范围内
@NotEmpty  被注释的字符串必须非空
@Range  被注释的元素必须在合适的范围内

 *
 */
public class ValidUtil {
	
	/**
	 * 传入需要验证的对象
	 * @param <T>
	 * @param ValidObj
	 * @Exception 验证有误抛出ValidationException异常
	 */
	public static <T> void validate(T ValidObj) {
		
		Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
		Set<ConstraintViolation<T>> constraintViolations = validator.validate(ValidObj);
		if (!constraintViolations.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			constraintViolations.forEach(constraintViolation -> {
				sb.append(constraintViolation.getPropertyPath()).append("[");
				sb.append(constraintViolation.getMessage()).append("]");
			});
			throw new ValidationException(sb.toString());
		}
	}

}



/**
import java.util.Date;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

//测试验证用户信息
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
 
 
 
@NotNull(message = "字段值不能为空,Map 和 Array 对象不能是 null, 但可以是空集（size = 0）")
private String name;
@NotEmpty(message = "Map 和 Array 对象不能是 null 并且相关对象的 size 大于 0")
private String name1;
@NotBlank(message = "String 不是 null 且去除两端空白字符后的长度（trimmed length）大于 0")
private String name2;
@Max(value = 20,message = "最大长度为20")
private String address;
@NotNull
@Size(max=10,min=5,message = "字段长度要在5-10之间")
private String fileName;
@Pattern(regexp = "正则表达式",message = "不满足正则表达式")
private String email;
@AssertTrue(message = "字段为true才能通过")
private boolean isSave;
@Future(message = "时间在当前时间之后才可以通过")
private Date date;

//在controller中MVC集成验证
@RequestMapping("validated")
public String validated(@Validated @RequestBody EmpUser empUser,BindingResult result) {
	
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
 
 
 
 */

