package com.github.chenyuxin.commonframework.cipher;

import java.nio.charset.Charset;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.SecretKeySpec;

import com.github.chenyuxin.commonframework.base.constant.StringPool;
import com.github.chenyuxin.commonframework.base.converter.ByteConverter;
import com.github.chenyuxin.commonframework.base.util.CharsetUtil;

@SuppressWarnings("unchecked")
public enum Cipher {
	/**
	 * AES无向量
	 */
	AES(CipherType.Symmetric,"AES") {
		@Override
		Key getKey(byte[] key,boolean isPublic) {
			return new SecretKeySpec(key, "AES"); 
		}
	},
	/**
	 * AES带向量
	 */
	AESIv(CipherType.Symmetric,"AES/CBC/PKCS5Padding") {
		@Override
		Key getKey(byte[] key,boolean isPublic) {
			return new SecretKeySpec(key, "AES"); 
		}
	},
	DES(CipherType.Symmetric,"desede/CBC/PKCS5Padding") {
		@Override
		Key getKey(byte[] key,boolean isPublic) throws Exception {
			KeySpec keySpec=new DESedeKeySpec(key);
			SecretKeyFactory keyFactory=SecretKeyFactory.getInstance("desede");              
			return keyFactory.generateSecret(keySpec);
		}
	},
	ThreeDES(CipherType.Symmetric,"desede/CBC/PKCS5Padding") {
		@Override
		Key getKey(byte[] key,boolean isPublic) {
			return new SecretKeySpec(key, "desede"); 
		}
	},
	/**
	 * SM4无向量
	 */
	SM4(CipherType.Symmetric,"SM4/ECB/PKCS7Padding") {
		@Override
		Key getKey(byte[] key,boolean isPublic) {
			return new SecretKeySpec(key, "SM4"); 
		}
	},
	/**
	 * SM4带向量
	 */
	SM4Iv(CipherType.Symmetric,"SM4/CBC/PKCS5Padding") {
		@Override
		Key getKey(byte[] key,boolean isPublic) {
			return new SecretKeySpec(key, "SM4"); 
		}
	},
	
	
	
	
	RSA(CipherType.Asymmetric, "RSA"){
		@Override
		Key getKey(byte[] key,boolean isPublic) throws Exception {
			if (isPublic) {
				return KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(key));
			} else {
				return KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(key));
			}
		}
	},
	/**
	 * 数字签名<br>
	 * encrypt方法为使用私钥签名<br>
	 * decrypt方法为使用公钥验证<br>
	 */
	DSA(CipherType.DigitalSign, "DSA"){
		@Override
		Key getKey(byte[] key,boolean isPublic) throws Exception {
			if (isPublic) {//签名用私钥加密，公钥验证
				return KeyFactory.getInstance("DSA").generatePrivate(new PKCS8EncodedKeySpec(key));
			} else {
				return KeyFactory.getInstance("DSA").generatePublic(new X509EncodedKeySpec(key));
			}
		}
	},
