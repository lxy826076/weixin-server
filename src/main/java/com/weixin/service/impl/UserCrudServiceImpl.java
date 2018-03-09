package com.weixin.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.PublicKey;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.weixin.dao.RedisBaseDao;
import com.weixin.dao.UserCrudDao;
import com.weixin.datasource.DataSource;
import com.weixin.datasource.DynamicDataSourceGlobal;
import com.weixin.model.User;
import com.weixin.service.UserCrudService;
import com.weixin.utils.JSONPUtil;
import com.weixin.utils.RSAUtil;

@Service("userCrudService")
public class UserCrudServiceImpl implements UserCrudService{

	@Resource(name="userCrudDao")
	private UserCrudDao ucd;

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Autowired
	RedisBaseDao redis;

//	@Cacheable(value = "user", key = "'id_'+#id")
	public User selectUserByID(int id) throws Exception {
		System.out.println("get data from database!");
		return ucd.selectUserByID(id);
	}

	public List<User> selectUsersByName(String userName) throws Exception {
		return ucd.selectUsersByName(userName);
	}

	public void addUser(User user) throws Exception {
		ucd.addUser(user);
		redis.hset("user:"+user.getId(), "name", user.getUserName());
	}
		
	@CachePut(value = "cacheManager", key = "'id_'+#user.getId()")
	public void updateUser(User user) throws Exception {
		ucd.updateUser(user);
	}

	@CacheEvict(value="cacheManager",key = "'id_'+#id")// 清空cacheManager 缓存
	public void deleteUser(int id) throws Exception {
		ucd.deleteUser(id);

	}

	public List<User> getAllUser() throws Exception {
		return ucd.getAllUser();
	}

	public User findPwdByUname(String uName) throws Exception{
		return ucd.getUserInfoByName(uName);
	}

	public JSONObject validateUserSign(Map<String, String> dataMap) throws Exception {
		JSONObject json = new JSONObject();
		String appId = dataMap.get("appId");
		String sign = dataMap.get("sign");
		dataMap.remove("appId");
		dataMap.remove("sign");

		//判断用户是否存在
		String publicKeyStr = redis.hget("user:"+appId, "publicKey");
		if (StringUtils.isEmpty(publicKeyStr)) {
			json.put("state", false);
			json.put("msg", "获取用户公钥失败！");
			return json;
		}
		//验证数据合法性
		PublicKey publicKey = RSAUtil.getPublicKey(publicKeyStr);
		boolean isValid = RSAUtil.verify(dataMap.toString().getBytes("UTF-8"), sign, publicKey);
		if (!isValid) {
			json.put("state", false);
			json.put("msg", "签名失败！");
			return json;
		}
		json.put("state", true);
		return json;
	}


}
