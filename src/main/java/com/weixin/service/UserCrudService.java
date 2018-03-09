package com.weixin.service;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.weixin.model.User;


public interface UserCrudService {
	
	public User selectUserByID(int id) throws Exception;

	public List<User> selectUsersByName(String userName) throws Exception;
	
	public List<User> getAllUser() throws Exception;

	public void addUser(User user) throws Exception;

	public void updateUser(User user) throws Exception;

	public void deleteUser(int id) throws Exception;

	public User findPwdByUname(String uName) throws Exception;
	
	public String getKeyt() throws Exception;

	public JSONObject getUserInfo(String userInfoGetUrl,Map<String, String> dataMap);
}
