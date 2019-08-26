package com.example.demo;


public class StringTest {

	public static void main(String[] args) {
		String str1 = "123";
		String str2 = "123";
		char[] c = {'1','2','3'};
		String str3 = String.valueOf(c);
		System.out.println(str1.toString());
		System.out.println(str2.toString());
		System.out.println(str3.toString());
		System.out.println(str1 == str2);
		System.out.println(str1 == str3);
	}
}
