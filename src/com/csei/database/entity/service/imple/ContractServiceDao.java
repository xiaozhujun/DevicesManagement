package com.csei.database.entity.service.imple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.csei.database.DBOptions;
import com.csei.database.entity.service.ContractService;

public class ContractServiceDao implements ContractService {
	private DBOptions dbOptions;
	private SQLiteDatabase db;

	public ContractServiceDao(Context context) {
		dbOptions = DBOptions.getInstance(context);
		db = dbOptions.getWritableDatabase();
	}

	@Override
	public void addStore(List<HashMap<String, String>> list) {
		// TODO Auto-generated method stub
		db.beginTransaction();
		for (int i = 0; i < list.size(); i++) {
			db.execSQL(
					"insert into contrat(id,name,customerName,number,startTime,endTime,signTime,appId) values(?,?,?,?,?,?,?,?)",
					new Object[] { list.get(i).get("id"),
							list.get(i).get("name"),
							list.get(i).get("customerName"),
							list.get(i).get("number"),
							list.get(i).get("startTime"),
							list.get(i).get("endTime"),
							list.get(i).get("signTime"),
							list.get(i).get("appId") });
		}

		// 事务成功
		db.setTransactionSuccessful();
		// 关闭事务
		db.endTransaction();
	}

	@Override
	public int findIdByName(String name) {
		// TODO Auto-generated method stub
		Cursor cursor = db.rawQuery("select * from contrat where name=?",
				new String[] { name });
		while (cursor.moveToNext()) {
			return Integer.parseInt(cursor.getString(cursor
					.getColumnIndex("id")));
		}
		return 0;
	}

	@Override
	public List<HashMap<String, String>> findList() {
		// TODO Auto-generated method stub
		List<HashMap<String, String>> list = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> map = new HashMap<String,String>();
		Cursor cursor = db.rawQuery("select * from contrat", null);
		if (cursor.moveToFirst()) {
			while (cursor.moveToNext()) {
				map.put("id", cursor.getInt(cursor.getColumnIndex("id")) + "");
				map.put("name", cursor.getString(cursor.getColumnIndex("name")));
				map.put("customerName",
						cursor.getString(cursor.getColumnIndex("customerName")));
				map.put("number",
						cursor.getString(cursor.getColumnIndex("number")));
				map.put("startTime",
						cursor.getString(cursor.getColumnIndex("startTime")));
				map.put("endTime",
						cursor.getString(cursor.getColumnIndex("endTime")));
				map.put("signTime",
						cursor.getString(cursor.getColumnIndex("signTime")));
				map.put("appId", cursor.getInt(cursor.getColumnIndex("appId"))
						+ "");
				list.add(map);
			}
			return list;
		} else {
			return null;
		}
	}
}
