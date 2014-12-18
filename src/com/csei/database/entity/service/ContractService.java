package com.csei.database.entity.service;

import java.util.HashMap;
import java.util.List;

public interface ContractService {
	public void addStore(List<HashMap<String, String>> list);
	public int findIdByName(String name);
	public List<HashMap<String, String>> findList();
}
