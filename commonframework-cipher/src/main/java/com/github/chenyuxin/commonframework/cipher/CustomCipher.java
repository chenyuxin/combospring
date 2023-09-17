package com.github.chenyuxin.commonframework.cipher;

/**
 * 其它自定义加密<br>
 * 请写成单例类<br>
 * 使用getSingleInstance方法名获取单例 
 * 实现本接口<br>
 */
public interface CustomCipher {
	
	/**
	 * 自定义加密标准方法
	 * @param source 源文
	 * @param key 密钥
	 * @param iv 向量
	 * @return
	 * @throws Exception
	 */
	byte[] encrypt(byte[] source,byte[] key,byte[] iv) throws Exception;

	/**
	 * 自定义解密标准方法
	 * @param cipher 密文
	 * @param key 密钥
	 * @param iv 向量
	 * @return
	 * @throws Exception
	 */
	byte[] decrypt(byte[] cipher,byte[] key,byte[] iv) throws Exception;
	
}
