package com.github.chenyuxin.commonframework.webservice.util;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import lombok.extern.slf4j.Slf4j;

/**
 * 信任所有https证书
 */
@Slf4j
public class MyX509TrustManager implements X509TrustManager {
	
	private static final X509Certificate[] acceptedIssuers = new X509Certificate[] {};
	
	
	private static TrustManager[] trustManagers = new TrustManager[] { new MyX509TrustManager() };
	private static volatile SSLContext sc = null;
	private static volatile SSLParameters sslParams = null;
	
	/**
	 * 信任所有https证书
	 */
	public static SSLContext getSSLContext() {
		if (null == sc) {
			synchronized(MyX509TrustManager.class) {
				if (null == sc) {
					try {
						sc = SSLContext.getInstance("SSL");
//						sc = SSLContext.getInstance("TLS");
						System.setProperty("jdk.internal.httpclient.disableHostnameVerification", "true");//取消主机名验证
						sc.init(null, trustManagers, new SecureRandom());
					} catch (Exception e) {
						log.error(e.getMessage(),e);
					}
				}
			}
		}
        return sc;
	}
	
	/**
	 * 信任所有https证书
	 */
	public static SSLParameters getSslParams() {
		if (null == sslParams) {
			synchronized(MyX509TrustManager.class) {
				if (null == sslParams) {
					sslParams = new SSLParameters();
					sslParams.setEndpointIdentificationAlgorithm("");
				}
			}
		}
        return sslParams;
	}
	
	
	
	
	
	public static TrustManager[] getTrustManagers() {
        TrustManager[] trustAllCertificates = {new MyX509TrustManager()};
        return trustAllCertificates;
    }

	@Override
	public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
		
	}

	@Override
	public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
		
	}

	@Override
	public X509Certificate[] getAcceptedIssuers() {
		return acceptedIssuers;
	}

}
