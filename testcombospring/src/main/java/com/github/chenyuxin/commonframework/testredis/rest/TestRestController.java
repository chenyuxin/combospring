package com.github.chenyuxin.commonframework.testredis.rest;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.impl.Slf4jLogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson2.JSON;
import com.github.chenyuxin.commonframework.testredis.service.intf.TestService;


@RestController
@Validated
public class TestRestController {
	
	@Autowired TestService testService;
	
	@RequestMapping(value = "info/{action}",
			method= {RequestMethod.POST, RequestMethod.GET},
			produces= MediaType.APPLICATION_JSON_VALUE)
	public List<Map<String, Object>> info(@RequestBody Map<String,Object> body,@RequestHeader Map<String,String> header,
			@RequestParam Map<String, String> getParam,@PathVariable String action ){ 
		
		//TODO validate
		
		String phoneNo = String.valueOf( body.get("phone_no") );
		
		List<Map<String, Object>> list = testService.queryByPhoneNo(phoneNo);
		return list;
	}
	
	@RequestMapping(value = "ds/{action}",
			method= {RequestMethod.POST, RequestMethod.GET},
			produces= MediaType.APPLICATION_JSON_VALUE)
	public List<Map<String, Object>> ds1(@RequestParam Map<String, String> getParam, @PathVariable String action ){ 
		
		//TODO validate
		
		String phoneNo = String.valueOf( action );
		
		List<Map<String, Object>> list1 = testService.queryByPhoneNoDs1(phoneNo);
		List<Map<String, Object>> list2 = testService.queryByPhoneNoDs2(phoneNo);
		System.out.println("list1:" + JSON.toJSONString(list1));
		System.out.println("list2:" + JSON.toJSONString(list2));
		return list1;
	}

}
