package com.shaoxia.common.utils;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;


/**
 * @author wjc28
 */
public class PasswordUtil {

	public static String getSalt() {
		// 生成一个随机的盐值
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[16];
		random.nextBytes(salt);
		return Base64.getEncoder().encodeToString(salt);
	}
	public static String hashPassword(String password){
		String generatedPassword = null;
		try {
			// 创建一个MD5哈希算法实例
			MessageDigest md = MessageDigest.getInstance("MD5");
			// 将密码转换为字节数组并进行哈希
			byte[] bytes = md.digest(password.getBytes());
			// 将字节数组转换为Base64字符串
			generatedPassword = Base64.getEncoder().encodeToString(bytes);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return generatedPassword;
	}



	public static boolean verifyPassword(String inputPassword, String storedPassword) {
		return inputPassword.equals(storedPassword);
	}

}
