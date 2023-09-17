package com.github.chenyuxin.commonframework.test.cipher;

import java.security.MessageDigest;

public class MD5 {
	
	 /***  
    * MD5加码 生成32位md5码  
    */  
	public final static String EncoderByMd5(String s) { 
		
		char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};         
		  
       try {  
           byte[] btInput = s.getBytes("utf-8");  
           // 获得MD5摘要算法的 MessageDigest 对象  
           MessageDigest mdInst = MessageDigest.getInstance("MD5");  
           // 使用指定的字节更新摘要  
           mdInst.update(btInput);  
           // 获得密文  
           byte[] md = mdInst.digest();  
           // 把密文转换成十六进制的字符串形式  
           int j = md.length;  
           char str[] = new char[j * 2];  
           int k = 0;  
           for (int i = 0; i < j; i++) {  
               byte byte0 = md[i];  
               str[k++] = hexDigits[byte0 >>> 4 & 0xf];  
               str[k++] = hexDigits[byte0 & 0xf];  
           }  
           return new String(str);  
       } catch (Exception e) {  
           e.printStackTrace();  
           return null;  
       }  
   } 
	
	public static void main(String args[]) {
	   	String inputStr = "A@中国245b";   
	   	    	
	   	String md5String = EncoderByMd5(inputStr);
	   	System.out.println("SourceMD5加密后: "+md5String);  //SourceMD5加密后: dc14b22ec9f8102d5f40382f6ed00847
	   	/*
		String inputStr="123456";
		String md5String = EncoderByMd5(inputStr);
   		System.out.println("SourceMD5加密后: "+md5String);
		 */
   	
   }
	
}
