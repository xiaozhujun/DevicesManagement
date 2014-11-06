package com.csei.database.entity.service.imple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.csei.database.DBOptions;
import com.csei.database.entity.service.HistoryService;

public class HistoryServiceImple implements HistoryService {

	private DBOptions dbOptions;
	private SQLiteDatabase db;

	public HistoryServiceImple(Context context) {
		dbOptions = DBOptions.getInstance(context);
		db = dbOptions.getWritableDatabase();
	}

	@Override
	public void addHistory(HashMap<String, String> map) {
		// TODO Auto-generated method stub
		// 开启事务
		db.beginTransaction();
		db.execSQL(
				"insert into history(userId,deviceId,storeId,time,optionType,mainDeviceId,upLoadFlag,driverName,carNum,driverTel) values(?,?,?,?,?,?,?,?,?,?)",
				new Object[] { Integer.parseInt(map.get("userId")),
						Integer.parseInt(map.get("deviceId")),
						Integer.parseInt(map.get("storeId")), map.get("time"),
						Integer.parseInt(map.get("optionType")),
						Integer.parseInt(map.get("mainDeviceId")),
						Integer.parseInt(map.get("upLoadFlag")),
						map.get("driverName"),
						map.get("carNum"),
						map.get("driverTel") });
		// 事务成功
		db.setTransactionSuccessful();
		// 关闭事务
		db.endTransaction();
	}

	@Override
	public List<HashMap<String, String>> findListByUpLoadFlag(int upLoadFlag) {
		// TODO Auto-generated method stub
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		Cursor cursor = db.rawQuery("select * from history where upLoadFlag=?",
				new String[] { upLoadFlag + "" });
		while (cursor.moveToNext()) {
			HashMap<String, String> map = new HashMap<String,String>();
			map.put("userId", cursor.getInt(cursor.getColumnIndex("userId"))
					+ "");
			map.put("deviceId",
					cursor.getInt(cursor.getColumnIndex("deviceId")) + "");
			map.put("storeId", cursor.getInt(cursor.getColumnIndex("storeId"))
					+ "");
			map.put("time", cursor.getString(cursor.getColumnIndex("time"))
					+ "");
			map.put("optionType",
					cursor.getInt(cursor.getColumnIndex("optionType")) + "");
			map.put("mainDeviceId",
					cursor.getInt(cursor.getColumnIndex("mainDeviceId")) + "");
			map.put("driverName",
					cursor.getString(cursor.getColumnIndex("driverName")));
			map.put("carNum",
					cursor.getString(cursor.getColumnIndex("carNum")));
			map.put("driverTel",
					cursor.getString(cursor.getColumnIndex("driverTel")));
			list.add(map);
		}
		return list;
	}

	public boolean findRecordByDeviceId(String deviceId) {
		Cursor cursor = db.rawQuery("select * from history where deviceId=?",
				new String[] { deviceId + "" });
		while (cursor.moveToNext()) {
			return true;
		}
		return false;
	}

	@Override
	public List<HashMap<String, String>> findHistory(int userId) {
		// TODO Auto-generated method stub
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		Cursor cursor = db.rawQuery("select * from history where userId=?",
				new String[] { userId + "" });
		while (cursor.moveToNext()) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("id", cursor.getInt(cursor.getColumnIndex("id")) + "");
			map.put("userId", cursor.getInt(cursor.getColumnIndex("userId"))
					+ "");
			map.put("deviceId",
					cursor.getInt(cursor.getColumnIndex("deviceId")) + "");
			map.put("storeId", cursor.getInt(cursor.getColumnIndex("storeId"))
					+ "");
			map.put("time", cursor.getString(cursor.getColumnIndex("time"))
					+ "");
			map.put("optionType",
					cursor.getInt(cursor.getColumnIndex("optionType")) + "");
			map.put("mainDeviceId",
					cursor.getInt(cursor.getColumnIndex("mainDeviceId")) + "");
			map.put("upLoadFlag",
					cursor.getInt(cursor.getColumnIndex("upLoadFlag")) + "");
			map.put("driverName",
					cursor.getString(cursor.getColumnIndex("driverName")));
			map.put("carNum",
					cursor.getString(cursor.getColumnIndex("carNum")));
			map.put("driverTel",
					cursor.getString(cursor.getColumnIndex("driverTel")));
			list.add(map);
		}
		return list;
	}

	public void deleteHistoryById(int id) {

		//Cursor cursor = db.rawQuery("delete from history where id=?",
			//	new String[] { id + "" });
		db.delete("history", "id=?", new String[]{id+""});
	}

	public void updateUpLoadFlagHistoryById(int id) {

		ContentValues cv = new ContentValues();
		cv.put("upLoadFlag", 1+"");
		db.update("history", cv, "id = ?", new String[] { id + "" });
	}

}
