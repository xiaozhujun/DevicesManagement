package com.csei.devicesmanagement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import com.csei.adapter.UninstallAdapter;
import com.csei.application.MyApplication;
import com.csei.database.entity.Device;
import com.csei.database.entity.Site;
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

public class UninstallActivity extends Activity {
	private ExpandableListView addItemListView;
	private Button addItemButton;
	private Button uploadButton;
	private Button saveButton;
	private Button cancelButton;
	private ImageView left_back;
	private ProgressDialog dialog;
	private Handler handler;
	private int upLoadFlag = 0;
	private ArrayList<Site> siteList;
	private ArrayList<Device> deviceList;
	private UninstallAdapter myAdapter;
	private static int isSelected = -1;
	private String[] groupName = new String[]{"选择工地","设备"};
	private int userId;
	private int storeId;
	private static int saveDataFlag = 0;
	private LinearLayout linearlayout_button;
	
	public void initData(){
		siteList = new ArrayList<Site>();
		deviceList = new ArrayList<Device>();
		
		Site site = new Site();
		site.setId(111);
		site.setName("武昌工地");
		site.setAddress("武汉市武昌区武汉理工大学");
		site.setTelephone("18062024445");
		siteList.add(site);
		
		Site site1 = new Site();
		site1.setId(111);
		site1.setName("汉口工地");
		site1.setAddress("武汉市武昌区武汉理工大学");
		site1.setTelephone("18062024445");
		siteList.add(site1);
		
		Site site2 = new Site();
		site2.setId(111);
		site2.setName("汉阳工地");
		site2.setAddress("武汉市武昌区武汉理工大学");
		site2.setTelephone("18062024445");
		siteList.add(site2);
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_stock_in);
		userId = Integer.parseInt(getIntent().getStringExtra("userId"));
		
		left_back = (ImageView) findViewById(R.id.iv_topbar_left_back_stockin);
		addItemListView = (ExpandableListView) findViewById(R.id.add_item_listview_stockin);
		addItemButton = (Button) findViewById(R.id.scan_Button_stockin);
		uploadButton = (Button) findViewById(R.id.uploadbutton_stockin);
		saveButton = (Button) findViewById(R.id.savebutton_stockin);
		cancelButton = (Button) findViewById(R.id.cancelbutton_stockin);
		linearlayout_button = (LinearLayout) findViewById(R.id.linearlayout_button);
		
		initData();
		myAdapter = new UninstallAdapter(UninstallActivity.this,groupName,siteList,deviceList,isSelected);
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
					myAdapter = new UninstallAdapter(UninstallActivity.this, groupName,siteList,deviceList,isSelected);
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
					dialog = new ProgressDialog(UninstallActivity.this);
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
				dialog = new ProgressDialog(UninstallActivity.this);
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
				myAdapter = new UninstallAdapter(UninstallActivity.this, groupName,siteList,deviceList,isSelected);
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
			Toast.makeText(getApplicationContext(), "请选择工地", Toast.LENGTH_SHORT).show();
			return 0;
		}else if(myAdapter.getDeviceList().isEmpty()){
			Toast.makeText(getApplicationContext(), "请点击扫卡添加设备", Toast.LENGTH_SHORT).show();
			return 0;
		}else{
			isSelected = myAdapter.getIsSelected();
			deviceList = myAdapter.getDeviceList();
			siteList = myAdapter.getStoreList();
			DeviceServiceImple deviceDao = new DeviceServiceImple(UninstallActivity.this);
			for(Device device:deviceList){
				int id = device.getId();
				String name = device.getName();
//				storeId = siteList.get(isSelected).getId();
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
		siteList = myAdapter.getStoreList();
		HistoryServiceImple historyDao = new HistoryServiceImple(UninstallActivity.this);
		for(Device device:deviceList){
			int id = device.getId();
			storeId = siteList.get(isSelected).getId();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = df.format(new Date());
			HashMap<String, String> historyMap = new HashMap<String, String>();
			historyMap = getHistoryMap(time, 3, userId, 0, id, 0, upLoadFlag, "", "", "");
			historyDao.addHistory(historyMap);
		}
		upLoadFlag = 0;
		saveDataFlag = 0;
		Toast.makeText(getApplicationContext(), "记录保存成功", Toast.LENGTH_SHORT).show();
		deviceList = myAdapter.getDeviceList();
		deviceList.clear();
		isSelected = -1;
		myAdapter = new UninstallAdapter(UninstallActivity.this, groupName,siteList,deviceList,isSelected);
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
			Builder alertDialog = new AlertDialog.Builder(UninstallActivity.this);
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
													myAdapter = new UninstallAdapter(UninstallActivity.this, groupName,siteList,deviceList,isSelected);
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
