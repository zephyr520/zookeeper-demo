package com.example.demo;

import java.util.ArrayList;
import java.util.List;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

public class BloomFilterTest1 {
	
	private static int size = 1000000;
	
	private static BloomFilter<Integer> bloomFilter = null;

	public static void main(String[] args) {
//		mightContain();
		customeErrorRatio();
	}
	
	public static void mightContain() {
		bloomFilter = BloomFilter.create(Funnels.integerFunnel(), size);
		for (int i=0; i<size; i++) {
			bloomFilter.put(i);
		}
		long startTime = System.nanoTime();
		if (bloomFilter.mightContain(299999)) {
			System.out.println("命中了...");
		}
		long endTime = System.nanoTime();
		System.out.println("耗时：" + (endTime - startTime) + "ns");
	}
	
	public static void customeErrorRatio() {
		bloomFilter = BloomFilter.create(Funnels.integerFunnel(), size, 0.01);
		for (int i=0; i<size; i++) {
			bloomFilter.put(i);
		}
		List<Integer> list = new ArrayList<Integer>(10000);
		// 故意取10000个不在过滤器里的值，看看有多少个会被认为在过滤器里
		for (int i = size + 10000; i < size + 20000; i++) {
            if (bloomFilter.mightContain(i)) {
                list.add(i);
            }
        }
		System.out.println("误判的数量：" + list.size());
	}
}
