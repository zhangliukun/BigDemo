package com.zlk.bigdemo.application.utils;

import android.text.TextUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;

public class Md5Util {

	/**
	 * 获取文件md5值
	 * 
	 * @param filePath
	 *            文件路径
	 * @return 文件md5值 出错则返回空
	 */
	public static String getMD5(String filePath) {
		if (filePath == null)
			return null;

		FileInputStream fis = null;
		FileChannel fc = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			fis = new FileInputStream(filePath);
			fc = fis.getChannel();
			ByteBuffer buffer = ByteBuffer.allocate(100 * 1024);
			int length = -1;
			while ((length = fc.read(buffer)) != -1) {
				md.update(buffer.array(), 0, length);
				buffer.position(0);
				// 让出cpu
				Thread.sleep(1);
			}
			byte[] b = md.digest();
			return byteToHexString(b);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			if (fis != null)
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

			if (fc != null)
				try {
					fc.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	public static String getStringMD5(String input) {
		if (TextUtils.isEmpty(input)) {
			return null;
		}
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] inputByteArray = input.getBytes();
			md.update(inputByteArray);
			byte[] resultByteArray = md.digest();
			return byteToHexString(resultByteArray);
		} catch (Exception e) {
			return null;
		}
	}

	private final static char hexDigits[] = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	/**
	 * 把byte[]数组转换成十六进制字符串表示形式
	 *
	 * @param tmp
	 *            要转换的byte[]
	 * @return 十六进制字符串表示形式
	 */
	private static final String byteToHexString(byte[] tmp) {
		String s;
		// 用字节表示就是 16 个字节
		char str[] = new char[16 * 2]; // 每个字节用 16 进制表示的话，使用两个字符，
		// 所以表示成 16 进制需要 32 个字符
		int k = 0; // 表示转换结果中对应的字符位置
		for (int i = 0; i < 16; i++) { // 从第一个字节开始，对 MD5 的每一个字节
			// 转换成 16 进制字符的转换
			byte byte0 = tmp[i]; // 取第 i 个字节
			str[k++] = hexDigits[byte0 >>> 4 & 0xf]; // 取字节中高 4 位的数字转换,
			// >>> 为逻辑右移，将符号位一起右移
			str[k++] = hexDigits[byte0 & 0xf]; // 取字节中低 4 位的数字转换
		}
		s = new String(str); // 换后的结果转换为字符串
		return s;
	}

	public static String hex2Str(byte[] src) {
    	if(src == null || src.length == 0) return null;
    	String dst = new String();
    	for(int i = 0; i < src.length; i++) {
    		byte b = src[i];
    		byte ch = (byte) ((b >> 4) & 0x0f);
    		if(ch > 9)
    			ch += 55;
    		else
    			ch += 48;
    		dst += (char)ch;
    		ch = (byte)(b & 0x0f);
    		if(ch > 9)
    			ch += 55;
    		else
    			ch += 48;
    		dst += (char)ch;
    	}
    	return dst;
    }
	public static byte[] str2Hex(String src) {
    	if(src == null || src.isEmpty()) return null;
    	int len = src.length() / 2;
    	byte[] dst = new byte[len];
    	for(int i = 0; i < len; i++) {
    		byte hi = (byte)src.charAt(2*i);
    		if(hi >= 65)
    			hi -= 55;
    		else
    			hi -= 48;
    		byte lo = (byte)src.charAt(2*i+1);
    		if(lo >= 'A')
    			lo -= 55;
    		else
    			lo -= 48;
    		byte ch = (byte)((hi << 4) | lo);
    		dst[i] = ch;
    	}
    	return dst;
    }
}
