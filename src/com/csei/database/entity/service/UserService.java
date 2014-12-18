package com.csei.database.entity.service;

import java.util.HashMap;

public interface UserService {
	public void addUser(HashMap<String, String> map);
	public void update(HashMap<String, String> map);
	public boolean finduserById(int id);
}
