package com.csei.database.entity;

public class Contract {
	private int id;
	private int userId;
	private String customerName;
	private String number;
	private String startTime;
	private String endTime;
	private String signTime;
	private int appId;
	
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
	public String getCustomerName(){
		return customerName;
	}
	public void setCustomerName(String customerName){
		this.customerName=customerName;
	}
	
	public String getNumber(){
		return number;
	}
	public void setNumber(String number){
		this.number=number;
	}
	
	public String getStartTime(){
		return startTime;
	}
	public void setStartTime(String startTime){
		this.startTime=startTime;
	}
	
	public String getEndTime(){
		return endTime;
	}
	public void setEndTime(String endTime){
		this.endTime=endTime;
	}
	
	public String getSignTime(){
		return signTime;
	}
	public void setSignTime(String signTime){
		this.signTime=signTime;
	}
	
	public int getAppId(){
		return appId;
	}
	public void setAppId(int appId){
		this.appId=appId;
	}
}