//	ECC(CipherType.custom, "ECC"){
//		@Override
//		Key getKey(byte[] key,boolean isPublic) throws Exception {
//			if (isPublic) {
//				return KeyFactory.getInstance("ECC").generatePublic(new X509EncodedKeySpec(key));
//			} else {
//				return KeyFactory.getInstance("ECC").generatePrivate(new PKCS8EncodedKeySpec(key));
//			}
//		}
//	},
//	DH(CipherType.custom, "DH"){
//		@Override
//		Key getKey(byte[] key,boolean isPublic) throws Exception {
//			if (isPublic) {
//				return KeyFactory.getInstance("DH").generatePublic(new X509EncodedKeySpec(key));
//			} else {
//				return KeyFactory.getInstance("DH").generatePrivate(new PKCS8EncodedKeySpec(key));
//			}
//		}
//	},
	
	
	MD2(CipherType.Hash,"MD2") {
		@Override
		<T> T getKey(byte[] key,boolean isPublic) throws InvalidKeyException {
			throw new InvalidKeyException("MD2不使用key");
		}
	},
	MD5(CipherType.Hash,"MD5") {
		@Override
		<T> T getKey(byte[] key,boolean isPublic) throws InvalidKeyException {
			throw new InvalidKeyException("MD5不使用key");
		}
	} ,
	SHA_1(CipherType.Hash,"SHA-1") {
		@Override
		<T> T getKey(byte[] key,boolean isPublic) throws InvalidKeyException {
			throw new InvalidKeyException("SHA-1不使用key");
		}
	} ,
	SHA_256(CipherType.Hash,"SHA-256") {
		@Override
		<T> T getKey(byte[] key,boolean isPublic) throws InvalidKeyException {
			throw new InvalidKeyException("SHA-256不使用key");
		}
	} ,
	SHA_384(CipherType.Hash,"SHA-384"){
		@Override
		<T> T getKey(byte[] key,boolean isPublic) throws InvalidKeyException {
			throw new InvalidKeyException("SHA-384不使用key");
		}
	}, 
	SHA_512(CipherType.Hash,"SHA-512"){
		@Override
		<T> T getKey(byte[] key,boolean isPublic) throws InvalidKeyException {
			throw new InvalidKeyException("SHA-512不使用key");
		}
	}, 
	
	
	
	/**
	 * 自定义加密SM4 无向量
	 */
