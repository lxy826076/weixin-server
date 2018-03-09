package com.weixin.utils;

public class JSONPUtil {
	
	
	public static String transformationWrite(String callback,String text)
	{
		if(org.apache.commons.lang3.StringUtils.isNotEmpty(callback))
		{
			return callback+"("+text+")";
		}
		return text;
		
	}

}
