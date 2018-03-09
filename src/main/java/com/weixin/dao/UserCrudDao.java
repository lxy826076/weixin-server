package com.weixin.dao;

import java.util.List;

import com.weixin.datasource.DataSource;
import com.weixin.datasource.DynamicDataSourceGlobal;
import com.weixin.model.User;

public interface UserCrudDao {
	
	@DataSource(value=DynamicDataSourceGlobal.READ)
	public User selectUserByID(int id) throws Exception;

	public List<User> selectUsersByName(String userName) throws Exception;

	public void addUser(User user) throws Exception;
	
	@DataSource(value=DynamicDataSourceGlobal.WRITE)
	public void updateUser(User user) throws Exception;

	public void deleteUser(int id) throws Exception;

	public List<User> getAllUser() throws Exception;

	public User getUserInfoByName(String uName) throws Exception;
}
