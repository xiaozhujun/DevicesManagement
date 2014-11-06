package com.csei.database.entity;

public class Device {
	private int id;
	private String name;
	private int userId;
	private int storeId;
	private int mainDeviceId;
	//设备是否需要维修
	private int stateFlag;
	
	public int getId(){
		return id;
	}
	public void setId(int id){
		this.id=id;
	}
	
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name=name;
	}
	
	public int getUserId(){
		return userId;
	}
	public void setUserId(int userId){
		this.userId=userId;
	}
	
	public int getStoreId(){
		return storeId;
	}
	public void setStoreId(int storeId){
		this.storeId=storeId;
	}
	
	public int getMainDeviceId(){
		return mainDeviceId;
	}
	public void setMainDeviceId(int mainDeviceId){
		this.mainDeviceId=mainDeviceId;
	}
	
	
	public int getStateFlag(){
		return stateFlag;
	}
	public void setStateFlag(int stateFlag){
		this.stateFlag=stateFlag;
	}

}
