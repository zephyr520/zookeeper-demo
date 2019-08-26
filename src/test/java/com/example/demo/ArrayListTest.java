package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ArrayListTest {

	public static void main(String[] args) {
		List<String> list = new ArrayList<>();
		list.add("111");
		list.add("222");
		list.add("333");
		list.add(2, "000");
		list.remove(3);
		System.out.println(list);
		
		CopyOnWriteArrayList<Integer> copyOnWriteList = new CopyOnWriteArrayList<>();
		copyOnWriteList.add(1);
		copyOnWriteList.add(2);
	}
}
