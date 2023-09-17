package com.github.chenyuxin.commonframework.cipher;

import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import com.github.chenyuxin.commonframework.base.constant.StringPool;

/**
 * 加密类型
 */
enum CipherType {
	/**
	 * 单向散列加密，不可逆
	 */
	Hash {
		
		@Override
		<T> byte[] encrypt(byte[] source,String cipherName,T key,byte[] iv) throws NoSuchAlgorithmException {
			MessageDigest mdInst = MessageDigest.getInstance(cipherName);
			mdInst.update(source);
			return mdInst.digest();
		}

		@Override
		<T> byte[] decrypt(byte[] cipher, String cipherName,T key,byte[] iv) throws NoSuchAlgorithmException {
			throw new NoSuchAlgorithmException(cipherName.concat(",不能用作解密"));
		}
	},
	
	/**
	 * 对称加密
	 */
	Symmetric {
		
		@Override
		<T> byte[] encrypt(byte[] source, String cipherName,T key,byte[] iv) throws Exception {
			assert key instanceof Key;
			Cipher cipherTool = Cipher.getInstance(cipherName);  
			cipherTool.init(Cipher.ENCRYPT_MODE, (Key) key, null==iv?null:new IvParameterSpec(iv));  
			return cipherTool.doFinal(source);
		}
		
		@Override
		<T> byte[] decrypt(byte[] cipher, String cipherName,T key,byte[] iv) throws Exception {
			assert key instanceof Key;
			Cipher cipherTool = Cipher.getInstance(cipherName);  
            cipherTool.init(Cipher.DECRYPT_MODE, (Key) key, null==iv?null:new IvParameterSpec(iv));  
			return cipherTool.doFinal(cipher);
		}
	},
	
	/**
	 * 非对称加密
	 */
	Asymmetric {

		@Override
		<T> byte[] encrypt(byte[] source, String cipherName,T key,byte[] iv) throws Exception {
			assert key instanceof Key;
			Cipher cipherTool = Cipher.getInstance(cipherName);
			cipherTool.init(Cipher.ENCRYPT_MODE, (Key) key);
			return cipherTool.doFinal(source);
		}

		@Override
		<T> byte[] decrypt(byte[] cipher, String cipherName,T key,byte[] iv) throws Exception {
			assert key instanceof Key;
			Cipher cipherTool = Cipher.getInstance(cipherName);  
            cipherTool.init(Cipher.DECRYPT_MODE, (Key) key);
			return cipherTool.doFinal(cipher);
		}
	},
	
	/**
	 * 扩展 自定义的加密方法 内容<br>
	 * 加解密方法的格式固定要求：<br>
	 * cipherName为自定义方法类全路径,并且是实现CustomCipher接口的单例类。<br>
	 * 通过getSingleInstance()方法获取单例
	 */
//	Custom {
//
//		@Override
//		<T> byte[] encrypt(byte[] source, String cipherName, T key, byte[] iv) throws Exception {
//			assert key instanceof byte[];
//			Object customCipher = MyObj.invokeMethod(Class.forName(cipherName), "getSingleInstance");
//			Class<?>[] parameterTypes = {byte[].class,byte[].class,byte[].class};
//			return (byte[]) MyObj.invokeMethod(customCipher, "encrypt",parameterTypes,source, key, iv);
//		}
//
//		@Override
//		<T> byte[] decrypt(byte[] cipher, String cipherName, T key, byte[] iv) throws Exception {
//			assert key instanceof byte[];
//			Object customCipher = MyObj.invokeMethod(Class.forName(cipherName), "getSingleInstance");
//			Class<?>[] parameterTypes = {byte[].class,byte[].class,byte[].class};
//			return (byte[]) MyObj.invokeMethod(customCipher, "decrypt",parameterTypes,cipher, key, iv);
//		}
//	},
	
	/**
	 * 数字签名
	 */
	DigitalSign {

		@Override
		<T> byte[] encrypt(byte[] source, String cipherName, T key, byte[] iv) throws Exception {
			assert key instanceof PrivateKey;
			Signature signature = Signature.getInstance(cipherName);
			signature.initSign((PrivateKey) key);
			signature.update(source);
			return signature.sign();
		}

		@Override
		<T> byte[] decrypt(byte[] cipher, String cipherName, T key, byte[] sign) throws Exception {
			assert key instanceof PublicKey;
			Signature signature = Signature.getInstance(cipherName);
	        signature.initVerify((PublicKey) key);
	        signature.update(cipher);
	        
	        if (signature.verify(sign)) {
	        	return StringPool.BLANK.getBytes();//内容一致
	        } else {
				return null;//内容有篡改
	        }
		}
		
	},
	
	;
	
	/**
	 * 加密
	 * @param <T>
	 * @param source 源文
	 * @param cipherName 加密实列名
	 * @param key 密钥
	 * @param iv 向量
	 * @return
	 * @throws Exception
	 */
	abstract <T> byte[] encrypt(byte[] source,String cipherName,T key,byte[] iv) throws Exception;

	/**
	 * 解密
	 * @param <T>
	 * @param cipher 密文
	 * @param cipherName 加密实列名
	 * @param key 密钥
	 * @param iv 向量
	 * @return
	 * @throws Exception
	 */
	abstract <T> byte[] decrypt(byte[] cipher,String cipherName,T key,byte[] iv) throws Exception;
	
	
	static {
		Security.addProvider(new BouncyCastleProvider());//添加加密算法引用
	}

}
