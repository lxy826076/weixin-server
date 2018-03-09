package com.weixin.test;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.weixin.utils.HttpClientUtil;

public class Test {
	public static void main(String[] args) {
//		String url = "http://10.182.100.54:8080/GP16IVA/qa/hot_question";
		String url = "http://10.182.100.54:8080/GP16IVA/qa/hot_question";
		String body = "{key_app:95290000}";
		try {
			String sendPostByJson = HttpClientUtil.sendPostByJson(url, body);
			System.out.println(sendPostByJson);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
