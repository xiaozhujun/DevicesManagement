package com.csei.database.entity.service;

import java.util.HashMap;
import java.util.List;

import android.R.integer;

public interface HistoryService {
	public void addHistory(HashMap<String, String> map);
	public List<HashMap<String, String>> findListByUpLoadFlag(int upLoadFlag);
	public List<HashMap<String, String>> findHistory(int userId);

}
