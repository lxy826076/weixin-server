package com.weixin.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.weixin.dao.RedisBaseDao;
import com.weixin.service.SysService;
import com.weixin.utils.RSAUtil;

@Service("sysService")
@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)  
public class SysServiceImpl implements SysService {
	@Autowired
	private RedisBaseDao redis;

	public JSONObject getKeyt(String appId) throws Exception {
		JSONObject json = new JSONObject();
		String key = "user:" + appId;
		//判读用户有效性
		String name = redis.hget(key, "name");
		if (StringUtils.isEmpty(name)) {
			json.put("state", false);
			json.put("msg", "该用户不存在！");
			return json;
		}
		//判断当前用户是否已经有密钥
		String privateKey = redis.hget(key, "privateKey");
		if (!StringUtils.isEmpty(privateKey)) {
			json.put("privateKey", privateKey);
			json.put("state", true);
			json.put("msg", "sucess");
			return json;
		}
		return createKeyt(key); 
	}

	public JSONObject createKeyt(String key) throws Exception {
		JSONObject json = new JSONObject();
		Map<String, Object> initKey = RSAUtil.initKey();
		String privateKey = RSAUtil.getPrivateKey(initKey);
		String publicKey = RSAUtil.getPublicKey(initKey);
		//去除换行
		privateKey = privateKey.replaceAll("\r|\n", "");
		publicKey = publicKey.replaceAll("\r|\n", "");
		
		redis.hset(key, "privateKey", privateKey);
		redis.hset(key, "publicKey", publicKey);
		json.put("privateKey", privateKey);
		json.put("flag", true);
		json.put("msg", "sucess");
		return json;
	}
	
}
