package com.csei.devicesmanagement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.csei.adapter.InstallAdapter;
import com.csei.adapter.StockListAdapter;
import com.csei.adapter.TransportAdapter;
import com.csei.application.MyApplication;
import com.csei.client.CasClient;
import com.csei.database.entity.Device;
import com.csei.database.entity.Store;
import com.csei.database.entity.service.TrasnportService;
import com.csei.database.entity.service.imple.DeviceServiceDao;
import com.csei.database.entity.service.imple.TransportServiceDao;
import com.csei.devicesmanagement.R.id;
import com.csei.util.JSONUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class TransportActivity extends Activity {
	private ExpandableListView addItemListView;
	private Button addItemButton;
	private Button uploadButton;
	private Button saveButton;
	private Button cancelButton;
	private ImageView left_back;
	private Handler handler;
	private int upLoadFlag = 0;
	private ArrayList<Device> deviceList;
	private TransportAdapter myAdapter;
	private String[] groupName = new String[] { "运输信息", "设备信息" };
	private int userId;
	private static int saveDataFlag = 0;
	private LinearLayout linearlayout_button;

	private String driverName = "喻念";
	private String driverPhone = "110";
	private String carNumber = "27598799";
	private String address = "jifjakjglka";
	private String destination = "fdsjgkjalkg";
	private String[] image = new String[100];
	private ProgressDialog progressDialog;
	private Builder alertDialog;

	public void initData() {
		deviceList = new ArrayList<Device>();

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_transport);
		userId = Integer.parseInt(getIntent().getStringExtra("userId"));

		left_back = (ImageView) findViewById(R.id.iv_topbar_left_back_transport);
		addItemListView = (ExpandableListView) findViewById(R.id.add_item_listview_transport);
		addItemButton = (Button) findViewById(R.id.scan_Button_transport);
		uploadButton = (Button) findViewById(R.id.uploadbutton_transport);
		saveButton = (Button) findViewById(R.id.savebutton_transport);
		cancelButton = (Button) findViewById(R.id.cancelbutton_transport);
		linearlayout_button = (LinearLayout) findViewById(R.id.linearlayout_button);
		uploadDialogShow();
		initData();
		myAdapter = new TransportAdapter(TransportActivity.this, groupName,
				deviceList, driverName, driverPhone, carNumber, address,
				destination);
		linearlayout_button.setVisibility(ViewGroup.GONE);

		addItemListView.setAdapter(myAdapter);
		addItemListView.expandGroup(1);

		handler = new Handler() {
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {

				case 2:
					progressDialog.dismiss();
					upLoadFlag = 1;
					saveHistory();
					Toast.makeText(getApplicationContext(), "上传成功",
							Toast.LENGTH_LONG).show();
					break;
				case 3:
					progressDialog.dismiss();
					alertDialog.setTitle("提示").setMessage("图片上传成功").show();
					break;
				case 4:
					progressDialog.dismiss();
					alertDialog.setTitle("提示").setMessage("图片上传失败，请稍后重试")
							.show();
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
				if (saveData() == 1) {

					new Thread(new MyThread()).start();
					progressDialog.show();
				}
			}
		});

		addItemButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent it = new Intent(TransportActivity.this,
						ScanCodeActivity.class);
				startActivityForResult(it, 0);
			}

		});

		cancelButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				deviceList = myAdapter.getDeviceList();
				deviceList.clear();
				myAdapter = new TransportAdapter(TransportActivity.this,
						groupName, deviceList, myAdapter.getDriverName(),
						myAdapter.getDriverPhone(), myAdapter.getCarNumber(),
						myAdapter.getAddress(), myAdapter.getDestination());
				addItemListView.setAdapter(myAdapter);
				addItemListView.expandGroup(1);
				linearlayout_button.setVisibility(ViewGroup.GONE);
			}
		});

		saveButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				saveDataFlag = saveData();
				if (saveDataFlag == 1)
					saveHistory();
			}
		});
	}

	private void uploadDialogShow() {
		progressDialog = new ProgressDialog(this);
		progressDialog.setTitle("提示");
		progressDialog.setMessage("正在上传...");
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setCancelable(false);
		alertDialog = new Builder(this);
		alertDialog.setTitle("提示");
		alertDialog.setPositiveButton("确认",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						alertDialog.create().dismiss();
						finish();
					}
				});
	}

	protected int saveData() {
		// 仅更新device表
		if ("".equals(myAdapter.getDriverName())) {
			Toast.makeText(getApplicationContext(), "请输入司机姓名",
					Toast.LENGTH_SHORT).show();
			return 0;
		} else if (myAdapter.getDeviceList().isEmpty()) {
			Toast.makeText(getApplicationContext(), "请点击扫卡添加设备",
					Toast.LENGTH_SHORT).show();
			return 0;
		} else if ("".equals(myAdapter.getCarNumber())) {
			Toast.makeText(getApplicationContext(), "请输入车牌号",
					Toast.LENGTH_SHORT).show();
			return 0;
		} else if ("".equals(myAdapter.getDriverPhone())) {
			Toast.makeText(getApplicationContext(), "请输入司机电话",
					Toast.LENGTH_SHORT).show();
			return 0;
		} else if ("".equals(myAdapter.getAddress())) {
			Toast.makeText(getApplicationContext(), "请输入出发地",
					Toast.LENGTH_SHORT).show();
			return 0;
		} else if ("".equals(myAdapter.getDestination())) {
			Toast.makeText(getApplicationContext(), "请输入目的地",
					Toast.LENGTH_SHORT).show();
			return 0;
		} else {
			deviceList = myAdapter.getDeviceList();
			DeviceServiceDao deviceDao = new DeviceServiceDao(
					TransportActivity.this);
			for (Device device : deviceList) {
				int id = device.getId();
				String name = device.getName();
				HashMap<String, String> deviceMap = new HashMap<String, String>();
				deviceMap = getDeviceMap(id, name, userId, 0, 0, "0");
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
			int userId, int storehouseId, int mainDeviceId, String batchNumber) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("id", deviceId + "");
		map.put("name", name);
		map.put("userId", userId + "");
		map.put("storeId", storehouseId + "");
		map.put("mainDeviceId", mainDeviceId + "");
		map.put("batchNumber", batchNumber);
		return map;
	}

	public HashMap<String, String> getTransport(int userId, String driver,
			String telephone, String destination, String address, int deviceId,
			int uploadFlag, String image) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("userId", userId + "");
		map.put("driver", driver);
		map.put("telephone", telephone);
		map.put("destination", destination);
		map.put("address", address);
		map.put("deviceId", deviceId + "");
		map.put("uploadFlag", uploadFlag + "");
		map.put("image", image);

		return map;
	}

	@SuppressLint("SimpleDateFormat")
	protected void saveHistory() {

		deviceList = myAdapter.getDeviceList();
		Collections.reverse(deviceList);
		TransportServiceDao transportServiceDao = new TransportServiceDao(
				TransportActivity.this);
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

		for (int i = 0; i < deviceList.size(); i++) {
			int id = deviceList.get(i).getId();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = df.format(new Date());
			HashMap<String, String> transportMap = new HashMap<String, String>();
			transportMap = getTransport(userId, myAdapter.getDriverName(),
					myAdapter.getDriverPhone(), myAdapter.getDestination(),
					myAdapter.getAddress(), id, upLoadFlag, image[i]);
			list.add(transportMap);
		}
		transportServiceDao.add(list);
		upLoadFlag = 0;
		saveDataFlag = 0;
		Toast.makeText(getApplicationContext(), "记录保存成功", Toast.LENGTH_SHORT)
				.show();
		deviceList = myAdapter.getDeviceList();
		deviceList.clear();
		myAdapter = new TransportAdapter(TransportActivity.this, groupName,
				deviceList, myAdapter.getDriverName(),
				myAdapter.getDriverPhone(), myAdapter.getCarNumber(),
				myAdapter.getAddress(), myAdapter.getDestination());
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
			Builder alertDialog = new AlertDialog.Builder(
					TransportActivity.this);
			alertDialog
					.setTitle("提示")
					.setMessage("是否放弃此次操作？")
					.setNegativeButton("取消", null)
					.setPositiveButton("放弃",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									deviceList = myAdapter.getDeviceList();
									deviceList.clear();
									myAdapter = new TransportAdapter(
											TransportActivity.this, groupName,
											deviceList, myAdapter
													.getDriverName(), myAdapter
													.getDriverPhone(),
											myAdapter.getCarNumber(), myAdapter
													.getAddress(), myAdapter
													.getDestination());
									addItemListView.setAdapter(myAdapter);
									addItemListView.expandGroup(1);
									linearlayout_button
											.setVisibility(ViewGroup.GONE);
									finish();
								}
							})
					.setNeutralButton("保存",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									saveData();
									saveHistory();
									finish();
								}
							}).show();
		}

	}

	class MyThread implements Runnable {

		@Override
		public void run() {

			for (int j = 0; j < deviceList.size(); j++) {
				// 运输上传
				Message msg = Message.obtain();
				HashMap<String, String> map1 = new HashMap<String, String>();
				map1.put("deviceId", 111 + "");
				map1.put("driverName", "杨阳");
				map1.put("driverTel", "111");
				map1.put("destination", "武昌");
				map1.put("address", "汉口");
				int id = 0;
				try {
					id = JSONUtils.UploadTransport(
							getResources().getString(R.string.TRANSPORT_ADD),
							map1);
					if (id != 0) {
						msg.what = 2;
					} else {
						msg.what = 0;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				if (id != 0 && image[j] != null) {
					HashMap<String, String> params = new HashMap<String, String>();
					params.put("id", id + "");
					try {
						String result = CasClient.getInstance().doSendImage(
								getResources().getString(
										R.string.TRANSPORT_UPLOAD), image[j],
								params);
						Log.i("asdasd",
								"dasdasdasdasddddddddddddddddddddddddddddddddddddddddddd"
										+ result);
						int code = Integer.parseInt((new JSONObject(result)
								.getString("code")));
						Log.i("code", Integer.toString(code));
						if (code == 200) {
							msg.what = 3;
						} else {
							msg.what = 4;
						}
					} catch (Exception e) {
						msg.what = 4;
						e.printStackTrace();
					}
				}
				handler.sendMessage(msg);
			}

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		String code = data.getStringExtra("Code");
		if (code != null) {
			if (!code.substring(0, 4).equals("rent")) {
				Toast.makeText(TransportActivity.this, "请扫设备专用二维码！",
						Toast.LENGTH_SHORT).show();
				return;
			} else {

				Device device = new Device();
				device.setId(Integer.parseInt(code.split(",")[1]));
				device.setNumber(code.split(",")[2]);
				device.setName(code.split(",")[6]);
				device.setDeviceType(code.split(",")[6]);
				device.setMainDeviceId(Integer.parseInt(code.split(",")[7]));
				device.setBatchNumber(code.split(",")[4]);

				deviceList = myAdapter.getDeviceList();
				deviceList.add(device);
				myAdapter = new TransportAdapter(TransportActivity.this,
						groupName, deviceList, myAdapter.getDriverName(),
						myAdapter.getDriverPhone(), myAdapter.getCarNumber(),
						myAdapter.getAddress(), myAdapter.getDestination());
				addItemListView.setAdapter(myAdapter);
				addItemListView.expandGroup(1);
				linearlayout_button.setVisibility(ViewGroup.VISIBLE);

			}
		} else {
			image[data.getExtras().getInt("i")] = data.getExtras().getString(
					"image");

			if (data.getExtras().getString("image") != null) {
				Log.i("iTransport", data.getExtras().getInt("i") + "");
				Log.i("imageTransport", data.getExtras().getString("image"));
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}
}
