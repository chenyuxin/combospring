package com.github.chenyuxin.commonframework.util;

/**
 * 位运算工具
 */
public class CommonUtilBit {
	
	/**
	 * 检查对应位数的配置值是否开启 (0开启1关闭)
	 * @param switchValue 配置的值
	 * @param digit 检查配置的位数(1~32)
	 * @return
	 */
	public static boolean enable(int switchValue,int digit) {
		assert digit>0 && digit<32;
		return (switchValue&1<<(digit-1)) == 0;
	}
	
	/**
	 * 检查对应位数的配置值是否关闭 (0开启1关闭)
	 * @param switchValue 配置的值
	 * @param digit 检查配置的位数(1~32)
	 * @return
	 */
	public static boolean unable(int switchValue,int digit) {
		assert digit>0 && digit<32;
		return (switchValue&1<<(digit-1)) != 0;
	}

}
