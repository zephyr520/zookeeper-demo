package com.example.demo.bloomfilter;

import com.google.common.base.Preconditions;
import com.google.common.hash.Funnel;
import com.google.common.hash.Hashing;

public class BloomFilterHelper<T> {

	private int numHashFunctions;
	
	private int bitSize;
	
	private Funnel<T> funnel;
	
	public BloomFilterHelper(Funnel<T> funnel, int expectedInsertions, double fpp) {
		Preconditions.checkArgument(funnel != null, "funnel不能为空");
		this.funnel = funnel;
		this.bitSize = optimalNumOfBits(expectedInsertions, fpp);
		this.numHashFunctions = optimalNumOfHashFunctions(expectedInsertions, this.bitSize);
	}
	
	int[] murmurHashOffset(T value) {
		int[] offset = new int[numHashFunctions];

        long hash64 = Hashing.murmur3_128().hashObject(value, funnel).asLong();
        int hash1 = (int) hash64;
        int hash2 = (int) (hash64 >>> 32);
        for (int i = 1; i <= numHashFunctions; i++) {
            int nextHash = hash1 + i * hash2;
            if (nextHash < 0) {
                nextHash = ~nextHash;
            }
            offset[i - 1] = nextHash % bitSize;
        }
		return offset;
	}
	
	/**
	 * @description 计算bit数组的长度
	 * @param n 期望放入的元素最大个数
	 * @param p 容错率
	 * @return
	 */
	private int optimalNumOfBits(long n, double p) {
		if (p == 0) {
            p = Double.MIN_VALUE;
        }
        return (int) (-1 * n * Math.log(p) / (Math.log(2) * Math.log(2)));
	}
	
	/**
	 * @description 计算hash方法执行次数
	 * @param n 期望放入的元素最大个数
	 * @param m bit数组的长度
	 * @return
	 */
	private int optimalNumOfHashFunctions(long n, long m) {
		return Math.max(1, (int) Math.round((double) m / n * Math.log(2)));
	}
}
