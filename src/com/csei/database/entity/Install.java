package com.csei.database.entity;

public class Install {
	private int id;
	private int userId;
	private int contractId;
	private String type;
	private String installMan;
	private String installStatus;
	private int deviceId;
	private int upLoadFlag;
	private String image;
	
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
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
	
	public String getType(){
		return type;
	}
	public void setType(String type){
		this.type=type;
	}
	
	public String getInstallMan(){
		return installMan;
	}
	public void setInstallMan(String installMan){
		this.installMan=installMan;
	}
	
	public String getInstallStatus(){
		return installStatus;
	}
	public void setInstallStatus(String installStatus){
		this.installStatus=installStatus;
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
