package com.csei.database.entity.service.imple;

import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.csei.database.DBOptions;
import com.csei.database.entity.service.UserService;

public class UserServiceDao implements UserService {
	private DBOptions dbOptions;
	private SQLiteDatabase db;

	public UserServiceDao(Context context) {
		dbOptions = DBOptions.getInstance(context);
		db = dbOptions.getWritableDatabase();
	}

	@Override
	public void addUser(HashMap<String, String> map) {
		// TODO Auto-generated method stub
		// 开启事务
		db.beginTransaction();
		db.execSQL(
				"insert into user(id,name,sex,role,status,appId,appName,image) values(?,?,?,?,?,?,?,?)",
				new Object[] { map.get("id"), map.get("name"), map.get("sex"),
						map.get("role"), map.get("status"), map.get("appId"),
						map.get("appName"), map.get("image") });
		// 事务成功
		db.setTransactionSuccessful();
		// 关闭事务
		db.endTransaction();
	}

	@Override
	public void update(HashMap<String, String> map) {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		cv.put("name", map.get("name"));
		cv.put("sex", map.get("sex"));
		cv.put("role", map.get("role"));
		cv.put("status", map.get("status"));
		cv.put("appId", Integer.parseInt(map.get("appId")));
		cv.put("appName", map.get("appName"));
		cv.put("image", map.get("image"));
		db.update("user", cv, "id = ?", new String[] { (String) map.get("id") });
	}

	@Override
	public boolean finduserById(int id) {
		// TODO Auto-generated method stub
		Cursor cursor = db.rawQuery("select * from user where id=?",
				new String[] { id + "" });
		while (cursor.moveToNext()) {
			return true;
		}
		return false;
	}

}
