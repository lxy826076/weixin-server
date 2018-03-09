package com.weixin.dao;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.weixin.model.User;

@Transactional
public interface UserCrudDao {
	
	public User selectUserByID(int id) throws Exception;

	public List<User> selectUsersByName(String userName) throws Exception;

	public void addUser(User user) throws Exception;

	public void updateUser(User user) throws Exception;

	public void deleteUser(int id) throws Exception;

	public List<User> getAllUser() throws Exception;

	public User getUserInfoByName(String uName) throws Exception;
}
