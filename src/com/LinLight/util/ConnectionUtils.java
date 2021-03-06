package com.LinLight.util;

//蓝牙连接的工具类
public class ConnectionUtils {
	public static boolean DEBUG = false;

	public static void Log(String TAG, String data) {
		android.util.Log.i(TAG, data);
	}

	/**
	 * Parse the hexadecimal string as string
	 * 
	 * @param bytes
	 *            a given hexadecimal string
	 * 
	 */

	public static String toStringValue(byte[] bytes) {
		return toStringValue(bytesToString(bytes));
	}

	public static String toStringValue(String s) {
		String[] sss = s.split(" ");
		String s1 = "";
		for (String ss : sss) {
			if (ss.equals("00")) {
				continue;
			}
			s1 += (char) hexStringToAlgorism(ss);
		}
		return s1;
	}

	// 将十六进制字符转化为十进制数
	public static int hexStringToAlgorism(String hex) {
		hex = hex.toUpperCase();
		int max = hex.length();
		int result = 0;
		for (int i = max; i > 0; i--) {
			char c = hex.charAt(i - 1);
			int algorism = 0;
			if (c >= '0' && c <= '9') {
				algorism = c - '0';
			} else {
				algorism = c - 55;
			}
			result += Math.pow(16, max - i) * algorism;
		}
		return result;
	}

	/**
	 * Parse the hexadecimal byte array as hexadecimal string
	 * 
	 * @param bytes
	 *            a given hexadecimal byte
	 * 
	 * */
	// 将十六进制字节码转换为十六进制字符串输出,2为指定的输出字段的宽度.如果位数小于2,则左端补0
	public static String bytesToString(byte[] bytes) {
		StringBuilder stringBuilder = new StringBuilder(bytes.length);
		for (byte byteChar : bytes)
			stringBuilder.append(String.format("%02X ", byteChar));
		return stringBuilder.toString();
	}
}