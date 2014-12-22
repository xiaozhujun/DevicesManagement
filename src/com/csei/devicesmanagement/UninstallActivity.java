package com.csei.devicesmanagement;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.csei.adapter.InstallAdapter;
import com.csei.adapter.UninstallAdapter;
import com.csei.application.MyApplication;
import com.csei.client.CasClient;
import com.csei.database.entity.Contract;
import com.csei.database.entity.Device;
import com.csei.database.entity.service.imple.DeviceServiceDao;
import com.csei.database.entity.service.imple.UninstallServiceDao;
import com.csei.devicesmanagement.TransportActivity.MyThread;
import com.csei.util.JSONUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
	private ArrayList<Contract> contractList;
	private ArrayList<Device> deviceList;
	private UninstallAdapter myAdapter;
	private int contractSelected = -1;
	private String[] groupName = new String[] { "合同信息", "卸载信息", "设备信息" };
	private int userId;
	private int contractId;
	private int saveDataFlag = 0;
	private LinearLayout linearlayout_button;

	private String removeMan = "喻念";
	private String removeStatus = "removeStatus";
	private String[] image = new String[100];
	private ProgressDialog progressDialog;
	private Builder alertDialog;

	public void initData() {
		contractList = new ArrayList<Contract>();
		deviceList = new ArrayList<Device>();

		Contract contract = new Contract();
		contract.setId(111);
		contract.setCustomerName("喻念");
		contract.setStartTime("2014.12.12");
		contract.setEndTime("2014.12.13");
		contract.setSignTime("2014.1.1");
		contractList.add(contract);

		Contract contract2 = new Contract();
		contract2.setId(222);
		contract2.setCustomerName("喻念");
		contract2.setStartTime("2014.12.12.2");
		contract2.setEndTime("2014.12.13.2");
		contract2.setSignTime("2014.1.1.2");
		contractList.add(contract2);

		Contract contract3 = new Contract();
		contract3.setId(333);
		contract3.setCustomerName("喻念");
		contract3.setStartTime("2014.12.12.3");
		contract3.setEndTime("2014.12.13.3");
		contract3.setSignTime("2014.1.1.3");
		contractList.add(contract3);
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
		uploadDialogShow();

		initData();
		myAdapter = new UninstallAdapter(UninstallActivity.this, groupName,
				contractList, deviceList, removeMan, removeStatus,
				contractSelected);
		linearlayout_button.setVisibility(ViewGroup.GONE);

		addItemListView.setAdapter(myAdapter);
		addItemListView.expandGroup(2);

		handler = new Handler() {
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {

				case 2:
					dialog.dismiss();
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

				Intent it = new Intent(UninstallActivity.this,
						ScanCodeActivity.class);
				startActivityForResult(it, 0);
			}

		});

		cancelButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				deviceList = myAdapter.getDeviceList();
				deviceList.clear();
				contractSelected = -1;
				myAdapter = new UninstallAdapter(UninstallActivity.this,
						groupName, contractList, deviceList, "", "",
						contractSelected);
				addItemListView.setAdapter(myAdapter);
				addItemListView.expandGroup(2);
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
		if (myAdapter.getContractSelected() == -1) {
			Toast.makeText(getApplicationContext(), "请选择合同", Toast.LENGTH_SHORT)
					.show();
			return 0;
		} else if (myAdapter.getDeviceList().isEmpty()) {
			Toast.makeText(getApplicationContext(), "请点击扫卡添加设备",
					Toast.LENGTH_SHORT).show();
			return 0;
		} else {
			// contractSelected = myAdapter.getContractSelected();
			// deviceList = myAdapter.getDeviceList();
			// DeviceServiceDao deviceDao = new
			// DeviceServiceDao(UninstallActivity.this);
			// for(Device device:deviceList){
			// int id = device.getId();
			// String name = device.getName();
			// HashMap<String, String> deviceMap = new HashMap<String,
			// String>();
			// deviceMap = getDeviceMap(id, name, userId, 0, 0, "0");
			// if (deviceDao.findDeviceById(id)) {
			// deviceDao.updateData(deviceMap);
			// } else {
			// deviceDao.addDevice(deviceMap);
			// }
			// }
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

	public HashMap<String, String> getUninstall(int userId, int contractId,
			String removeMan, String removeStatus, int deviceId,
			int upLoadFlag, String image) {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("userId", userId + "");
		map.put("contractId", contractId + "");
		map.put("removeMan", removeMan);
		map.put("removeStatus", removeStatus);
		map.put("deviceId", deviceId + "");
		map.put("upLoadFlag", upLoadFlag + "");
		map.put("image", image);
		return map;
	}

	@SuppressLint("SimpleDateFormat")
	protected void saveHistory() {

		contractId = myAdapter.getContractSelected();
		deviceList = myAdapter.getDeviceList();
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		Collections.reverse(deviceList);
		UninstallServiceDao uninstallServiceDao = new UninstallServiceDao(
				UninstallActivity.this);

		for (int i = 0; i < deviceList.size(); i++) {
			int id = deviceList.get(i).getId();
			contractId = contractList.get(contractId).getId();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String time = df.format(new Date());
			HashMap<String, String> uninstallMap = new HashMap<String, String>();
			uninstallMap = getUninstall(userId, contractId,
					myAdapter.getRemoveMan(), myAdapter.getRemoveStatus(), id,
					upLoadFlag, image[i]);
			list.add(uninstallMap);
		}

		uninstallServiceDao.add(list);
		upLoadFlag = 0;
		saveDataFlag = 0;
		Toast.makeText(getApplicationContext(), "记录保存成功", Toast.LENGTH_SHORT)
				.show();
		deviceList = myAdapter.getDeviceList();
		deviceList.clear();
		contractSelected = -1;
		myAdapter = new UninstallAdapter(UninstallActivity.this, groupName,
				contractList, deviceList, "", "", contractSelected);
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
			Builder alertDialog = new AlertDialog.Builder(
					UninstallActivity.this);
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
									contractSelected = -1;
									myAdapter = new UninstallAdapter(
											UninstallActivity.this, groupName,
											contractList, deviceList, "", "",
											contractSelected);
									addItemListView.setAdapter(myAdapter);
									addItemListView.expandGroup(2);
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
				// 卸载上传
				Message msg = Message.obtain();
				HashMap<String, String> map1 = new HashMap<String, String>();

				map1.put("contractId", "1");
				map1.put("removeMan", "sadsa2222");
				map1.put("removeStatus", "未完成");
				map1.put("deviceId", "1");
				Log.i("map", String.valueOf(map1));
				int id = 0;
				try {
					id = JSONUtils.UploadUninstall(
							getResources().getString(R.string.UNINSTALL_ADD),
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
										R.string.UNINSTALL_UPLOAD), image[j],
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
				Toast.makeText(UninstallActivity.this, "请扫设备专用二维码！",
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
				myAdapter = new UninstallAdapter(UninstallActivity.this,
						groupName, contractList, deviceList,
						myAdapter.getRemoveMan(), myAdapter.getRemoveStatus(),
						myAdapter.getContractSelected());
				addItemListView.setAdapter(myAdapter);
				addItemListView.expandGroup(2);
				linearlayout_button.setVisibility(ViewGroup.VISIBLE);

			}
		} else {
			image[data.getExtras().getInt("i")] = data.getExtras().getString(
					"image");

			if (data.getExtras().getString("image") != null) {
				Log.i("iUninstall", data.getExtras().getInt("i") + "");
				Log.i("imageUninstall", data.getExtras().getString("image"));
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}
}
