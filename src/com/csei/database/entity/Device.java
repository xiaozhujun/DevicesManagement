package com.csei.database.entity;

public class Device {
	private int id;
	private String name;
	private String number;
	private String deviceType;
	private int mainDeviceId;
	private String batchNumber;
	
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
	
	public String getNumber(){
		return number;
	}
	public void setNumber(String number){
		this.number=number;
	}
	
	public String getDeviceType(){
		return deviceType;
	}
	public void setDeviceType(String deviceType){
		this.deviceType=deviceType;
	}

	public int getMainDeviceId(){
		return mainDeviceId;
	}
	public void setMainDeviceId(int mainDeviceId){
		this.mainDeviceId=mainDeviceId;
	}
	
	public String getBatchNumber(){
		return batchNumber;
	}
	public void setBatchNumber(String batchNumber){
		this.batchNumber=batchNumber;
	}
}
