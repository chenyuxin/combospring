package com.github.chenyuxin.commonframework.webservice.util;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.time.Duration;
import java.util.Map;

import org.springframework.http.HttpMethod;

/**
 * http请求的多种传参
 */
public class HttpReqParams {
	
	/**
	 * 请求方法
	 * POST GET
	 */
	private HttpMethod httpMethod;
	
	/**
	 * 请求头
	 */
	private Header header;
	
	/**
	 * 请求体
	 */
	private String body;
	
	/**
	 * url传参
	 */
	private Map<String,String> params;
	
	/**
	 * 超时设置
	 */
	private Duration duration;
	
	
	public HttpReqParams(Object... otherParams){
		for (int i=0;i<otherParams.length;i++) {
			Object obj = otherParams[i];
			if(obj instanceof HttpMethod httpMethod) {
				this.httpMethod = httpMethod;
			} else if (obj instanceof Header header) {
				this.header = header;
			} else if (obj instanceof String body) {
				this.body = body;
			} else if (obj instanceof Map) {
				@SuppressWarnings("unchecked")
				Map<String,String> urlParams = (Map<String,String>)obj;
				this.params = urlParams;
			} else if (obj instanceof Duration duration) {
				this.duration = duration;
			}
		}
	}
	
	public HttpRequest of(String url){
		java.net.http.HttpRequest.Builder httpRequestBuilder;
		
		if (null != this.params) {//初始url
			String lastUrl = WebserviceUtil.getUrl(url, this.params);
			httpRequestBuilder = HttpRequest.newBuilder(URI.create(lastUrl));
		} else {
			httpRequestBuilder = HttpRequest.newBuilder(URI.create(url));
		}
		
		//请求body
		if (null != this.httpMethod && 
				this.httpMethod == HttpMethod.GET || 
				this.httpMethod == HttpMethod.DELETE ) {
			//doNothing
		} else {//post等请求
			if (null != this.body) {
				httpRequestBuilder = httpRequestBuilder.POST(BodyPublishers.ofString(this.body));
			}
		}
		
		if (null == this.header) {//设置请求头
			this.header = Header.of();
		}
		httpRequestBuilder = WebserviceUtil.setHeader(this.header, httpRequestBuilder);
		
		if (null != this.duration) {//设置超时时限
			httpRequestBuilder = httpRequestBuilder.timeout(this.duration);
		}
		
		return httpRequestBuilder.build();
	}

	public HttpMethod getHttpMethod() {
		return httpMethod;
	}


	public void setHttpMethod(HttpMethod httpMethod) {
		this.httpMethod = httpMethod;
	}


	public Header getHeader() {
		return header;
	}


	public void setHeader(Header header) {
		this.header = header;
	}


	public String getBody() {
		return body;
	}


	public void setBody(String body) {
		this.body = body;
	}


	public Map<String, String> getParams() {
		return params;
	}


	public void setParams(Map<String, String> params) {
		this.params = params;
	}


	public Duration getDuration() {
		return duration;
	}


	public void setDuration(Duration duration) {
		this.duration = duration;
	}
	
	
	

}
