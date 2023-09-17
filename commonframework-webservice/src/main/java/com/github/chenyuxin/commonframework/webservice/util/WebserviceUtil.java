package com.github.chenyuxin.commonframework.webservice.util;

import java.net.URLEncoder;
import java.net.http.HttpRequest.Builder;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;

/**
 * 网络请求的常用工具
 */
public class WebserviceUtil {
	
	/**
	 * 拼接url的请求参数
	 * @param URLString
	 * @param param
	 * @return
	 */
	static String getUrl(String URLString,Map<String, String> param) {
		StringBuilder lastURLString = new StringBuilder(URLString);
		boolean firstP = true;//get请求地址？区别没有带参数和带参数的情况
		if (URLString.contains("?")) {
			firstP = false;
		}
		if (null != param && !param.isEmpty()) {
			for (Map.Entry<String, String> p : param.entrySet()) {
				if (firstP) {
					lastURLString.append("?");
					firstP = false;
				} else {
					lastURLString.append("&");
				}
				lastURLString.append(p.getKey()).append("=").append(URLEncoder.encode(p.getValue(), StandardCharsets.UTF_8));
			}
		}
		return lastURLString.toString();
	}
	
	/**
	 * 设置请求头
	 * @param header
	 * @param httpRequestBuilder
	 * @return
	 */
	static Builder setHeader(Header header,Builder httpRequestBuilder) {
		Iterator<Map.Entry<String, String>> e = header.getHeaders().entrySet().iterator();
		while (e.hasNext()) {
			Map.Entry<String, String> h = e.next();
			httpRequestBuilder = httpRequestBuilder.header(h.getKey(), h.getValue());
		}
		return httpRequestBuilder;
	}

}
