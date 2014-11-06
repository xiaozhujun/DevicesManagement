package com.csei.database.entity.service;

import java.util.HashMap;
import java.util.List;

public interface DeviceService {
	public void addDevice(HashMap<String, String> map);
	public boolean findDeviceById(int id);
	public String findNameById(int id);
	public int findIdByName(String name);
	public List<String> findMainDevice();
	public List<String> getListByMainDeviceId(int mainDeviceId);
	public void updateData(HashMap<String, String> map);

}
