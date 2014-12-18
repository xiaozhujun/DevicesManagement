package com.csei.database.entity;

public class User {
	private int id;
	private String name;
	private String sex;
	private String role;
	private String status;
	private int appId;
	private String appName;
	private String image;
	
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
	
	public String getSex(){
		return sex;
	}
	public void setSex(String sex){
		this.sex=sex;
	}
	
	public String getRole(){
		return role;
	}
	public void setRole(String role){
		this.role=role;
	}
	
	public String getStatus(){
		return status;
	}
	public void setStatus(String status){
		this.status=status;
	}
	
	public int getAppId(){
		return appId;
	}
	public void setAppId(int appId){
		this.appId=appId;
	}
	
	public String getAppName(){
		return appName;
	}
	public void setAppName(String AppName){
		this.appName=AppName;
	}
	
	public String getImage(){
		return image;
	}
	public void setImage(String image){
		this.image=image;
	}
}
