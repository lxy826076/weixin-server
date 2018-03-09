package com.weixin.utils;

/**
 * 
 * 以List读取配置文件返回类型  id:对应配置文件中的id值  name:对应配置文件中的name值
 * @author 郭婷婷
 * </br>2011-11-15 下午03:04:30
 * @version V1.0
 */
public class PropertiesItem {
	private String id; //配置文件中的id值
	private String name;//配置文件中的name值
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
