package com.github.chenyuxin.combo.base.type;

public interface Type {
	
	/**
	 * 类型名
	 */
	public String getTypeName();
	
	/**
	 * 通过类型名获取类型
	 * @param <T>
	 * @param typeName
	 * @return
	 */
	public <T extends Type> T getType();

}
