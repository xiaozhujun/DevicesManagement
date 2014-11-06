package com.csei.database.entity.service.imple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.csei.database.DBOptions;
import com.csei.database.entity.service.DeviceService;

public class DeviceServiceImple implements DeviceService {
	private DBOptions dbOptions;
	private SQLiteDatabase db;

	public DeviceServiceImple(Context context) {
		dbOptions = DBOptions.getInstance(context);
		db = dbOptions.getWritableDatabase();
	}

	@Override
	public void addDevice(HashMap<String, String> map) {
		// TODO Auto-generated method stub
		// 开启事务
		db.beginTransaction();
		db.execSQL(
				"insert into device(id,name,userId,storeId,mainDeviceId,stateFlag) values(?,?,?,?,?,?)",
				new Object[] { map.get("id"), map.get("name"),
						map.get("userId"), map.get("storeId"),
						map.get("mainDeviceId"), map.get("stateFlag") });
		// 事务成功
		db.setTransactionSuccessful();
		// 关闭事务
		db.endTransaction();
	}

	@Override
	public String findNameById(int id) {
		// TODO Auto-generated method stub
		Cursor cursor = db.rawQuery("select * from device where id=?",
				new String[] { id + "" });
		while (cursor.moveToNext()) {
			return cursor.getString(cursor.getColumnIndex("name"));
		}
		return null;
	}

	@Override
	public int findIdByName(String name) {
		// TODO Auto-generated method stub
		Cursor cursor = db.rawQuery("select * from device where name=?",
				new String[] { name });
		while (cursor.moveToNext()) {
			return Integer.parseInt(cursor.getString(cursor
					.getColumnIndex("id")));
		}
		return 0;
	}

	@Override
	public List<String> findMainDevice() {
		// TODO Auto-generated method stub
		List<String> list = new ArrayList<String>();
		Cursor cursor = db.rawQuery("select * from device", null);
		if (cursor.moveToFirst()) {
			while (cursor.moveToNext()) {
				if (cursor.getColumnIndex("id") == cursor
						.getColumnIndex("mainDeviceId")) {
					list.add(cursor.getString(cursor.getColumnIndex("id")));
				}
			}
			return list;
		} else {
			return null;
		}

	}

	@Override
	public List<String> getListByMainDeviceId(int mainDeviceId) {
		// TODO Auto-generated method stub
		List<String> list = new ArrayList<String>();
		Cursor cursor = db.rawQuery(
				"select * from device where mainDeviceId=?",
				new String[] { mainDeviceId + "" });
		while (cursor.moveToNext()) {
			list.add(cursor.getString(cursor.getColumnIndex("id")));
		}
		return list;
	}

	@Override
	public boolean findDeviceById(int id) {
		// TODO Auto-generated method stub
		Cursor cursor = db.rawQuery("select * from device where id=?",
				new String[] { id + "" });
		while (cursor.moveToNext()) {
			return true;
		}
		return false;
	}

	@Override
	public void updateData(HashMap<String, String> map) {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		cv.put("name", map.get("name"));
		cv.put("userId", Integer.parseInt( map.get("userId")));
		cv.put("storeId", Integer.parseInt(map.get("storeId")));
		cv.put("mainDeviceId", Integer.parseInt(map.get("mainDeviceId")));
		cv.put("stateFlag", Integer.parseInt(map.get("stateFlag")));
		db.update("device", cv, "id = ?",
				new String[] { (String) map.get("id") });
	}

}
