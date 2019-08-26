package com.example.demo.bloomfilter;

import com.google.common.base.Charsets;
import com.google.common.hash.Funnel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

	private RedisTemplate<String, Object> redisTemplate;

	private BloomFilterHelper<Model> modelBloomFilterHelper = new BloomFilterHelper<Model>((Funnel<Model>) (from,
			into) -> into.putString(from.getKey(), Charsets.UTF_8).putString(from.getValue(), Charsets.UTF_8), 100,
			0.01);
	
	public void insertByBloomFilter(String key, String value) {
		addByBloomFilter(modelBloomFilterHelper, key, new Model(key, value));
	}

	/***
	 * @description 根据给定的布隆过滤器添加值
	 * @param <T>
	 * @param bloomFilterHelper
	 * @param key
	 * @param value
	 */
	public <T> void addByBloomFilter(BloomFilterHelper<T> bloomFilterHelper, String key, T value) {
		int[] offset = bloomFilterHelper.murmurHashOffset(value);
		for (int i : offset) {
			redisTemplate.opsForValue().setBit(key, i, true);
		}
	}

	/**
	 * @description 根据给定的布隆过滤器判断值是否存在
	 * @param <T>
	 * @param bloomFilterHelper
	 * @param key
	 * @param value
	 * @return
	 */
	public <T> boolean includeByBloomFilter(BloomFilterHelper<T> bloomFilterHelper, String key, T value) {
		int[] offset = bloomFilterHelper.murmurHashOffset(value);
		for (int i : offset) {
			if (!redisTemplate.opsForValue().getBit(key, i)) {
				return false;
			}
		}
		return true;
	}
}
