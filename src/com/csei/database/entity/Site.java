package com.csei.database.entity;

public class Site {
	private int id;
	private String name;
	private String address;
	private String telephone;
	
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
	public String getAddress(){
		return address;
	}
	public void setAddress(String address){
		this.address=address;
	}
	public String getTelephone(){
		return telephone;
	}
	public void setTelephone(String telephone){
		this.telephone=telephone;
	}

}
