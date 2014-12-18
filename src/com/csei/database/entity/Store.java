package com.csei.database.entity;

public class Store {
	private int id;
	private String name;
	private String description;
	private String createTime;
	private String address;
	private String linkMan;
	private String telephone;
	private int appId;
	
	
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
	
	public String getDescription(){
		return description;
	}
	public void setDescription(String description){
		this.description=description;
	}
	
	public String getCreateTime(){
		return createTime;
	}
	public void setCreateTime(String createTime){
		this.createTime=createTime;
	}
	
	public String getAddress(){
		return address;
	}
	public void setAddress(String address){
		this.address=address;
	}
	
	public String getLinkMan(){
		return linkMan;
	}
	public void setLinkMan(String linkMan){
		this.linkMan=linkMan;
	}
	
	public String getTelephone(){
		return telephone;
	}
	public void setTelephone(String telephone){
		this.telephone=telephone;
	}
	
	public int getAppId(){
		return appId;
	}
	public void setAppId(int appId){
		this.appId=appId;
	}
}
