package com.example.demo.bloomfilter;

import java.io.Serializable;

public class Model implements Serializable {
	
	private static final long serialVersionUID = -3546950587258964553L;
	
	private String key;
	private String value;

	public Model() {
		super();
	}

	public Model(String key, String value) {
		super();
		this.key = key;
		this.value = value;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
