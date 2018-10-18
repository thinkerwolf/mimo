package com.thinkerwolf.mimo.util;

public class ObjectUtil {
	
	public static <T> T isNotNull(T obj, String text) {
		if (obj == null) {
			throw new IllegalArgumentException(text);
		}
		return obj;
	}
	
}
