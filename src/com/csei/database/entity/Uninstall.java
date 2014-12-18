package com.csei.database.entity;

public class Uninstall{
private int id;
private int userId;
private int contractId;
private String removeMan;
private String removeStatus;
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

public int getContractId(){
	return contractId;
}
public void setContractId(int contractId){
	this.contractId=contractId;
}

public String getRemoveMan(){
	return removeMan;
}
public void setRemoveMan(String removeMan){
	this.removeMan=removeMan;
}

public String getRemoveStatus(){
	return removeStatus;
}
public void setRemoveStatus(String removeStatus){
	this.removeStatus=removeStatus;
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