//	CustomSM4(CipherType.Custom,CustomSM4.class.getName()) {
//		@Override
//		byte[] getKey(byte[] key,boolean isPublic) {
//			return key; 
//		}
//	},
	
	;
	
	private final CipherType cipherType;
	
	/**
	 * Algorithm
	 */
	private final String cipherName;
	
	Cipher(CipherType cipherType,String cipherName) {
		this.cipherType = cipherType;
		this.cipherName = cipherName;
	}
	
	public static final byte[] isPrivateKey = StringPool.BLANK.getBytes();
	public static final byte[] isPublicKey = null;
	
	abstract <T> T getKey(byte[] key,boolean isPublic) throws Exception;
	
	/**
	 * 哈希散列加密
	 * @param source 原文
	 * @return
	 * @throws Exception
	 */
	public byte[] encrypt(byte[] source) throws Exception {
		assert this.cipherType == CipherType.Hash;
		return this.cipherType.encrypt(source, this.cipherName, null,null);
	}
	
	/**
	 * 加密
	 * @param source 原文
	 * @param key 密钥(非对称加密默认为公钥)
	 * @return
	 * @throws Exception
	 */
	public byte[] encrypt(byte[] source,byte[] key) throws Exception {
		return this.cipherType.encrypt(source, this.cipherName, this.getKey(key,true), null);
	}
	
	/**
	 * 加密 (添加向量或公私钥标识)
	 * @param source 原文
	 * @param key 密钥
	 * @param iv 向量 或在非对称加密中iv是公私钥填值Cipher.isPrivateKey为私钥,Cipher.isPublicKey为公钥
	 * @return
	 * @throws Exception
	 */
	public byte[] encrypt(byte[] source,byte[] key,byte[] iv) throws Exception {
		assert this.cipherType != CipherType.DigitalSign;
		return this.cipherType.encrypt(source, this.cipherName, this.getKey(key, iv==null), iv);
	}
	
	/**
	 * 解密
	 * @param chiper 密文
	 * @param key 密钥 (非对称加密默认为私钥解密)
	 * @return
	 * @throws Exception
	 */
	public byte[] decrypt(byte[] chiper,byte[] key) throws Exception {
		assert this.cipherType != CipherType.DigitalSign;
		return this.cipherType.decrypt(chiper, this.cipherName, this.getKey(key,false),null);
	}
	
	/**
	 * 解密 (添加向量或公私钥标识或签名结果)
	 * @param chiper 密文
	 * @param key 密钥 (非对称加密默认为私钥解密)
	 * @param iv 向量 或签名结果 或在非对称加密中iv是公私钥填值Cipher.isPrivateKey为私钥,Cipher.isPublicKey为公钥
	 * @return
	 * @throws Exception
	 */
	public byte[] decrypt(byte[] chiper,byte[] key,byte[] iv) throws Exception {
		return this.cipherType.decrypt(chiper, this.cipherName, this.getKey(key, iv==null),iv);
	}
	
	
	
	
	
	
	
	
	/**
	 * 哈希散列加密
	 * @param source 原文
	 * @param confs 可选参数：
	 * @param ByteConverter (可选参数)byteConverter转换类型,默认使用16进制小写ByteConverter.Hex
	 * @return 转换类型定义的密文字符串
	 * @throws Exception
	 */
	public String encrypt(byte[] source,ByteConverter... confs) throws Exception {
		ByteConverter byteConverter = confs.length==0?ByteConverter.Hex:confs[0];
		byte[] enCode =  this.encrypt(source);
		return byteConverter.parse(enCode);
	}
	
	/**
	 * 哈希散列加密
	 * @param source 原文
	 * @param confs 可选参数：
	 * @param ByteConverter (可选参数)byteConverter转换类型,默认使用16进制小写ByteConverter.Hex
	 * @param Charset (可选参数)charset定义转换的字符集解码原文,默认使用CharsetUtil.CHARSET_UTF_8
	 * @return 转换类型定义的密文字符串
	 * @throws Exception
	 */
	public String encrypt(String source,Object... confs) throws Exception {
		ByteConverter byteConverter = ByteConverter.Hex;
		Charset charset = CharsetUtil.CHARSET_UTF_8;
		for (Object conf : confs) {
			if (conf instanceof Charset confCharset) {
				charset = confCharset;
			} else if (conf instanceof ByteConverter confByteConverter) {
				byteConverter = confByteConverter;
			}
		}
		byte[] enCode =  this.encrypt(source.getBytes(charset));
		return byteConverter.parse(enCode);
	}
	
	/**
	 * 加密
	 * @param source 原文
	 * @param key 密钥(非对称加密默认为公钥)
	 * @param confs 可选参数：
	 * @param conf (byte[]的可选参数)在非对称加密中公私钥声明,Cipher.isPrivateKey为私钥,Cipher.isPublicKey为公钥,默认使用公钥加密
	 * @param String (或者类型为byte[]的视情况选填)iv向量在使用向量的加密中为必填,字符串类型由ByteConverter确定
	 * @param ByteConverter (可选参数)byteConverter转换类型,默认使用BASE64
	 * @return 转换类型定义的密文字符串
	 * @throws Exception
	 */
	public String encrypt(byte[] source,byte[] key,Object... confs) throws Exception {
		ByteConverter byteConverter = ByteConverter.BASE64;
		byte[] bytes = Cipher.isPublicKey;
		String ivString = null;
		for (Object conf : confs) {
			if (conf instanceof ByteConverter confByteConverter) {
				byteConverter = confByteConverter;
			} else if (conf instanceof String confString) {
				ivString = confString;
			}  else if (conf instanceof byte[] || Cipher.isPublicKey == conf) {
				if (this.cipherType == CipherType.Symmetric ) {
					if (!Cipher.isPrivateKey.equals(conf) && Cipher.isPublicKey != conf) {//对称加密不需要公私钥匙
						bytes = (byte[])conf;
					}
				} else {
					if (Cipher.isPublicKey == conf) {
						bytes = Cipher.isPublicKey;
					} else {
						bytes = Cipher.isPrivateKey;
					}
					
				}
			}
		}
		
		if (null != ivString) {
			bytes = byteConverter.escape(ivString);
		}
		byte[] enCode;
		if (this.cipherType==CipherType.Symmetric && Cipher.isPublicKey == bytes) {
			enCode = this.encrypt(source,key);
		} else {
			enCode = this.encrypt(source,key,bytes);
		}
		return byteConverter.parse(enCode);
	}
	
	/**
	 * 加密
	 * @param source 原文
	 * @param key 密钥(非对称加密默认为公钥)
	 * @param confs 可选参数：
	 * @param conf (byte[]的可选参数)在非对称加密中公私钥声明,Cipher.isPrivateKey为私钥,Cipher.isPublicKey为公钥,默认使用公钥加密
	 * @param String (或者类型为byte[]的视情况选填)iv向量在使用向量的加密中为必填,字符串类型由ByteConverter确定
	 * @param ByteConverter (可选参数)byteConverter转换类型,默认使用BASE64
	 * @param Charset (可选参数)charset定义转换的字符集解码原文,默认使用CharsetUtil.CHARSET_UTF_8
	 * @return 转换类型定义的密文字符串
	 * @throws Exception
	 */
	public String encrypt(String source,byte[] key,Object... confs) throws Exception {
		ByteConverter byteConverter = ByteConverter.BASE64;
		Charset charset = CharsetUtil.CHARSET_UTF_8;
		byte[] bytes = Cipher.isPublicKey;
		String ivString = null;
		for (Object conf : confs) {
			if (conf instanceof Charset confCharset) {
				charset = confCharset;
			} else if (conf instanceof ByteConverter confByteConverter) {
				byteConverter = confByteConverter;
			} else if (conf instanceof String confString) {
				ivString = confString;
			}  else if (conf instanceof byte[] || Cipher.isPublicKey == conf) {
				if (this.cipherType == CipherType.Symmetric ) {
					if (!Cipher.isPrivateKey.equals(conf) && Cipher.isPublicKey != conf) {//对称加密不需要公私钥匙
						bytes = (byte[])conf;
					}
				} else {
					if (Cipher.isPublicKey == conf) {
						bytes = Cipher.isPublicKey;
					} else {
						bytes = Cipher.isPrivateKey;
					}
					
				}
			}
		}
		
		if (null != ivString) {
			bytes = byteConverter.escape(ivString);
		}
		byte[] sourceBytes = source.getBytes(charset);
		byte[] enCode;
		if (this.cipherType==CipherType.Symmetric && Cipher.isPublicKey == bytes || this.cipherType==CipherType.DigitalSign) {
			enCode = this.encrypt(sourceBytes,key);
		} else {
			enCode = this.encrypt(sourceBytes,key,bytes);
		}
		return byteConverter.parse(enCode);
	}
	
	/**
	 * 加密
	 * @param source 原文
	 * @param key 密钥(非对称加密默认为公钥),字符串类型由ByteConverter确定
	 * @param confs 可选参数：
	 * @param conf (byte[]的可选参数) 在非对称加密中公私钥声明,Cipher.isPrivateKey为私钥,Cipher.isPublicKey为公钥,默认使用公钥加密
	 * @param String (或者类型为byte[]的视情况选填)iv向量在使用向量的加密中为必填,字符串类型由ByteConverter确定
	 * @param ByteConverter (可选参数)byteConverter转换类型,默认使用BASE64
	 * @param Charset (可选参数)charset定义转换的字符集解码原文,默认使用CharsetUtil.CHARSET_UTF_8
	 * @return 转换类型定义的密文字符串
	 * @throws Exception
	 */
	public String encrypt(String source,String key,Object... confs) throws Exception {
		ByteConverter byteConverter = ByteConverter.BASE64;
		Charset charset = CharsetUtil.CHARSET_UTF_8;
		byte[] bytes = Cipher.isPublicKey;
		String ivString = null;
		for (Object conf : confs) {
			if (conf instanceof Charset confCharset) {
				charset = confCharset;
			} else if (conf instanceof ByteConverter confByteConverter) {
				byteConverter = confByteConverter;
			} else if (conf instanceof String confString) {
				ivString = confString;
			} else if (conf instanceof byte[] || Cipher.isPublicKey == conf) {
				if (this.cipherType == CipherType.Symmetric ) {
					if (!Cipher.isPrivateKey.equals(conf) && Cipher.isPublicKey != conf) {//对称加密不需要公私钥匙
						bytes = (byte[])conf;
					}
				} else {
					if (Cipher.isPublicKey == conf) {
						bytes = Cipher.isPublicKey;
					} else {
						bytes = Cipher.isPrivateKey;
					}
					
				}
			}	
		}
		
		if (null != ivString) {
			bytes = byteConverter.escape(ivString);
		}
		byte[] keyBytes = byteConverter.escape(key);
		byte[] sourceBytes = source.getBytes(charset);
		byte[] enCode;
		if (this.cipherType==CipherType.Symmetric && Cipher.isPublicKey == bytes || this.cipherType==CipherType.DigitalSign) {
			enCode = this.encrypt(sourceBytes,keyBytes);
		} else {
			enCode = this.encrypt(sourceBytes,keyBytes,bytes);
		}
		return byteConverter.parse(enCode);
	}
	
	
	
	
	
	
	
	/**
	 * 解密
	 * @param chiper 密文
	 * @param key 密钥(非对称加密默认为私钥)
	 * @param confs 可选参数： 
	 * @param conf (byte[]的可选参数)iv向量在使用向量的加密中为必填 或者 在非对称加密中公私钥声明,Cipher.isPrivateKey为私钥,Cipher.isPublicKey为公钥,默认使用私钥解密 
	 * @param Charset (可选参数)charset定义转换的字符集生成原文,默认使用CharsetUtil.CHARSET_UTF_8
	 * @return 字符集的原文字符串
	 * @throws Exception
	 */
	public String decrypt(byte[] chiper,byte[] key,Object... confs) throws Exception {
		Charset charset = CharsetUtil.CHARSET_UTF_8;
		byte[] bytes = Cipher.isPrivateKey;//非对称加密默认为私钥
		for (Object conf : confs) {
			if (conf instanceof Charset confCharset) {
				charset = confCharset;
			} else if (conf instanceof byte[] || Cipher.isPublicKey == conf) {
				if (this.cipherType == CipherType.Symmetric ) {
					if (!Cipher.isPrivateKey.equals(conf) && Cipher.isPublicKey != conf) {//对称加密不需要公私钥匙
						bytes = (byte[])conf;
					}
				} else {
					if (Cipher.isPublicKey == conf) {
						bytes = Cipher.isPublicKey;
					} else {
						bytes = Cipher.isPrivateKey;
					}
					
				}
			}	
		}
		
		byte[] deCode;
		if (this.cipherType==CipherType.Symmetric && Cipher.isPrivateKey.equals(bytes)) {
			deCode = this.decrypt(chiper,key);
		} else {
			deCode = this.decrypt(chiper,key,bytes);
		}
		return new String(deCode,charset);
	}
	
	/**
	 * 解密
	 * @param chiper 密文
	 * @param key 密钥(非对称加密默认为私钥)
	 * @param confs 可选参数： 
	 * @param ByteConverter (可选参数)byteConverter转换类型,默认使用BASE64 (转换字符串类型的密文)
	 * @param conf (byte[]的可选参数)在非对称加密中公私钥声明,Cipher.isPrivateKey为私钥,Cipher.isPublicKey为公钥,默认使用私钥解密 
	 * @param String (或者类型为byte[]的视情况选填)iv向量在使用向量的加密中为必填,字符串类型由ByteConverter确定
	 * @param Charset (可选参数)charset定义转换的字符集生成原文,默认使用CharsetUtil.CHARSET_UTF_8
	 * @return 字符集的原文字符串
	 * @throws Exception
	 */
	public String decrypt(String chiper,byte[] key,Object... confs) throws Exception {
		ByteConverter byteConverter = ByteConverter.BASE64;
		Charset charset = CharsetUtil.CHARSET_UTF_8;
		byte[] bytes = Cipher.isPrivateKey;
		String ivString = null;
		for (Object conf : confs) {
			if (conf instanceof Charset confCharset) {
				charset = confCharset;
			} else if (conf instanceof ByteConverter confByteConverter) {
				byteConverter = confByteConverter;
			} else if (conf instanceof String confString) {
				ivString = confString;
			} else if (conf instanceof byte[] || Cipher.isPublicKey == conf) {
				if (this.cipherType == CipherType.Symmetric || this.cipherType == CipherType.DigitalSign) {
					if (!Cipher.isPrivateKey.equals(conf) && Cipher.isPublicKey != conf) {//对称加密不需要公私钥匙
						bytes = (byte[])conf;
					}
				} else {
					if (Cipher.isPublicKey == conf) {
						bytes = Cipher.isPublicKey;
					} else {
						bytes = Cipher.isPrivateKey;
					}
					
				}
			}	
		}
		
		if (null != ivString) {
			bytes = byteConverter.escape(ivString);
		}
		byte[] chiperBytes = this.cipherType==CipherType.DigitalSign?chiper.getBytes(charset):byteConverter.escape(chiper);
		byte[] deCode;
		if (this.cipherType==CipherType.Symmetric && Cipher.isPrivateKey.equals(bytes)) {
			deCode = this.decrypt(chiperBytes,key);
		} else {
			deCode = this.decrypt(chiperBytes,key,bytes);
		}
		if (null == deCode) {
			return null;
		}
		return new String(deCode,charset);
	}
	
	
	/**
	 * 解密
	 * @param chiper 密文
	 * @param key 密钥(非对称加密默认为私钥),字符串类型由ByteConverter确定
	 * @param confs 可选参数： 
	 * @param conf (byte[]的可选参数) 在非对称加密中公私钥声明,Cipher.isPrivateKey为私钥,Cipher.isPublicKey为公钥,默认使用私钥解密
	 * @param String (或者类型为byte[]的视情况选填)iv向量在使用向量的加密中为必填,字符串类型由ByteConverter确定
	 * @param ByteConverter (可选参数)byteConverter转换类型,默认使用BASE64 (转换字符串类型的密文密钥向量)
	 * @param Charset (可选参数)charset定义转换的字符集生成原文,默认使用CharsetUtil.CHARSET_UTF_8
	 * @return 字符集的原文字符串
	 * @throws Exception
	 */
	public String decrypt(String chiper,String key,Object... confs) throws Exception {
		ByteConverter byteConverter = ByteConverter.BASE64;
		Charset charset = CharsetUtil.CHARSET_UTF_8;
		byte[] bytes = Cipher.isPrivateKey;
		String ivString = null;
		for (Object conf : confs) {
			if (conf instanceof Charset confCharset) {
				charset = confCharset;
			} else if (conf instanceof ByteConverter confByteConverter) {
				byteConverter = confByteConverter;
			} else if (conf instanceof String confString) {
				ivString = confString;
			} else if (conf instanceof byte[] || Cipher.isPublicKey == conf) {
				if (this.cipherType == CipherType.Symmetric || this.cipherType == CipherType.DigitalSign) {
					if (!Cipher.isPrivateKey.equals(conf) && Cipher.isPublicKey != conf) {//对称加密不需要公私钥匙,数字签名解密只能使用公钥
						bytes = (byte[])conf;
					}
				} else {
					if (Cipher.isPublicKey == conf) {
						bytes = Cipher.isPublicKey;
					} else {
						bytes = Cipher.isPrivateKey;
					}
					
				}
			}	
		}
		
		if (null != ivString) {
			bytes = byteConverter.escape(ivString);
		}
		byte[] keyBytes = byteConverter.escape(key);
		byte[] chiperBytes = this.cipherType==CipherType.DigitalSign?chiper.getBytes(charset):byteConverter.escape(chiper);
		byte[] deCode;
		if (this.cipherType==CipherType.Symmetric && Cipher.isPrivateKey.equals(bytes)) {
			deCode = this.decrypt(chiperBytes,keyBytes);
		} else {
			deCode = this.decrypt(chiperBytes,keyBytes,bytes);
		}
		if (null == deCode) {
			return null;
		}
		return new String(deCode,charset);
	}
	
	
	
	
	
	
	
	
	
}
