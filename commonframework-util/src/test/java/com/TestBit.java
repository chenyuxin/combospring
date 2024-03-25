package com;

import org.junit.jupiter.api.Test;

public class TestBit {
	
	@Test
	public void testBit() {
		int switchValue = 500;
		int a =  1<<3 ;
		//System.out.println(~a);
		
		//System.out.println(switchValue|a);
		System.out.println(switchValue&a);
		//System.out.println(switchValue^a);
	}
	
	@Test
	public void testBit2() {
		System.out.println(enable(500,3));
		System.out.println(enable(511,3));
		System.out.println(enable(0,3));
	}
	
	public static boolean enable(int switchValue,int digit) {
		assert digit>0 && digit<32;
		return (switchValue&1<<digit) == 0;
	}

}
