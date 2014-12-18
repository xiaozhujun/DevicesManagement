package com.csei.database.entity;

public class Transport {
	private int id;
	private int userId;
	private String driver;
	private String telephone;
	private String destination;
	private String address;
	private int deviceId;
	private int upLoadFlag;
	
	public int getId(){
		return id;
	}
	public void setId(int id){
		this.id=id;
	}
	
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public String getDriver(){
		return driver;
	}
	public void setDriver(String driver){
		this.driver=driver;
	}
	
	public String getTelephone(){
		return telephone;
	}
	public void setTelephone(String telephone){
		this.telephone=telephone;
	}
	
	public String getDestination(){
		return destination;
	}
	public void setDestination(String destination){
		this.destination=destination;
	}
	
	public String getAddress(){
		return address;
	}
	public void setAddress(String address){
		this.address=address;
	}
	
	public int getDeviceId(){
		return deviceId;
	}
	public void setDeviceId(int deviceId){
		this.deviceId=deviceId;
	}
	
	public int getUpLoadFlag(){
		return upLoadFlag;
	}
	public void setUpLoadFlag(int upLoadFlag){
		this.upLoadFlag=upLoadFlag;
	}
}
