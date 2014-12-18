package com.csei.database.entity.service.imple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.R.integer;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.csei.database.DBOptions;
import com.csei.database.entity.service.DeviceService;

public class DeviceServiceDao implements DeviceService {
	private DBOptions dbOptions;
	private SQLiteDatabase db;

	public DeviceServiceDao(Context context) {
		dbOptions = DBOptions.getInstance(context);
		db = dbOptions.getWritableDatabase();
	}

	@Override
	public void addDevice(HashMap<String, String> map) {
		// TODO Auto-generated method stub
		db.beginTransaction();
		db.execSQL(
				"insert into device(id,name,number,deviceType,mainDeviceId,batchNumber) values(?,?,?,?,?,?)",
				new Object[] { map.get("id"), map.get("name"),
						map.get("number"), map.get("deviceType"),
						map.get("mainDeviceId"), map.get("batchNumber") });
		// 事务成功
		db.setTransactionSuccessful();
		// 关闭事务
		db.endTransaction();
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
	public void updateData(HashMap<String, String> map) {
		// TODO Auto-generated method stub
		ContentValues cv = new ContentValues();
		cv.put("name", map.get("name"));
		cv.put("number", map.get("number"));
		cv.put("deviceType", map.get("deviceType"));
		cv.put("mainDeviceId", Integer.parseInt(map.get("mainDeviceId")));
		cv.put("batchNumber", map.get("batchNumber"));
		db.update("device", cv, "id = ?",
				new String[] { (String) map.get("id") });
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

}
