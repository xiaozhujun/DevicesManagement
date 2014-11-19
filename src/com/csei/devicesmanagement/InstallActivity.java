package com.csei.devicesmanagement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import com.csei.adapter.InstallAdapter;
import com.csei.adapter.StockListAdapter;
import com.csei.application.MyApplication;
import com.csei.database.entity.Device;
import com.csei.database.entity.Site;
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

public class InstallActivity extends Activity {
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
	private InstallAdapter myAdapter;
	private static int isSelected = -1;
	private String[] groupName = new String[]{"工地","主设备","设备"};
	private int userId;
	private int siteId;
	private static int saveDataFlag = 0;
	private LinearLayout linearlayout_button;
	private Device mainDevice;
	
	public void initData(){
		siteList = new ArrayList<Site>();
		deviceList = new ArrayList<Device>();
		mainDevice = new Device();
		mainDevice.setId(99999);
		mainDevice.setName("hahkgakjglda");
		
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
		
		Device device = new Device();
		device.setId(333);
		device.setName("kgjakgkda");
		deviceList.add(device);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_install);
		userId = Integer.parseInt(getIntent().getStringExtra("userId"));
		
		left_back = (ImageView) findViewById(R.id.iv_topbar_left_back_install);
		addItemListView = (ExpandableListView) findViewById(R.id.add_item_listview_install);
		addItemButton = (Button) findViewById(R.id.scan_Button_install);
		uploadButton = (Button) findViewById(R.id.uploadbutton_install);
		saveButton = (Button) findViewById(R.id.savebutton_install);
		cancelButton = (Button) findViewById(R.id.cancelbutton_install);
		linearlayout_button = (LinearLayout) findViewById(R.id.linearlayout_button);
		
		initData();
		myAdapter = new InstallAdapter(InstallActivity.this,groupName,siteList,deviceList,isSelected,mainDevice);
		linearlayout_button.setVisibility(ViewGroup.GONE);
		
		addItemListView.setAdapter(myAdapter);
		addItemListView.expandGroup(2);
		
		handler = new Handler() {
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 1:
					dialog.dismiss();
					final int deviceId = (int)(1+Math.random()*(1000-1+1));
					if (deviceId<200) {
						mainDevice = myAdapter.getMainDevice();
						if(mainDevice==null){
							mainDevice = new Device();
							mainDevice.setId(deviceId);
							mainDevice.setName("这是主设备");
							myAdapter.setMainDevice(mainDevice);
							addItemListView.expandGroup(1);
						}else{
							Builder alertDialog = new AlertDialog.Builder(InstallActivity.this);
							alertDialog.setTitle("提示").setMessage("是否更换主设备？")
							.setNegativeButton("取消", null)
							.setPositiveButton("更换",new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,int which) {
									mainDevice = new Device();
									mainDevice.setId(deviceId);
									mainDevice.setName("这是主设备");
									myAdapter.setMainDevice(mainDevice);
									addItemListView.expandGroup(1);
								}}).show();
						}
					}else{
						Device device = new Device();
						device.setId(deviceId);
						device.setName("塔吊");
						deviceList = myAdapter.getDeviceList();
						deviceList.add(device);
						mainDevice = myAdapter.getMainDevice();
						myAdapter = new InstallAdapter(InstallActivity.this,groupName,siteList,deviceList,isSelected,mainDevice);
						addItemListView.setAdapter(myAdapter);
						addItemListView.expandGroup(2);
						linearlayout_button.setVisibility(ViewGroup.VISIBLE);
					}
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
					dialog = new ProgressDialog(InstallActivity.this);
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
				dialog = new ProgressDialog(InstallActivity.this);
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
				myAdapter = new InstallAdapter(InstallActivity.this, groupName,siteList,deviceList,isSelected,mainDevice);
				addItemListView.setAdapter(myAdapter);
				addItemListView.expandGroup(2);
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
			siteList = myAdapter.getSiteList();
			DeviceServiceImple deviceDao = new DeviceServiceImple(InstallActivity.this);
			for(Device device:deviceList){
				int id = device.getId();
				String name = device.getName();
				siteId = siteList.get(isSelected).getId();
				HashMap<String, String> deviceMap = new HashMap<String, String>();
				mainDevice = myAdapter.getMainDevice();
				deviceMap = getDeviceMap(id, name, userId, 0, mainDevice.getId(), 0);
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
		siteList = myAdapter.getSiteList();
		HistoryServiceImple historyDao = new HistoryServiceImple(InstallActivity.this);
		for(Device device:deviceList){
			int id = device.getId();
			siteId = siteList.get(isSelected).getId();
			mainDevice = myAdapter.getMainDevice();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = df.format(new Date());
			HashMap<String, String> historyMap = new HashMap<String, String>();
			historyMap = getHistoryMap(time, 2, userId, 0, id, mainDevice.getId(), upLoadFlag, "", "", "");
			historyDao.addHistory(historyMap);
		}
		upLoadFlag = 0;
		saveDataFlag = 0;
		Toast.makeText(getApplicationContext(), "记录保存成功", Toast.LENGTH_SHORT).show();
		deviceList = myAdapter.getDeviceList();
		deviceList.clear();
		isSelected = -1;
		myAdapter = new InstallAdapter(InstallActivity.this, groupName,siteList,deviceList,isSelected,mainDevice);
		addItemListView.setAdapter(myAdapter);
		addItemListView.expandGroup(2);
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
			Builder alertDialog = new AlertDialog.Builder(InstallActivity.this);
			alertDialog
					.setTitle("提示")
					.setMessage("是否放弃此次操作？")
					.setNegativeButton("取消", null)
					.setPositiveButton("放弃",new DialogInterface.OnClickListener() {
												@Override
												public void onClick(DialogInterface dialog,int which) {
													deviceList = myAdapter.getDeviceList();
													mainDevice = new Device();
													deviceList.clear();
													isSelected = -1;
													myAdapter = new InstallAdapter(InstallActivity.this, groupName,siteList,deviceList,isSelected,mainDevice);
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
