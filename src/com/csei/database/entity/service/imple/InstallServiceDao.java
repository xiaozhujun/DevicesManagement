package com.csei.database.entity.service.imple;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.csei.database.DBOptions;
import com.csei.database.entity.service.InstallService;

public class InstallServiceDao implements InstallService {
	private DBOptions dbOptions;
	private SQLiteDatabase db;

	public InstallServiceDao(Context context) {
		dbOptions = DBOptions.getInstance(context);
		db = dbOptions.getWritableDatabase();
	}

	@Override
	public void add(List<HashMap<String, String>> list) {
		// TODO Auto-generated method stub
		// 开启事务
		db.beginTransaction();
		for (int j = 0; j < list.size(); j++) {
			db.execSQL(
					"insert into install(userId,contractId,type,installMan,installStatus,deviceId,upLoadFlag,image) values(?,?,?,?,?,?,?,?)",
					new Object[] { list.get(j).get("userId"),
							list.get(j).get("contractId"),
							list.get(j).get("type"),
							list.get(j).get("installMan"),
							list.get(j).get("installStatus"),
							list.get(j).get("deviceId"),
							list.get(j).get("upLoadFlag"),
							list.get(j).get("image") });
		}

		// 事务成功
		db.setTransactionSuccessful();
		// 关闭事务
		db.endTransaction();
	}

	@Override
	public List<HashMap<String, String>> findListByUpLoadFlag(int upLoadFlag,
			int userId) {
		// TODO Auto-generated method stub
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		Cursor cursor = db.rawQuery(
				"select * from install where upLoadFlag=? and userId=?",
				new String[] { upLoadFlag + "", userId + "" });
		while (cursor.moveToNext()) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("userId",
					cursor.getString(cursor.getColumnIndex("userId"))+"");
			map.put("contractId",
					cursor.getInt(cursor.getColumnIndex("contractId")) + "");
			map.put("type", cursor.getString(cursor.getColumnIndex("type")));
			map.put("installMan",
					cursor.getString(cursor.getColumnIndex("installMan")));
			map.put("installStatus",
					cursor.getString(cursor.getColumnIndex("installStatus")));
			map.put("deviceId",
					cursor.getInt(cursor.getColumnIndex("deviceId")) + "");
			map.put("upLoadFlag",
					cursor.getInt(cursor.getColumnIndex("upLoadFlag")) + "");
			map.put("image",
					cursor.getString(cursor.getColumnIndex("image")));
			list.add(map);
		}
		return list;
	}

	@Override
	public List<HashMap<String, String>> findHistory(int userId) {
		// TODO Auto-generated method stub
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		Cursor cursor = db.rawQuery(
				"select * from install where userId=?",
				new String[] { userId + "" });
		while (cursor.moveToNext()) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("userId",
					cursor.getString(cursor.getColumnIndex("userId"))+"");
			map.put("contractId",
					cursor.getInt(cursor.getColumnIndex("contractId")) + "");
			map.put("type", cursor.getString(cursor.getColumnIndex("type")));
			map.put("installMan",
					cursor.getString(cursor.getColumnIndex("installMan")));
			map.put("installStatus",
					cursor.getString(cursor.getColumnIndex("installStatus")));
			map.put("deviceId",
					cursor.getInt(cursor.getColumnIndex("deviceId")) + "");
			map.put("upLoadFlag",
					cursor.getInt(cursor.getColumnIndex("upLoadFlag")) + "");
			map.put("image",
					cursor.getString(cursor.getColumnIndex("image")));
			list.add(map);
		}
		return list;
	}
	
	public void deleteHistoryById(int id) {

		//Cursor cursor = db.rawQuery("delete from history where id=?",
			//	new String[] { id + "" });
		db.delete("install", "id=?", new String[]{id+""});
	}

	public void updateUpLoadFlagHistoryById(int id) {

		ContentValues cv = new ContentValues();
		cv.put("upLoadFlag", 1+"");
		db.update("install", cv, "id = ?", new String[] { id + "" });
	}
}
