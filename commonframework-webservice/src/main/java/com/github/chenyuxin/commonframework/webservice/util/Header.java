package com.github.chenyuxin.commonframework.webservice.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.MediaType;

/**
 * 常用httpReq请求,的header
 */
public class Header {
	public static final String CONTENT_TYPE = "Content-Type";
//	public static final String CONNECTION = "Connection";
	
	private Map<String, String> headers = new HashMap<>();
	
	Header(String key,String value){
		init();
		this.headers.put(key, value);
	}
	
	Header(Map<String,String> headers) {
		init();
		headers.forEach((k, v) -> {
			this.headers.put(k, v);
		});
	}
	
	Header() {
		init();
	}
	
	/**
	 * 默认请求头
	 */
	private void init() {
		this.put(CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//	        .put(CONNECTION, "keep-alive")
	        ;
	}
	
	public static Header of() {
		return new Header(); 
	}
	
	public static Header of(Map<String,String> headers) {
		return new Header(headers); 
	}
	
	public static Header of(String key,String value) {
		return new Header(key,value); 
	}
	
	public Header put(String key,String value) {
		this.headers.put(key, value);
		return this;
	}
	
	public Map<String, String> getHeaders() {
		return this.headers;
	}
	
	
}