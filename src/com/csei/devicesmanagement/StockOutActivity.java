package com.csei.devicesmanagement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import com.csei.adapter.StockListAdapter;
import com.csei.application.MyApplication;
import com.csei.database.entity.Device;
import com.csei.database.entity.Store;
import com.csei.database.entity.service.imple.DeviceServiceImple;
import com.csei.database.entity.service.imple.HistoryServiceImple;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class StockOutActivity extends Activity {
	private ExpandableListView addItemListView;
	private Button addItemButton;
	private Button uploadButton;
	private Button saveButton;
	private Button cancelButton;
	private ImageView left_back;
	private ProgressDialog dialog;
	private Handler handler;
	private int upLoadFlag = 0;
	private ArrayList<Store> storeList;
	private ArrayList<Device> deviceList;
	private StockListAdapter myAdapter;
	private static int isSelected = -1;
	private String[] groupName = new String[]{"选择仓库","设备"};
	private int userId;
	private int storeId;
	private static int saveDataFlag = 0;
	private LinearLayout linearlayout_button;
	
	public void initData(){
		storeList = new ArrayList<Store>();
		deviceList = new ArrayList<Device>();
		
		Store store = new Store();
		store.setId(111);
		store.setName("武昌仓库");
		store.setAddress("武汉市武昌区武汉理工大学");
		store.setTelephone("18062024445");
		storeList.add(store);
		
		Store store1 = new Store();
		store1.setId(222);
		store1.setName("汉口仓库");
		store1.setAddress("武汉市汉口区武汉理工大学");
		store1.setTelephone("18062024445");
		storeList.add(store1);
		
		Store store2 = new Store();
		store2.setId(333);
		store2.setName("汉阳仓库");
		store2.setAddress("武汉市汉阳区武汉理工大学");
		store2.setTelephone("18062024445");
		storeList.add(store2);
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_stock_out);
		userId = Integer.parseInt(getIntent().getStringExtra("userId"));
		
		left_back = (ImageView) findViewById(R.id.iv_topbar_left_back_stockout);
		addItemListView = (ExpandableListView) findViewById(R.id.add_item_listview_stockout);
		addItemButton = (Button) findViewById(R.id.scan_Button_stockout);
		uploadButton = (Button) findViewById(R.id.uploadbutton_stockout);
		saveButton = (Button) findViewById(R.id.savebutton_stockout);
		cancelButton = (Button) findViewById(R.id.cancelbutton_stockout);
		linearlayout_button = (LinearLayout) findViewById(R.id.linearlayout_button);
		
		initData();
		myAdapter = new StockListAdapter(StockOutActivity.this,groupName,storeList,deviceList,isSelected);
		linearlayout_button.setVisibility(ViewGroup.GONE);
		addItemListView.setAdapter(myAdapter);
		addItemListView.expandGroup(1);
		
		handler = new Handler() {
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 1:
					dialog.dismiss();
					Device device = new Device();
					device.setId((int)(1+Math.random()*(1000-1+1)));
					device.setName("塔吊");
					deviceList = myAdapter.getDeviceList();
					deviceList.add(device);
					myAdapter = new StockListAdapter(StockOutActivity.this, groupName,storeList,deviceList,isSelected);
					addItemListView.setAdapter(myAdapter);
					addItemListView.expandGroup(1);
					linearlayout_button.setVisibility(ViewGroup.VISIBLE);
					break;
				case 2:
					dialog.dismiss();
					Toast.makeText(getApplicationContext(), "上传成功", Toast.LENGTH_LONG).show();
					upLoadFlag = 1;
					if(saveDataFlag==1)
						saveHistory();
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

		uploadButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(saveData()==1){
					dialog = new ProgressDialog(StockOutActivity.this);
					dialog.setTitle("提示");
					dialog.setMessage("正在上传...");
					dialog.show();
					saveDataFlag = 1;
					uploadThread thread = new uploadThread();
					new Thread(thread).start();
				}
			}
		});

		addItemButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog = new ProgressDialog(StockOutActivity.this);
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
				deviceList = myAdapter.getDeviceList();
				deviceList.clear();
				isSelected = -1;
				myAdapter = new StockListAdapter(StockOutActivity.this, groupName,storeList,deviceList,isSelected);
				addItemListView.setAdapter(myAdapter);
				addItemListView.expandGroup(1);
				linearlayout_button.setVisibility(ViewGroup.GONE);
			}
		});

		saveButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				saveDataFlag = saveData();
				if(saveDataFlag==1)
					saveHistory();
			}
		});
	}

	protected int saveData() {
		//仅更新device表
		if(myAdapter.getIsSelected()==-1){
			Toast.makeText(getApplicationContext(), "请选择仓库", Toast.LENGTH_SHORT).show();
			return 0;
		}else if(myAdapter.getDeviceList().isEmpty()){
			Toast.makeText(getApplicationContext(), "请点击扫卡添加设备", Toast.LENGTH_SHORT).show();
			return 0;
		}else{
			isSelected = myAdapter.getIsSelected();
			deviceList = myAdapter.getDeviceList();
			storeList = myAdapter.getStoreList();
			DeviceServiceImple deviceDao = new DeviceServiceImple(StockOutActivity.this);
			for(Device device:deviceList){
				int id = device.getId();
				String name = device.getName();
				HashMap<String, String> deviceMap = new HashMap<String, String>();
				deviceMap = getDeviceMap(id, name, userId, 0, 0, 0);
				if (deviceDao.findDeviceById(id)) {
					deviceDao.updateData(deviceMap);
				} else {
					deviceDao.addDevice(deviceMap);
				}
			}
			return 1;
		}
	}
	
	public HashMap<String, String> getDeviceMap(int deviceId, String name,
			int userId, int storeId, int mainDeviceId, int stateFlag) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("id", deviceId + "");
		map.put("name", name);
		map.put("userId", userId + "");
		map.put("storeId", storeId + "");
		map.put("mainDeviceId", mainDeviceId + "");
		map.put("stateFlag", stateFlag + "");
		return map;
	}
	
	public HashMap<String, String> getHistoryMap(String time, int optionType,
			int userId, int storeId, int deviceId, int mainDeviceId,
			int upLoadFlag, String driverName, String carNum, String driverTel) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("deviceId", deviceId + "");
		map.put("time", time);
		map.put("userId", userId + "");
		map.put("storeId", storeId + "");
		map.put("mainDeviceId", mainDeviceId + "");
		map.put("upLoadFlag", upLoadFlag + "");
		map.put("optionType", optionType + "");
		map.put("driverName", driverName);
		map.put("carNum", carNum);
		map.put("driverTel", driverTel);
		return map;
	}

	@SuppressLint("SimpleDateFormat") 
	protected void saveHistory() {
		
		isSelected = myAdapter.getIsSelected();
		deviceList = myAdapter.getDeviceList();
		Collections.reverse(deviceList);
		storeList = myAdapter.getStoreList();
		HistoryServiceImple historyDao = new HistoryServiceImple(StockOutActivity.this);
		for(Device device:deviceList){
			int id = device.getId();
			storeId = storeList.get(isSelected).getId();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = df.format(new Date());
			HashMap<String, String> historyMap = new HashMap<String, String>();
			historyMap = getHistoryMap(time, 1, userId, storeId, id, 0, upLoadFlag, "", "", "");
			historyDao.addHistory(historyMap);
		}
		saveDataFlag = 0;
		isSelected = -1;
		Toast.makeText(getApplicationContext(), "记录保存成功", Toast.LENGTH_SHORT).show();
		deviceList = myAdapter.getDeviceList();
		deviceList.clear();
		myAdapter = new StockListAdapter(StockOutActivity.this, groupName,storeList,deviceList,isSelected);
		addItemListView.setAdapter(myAdapter);
		addItemListView.expandGroup(1);
		linearlayout_button.setVisibility(ViewGroup.GONE);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onBackPressed() {
		Back();
	}
	
	private void Back() {
		if (myAdapter.getDeviceList().isEmpty()) {
			finish();
		} else {
			Builder alertDialog = new AlertDialog.Builder(StockOutActivity.this);
			alertDialog
					.setTitle("提示")
					.setMessage("是否放弃此次操作？")
					.setNegativeButton("取消", null)
					.setPositiveButton("放弃",new DialogInterface.OnClickListener() {
												@Override
												public void onClick(DialogInterface dialog,int which) {
													deviceList = myAdapter.getDeviceList();
													deviceList.clear();
													isSelected = -1;
													myAdapter = new StockListAdapter(StockOutActivity.this, groupName,storeList,deviceList,isSelected);
													addItemListView.setAdapter(myAdapter);
													addItemListView.expandGroup(1);
													linearlayout_button.setVisibility(ViewGroup.GONE);
													finish();
												}})
					.setNeutralButton("保存",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(
										DialogInterface dialog,
										int which) {
									saveData();
									saveHistory();
									finish();
								}
							}).show();
		}

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
	
}
