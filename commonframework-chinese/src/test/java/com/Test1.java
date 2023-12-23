package com;

import org.junit.jupiter.api.Test;

public class Test1 {
	
	@Test
	public void test1() {
//		char doc = '·';
		byte[] a = "·".getBytes();
		System.out.println(a[0]);
		
		System.out.println('·' > 128);
//		System.out.println('·' == 183);
		System.out.println((char)(129 + '0'));
		for(int i = -1000;i<1000;i++) {
			System.out.print(i+": ");
			System.out.println((char)(i));
		}
		
	}
	
	@Test
	public void test2() {
		char doc = '中';
		System.out.println(doc+0);
		for(int i = 20000;i<21000;i++) {
			System.out.println((char)i);
		}

	}

}
