package com.weixin.service.impl;

import java.security.PrivateKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.weixin.dao.RedisBaseDao;
import com.weixin.dao.UserCrudDao;
import com.weixin.model.User;
import com.weixin.service.UserCrudService;
import com.weixin.utils.HttpClientUtil;
import com.weixin.utils.RSAUtil;

@Service("userCrudService")
@Transactional(propagation=Propagation.REQUIRED, rollbackFor=Exception.class)  
public class UserCrudServiceImpl implements UserCrudService{
	
	@Resource(name="userCrudDao")
	private UserCrudDao ucd;
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	@Autowired
	RedisBaseDao redis;

	@Cacheable(value = "user", key = "'id_'+#id")
	public User selectUserByID(int id) throws Exception {
		System.out.println("get data from database!");
		return ucd.selectUserByID(id);
	}

	public List<User> selectUsersByName(String userName) throws Exception {
		return ucd.selectUsersByName(userName);
	}

	public void addUser(User user) throws Exception {
		ucd.addUser(user);
		redisTemplate.opsForValue().set("id_"+user.getId(), user.toString());
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
	
	
	public String getKeyt() throws Exception{
//		redis.cacheValue("appId", "32");
//		redis.cacheValue("getKeyUrl", "http://127.0.0.1:8080/WeiXin/sys/getKeyt.do");
		String appId = redis.getValue("appId");
		String getKeyUrl = redis.getValue("getKeyUrl");
		Map<String, String> map = new HashMap<String, String>();
		map.put("appId", appId);
		String sendPostByJson = HttpClientUtil.sendPostRequestByJava(getKeyUrl, map);
		return sendPostByJson;
	}

	public JSONObject getUserInfo(String userInfoGetUrl, Map<String, String> dataMap) {
		//加密规则为对请求参数放入treemap中进行排序
		//加密编码为utf-8
		String requstDatas = dataMap.toString();
		String strPrivateKey = redis.hget("user:32", "privateKey");//这里的key从客户端自己的redis中取值
		PrivateKey privateKey = RSAUtil.getPrivateKey(strPrivateKey);
		String sign = RSAUtil.sign(requstDatas.getBytes(), privateKey);
		dataMap.put("sign", sign);
		dataMap.put("appId", Integer.toString(32));
		String responseStr = HttpClientUtil.sendPostRequestByJava(userInfoGetUrl, dataMap);
		JSONObject json = JSONObject.parseObject(responseStr);
		return json;
	}

}
