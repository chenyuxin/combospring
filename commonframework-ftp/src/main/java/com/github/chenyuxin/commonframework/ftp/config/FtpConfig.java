package com.github.chenyuxin.commonframework.ftp.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "commonframework")
public class FtpConfig {
	
	public static Map<String,FtpInfo> ftps = new HashMap<>();
	
	public Map<String, FtpInfo> getFtps() {
		return ftps;
	}

	public void setFtps(Map<String, FtpInfo> ftps) {
		FtpConfig.ftps = ftps;
	}
	
	public static String defualtFtp;

	public String getDefualtFtp() {
		return defualtFtp;
	}

	public void setDefualtFtp(String defualtFtp) {
		if (null == defualtFtp || "".equals(defualtFtp)) {
			if (ftps.size()>0) {
				FtpConfig.defualtFtp = String.valueOf(ftps.keySet().toArray()[0]);
			}
		} else {
			FtpConfig.defualtFtp = defualtFtp;
		}
	}
	
	

}