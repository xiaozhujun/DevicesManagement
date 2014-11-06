package com.csei.database.entity.service.imple;

import java.util.HashMap;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.csei.database.DBOptions;
import com.csei.database.entity.service.UserService;

public class UserServiceImple implements UserService {
	private DBOptions dbOptions;
	private SQLiteDatabase db;

	public UserServiceImple(Context context) {
		dbOptions = DBOptions.getInstance(context);
		db = dbOptions.getWritableDatabase();
	}

	@Override
	public void addUser(HashMap<String, String> map) {
		// TODO Auto-generated method stub
		//开启事务
		db.beginTransaction();
		db.execSQL(
				"insert into user(name,userName,id,image,sex,userRole) values(?,?,?,?,?,?)",
				new Object[] { map.get("name"), map.get("userName"),
						map.get("id"), map.get("image"), map.get("sex"),
						map.get("userRole") });
		//事务成功
		db.setTransactionSuccessful();
		//关闭事务
		db.endTransaction();
	}

	@Override
	public boolean findUserByUserName(String userName) {
		// TODO Auto-generated method stub
		Cursor cursor = db.rawQuery("select * from user where userName=?",  new String[]{userName});
		while(cursor.moveToNext()){
			return true;
		}
		return false;
	}

	@Override
	public int findIdByUserName(String userName) {
		// TODO Auto-generated method stub
		Cursor cursor = db.rawQuery("select * from user where userName=?",  new String[]{userName});
		while(cursor.moveToNext()){
			return Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
		}
		return 0;
	}
	
	public String findNameByUserName(String userName){
		Cursor cursor = db.rawQuery("select * from user where userName=?",  new String[]{userName});
		while(cursor.moveToNext()){
			return cursor.getString(cursor.getColumnIndex("name"));
		}
		return null;
	}

}
