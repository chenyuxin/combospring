package com.github.chenyuxin.commonframework.base.type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.chenyuxin.commonframework.base.constant.RegexConst;
import com.github.chenyuxin.commonframework.base.constant.StringPool;

/**
 * 隐私类型 用于内容脱敏转换
 * 枚举类型 1中文姓名 2身份证 3固定电话 4手机号码 5地址 6电子邮箱 7银行卡号 8 IPv4
 */
public enum PrivacyType {

	/**
	 * 无脱敏
	 */
	None {
		@Override
		public String transform(String str) {
			return str;
		}

		@Override
		public String getDisplayName() {
			return StringPool.BLANK;
		}

		@Override
		public String getRegex() {
			return StringPool.BLANK;
		}

	},
	/**
	 * 脱敏中文姓名
	 */
	ChineseName {
		@Override
		public String transform(String name) {
			if (null == name || StringPool.BLANK.equals(name)) {
				return name;
			}
			StringBuffer sBuffer = new StringBuffer(name.substring(0, 1));
			for (int i=1;i<name.length();i++){
				sBuffer.append('*');
			}
			return sBuffer.toString();
		}

		@Override
		public String getDisplayName() {
			return "中文姓名";
		}

		@Override
		public String getRegex() {
			return RegexConst.CHINESE;
		}

	},
	/**
	 * 脱敏身份证号
	 */
	IdCard {
		@Override
		public String transform(String idCard) {
			if (null == idCard || StringPool.BLANK.equals(idCard)) {
				return idCard;
			}
			return idCard.substring(0, 1).concat("*************").concat(idCard.substring(14));
		}

		@Override
		public String getDisplayName() {
			return "身份证";
		}

		@Override
		public String getRegex() {
			return RegexConst.IDCARD;
		}

	},
	/**
	 * 脱敏固定电话
	 */
	TelNum {
		@Override
		public String transform(String telNum) {
			if (null == telNum || StringPool.BLANK.equals(telNum)) {
				return telNum;
			}
			return "*******".concat(telNum.substring(telNum.length()-4));
		}

		@Override
		public String getDisplayName() {
			return "固定电话";
		}

		@Override
		public String getRegex() {
			return RegexConst.TEL;
		}

	},
	/**
	 * 脱敏手机号码
	 */
	PhoneNum {
		@Override
		public String transform(String phoneNum) {
			if (null == phoneNum || StringPool.BLANK.equals(phoneNum)) {
				return phoneNum;
			}
			return phoneNum.substring(0,1).concat("********").concat(phoneNum.substring(phoneNum.length()-2));
		}

		@Override
		public String getDisplayName() {
			return "手机号码";
		}

		@Override
		public String getRegex() {
			return RegexConst.PHONE;
		}

	},
	/**
	 * 脱敏地址
	 */
	Address {
		@Override
		public String transform(String address) {
			if (null == address || StringPool.BLANK.equals(address)) {
				return address;
			}
			StringBuffer sBuffer = new StringBuffer(address.substring(0, 6));
			for (int i=0;i<address.length()-6;i++){
				sBuffer.append('*');
			}
			return sBuffer.toString();
		}

		@Override
		public String getDisplayName() {
			return "地址";
		}

		@Override
		public String getRegex() {
			return RegexConst.ADDRESS;
		}

	},
	/**
	 * 脱敏电子邮箱
	 */
	Email {
		@Override
		public String transform(String email) {
			if (null == email || StringPool.BLANK.equals(email)) {
				return email;
			}
			StringBuffer sBuffer = new StringBuffer(email.substring(0, 1));
			for (int i=0;i<email.indexOf('@')-1;i++){
				sBuffer.append('*');
			}
			sBuffer.append(email.substring(email.indexOf('@')));
			return sBuffer.toString();
		}

		@Override
		public String getDisplayName() {
			return "电子邮箱";
		}

		@Override
		public String getRegex() {
			return RegexConst.EMAIL;
		}

	},
	/**
	 * 脱敏银行卡号
	 */
	BankCardNum {
		@Override
		public String transform(String bankCardNum) {
			if (null == bankCardNum || StringPool.BLANK.equals(bankCardNum)) {
				return bankCardNum;
			}
			StringBuffer sBuffer = new StringBuffer(bankCardNum.substring(0, 6));
			for (int i=0;i<bankCardNum.length()-10;i++){
				sBuffer.append('*');
			}
			sBuffer.append(bankCardNum.substring(bankCardNum.length()-4));
			return sBuffer.toString();
		}

		@Override
		public String getDisplayName() {
			return "银行卡号";
		}

		@Override
		public String getRegex() {
			return RegexConst.BANKCARDNUM;
		}

	},
	
	IPv4 {
		@Override
		public String transform(String IPv4) {
			if (null == IPv4 || StringPool.BLANK.equals(IPv4)) {
				return IPv4;
			}
			String[] ipcs = IPv4.split("\\.");
			StringBuilder stringBuilder = new StringBuilder(ipcs[0]);
			stringBuilder.append(".***.***.");
			stringBuilder.append(ipcs[3]);
			return stringBuilder.toString();
		}

		@Override
		public String getDisplayName() {
			return "IPv4地址";
		}

		@Override
		public String getRegex() {
			return RegexConst.IPV4;
		}

	}
	
	;

	
	/**
	 * 返回对应顺序的隐私类型，
	 * 顺序可以由this.ordinal()获取
	 * @param order
	 * @return
	 */
	public static PrivacyType getPrivacyType(int order) {
		return PrivacyType.values()[order];
	}
	
	/**
	 * 脱敏转换
	 * @param str
	 * @return
	 */
	public abstract String transform(String str);
	
	/**
	 * 获取类型脱敏中文说明
	 * @return
	 */
	public abstract String getDisplayName();
	
	/**
	 * 获取验证的正则<br>
	 * 某些正则并非强验证，请测试
	 * @return
	 */
	public abstract String getRegex();
	
	/**
	 * 获取所有隐私类型 的List
	 */
	private static final List<Map<String, Object>> privacyTypeItems = new ArrayList<Map<String, Object>>(PrivacyType.values().length);
	/**
	 * 获取所有隐私类型 的List
	 */
	public static List<Map<String,Object>> getItems(){
		if ( privacyTypeItems.isEmpty() ){
			synchronized (privacyTypeItems) {
				if ( privacyTypeItems.isEmpty() ) {
					for(PrivacyType privacyType : PrivacyType.values()) {
						Map<String,Object> map = new HashMap<String, Object>();
						map.put("name", privacyType.getDisplayName());
						map.put("value", privacyType.ordinal());
						privacyTypeItems.add(map);
					}
				}
			}
		}
		return privacyTypeItems;
	}	
}
