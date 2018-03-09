package com.weixin.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.weixin.datasource.DataSource;
import com.weixin.datasource.DynamicDataSourceGlobal;
import com.weixin.model.User;


public interface UserCrudService {
	
	@Transactional(propagation =Propagation.NOT_SUPPORTED)
	public User selectUserByID(int id) throws Exception;

	public List<User> selectUsersByName(String userName) throws Exception;
	
	
	public List<User> getAllUser() throws Exception;

	public void addUser(User user) throws Exception;
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateUser(User user) throws Exception;

	public void deleteUser(int id) throws Exception;

	public User findPwdByUname(String uName) throws Exception;

	public JSONObject validateUserSign(Map<String, String> dataMap) throws Exception;
}
