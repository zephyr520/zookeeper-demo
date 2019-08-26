package com.example.demo;

public class IntegerTest {

	public static void main(String[] args) {
		Integer i1 = 127;
		Integer i2 = 128;
		Integer i3 = Integer.valueOf(127);
		Integer i4 = Integer.valueOf(128);
		System.out.println(i1==i3);
		System.out.println(i2==i4);
		System.out.println(i2.equals(i4));
		System.out.println(i2.compareTo(i4));
	}

}
