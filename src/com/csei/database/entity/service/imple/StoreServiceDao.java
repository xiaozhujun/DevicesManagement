package com.csei.database.entity.service.imple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.csei.database.DBOptions;
import com.csei.database.entity.service.StoreService;

public class StoreServiceDao implements StoreService {
	private DBOptions dbOptions;
	private SQLiteDatabase db;

	public StoreServiceDao(Context context) {
		dbOptions = DBOptions.getInstance(context);
		db = dbOptions.getWritableDatabase();
	}

	@Override
	public void addStore(List<HashMap<String, String>> list) {
		// TODO Auto-generated method stub
		db.beginTransaction();
		for (int i = 0; i < list.size(); i++) {
			db.execSQL(
					"insert into store(id,name,description,createTime,address,linkMan,telephone,appId) values(?,?,?,?,?,?,?,?)",
					new Object[] { list.get(i).get("id"),
							list.get(i).get("name"),
							list.get(i).get("description"),
							list.get(i).get("createTime"),
							list.get(i).get("address"),
							list.get(i).get("linkMan"),
							list.get(i).get("telephone"),
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
		Cursor cursor = db.rawQuery("select * from store where name=?",
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
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		HashMap<String, String> map = new HashMap<String,String>();
		Cursor cursor = db.rawQuery("select * from store", null);
		if (cursor.moveToFirst()) {
			while (cursor.moveToNext()) {
				map.put("id", cursor.getInt(cursor.getColumnIndex("id")) + "");
				map.put("name", cursor.getString(cursor.getColumnIndex("name")));
				map.put("description",
						cursor.getString(cursor.getColumnIndex("description")));
				map.put("createTime",
						cursor.getString(cursor.getColumnIndex("createTime")));
				map.put("address",
						cursor.getString(cursor.getColumnIndex("address")));
				map.put("linkMan",
						cursor.getString(cursor.getColumnIndex("linkMan")));
				map.put("telephone",
						cursor.getString(cursor.getColumnIndex("telephone")));
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
