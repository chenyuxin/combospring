package com.github.chenyuxin.commonframework.webservice;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpClient.Version;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

import com.github.chenyuxin.commonframework.webservice.util.HttpReqParams;
import com.github.chenyuxin.commonframework.webservice.util.MyX509TrustManager;

import lombok.extern.slf4j.Slf4j;

/**
 * http请求
 */
@Slf4j
public class HttpReq {
	
	/**
	 * 默认客户端
	 */
	private static HttpClient defaultClient = HttpClient.newBuilder()
	        .version(Version.HTTP_1_1)
	        .followRedirects(Redirect.NORMAL)
	        .connectTimeout(Duration.ofMinutes(30))
	        .sslContext(MyX509TrustManager.getSSLContext())
	        .sslParameters(MyX509TrustManager.getSslParams())
	        //.proxy(ProxySelector.of(new InetSocketAddress("proxy.example.com", 80)))
	        //.authenticator()
	        .build();
	
	/**
	 * 同步发送http请求，(常用)
	 * @param url 请求url
	 * @param otherParam 其它参数(可选,不选使用默认)
	 * @param HttpMethod 请求方法
	 * @param Header 请求头
	 * @param String body 请求体
	 * @param Map<String,String> url传参
	 * @param Duration 超时设置
	 * @return
	 * 
	 * <br>样例
	 * <br> HttpResponse<String> response = HttpReq.send("http://10.114.226.80:8080/services/testSoap",
	 * <br>			Header.of("Content-Type", "text/xml;charset=UTF-8")
	 * <br>			.put("SOAPAction", ""),
	 * <br>			soapMessageString);
	 * <br>	
	 * <br>	System.out.println(response.statusCode());
	 * <br>	System.out.println(response.body()); 
	 * <br>
	 */
	public static HttpResponse<String> send(String url,Object... otherParam) {
		HttpRequest httpRequest = new HttpReqParams(otherParam).of(url);
		try {
			return defaultClient.send(httpRequest, BodyHandlers.ofString(StandardCharsets.UTF_8));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return null;
		}
	}

	/**
	 * 异步发送http请求，(常用)
	 * @param url
	 * @param otherParam
	 * @return CompletableFuture<HttpResponse<String>>
	 */
	public static CompletableFuture<HttpResponse<String>> sendAsync(String url, Object... otherParam) {
		HttpRequest httpRequest = new HttpReqParams(otherParam).of(url);
		return defaultClient.sendAsync(httpRequest, BodyHandlers.ofString(StandardCharsets.UTF_8));
	}
	
	
}
	
//  HttpRequest.newBuilder()
//	  .timeout(Duration.ofMinutes(2))
//  .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
//  .POST(BodyPublishers.ofString("test123"))
//  .header("Content-Type", "text/xml;charset=UTF-8")
//  .header("SOAPAction", "")
	

//	 defaultClient.sendAsync(request, BodyHandlers.ofString())
//  .thenApply(HttpResponse::body)
//  .thenAccept(s->{
//  	log.info(s);
//  })
//  ;
	

