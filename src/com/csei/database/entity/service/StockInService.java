package com.csei.database.entity.service;

import java.util.HashMap;
import java.util.List;

public interface StockInService {
	public void add(List<HashMap<String, String>> list);
	public List<HashMap<String, String>> findListByUpLoadFlag(int upLoadFlag,int userId);
	public List<HashMap<String, String>> findHistory(int userId);
	public void deleteHistoryById(int id);
	public void updateUpLoadFlagHistoryById(int id);	
}
