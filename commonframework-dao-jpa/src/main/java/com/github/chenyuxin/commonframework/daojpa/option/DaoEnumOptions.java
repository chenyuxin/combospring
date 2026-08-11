package com.github.chenyuxin.commonframework.daojpa.option;

/**
 * commonDao特性设置
 * @author chenyuxin
 */
public enum DaoEnumOptions {
	
	/**
	 * 不配置此项，
	 * 默认commonDao在执行出错时抛出RuntimeException异常，
	 * 配置此项返回失败消息或空值。
	 */
	MsgException,
	
	
	;


}