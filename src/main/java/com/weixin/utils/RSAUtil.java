package com.weixin.utils;

import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class RSAUtil {
	
	protected static Logger log = LogManager.getLogger(RSAUtil.class);

	public static final String KEY_ALGORITHM = "RSA"; //密钥加密算法
	public static final String SIGNATURE_ALGORITHM = "MD5withRSA";
	private static final String PUBLIC_KEY = "RSAPublicKey";//公钥
	private static final String PRIVATE_KEY = "RSAPrivateKey";//私钥

	public static Map<String, Object> initKey() throws Exception {
		KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance(KEY_ALGORITHM);
		keyPairGen.initialize(1024);
		KeyPair keyPair = keyPairGen.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		Map<String, Object> keyMap = new HashMap<String, Object>(2);
		keyMap.put(PUBLIC_KEY, publicKey);
		keyMap.put(PRIVATE_KEY, privateKey);
		return keyMap;
	}

	public static String getPublicKey(Map<String, Object> keyMap) throws Exception {
		Key key = (Key) keyMap.get(PUBLIC_KEY); 
		byte[] publicKey = key.getEncoded(); 
		return encryptBASE64(publicKey);
	}
	public static String getPrivateKey(Map<String, Object> keyMap) throws Exception {
		Key key = (Key) keyMap.get(PRIVATE_KEY); 
		byte[] privateKey =key.getEncoded(); 
		return encryptBASE64(privateKey);
	}  

	//解密
	public static byte[] decryptBASE64(String key) throws Exception {
		return (new BASE64Decoder()).decodeBuffer(key);               
	}                                 

	//加密
	public static String encryptBASE64(byte[] key) throws Exception {               
		return (new BASE64Encoder()).encodeBuffer(key);               
	} 

	public static PublicKey getPublicKey(String key) {
		try {
			byte[] keyBytes = (Base64.decodeBase64(key));
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			PublicKey publicKey = keyFactory.generatePublic(keySpec);
			return publicKey;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static PrivateKey getPrivateKey(String key) {
		try {
			byte[] keyBytes = (Base64.decodeBase64(key));
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
			KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
			PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
			return privateKey;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String sign(byte[] data, PrivateKey priKey) {
		try {
			Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
			signature.initSign(priKey);
			signature.update(data);
			return Base64.encodeBase64URLSafeString(signature.sign());
		} catch (Exception e) {
			log.error("sign message error", e);
			log.error("加密报文出错", e);
			return null;
		}
	}

	public static boolean verify(byte[] data, String sign, PublicKey pubKey) {
		try {
			Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
			signature.initVerify(pubKey);
			signature.update(data);
			return signature.verify(Base64.decodeBase64(sign));
		} catch (Exception e) {
			log.error("verify message error", e);
			log.error("解密报文出错", e);
			return false;
		}
	}


}
