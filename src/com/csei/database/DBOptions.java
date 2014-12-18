package com.csei.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOptions extends SQLiteOpenHelper {
	private static DBOptions instance;

	public DBOptions(Context context) {
		super(context, "db", null, 1);
	}

	public static DBOptions getInstance(Context context) {
		if (instance == null) {
			synchronized (DBOptions.class) {
				if (instance == null) {
					instance = new DBOptions(context);
				}
			}
		}
		return instance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE IF NOT EXISTS USER(id integer primary key,name varchar(255),sex varchar(255),role varchar(255),status varchar(255),appId integer,appName varchar(255),image varchar(255))");
		db.execSQL("CREATE TABLE IF NOT EXISTS STORE(id integer primary key,name varchar(255),description varchar(255),createTime varchar(255),address varchar(255),linkMan varchar(255),telephone varchar(255),appId integer)");
		db.execSQL("CREATE TABLE IF NOT EXISTS CONTRACT(id integer primary key,name varchar(255),customerName varchar(255),number varchar(255),startTime varchar(255),endTime varchar(255),signTime varchar(255),appId integer)");
		db.execSQL("CREATE TABLE IF NOT EXISTS DEVICE(id integer primary key,name varchar(255),number varchar(255),deviceType varchar(255),mainDeviceId integer,batchNumber varchar(255))");
		//下面是记录表
		db.execSQL("CREATE TABLE IF NOT EXISTS TRANSPORT(id integer primary key autoincrement,userId integer,driver varchar(255),telephone varchar(255),destination varchar(255),address varchar(255),deviceId integer,upLoadFlag integer)");
		db.execSQL("CREATE TABLE IF NOT EXISTS STOCKIN(id integer primary key autoincrement,userId integer,storehouseId integer,number varchar(255),contractId integer,driver varchar(255),carNumber varchar(255),description varchar(255),deviceId integer,upLoadFlag integer)");
		db.execSQL("CREATE TABLE IF NOT EXISTS STOCKOUT(id integer primary key autoincrement,userId integer,storehouseId integer,number varchar(255),contractId integer,driver varchar(255),carNumber varchar(255),description varchar(255),deviceId integer,upLoadFlag integer)");
		db.execSQL("CREATE TABLE IF NOT EXISTS INSTALL(id integer primary key autoincrement,userId integer,contractId integer,type varchar(255),installMan varchar(255),installStatus varchar(255),deviceId integer,upLoadFlag integer)");
		db.execSQL("CREATE TABLE IF NOT EXISTS UNINSTALL(id integer primary key autoincrement,userId integer,contractId integer,removeMan varchar(255),removeStatus varchar(255),deviceId integer,upLoadFlag integer)");
		
	
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS USER");
		db.execSQL("DROP TABLE IF EXISTS STORE"); 
		db.execSQL("DROP TABLE IF EXISTS CONTRACT"); 
		db.execSQL("DROP TABLE IF EXISTS DEVICE"); 
		
		db.execSQL("DROP TABLE IF EXISTS TRANSPORT"); 
		db.execSQL("DROP TABLE IF EXISTS STOCKIN"); 
		db.execSQL("DROP TABLE IF EXISTS STOCKOUT"); 
		db.execSQL("DROP TABLE IF EXISTS INSTALL"); 
		db.execSQL("DROP TABLE IF EXISTS UNINSTALL"); 
		onCreate(db); 
	}

}
