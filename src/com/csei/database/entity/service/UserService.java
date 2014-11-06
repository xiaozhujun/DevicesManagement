package com.csei.database.entity.service;

import java.util.HashMap;

public interface UserService {

	//添加用户
	public void addUser(HashMap<String, String> map);
	//查询用户是否存在
	public boolean findUserByUserName(String userName);
	//找到用户ID
	public int findIdByUserName(String userName);
	
}
