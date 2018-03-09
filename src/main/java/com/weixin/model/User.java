package com.weixin.model;

import java.io.Serializable;

public class User implements Serializable{

	private int id;
	private String userName;
	private String userPwd;
	private String userAge;
	private String userAddress;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPwd() {
		return userPwd;
	}
	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}
	public String getUserAge() {
		return userAge;
	}
	public void setUserAge(String userAge) {
		this.userAge = userAge;
	}
	public String getUserAddress() {
		return userAddress;
	}
	public void setUserAddress(String userAddress) {
		this.userAddress = userAddress;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", userAddress=" + userAddress + ", userAge="
				+ userAge + ", userName=" + userName + ", userPwd=" + userPwd
				+ "]";
	}
}
