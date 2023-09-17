package com.github.chenyuxin.commonframework.base.constant;

/**
 * 编码常量
 */
public interface IdCodeConst {
	
	/**
	 * 自编64进制字符
	 * 同时支持16进制小写字符
	 */
	String idBase64="0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-_";
				 //	 123456789  12 15 18 21 24 27 30 33 36 39 42 45 48 51 54 57 60 63   
				 //           10 13 16 19 22 25 28 31 34 37 40 43 46 49 52 55 58 61 64
				 //            11 14 17 20 23 26 29 32 35 38 41 44 47 50 53 56 59 62 

	/**
	 * 16进制大写字符
	 */
	String hexDigits = "0123456789ABCDEF";
	
}
