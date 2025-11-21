package com;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import com.github.chenyuxin.commonframework.util.CommonUtilUUID;

public class TestUUID {

	@Test
	public void testUUID1() {
		String uuid64 = CommonUtilUUID.getUUID64();
		System.out.println(uuid64);
		
	}
	
	@Test
	public void testUUID2() {
		String uuid = UUID.randomUUID().toString();
		System.out.println(uuid);
	}
}
