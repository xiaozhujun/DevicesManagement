package com.csei.database.entity.service.imple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.csei.database.DBOptions;
import com.csei.database.entity.service.StockOutService;

public class StockOutServiceDao implements StockOutService{
	private DBOptions dbOptions;
	private SQLiteDatabase db;

	public StockOutServiceDao(Context context) {
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
					"insert into stockout(userId,storehouseId,number,contractId,driver,carNumber,description,deviceId,upLoadFlag) values(?,?,?,?,?,?,?,?,?)",
					new Object[] { list.get(j).get("userId"),
							list.get(j).get("storehouseId"),
							list.get(j).get("number"),
							list.get(j).get("contractId"),
							list.get(j).get("driver"),
							list.get(j).get("carNumber"),
							list.get(j).get("description"),
							list.get(j).get("deviceId"),
							list.get(j).get("upLoadFlag") });
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
				"select * from stockout where upLoadFlag=? and userId=?",
				new String[] { upLoadFlag + "", userId + "" });
		while (cursor.moveToNext()) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("userId",
					cursor.getString(cursor.getColumnIndex("userId"))+"");
			map.put("storehouseId",
					cursor.getInt(cursor.getColumnIndex("storehouseId")) + "");
			map.put("number", cursor.getString(cursor.getColumnIndex("number")));
			map.put("contractId",
					cursor.getInt(cursor.getColumnIndex("contractId")) + "");
			map.put("driver", cursor.getString(cursor.getColumnIndex("driver")));
			map.put("carNumber",
					cursor.getString(cursor.getColumnIndex("carNumber")));
			map.put("description",
					cursor.getString(cursor.getColumnIndex("description")));
			map.put("deviceId",
					cursor.getInt(cursor.getColumnIndex("deviceId")) + "");
			map.put("upLoadFlag",
					cursor.getInt(cursor.getColumnIndex("upLoadFlag")) + "");
			list.add(map);
		}
		return list;
	}

	@Override
	public List<HashMap<String, String>> findHistory(int userId) {
		// TODO Auto-generated method stub
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		Cursor cursor = db.rawQuery(
				"select * from stockout where userId=?",
				new String[] { userId + "" });
		while (cursor.moveToNext()) {
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("userId",
					cursor.getString(cursor.getColumnIndex("userId"))+"");
			map.put("storehouseId",
					cursor.getInt(cursor.getColumnIndex("storehouseId")) + "");
			map.put("number", cursor.getString(cursor.getColumnIndex("number")));
			map.put("contractId",
					cursor.getInt(cursor.getColumnIndex("contractId")) + "");
			map.put("driver", cursor.getString(cursor.getColumnIndex("driver")));
			map.put("carNumber",
					cursor.getString(cursor.getColumnIndex("carNumber")));
			map.put("description",
					cursor.getString(cursor.getColumnIndex("description")));
			map.put("deviceId",
					cursor.getInt(cursor.getColumnIndex("deviceId")) + "");
			map.put("upLoadFlag",
					cursor.getInt(cursor.getColumnIndex("upLoadFlag")) + "");
			list.add(map);
		}
		return list;
	}
	
	public void deleteHistoryById(int id) {

		//Cursor cursor = db.rawQuery("delete from history where id=?",
			//	new String[] { id + "" });
		db.delete("stockout", "id=?", new String[]{id+""});
	}

	public void updateUpLoadFlagHistoryById(int id) {

		ContentValues cv = new ContentValues();
		cv.put("upLoadFlag", 1+"");
		db.update("stockout", cv, "id = ?", new String[] { id + "" });
	}
}
