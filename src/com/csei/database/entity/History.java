package com.csei.database.entity;

public class History {
	private int id;
	private int userId;
	private int deviceId;
	private int storeId;
	private String time;
	private int optionType;
	private int mainDeviceId;
	private int upLoadFlag;
	private String driverName;
	private String carNum;
	private String driverTel;
	
	public int getId(){
		return id;
	}
	public void setId(int id){
		this.id=id;
	}
	
	public int getUserId(){
		return userId;
	}
	public void setUserId(int userId){
		this.userId=userId;
	}

	public int getDeviceId(){
		return deviceId;
	}
	public void setDeviceId(int deviceId){
		this.deviceId=deviceId;
	}

	public int getStoreId(){
		return storeId;
	}
	public void setStoreId(int storeId){
		this.storeId=storeId;
	}
	
	public String getTime(){
		return time;
	}
	public void setTime(String time){
		this.time=time;
	}

	public int getOptionType(){
		return optionType;
	}
	public void setOptionType(int optionType){
		this.optionType=optionType;
	}

	public int getMainDeviceId(){
		return mainDeviceId;
	}
	public void setMainDeviceId(int mainDeviceId){
		this.mainDeviceId=mainDeviceId;
	}

	

	public int getUpLoadFlag(){
		return upLoadFlag;
	}
	public void setUpLoadFlag(int upLoadFlag){
		this.upLoadFlag=upLoadFlag;
	}
	
	public String getDriverName()
	{
		return driverName;
	}
	public void setDriverName(String driverName){
		this.driverName=driverName;
	}
	
	public String getCarNum()
	{
		return carNum;
	}
	public void setCarNum(String carNum){
		this.carNum=carNum;
	}
	
	public String getDriverTel()
	{
		return driverTel;
	}
	public void setDriverTel(String driverTel){
		this.driverTel=driverTel;
	}

}
