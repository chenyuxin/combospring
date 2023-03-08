package com.github.chenyuxin.combo.util.transform;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.chenyuxin.combo.base.type.other.PrivacyType;

/**
 * 隐私数据脱敏转换后返回
 */
public class TransformPrivacy {

	/**
	 * 脱敏转换
	 * @param str 源字符串
	 * @param privacyType 隐私类型  1中文姓名 2身份证 3固定电话 4手机号码 5地址 6电子邮箱 7银行卡号 8地址IP
	 * @return 脱敏后内容
	 */
	public static String transform(String str, PrivacyType privacyType){
		return privacyType.transform(str);
	}

	/**
	 * 脱敏转换
	 * @param str 源字符串
	 * @param privacyType 隐私类型  1中文姓名 2身份证 3固定电话 4手机号码 5地址 6电子邮箱 7银行卡号 8地址IP
	 * @return 脱敏后内容
	 */
	public static String transform(String str, int privacyType) {
		return PrivacyType.getPrivacyType(privacyType).transform(str);
	}
	
	/**
     * 字符串包含匹配脱敏类型后,脱敏转换
     * @param str
     * @param regex 正则规则匹配 （匹配需要包含的脱敏内容CommonUtilValidation，PrivacyType可以获取）
     * @param privacyType 隐私类型（脱敏类型）
     * @return
     */
    public static String constantTransform(String str,PrivacyType privacyType) {
    	Pattern pattern = Pattern.compile(privacyType.getRegex(),Pattern.CASE_INSENSITIVE);
        StringBuffer sb = new StringBuffer();
        Matcher m = pattern.matcher(str);
        while (m.find()) {
        	String findStr= str.substring(m.start(),m.end());
            m.appendReplacement(sb, privacyType.transform(findStr));
        }
        m.appendTail(sb);
        return sb.toString();
    }

	/*
		String aString = transformName("李某某啊");
		System.out.println(aString);
		String idCard = transformIdCard("511102198804112099");
		System.out.println(idCard);
		String tel = transformTel("0283-2601166");
		System.out.println(tel);
		String phone = transformPhone("13808139635");
		System.out.println(phone);
		String adr = transformAdr("四川省成都市青羊区汪家拐234号");
		System.out.println(adr);
		String email = transformEmail("Wjhk@163.com");
		System.out.println(email);
		String bankCard = transformBankCard("123456888888881234");
		System.out.println(bankCard);
	*/	

}
