package com.csei.database.entity;

public class User {
	private int number;
	private String role;
	private int roleNum;
	private String name;
	private String userName;
	private int id;
	private String image;
	private String sex;
	private String userRole;
	
	public int getNumber(){
		return number;
	}
	public void setNumber(int number){
		this.number=number;
	}
	
	public String getRole(){
		return role;
	}
	public void setRole(String role){
		this.role=role;
	}
	
	public int getRoleNum(){
		return roleNum;
	}
	public void setRole(int roleNum){
		this.roleNum=roleNum;
	}public String getName(){
		return name;
	}
	public void setName(String name){
		this.name=name;
	}
	public String getUserName(){
		return userName;
	}
	public void setUserName(String userName){
		this.userName=userName;
	}
	public int getId(){
		return id;
	}
	public void setId(int id){
		this.id=id;
	}
	public String getImage(){
		return image;
	}
	public void setImage(String image){
		this.image=image;
	}
	public String getSex(){
		return sex;
	}
	public void setSex(String sex){
		this.sex=sex;
	}
	public String getUserRole(){
		return userRole;
	}
	public void set(String userRole){
		this.userRole=userRole;
	}

}
