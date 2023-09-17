package com.github.chenyuxin.commonframework.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;

import com.github.chenyuxin.commonframework.base.constant.RegexConst;
import com.github.chenyuxin.commonframework.base.constant.StringPool;
import com.github.chenyuxin.commonframework.base.util.BaseUtil;

/**
 * 验证工具
 * @author chenyuxin
 */
public class CommonUtilValidation {
	
	/**
	 * 判断是否为纯数字字母
	 */
	public static boolean isLetterDigit(String str) {
		return str.matches(RegexConst.NUM_WORD);
	}
	
	/**
     * 判断字符串是否为数字
     * @param strNum
     * @return
     */
	public static boolean isStrNum(String strNum) {
        return strNum.matches(RegexConst.NUMBERS);
    }
    
    /**
     * 判断字符串是否为正确的日期格式
     * @param strDate
     * @return
     */
    public static boolean isStrDate(String strDate) {
    	return strDate.matches(RegexConst.DATE);
    }
    
    /**
     * 校验邮箱格式
     *
     * @param strEmail
     * @return
     */
    public static boolean isEmail(String strEmail) {
        return strEmail.matches(RegexConst.EMAIL);
    }
    
    /**
     * 检验qq号码
     *
     * @param strQQ
     * @return
     */
    public static boolean isQQ(String strQQ) {
        return strQQ.matches(RegexConst.QQ);
    }
    
    /**
     * 校验类型：0校验手机号码，1校验座机号码，2两者都校验满足其一就可
     *
     * @param checkType 0校验手机号码，1校验座机号码，2两者都校验满足其一就可
     * @param strPhoneNum 
     * @return
     */
    public static boolean isPhoneNum(int checkType, String strPhoneNum) {
    	if (0 == checkType) {
    		return strPhoneNum.matches(RegexConst.PHONE);
    	} else if (1 == checkType) {
    		return strPhoneNum.matches(RegexConst.TEL);
    	} else if (2 == checkType) {
    		return strPhoneNum.matches(RegexConst.PHONE) || strPhoneNum.matches(RegexConst.TEL);
    	}
    	return false;
    }
    
    
    /**
     *身份证验证
     * @param idStr
     * @return
     */
    public static String IdentityCardVerification(String idStr){
        String[] wf = { "1", "0", "X", "9", "8", "7", "6", "5", "4", "3", "2" };
        String[] checkCode = { "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7", "9", "10", "5", "8", "4", "2" };
        String iDCardNo = StringPool.BLANK;
        try {
            //判断号码的长度 15位或18位
            if (idStr.length() != 15 && idStr.length() != 18) {
                return "身份证号码长度应该为15位或18位";
            }
            if (idStr.length() == 18) {
                iDCardNo = idStr.substring(0, 17);
            } else if (idStr.length() == 15) {
                iDCardNo = idStr.substring(0, 6) + "19" + idStr.substring(6, 15);
            }
            if (isStrNum(iDCardNo) == false) {
                return "身份证15位号码都应为数字;18位号码除最后一位外,都应为数字";
            }
            //判断出生年月
            String strYear = iDCardNo.substring(6, 10);// 年份
            String strMonth = iDCardNo.substring(10, 12);// 月份
            String strDay = iDCardNo.substring(12, 14);// 月份
            if (isStrDate(strYear + "-" + strMonth + "-" + strDay) == false) {
                return "身份证生日无效";
            }
            GregorianCalendar gc = new GregorianCalendar();
            SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
            if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150 || (gc.getTime().getTime() - s.parse(strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
                return "身份证生日不在有效范围";
            }
            if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
                return "身份证月份无效";
            }
            if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
                return "身份证日期无效";
            }
            //判断地区码
			Hashtable<String,String> h = GetAreaCode();
            if (h.get(iDCardNo.substring(0, 2)) == null) {
                return "身份证地区编码错误";
            }
            //判断最后一位
            int theLastOne = 0;
            for (int i = 0; i < 17; i++) {
                theLastOne = theLastOne + Integer.parseInt(String.valueOf(iDCardNo.charAt(i))) * Integer.parseInt(checkCode[i]);
            }
            int modValue = theLastOne % 11;
            String strVerifyCode = wf[modValue];
            iDCardNo = iDCardNo + strVerifyCode;
            if (idStr.length() == 18 &&!iDCardNo.equals(idStr)) {
                return "身份证无效，不是合法的身份证号码";
            }
        }catch (Exception e){
            e.printStackTrace();
            return "身份证验证时出错";
        }
        return StringPool.BLANK;
    }
    
    private final static Hashtable<String,String> hashtable = new Hashtable<String,String>(35);
    
    /**
     * 身份证上地区代码
     * @return Hashtable
     */
    private static Hashtable<String,String> GetAreaCode() {
        if(hashtable.isEmpty()) {
	        hashtable.put("11", "北京");
	        hashtable.put("12", "天津");
	        hashtable.put("13", "河北");
	        hashtable.put("14", "山西");
	        hashtable.put("15", "内蒙古");
	        hashtable.put("21", "辽宁");
	        hashtable.put("22", "吉林");
	        hashtable.put("23", "黑龙江");
	        hashtable.put("31", "上海");
	        hashtable.put("32", "江苏");
	        hashtable.put("33", "浙江");
	        hashtable.put("34", "安徽");
	        hashtable.put("35", "福建");
	        hashtable.put("36", "江西");
	        hashtable.put("37", "山东");
	        hashtable.put("41", "河南");
	        hashtable.put("42", "湖北");
	        hashtable.put("43", "湖南");
	        hashtable.put("44", "广东");
	        hashtable.put("45", "广西");
	        hashtable.put("46", "海南");
	        hashtable.put("50", "重庆");
	        hashtable.put("51", "四川");
	        hashtable.put("52", "贵州");
	        hashtable.put("53", "云南");
	        hashtable.put("54", "西藏");
	        hashtable.put("61", "陕西");
	        hashtable.put("62", "甘肃");
	        hashtable.put("63", "青海");
	        hashtable.put("64", "宁夏");
	        hashtable.put("65", "新疆");
	        hashtable.put("71", "台湾");
	        hashtable.put("81", "香港");
	        hashtable.put("82", "澳门");
	        hashtable.put("91", "国外");
        }
        return hashtable;
    }
    
    /**
	 * 是否为Base64字符串
	 * @param str
	 * @return
	 */
	public static boolean isBase64(String str) {
		return BaseUtil.isBase64(str);
	}
	
	/**
	 * 是否为ip地址
	 * @param strIp
	 * @return
	 */
	public static boolean isIP(String strIp) {
		return strIp.matches(RegexConst.IPV4);
	}
	/**
	 * 是否包含ip地址
	 * @param str
	 * @return
	 */
	public static boolean constantIp(String str) {
		String regex = ".*?".concat(RegexConst.IPV4).concat(".*?");
		return str.matches(regex);
	}
	
	
	/**
	 * 是否为32位uuid
	 */
	public static boolean isUUID32(String uuidString) {
		return uuidString.matches(RegexConst.UUID32);
	}
	
}
