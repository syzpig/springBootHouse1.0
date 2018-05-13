package com.mooc.house.common.HashUtils;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.nio.charset.Charset;
/**
 *密码加盐工具类，防止明文出现
 */
public class HashUtils {
	
	private static final HashFunction FUNCTION = Hashing.md5(); //借助gava中的加密类
	
	private static final String SALT = "mooc.com";//创建盐，因为防止md5被暴力破解，所以加盐
	
	public static String encryPassword(String password){
	   HashCode hashCode =	FUNCTION.hashString(password+SALT, Charset.forName("UTF-8"));
	   return hashCode.toString();
	}

}
