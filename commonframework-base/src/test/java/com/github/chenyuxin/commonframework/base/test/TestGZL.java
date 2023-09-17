package com.github.chenyuxin.commonframework.base.test;

import org.junit.jupiter.api.Test;

import com.github.chenyuxin.commonframework.base.util.BaseUtil;

public class TestGZL {
	
	@Test
	public void TestGZL1() {
//		String s = String.valueOf(System.currentTimeMillis());
//        String substring = s.substring(s.length()-8,s.length());
//        SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
//        String format = yyyyMMdd.format(new Date());
//        StringBuffer stringBuffer = new StringBuffer(format);
//        StringBuffer gzl = stringBuffer.append("GZL").append("280").append(substring);
//        return  gzl;
		
//		String woId = "7f7e3cfc-47e0-4360-b948-c505451d34f3";
		String woId = BaseUtil.getUUIDC("1@*(&kjGhdKJH");
		
		String woIdHashCode = String.valueOf(woId.hashCode());
		String woIdCode = woIdHashCode.substring(woIdHashCode.length()-8,woIdHashCode.length());
		
		StringBuilder sb = new StringBuilder("20230607");
		sb.append("GZL280");
		sb.append(woIdCode);
		
//		System.out.println(woIdCode); // 18808243
//		System.out.println(sb); // 20230607GZL28018808243
		
		System.out.println(woIdCode);
		System.out.println(sb);
		
	}
	
	@Test
	public void TestGZL2() {
		String woId = "7f7e3cfc-47e0-4360-b948-c505451d34f3";
		String dateString = "20230607"; 
		String id = setIdentifierNumber(dateString,woId);
		System.out.println(id);
	}
	
	/**
     * EIP通过日期和woId
     * 生成工单号:YYYYMMDD＋GZL＋3位省代码(280)＋8位流水号
     * @param yyyyMMdd
     * @param woId
     * @return
     */
	String setIdentifierNumber(String yyyyMMdd,String woId) {
    	String woIdHashCode = String.valueOf(woId.hashCode());
		String woIdCode = woIdHashCode.substring(woIdHashCode.length()-8,woIdHashCode.length());
		StringBuilder sb = new StringBuilder(yyyyMMdd);
		sb.append("GZL280");
		sb.append(woIdCode);
		return sb.toString();
    }

}
