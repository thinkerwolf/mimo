package com.thinkerwolf.mimo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.channels.SelectionKey;
import java.util.Set;

public class PrintUtil {

	private static final Logger logger = LoggerFactory.getLogger("Print");

	public static void printSelectedKeys(Set<SelectionKey> set) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (SelectionKey sk : set) {
			sb.append("{");
			if (sk.isConnectable()) {
				sb.append("CONNECT ");
			}
			if (sk.isAcceptable()) {
				sb.append("ACCEPT ");
			}
			if (sk.isReadable()) {
				sb.append("READ ");
			}
			if (sk.isWritable()) {
				sb.append("WRITE ");
			}
			sb.append("}");
			sb.append(",");
		}
		int index = sb.lastIndexOf(",");
		String result = null;
		if (index > 0) {
			result = sb.substring(0, index) + "]";
		} else {
			result = sb.append("]").toString();
		}
		logger.debug(result);
	}

}
