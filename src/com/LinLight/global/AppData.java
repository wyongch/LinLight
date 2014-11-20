
package com.LinLight.global;

public class AppData {
	
	/** 日志开关. */
	public static final boolean DEBUG = true;
	
	/** 性能测试. */
	public static boolean mMonitorOpened = false;
	
	/** 起始执行时间. */
	public static long startLogTimeInMillis = 0;
	
	/**
	 * 是否是测试环境，发布程序时需要置为false
	 */
	public static final boolean TEST = true;

}
