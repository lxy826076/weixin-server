package com.weixin.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class Base64Util {

	/**
	 * BASE64解密
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static String decryptBASE64(String key) throws Exception {
		if(StringUtil.isEmpty(key)) {
			return null;
		}
        byte[] bytes = (new BASE64Decoder()).decodeBuffer(key);
        return new String(bytes);
    }
 
    /**
     * BASE64加密
     * 
     * @param key
     * @return
     * @throws Exception
     */
    public static String encryptBASE64(String key) throws Exception {
        return (new BASE64Encoder()).encodeBuffer(key.getBytes());
    }
    
    public static void main(String[] args) {
    	String s = "{corpId:'wxaf2458dabc4c8c64',agentId:'1'}";
    	String key;
		try {
			key = Base64Util.encryptBASE64(s);
			String decry = Base64Util.decryptBASE64(key);
			System.out.println("key=" + key + ",decry=" + decry);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
