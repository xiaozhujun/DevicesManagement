package com.csei.devicesmanagement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.csei.application.MyApplication;
import com.csei.database.entity.service.imple.DeviceServiceImple;
import com.csei.database.entity.service.imple.HistoryServiceImple;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class TransportActivity extends Activity {
	private ListView addItemListView;
	private Button uploadButton;
	private Button saveButton;
	private Button cancelButton;
	private static int storeId;
	private View listviewLayout;
	private ImageView left_back;
	private Button scanDevice;
	private ProgressDialog dialog;
	private Handler handler;
	private int uploadFlag = 0;
	private static List<Map<String, Object>> data = new ArrayList<Map<String,Object>>();
	private String driverName="";
	private String carNum="";
	private String driverTel="";
	private EditText et_driverName;
	private EditText et_driverTel;
	private EditText et_carNum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_transport);
		
		addItemListView = (ListView) findViewById(R.id.add_item_listview_transport);
		uploadButton = (Button) findViewById(R.id.uploadbutton_transport);
		saveButton = (Button) findViewById(R.id.savebutton_transport);
		cancelButton = (Button) findViewById(R.id.cancelbutton_transport);
		listviewLayout = findViewById(R.id.listviewLayout_transport);
		left_back = (ImageView) findViewById(R.id.iv_topbar_left_back_transport);
		scanDevice = (Button) findViewById(R.id.scan_Button_transport);
		et_driverName=(EditText) findViewById(R.id.input_driverName_transport);
		et_driverTel=(EditText) findViewById(R.id.input_driverTel_transport);
		et_carNum=(EditText) findViewById(R.id.input_carNum_transport);
		
		et_carNum.setText("鄂A5555");
		et_driverName.setText("小明");
		et_driverTel.setText("027-88888888");
		
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
					uploadFlag = 1;
					saveHistory();
					Toast.makeText(getApplicationContext(), "上传成功", Toast.LENGTH_LONG).show();
					break;
				default:
					break;
				}
			};
		};
		scanDevice.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				driverName=et_driverName.getText().toString().trim();
				driverTel=et_driverTel.getText().toString().trim();
				carNum=et_carNum.getText().toString().trim();
				if ("".equals(driverName)) {
					Toast.makeText(getApplicationContext(), "请输入司机姓名", Toast.LENGTH_SHORT).show();
				}else if ("".equals(driverTel)) {
					Toast.makeText(getApplicationContext(), "请输入司机号码", Toast.LENGTH_SHORT).show();
				}else if ("".equals(carNum)) {
					Toast.makeText(getApplicationContext(), "请输入车牌号", Toast.LENGTH_SHORT).show();
				}else {
					dialog = new ProgressDialog(TransportActivity.this);
					dialog.setTitle("提示");
					dialog.setMessage("正在扫卡...");
					dialog.show();
					
					readCardThread thread = new readCardThread();
					new Thread(thread).start();
				}
				
			}
		});
		
		left_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				back();
			}
		});
		
		uploadButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog = new ProgressDialog(TransportActivity.this);
				dialog.setTitle("提示");
				dialog.setMessage("正在上传...");
				dialog.show();
				savaData();
				
				uploadThread thread = new uploadThread();
				new Thread(thread).start();
			}
		});
		
		
		cancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				data = new ArrayList<Map<String,Object>>();
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
	
	private void back(){
		if (data.isEmpty()) {
			finish();
		} else {
			Builder alertDialog = new AlertDialog.Builder(
					TransportActivity.this);
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
									data = new ArrayList<Map<String, Object>>();
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
	
	private void savaData(){
		Map<String, Object> itemMap = new HashMap<String, Object>();
		Iterator<Map<String, Object>> iterator = data.listIterator();
		try {
			while(iterator.hasNext()){
				int userId = Integer.parseInt(getIntent().getStringExtra("userId"));
				itemMap = iterator.next();
				int id = (Integer) itemMap.get("deviceId");
				String name = (String) itemMap.get("deviceName");
				Map<String, String> device = new HashMap<String, String>();
				device.put("id", id+"");
				device.put("name", name+"");
				device.put("userId", userId+"");
				device.put("storeId", "0"); 
				device.put("mainDeviceId", "0");
				device.put("stateFlag", "0");
				DeviceServiceImple deviceDao = new DeviceServiceImple(TransportActivity.this);
				
				if(deviceDao.findDeviceById(id)){
					deviceDao.updateData((HashMap<String, String>) device);
				}else{
					deviceDao.addDevice((HashMap<String, String>) device);
				}
				
				Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_LONG).show();
			}			
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
		}
		
		
	}
	
	private void saveHistory() {
		Map<String, Object> itemMap = new HashMap<String, Object>();
		Iterator<Map<String, Object>> iterator = data.listIterator();
		try {
			while(iterator.hasNext()){
				int userId = Integer.parseInt(getIntent().getStringExtra("userId"));
				itemMap = iterator.next();
				int id = (Integer) itemMap.get("deviceId");
				
				HistoryServiceImple historyDao = new HistoryServiceImple(TransportActivity.this);
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String timeString = df.format(new Date());
				Map<String, String> history = new HashMap<String, String>();
				history.put("userId", userId+"");
				history.put("deviceId", id+"");
				history.put("storeId", storeId+"");
				history.put("time", timeString+"");
				history.put("optionType", "4");
				history.put("mainDeviceId", "0");
				history.put("upLoadFlag", uploadFlag+"");
				history.put("driverName", driverName);
				history.put("carNum", carNum);
				history.put("driverTel", driverTel);
				historyDao.addHistory((HashMap<String, String>) history);
			}			
			Toast.makeText(getApplicationContext(), "记录保存成功", Toast.LENGTH_SHORT).show();
			data.clear();
			listViewRefresh();
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
		}
		
		
	}
	@Override
	protected void onResume() {
	    listViewRefresh();
		super.onResume();
	}
	public void listViewRefresh(){
		Iterator< Map<String, Object>> iterator = data.iterator();
		if(iterator.hasNext()){
			listviewLayout.setVisibility(0);
		}else{
			listviewLayout.setVisibility(4);
		}
		MyAdapter myAdapter = new MyAdapter(this,data,R.layout.layout_item_stock_in,new String[]{"deviceId","deviceName"},new int[]{R.id.deviceiditem,R.id.devicenameitem});
		addItemListView.setAdapter(myAdapter);
	}
	@Override
	public void onBackPressed() {
		back();
	}
	
	public int DeleteListviewItem(int position){
		data.remove(position);
		listViewRefresh();
		return 0;		
	}
	
	private class uploadThread implements Runnable {
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
	
	public class MyAdapter extends SimpleAdapter{

		public MyAdapter(Context context, List<? extends Map<String, ?>> data,
				int resource, String[] from, int[] to) {
			super(context, data, resource, from, to);
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v=super.getView(position, convertView, parent);
			Button button = (Button) v.findViewById(R.id.deleteButton);
			button.setTag(position);
			button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					int tag = (Integer) v.getTag();
					DeleteListviewItem(tag);
					Toast.makeText(getApplicationContext(), "删除成功", Toast.LENGTH_LONG).show();
				}
			});
			return v;
		}
	}
}
