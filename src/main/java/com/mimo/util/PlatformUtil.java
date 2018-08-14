package com.mimo.util;

import java.nio.channels.spi.SelectorProvider;

/**
 * 平台工具
 * 
 * @author wukai
 *
 */
public class PlatformUtil {

	public static final String OS_NAME = System.getProperty("os.name");
	public static final String JVM_VERSION = System.getProperty("java.runtime.version");
	public static final SelectorProvider DEFAULT_PROVIDER = SelectorProvider.provider();
	
	public static boolean islinux() {
		return OS_NAME.toLowerCase().contains("linux");
	}

	public static boolean iswindows() {
		return OS_NAME.toLowerCase().contains("win");
	}
	
	
	
	
}
