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
		db.execSQL("CREATE TABLE IF NOT EXISTS USER(id integer primary key,name varchar(255),userName varchar(255),image varchar(255),sex varchar(255),userRole varchar(255))");
		db.execSQL("CREATE TABLE IF NOT EXISTS STORE(id integer primary key,name varchar(255),address varchar(255),telephone varchar(255))");
		db.execSQL("CREATE TABLE IF NOT EXISTS DEVICE(id integer primary key,name varchar(255),userId integer,storeId integer,mainDeviceId integer,stateFlag integer)");
		//optionType：0入库；1出库；2安装；3卸载；4运输
		db.execSQL("CREATE TABLE IF NOT EXISTS HISTORY(id integer primary key autoincrement,userId integer,deviceId integer,storeId integer,time varchar(255),optionType integer,mainDeviceId integer,upLoadFlag integer,driverName varchar(255),carNum varchar(255),driverTel varchar(255))");
		db.execSQL("CREATE TABLE IF NOT EXISTS SITE(id integer primary key,name varchar(255),address varchar(255),telephone varchar(255))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS USER");
		db.execSQL("DROP TABLE IF EXISTS STORE"); 
		db.execSQL("DROP TABLE IF EXISTS DEVICE"); 
		db.execSQL("DROP TABLE IF EXISTS HISTORY"); 
		db.execSQL("DROP TABLE IF EXISTS SITE"); 
		onCreate(db); 
	}

}
