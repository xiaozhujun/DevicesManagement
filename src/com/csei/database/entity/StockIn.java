package com.csei.database.entity;

public class StockIn {
	private int id;
	private int userId;
	private int storehouseId;
	private String number;
	private int contractId;
	private String driver;
	private String carNumber;
	private String description;
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
	public int getStorehouseId(){
		return storehouseId;
	}
	public void setStorehouseId(int storehouseId){
		this.storehouseId=storehouseId;
	}
	
	public String getNumber(){
		return number;
	}
	public void setNumber(String number){
		this.number=number;
	}
	
	public int getContractId(){
		return contractId;
	}
	public void setContractId(int contractId){
		this.contractId=contractId;
	}
	
	public String getDriver(){
		return driver;
	}
	public void setDriver(String driver){
		this.driver=driver;
	}
	
	public String getCarNumber(){
		return carNumber;
	}
	public void setCarNumber(String carNumber){
		this.carNumber=carNumber;
	}
	
	public String getDescription(){
		return description;
	}
	public void setDescription(String description){
		this.description=description;
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
