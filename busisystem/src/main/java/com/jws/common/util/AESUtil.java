package com.jws.common.util;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 * 编码工具类
 */
public class AESUtil {
	
	public static void main(String[] args) throws Exception {
		
		String text = "0/1/be6081aad4d141c3a0261f0bed3f4271";
//		String text = "gitlab/1/08408300101";
		System.out.println("加密前：" + text);

		String key = "robot";
		System.out.println("加密密钥和解密密钥：" + key);
		
		String encrypt = encrypt(text, key);
		System.out.println("加密后：" + URLEncoder.encode(encrypt, "utf-8"));
		
		String decrypt = decrypt(URLDecoder.decode(encrypt, "utf-8"), key);
		System.out.println("解密后：" + decrypt);
	}
	
	/**
	 * AES加密
	 * @param plainText 普通文本
	 * @param secretKey 密钥
	 * @return
	 * @throws Exception 
	 */
	public static String encrypt(String plainText, String secretKey) throws Exception {
		// 添加aes支持，如果没有添加，在Linux环境下会提示找不到aes支持
		Security.addProvider(new BouncyCastleProvider());
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, getKey(secretKey));
		byte[] encryptBytes = cipher.doFinal(plainText.getBytes("utf-8"));
		
		return Base64.encode(encryptBytes);
	}
	
	/**
	 * AES解密
	 * 
	 * @param encryptText 加密文本
	 * @param secretKey 密钥
	 * @return
	 * @throws Exception
	 */
	public static String decrypt(String encryptText, String secretKey) throws Exception {
		// 添加aes支持，如果没有添加，在Linux环境下会提示找不到aes支持
		Security.addProvider(new BouncyCastleProvider());
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, getKey(secretKey));
		byte[] decryptBytes = cipher.doFinal(Base64.decode(encryptText));
		
		return new String(decryptBytes);
	}
	
	/**
	 * 获取secretKey
	 * @param secretKey
	 * @return
	 * @throws Exception
	 */
	private static SecretKey getKey(String secretKey) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		
		SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
		secureRandom.setSeed(secretKey.getBytes());
		
		kgen.init(128, secureRandom);
		
		return kgen.generateKey();
	}
	
}
