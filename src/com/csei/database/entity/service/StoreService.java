package com.csei.database.entity.service;

import java.util.HashMap;

public interface StoreService {
	//添加仓库
	public void addStore(HashMap<String, String> map);
	//查找id
	public int findIdByName(String name);
	//查找名字
	public String findNameById(int id);

}
