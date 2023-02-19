package com.github.chenyuxin.combo.testspringboot.rest;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.chenyuxin.combo.base.type.database.TableType;
import com.github.chenyuxin.combo.dao.intf.ComboDao;
import com.github.chenyuxin.combo.testspringboot.testdto.EmpUser;
import com.github.chenyuxin.combo.validator.ValidUtil;


@RestController
public class TestRestController {
	
	@Autowired ComboDao comboDao;
	
	@RequestMapping(value = "info/{action}",
			method= {RequestMethod.POST,RequestMethod.GET},
			produces="application/json;charset=UTF-8")
	public String info(@RequestBody String body,@RequestHeader Map<String,String> header,
			@RequestParam Map<String, String> getParam,@PathVariable String action ){
		List<Map<String, Object>> aString = comboDao.selectObjMap("select xm from brmp_grxx","dataSource");
		Date date = comboDao.selectBaseObj("select sysdate from dual", Date.class,"dataSource");
		Boolean a = comboDao.isTable(TableType.c("test2"),"dataSource");
		return a.toString() + date + aString;

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
	
	
//	@RequestMapping("/testR3")
//	public String test3() {
//		String sqlString = "select a.kz_renwuzhaopian as \"a\" from md_9316af152beb4483bbcd a where a.certificate_holder='孙丽婷'";
//		List<Map<String,Object>> kz_renwuzhaopian = comboDao.selectObjMap(sqlString);
//		byte[] b =  (byte[]) kz_renwuzhaopian.get(0).get("a");
//		String zpString = Base64Utils.encodeToString(b);
//		String zpString2 = BASE64Util.encryptBASE64(b);
//		if (zpString.equals(zpString2)) {
//			System.out.println("=");
//			//System.out.println(zpString);
//		}
//		try {
//			System.out.println(Cipher.MD5.encrypt(zpString));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		byte[] b2 = commonDao.selectBaseObj(sqlString, byte[].class);
//		String zp2String = Base64Utils.encodeToString(b2);
//		String zp2String2 = BASE64Util.encryptBASE64(b2);
//		if (zp2String.equals(zpString2)) {
//			System.out.println("=2");
//			//System.out.println(zp2String);
//		}
//		if (zp2String2.equals(zpString2)) {
//			System.out.println("=3");
//			//System.out.println(zp2String2);
//		}
//		try {
//			System.out.println(Cipher.MD5.encrypt(zp2String2));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		return zpString2 + "<br>" + zp2String2;
//	}


}
