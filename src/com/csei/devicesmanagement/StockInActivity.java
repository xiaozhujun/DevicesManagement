package com.csei.devicesmanagement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import com.csei.application.MyApplication;
import com.csei.database.entity.service.imple.DeviceServiceImple;
import com.csei.database.entity.service.imple.HistoryServiceImple;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class StockInActivity extends Activity {
	private Spinner storeHouseSp;
	private ListView addItemListView;
	private Button addItemButton;
	private Button uploadButton;
	private Button saveButton;
	private Button cancelButton;
	private static int storeId;
	private View listviewLayout;
	private ImageView left_back;
	private ProgressDialog dialog;
	private Handler handler;
	private int upLoadFlag = 0;
	private static List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_stock_in);
		
		left_back = (ImageView) findViewById(R.id.iv_topbar_left_back_stockin);
		storeHouseSp = (Spinner) findViewById(R.id.tv_topbar_right_stockin);
		addItemListView = (ListView) findViewById(R.id.add_item_listview_stockin);
		addItemButton = (Button) findViewById(R.id.scan_Button_stockin);
		uploadButton = (Button) findViewById(R.id.uploadbutton_stockin);
		saveButton = (Button) findViewById(R.id.savebutton_stockin);
		cancelButton = (Button) findViewById(R.id.cancelbutton_stockin);
		listviewLayout = findViewById(R.id.listviewLayout_stockin);
		String[] spItems = new String[] { "武昌仓库", "汉口仓库", "汉阳仓库" };
		
		ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, spItems);
		spAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
		storeHouseSp.setAdapter(spAdapter);
		listViewRefresh();
		handler = new Handler() {
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 1:
					dialog.dismiss();
					Map<String, Object> itemMap = new HashMap<String, Object>();
					itemMap.put("deviceId", (int) (1 + Math.random()
							* (1000 - 1 + 1)));
					itemMap.put("deviceName", "塔吊");
					data.add(itemMap);
					listViewRefresh();
					break;
				case 2:
					dialog.dismiss();
					upLoadFlag = 1;
					saveHistory();
					Toast.makeText(getApplicationContext(), "上传成功", Toast.LENGTH_LONG).show();
					break;
				default:
					break;
				}
			};
		};
		
		left_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Back();
			}
		});

		storeHouseSp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				storeId = position + 1;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				storeId = 1;
			}
		});

		uploadButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				savaData();
				dialog = new ProgressDialog(StockInActivity.this);
				dialog.setTitle("提示");
				dialog.setMessage("正在上传...");
				dialog.show();
				
				uploadThread thread = new uploadThread();
				new Thread(thread).start();
			}
		});

		addItemButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog = new ProgressDialog(StockInActivity.this);
				dialog.setTitle("提示");
				dialog.setMessage("正在扫卡...");
				dialog.show();
				
				readCardThread myThread = new readCardThread();
				new Thread(myThread).start();
				
			}

		});

		cancelButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				data = new ArrayList<Map<String, Object>>();
				listViewRefresh();
			}
		});

		saveButton.setOnClickListener(new View.OnClickListener() {

			@SuppressLint("SimpleDateFormat")
			@Override
			public void onClick(View v) {
				savaData();
				saveHistory();
			}
		});
	}

	@SuppressLint("SimpleDateFormat") 
	protected void savaData() {
		Map<String, Object> itemMap = new HashMap<String, Object>();
		Iterator<Map<String, Object>> iterator = data.listIterator();
		try {
			while (iterator.hasNext()) {
				int userId = Integer.parseInt(getIntent().getStringExtra(
						"userId"));
				itemMap = iterator.next();
				int id = (Integer) itemMap.get("deviceId");
				String name = (String) itemMap.get("deviceName");
				// 这一步将数据鞋服数据库*****重要等级五颗星
				Map<String, String> device = new HashMap<String, String>();
				device.put("id", id + "");
				device.put("name", name);
				device.put("userId", userId + "");
				device.put("storeId", storeId + "");
				device.put("mainDeviceId", "0");
				device.put("stateFlag", "0");
				DeviceServiceImple deviceDao = new DeviceServiceImple(StockInActivity.this);
				
				if (deviceDao.findDeviceById(id)) {
					deviceDao.updateData((HashMap<String, String>) device);
				} else {
					deviceDao.addDevice((HashMap<String, String>) device);
				}
			}
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(),
					Toast.LENGTH_LONG).show();
		}
	}
	
	protected void saveHistory() {
		Map<String, Object> itemMap = new HashMap<String, Object>();
		Iterator<Map<String, Object>> iterator = data.listIterator();
		try {
			while (iterator.hasNext()) {
				int userId = Integer.parseInt(getIntent().getStringExtra(
						"userId"));
				itemMap = iterator.next();
				int id = (Integer) itemMap.get("deviceId");
				
				HistoryServiceImple historyDao = new HistoryServiceImple(
						StockInActivity.this);
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String timeString = df.format(new Date());
				Map<String, String> history = new HashMap<String, String>();
				history.put("userId", userId + "");
				history.put("deviceId", id + "");
				history.put("storeId", storeId + "");
				history.put("time", timeString + "");
				history.put("optionType", "0");
				history.put("mainDeviceId", "0");
				history.put("upLoadFlag", upLoadFlag+"");
				history.put("driverName", "");
				history.put("carNum", "");
				history.put("driverTel", "");
				historyDao.addHistory((HashMap<String, String>) history);
			}
			Toast.makeText(getApplicationContext(), "记录保存成功", Toast.LENGTH_SHORT).show();
			data.clear();
			listViewRefresh();
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(),
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onResume() {
		listViewRefresh();
		super.onResume();
	}

	public void listViewRefresh() {
		if(data.isEmpty()) {
			listviewLayout.setVisibility(4);
		}else {
			listviewLayout.setVisibility(0);
		}
		MyAdapter myAdapter = new MyAdapter(this, data,
				R.layout.layout_item_stock_in, new String[] { "deviceId",
						"deviceName" }, new int[] { R.id.deviceiditem,
						R.id.devicenameitem });
		addItemListView.setAdapter(myAdapter);
	}

	@Override
	public void onBackPressed() {
		Back();
	}
	
	private void Back() {
		if (data.isEmpty()) {
			finish();
		} else {
			Builder alertDialog = new AlertDialog.Builder(
					StockInActivity.this);
			alertDialog
					.setTitle("提示")
					.setMessage("是否放弃此次操作？")
					.setNegativeButton("取消", null)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(
										DialogInterface dialog,
										int which) {
									data.clear();
									listViewRefresh();
									finish();
								}
							})
					.setNeutralButton("保存",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(
										DialogInterface dialog,
										int which) {
									savaData();
									saveHistory();
									finish();
								}
							}).show();
		}

	}

	public int DeleteListviewItem(int position) {
		data.remove(position);
		listViewRefresh();
		return 0;
	}
	
	public class uploadThread implements Runnable {

		@Override
		public void run() {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Message msg = Message.obtain();
			msg.what = 2;
			handler.sendMessage(msg);
		}
		
	}
	
	public class readCardThread implements Runnable {

		@Override
		public void run() {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Message msg = Message.obtain();
			msg.what = 1; 
			handler.sendMessage(msg);
		}
	}
	
	public class MyAdapter extends SimpleAdapter {

		public MyAdapter(Context context, List<? extends Map<String, ?>> data,
				int resource, String[] from, int[] to) {
			super(context, data, resource, from, to);
		}

		
	

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = super.getView(position, convertView, parent);
			Button button = (Button) v.findViewById(R.id.deleteButton);
			button.setTag(position);
			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					int tag = (Integer) v.getTag();
					DeleteListviewItem(tag);
					Toast.makeText(getApplicationContext(), "删除成功",
							Toast.LENGTH_LONG).show();
				}
			});
			return v;
		}
	}
}
