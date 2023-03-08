package com.github.chenyuxin.combo.base.type.other;

import java.math.BigDecimal;
import java.util.Base64;
import java.util.Date;

import com.github.chenyuxin.combo.base.constant.StringPool;
import com.github.chenyuxin.combo.base.type.format.DateType;
import com.github.chenyuxin.combo.base.util.BaseUtil;

/**
 * 字段类型用于同数据库交互
 *
 */
@SuppressWarnings("rawtypes")
public enum FieldType implements Parse {
	
	/**
	 * java程序的类型
	 * String
	 */
	STRING {

		@Override
		public String parse(Object value) {
			if (value == null) {
				//value = "-";//替换null值
				return null;
			}
			return String.valueOf(value);
		}
		
		@Override
		public Class getJavaClass() {
			return String.class;
		}

	},
	
	/**
	 * java程序的类型
	 * Inter,int
	 */
	INT {

		@Override
		public Integer parse(Object value) {
			if ( null == value || StringPool.BLANK.equals(value) ) {
				//value = -1;//替换null值
				return null;
			} else {
				if (value instanceof Integer) {
					return (Integer) value;
				} else {
					return Integer.parseInt(String.valueOf(value));
				}
			}
		}
		
		@Override
		public Class getJavaClass() {
			return Integer.class;
		}

	},
	
	/**
	 * java程序的类型
	 * Float,float
	 */
	FLOAT {

		@Override
		public Float parse(Object value) {
			if ( null == value || StringPool.BLANK.equals(value) ) {
				//value = 0.0;
				return null;
			} else {
				if (value instanceof Float) {
					return (Float) value;
				} else {
					return Float.parseFloat(String.valueOf(value));
				}
			}
		}
		
		@Override
		public Class getJavaClass() {
			return Float.class;
		}
		
	},
	
	/**
	 * java程序的类型
	 * Date
	 */
	DATE {

		@Override
		public Date parse(Object value) {
			if ( null == value || StringPool.BLANK.equals(value) ) {
				//value = DateType.getInstance().getParseValue("1900-01-01");//替换null值
				return null;
			} else {
				if (value instanceof Date) {
					return (Date) value;
				} else {
					return DateType.getInstance().getParseValue(String.valueOf(value));
				}
			}
		}
		
		@Override
		public Class getJavaClass() {
			return Date.class;
		}
		
	},
	
	/**
	 * java程序的类型
	 * Long，long
	 */
	LONG {

		@Override
		public Long parse(Object value) {
			if ( null == value || StringPool.BLANK.equals(value) ) {
				//value = -1L;
				return null;
			} else {
				if (value instanceof Long) {
					return (Long) value;
				} else {
					return Long.parseLong(String.valueOf(value));
				}
			}
		}
		
		@Override
		public Class getJavaClass() {
			return Long.class;
		}
	},
	
	/**
	 * java程序的类型
	 * Double,double
	 */
	DOUBLE {

		@Override
		public Double parse(Object value) {
			if ( null == value || StringPool.BLANK.equals(value) ) {
				//value = 0.0;
				return null;
			} else {
				if (value instanceof Double) {
					return (Double) value;
				} else {
					return Double.parseDouble(String.valueOf(value));
				}
			}
		}
		
		@Override
		public Class getJavaClass() {
			return Double.class;
		}
	},
	
	/**
	 * java程序的类型
	 * String
	 * 
	 * 对应数据库nvarchar2
	 */
	STRINGN {
		
		@Override
		public String parse(Object value) {
			if (value == null) {
				//value = "-";//替换null值
				return null;
			}
			return String.valueOf(value);
		}

		@Override
		public Class getJavaClass() {
			return null;
		}
		
	},
	
	/**
	 * java程序的类型
	 * String
	 * 
	 * 对应数据库clob
	 */
	STRINGC {
		
		@Override
		public String parse(Object value) {
			if (value == null) {
				//value = "-";//替换null值
				return null;
			}
			return String.valueOf(value);
		}

		@Override
		public Class getJavaClass() {
			return null;
		}
		
	},
	
	/**
	 * java程序的类型
	 * byte[]
	 * 
	 * 对应数据库blob
	 */
	BYTE {

		@Override
		public byte[] parse(Object value) {
			if ( null == value || StringPool.BLANK.equals(value) ) {
				//value = [];
				return null;
			} else {
				if (value instanceof byte[]) {
					return (byte[]) value;
				} else if (value instanceof String) {
					String str = String.valueOf(value);
					if (BaseUtil.isBase64(str)) {
						//传输过来的byte[]转json时默认转为Base64的字符串,
						
						return Base64.getDecoder().decode(str);
					} else {
						return str.getBytes();
					}
				} 
			}
			return null;
		}
		
		@Override
		public Class getJavaClass() {
			return byte[].class;
		}
	},
	
	BIGDECIMAL {

		@Override
		public BigDecimal parse(Object value) {
			if ( null == value || StringPool.BLANK.equals(value) ) {
				//value = 0;
				return null;
			} else {
				if (value instanceof BigDecimal) {
					return (BigDecimal) value;
				} else {
					return new BigDecimal(String.valueOf(value));
				}
			}
		}
		
		@Override
		public Class getJavaClass() {
			return BigDecimal.class;
		}
	}
	
	;
	
	
	/**
	 * 设置数据库字段类型， 数据库类型 枚举类初始化后调用方法， 并赋值 dbColType
	protected abstract void setFieldTypes(DataBaseTypeTemp dataBaseType);
	 */
	
	/**
	 * 获取
	 * 对应代表的Java类型
	 */
	public abstract Class getJavaClass();
	

	/**
	 * 字段类型编号
	 * 
	 * 字段类型编号会存入数据库中保存，请勿调换枚举顺序
	 * @return
	 */
	public int getModelColType(){
		return ordinal();
	}

	/**
	 * 返回对应顺序的字段类型，
	 * 顺序可以由this.ordinal()获取
	 * @param order
	 * @return
	 */
	public static FieldType getFieldType(int order) {
		return FieldType.values()[order];
	}
	
	/**
	 * 如果是基本类型，返回字段类型
	 * @param clazz
	 * @return
	 */
	public static FieldType getFieldType(Class<?> clazz) {
		for(FieldType fieldType : FieldType.values()) {
			if( fieldType.getJavaClass() == clazz ) {
				return fieldType;
			}
		}
		return null;
	}
	

	
	
	
	

}
