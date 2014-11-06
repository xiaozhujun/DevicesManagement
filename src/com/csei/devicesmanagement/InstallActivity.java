package com.csei.devicesmanagement;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.csei.database.entity.service.imple.DeviceServiceImple;
import com.csei.database.entity.service.imple.HistoryServiceImple;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class InstallActivity extends Activity {

	private ListView listView;
	private List<Map<String, Object>> list;
	private EditText input_DeviceId;
	private EditText input_MainDeviceId;
	private Button add;
	private String deviceId;
	private String mainDeviceId;
	private LinearLayout listAndBottom_bar_install;
	private MyAdapter adapter;
	private Button save;
	private Button upload;
	private Button cancel;
	private int saveFlag = 0;
	private DeviceServiceImple deviceDao;
	private HistoryServiceImple historyDao;
	private int userId;
	private String time;
	private ImageView left_back;
	private Button scanDevice;
	private Button scanMaindevice;
	private ProgressDialog dialog;
	private Handler handler;
	private static int uploadFlag = 0;
	private Spinner sp_install;
	private static int siteId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_install);

		listView = (ListView) findViewById(R.id.lv_install);

		Intent intent = getIntent();

		userId = Integer.parseInt(intent.getStringExtra("userId"));

		listAndBottom_bar_install = (LinearLayout) findViewById(R.id.listAndBottom_bar_install);

		list = new ArrayList<Map<String, Object>>();
		input_DeviceId = (EditText) findViewById(R.id.input_deviceId_install);
		input_MainDeviceId = (EditText) findViewById(R.id.input_mainDeviceId_install);

		save = (Button) findViewById(R.id.save_install);
		upload = (Button) findViewById(R.id.upload_install);
		cancel = (Button) findViewById(R.id.cancel_install);
		left_back = (ImageView) findViewById(R.id.iv_topbar_left_back_install);
		scanDevice = (Button) findViewById(R.id.scan_Button_deviceId_install);
		scanMaindevice = (Button) findViewById(R.id.scan_Button_mainDeviceId_install);
		String[] spItems = new String[] { "工地1", "工地2", "工地3" };

		ArrayAdapter<String> spAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, spItems);
		spAdapter
				.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
		sp_install.setAdapter(spAdapter);

		deviceDao = new DeviceServiceImple(InstallActivity.this);
		historyDao = new HistoryServiceImple(InstallActivity.this);

		handler = new Handler() {
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				switch (msg.what) {
				case 1:
					dialog.dismiss();
					input_MainDeviceId.setText((int) (1 + Math.random()
							* (1000 - 1 + 1))
							+ "");
					break;
				case 2:
					dialog.dismiss();
					input_DeviceId.setText((int) (1 + Math.random()
							* (1000 - 1 + 1))
							+ "");
					break;
				case 3:
					dialog.dismiss();
					uploadFlag = 1;
					saveHistory();
					Toast.makeText(getApplicationContext(), "上传成功",
							Toast.LENGTH_LONG).show();
					for (int i = list.size() - 1; i >= 0; i--) {
						list.remove(i);
					}
					adapter.notifyDataSetChanged();
					if (list.size() == 0) {
						listAndBottom_bar_install.setVisibility(View.INVISIBLE);
					}
					break;
				default:
					break;
				}
			};
		};
		sp_install.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				siteId = position + 1;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				siteId = 1;
			}
		});
		scanDevice.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog = new ProgressDialog(InstallActivity.this);
				dialog.setTitle("提示");
				dialog.setMessage("正在扫卡...");
				dialog.show();

				readDeviceThread thread = new readDeviceThread();
				new Thread(thread).start();

			}

		});

		scanMaindevice.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				dialog = new ProgressDialog(InstallActivity.this);
				dialog.setTitle("提示");
				dialog.setMessage("正在扫卡...");
				dialog.show();

				readMainDeviceThread thread = new readMainDeviceThread();
				new Thread(thread).start();
			}
		});

		left_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				OnBack();
			}
		});

		save.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				saveFunction();
				uploadFlag = 0;
				saveHistory();
				for (int i = list.size() - 1; i >= 0; i--) {
					list.remove(i);
				}
				adapter.notifyDataSetChanged();
				if (list.size() == 0) {
					listAndBottom_bar_install.setVisibility(View.INVISIBLE);
				}
			}
		});
		upload.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (saveFlag == 0) {
					saveFunction();
					saveHistory();
				} else {
					dialog = new ProgressDialog(InstallActivity.this);
					dialog.setTitle("提示");
					dialog.setMessage("正在上传...");
					dialog.show();
					saveFunction();

					uploadThread thread = new uploadThread();
					new Thread(thread).start();
				}
			}
		});
		cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for (int i = list.size() - 1; i >= 0; i--) {
					list.remove(i);
				}
				adapter.notifyDataSetChanged();
				if (list.size() == 0) {
					listAndBottom_bar_install.setVisibility(View.INVISIBLE);
				}
			}
		});

		adapter = new MyAdapter(this, list);
		add = (Button) findViewById(R.id.add_device_install);
		add.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				deviceId = input_DeviceId.getText().toString().trim();
				mainDeviceId = input_MainDeviceId.getText().toString().trim();
				if ("".equals(deviceId)) {
					Toast.makeText(getApplicationContext(), "请点击添加设备",
							Toast.LENGTH_SHORT).show();
				} else if ("".equals(mainDeviceId)) {
					Toast.makeText(getApplicationContext(), "请点击添加主设备",
							Toast.LENGTH_SHORT).show();
				} else {
					HashMap<String, Object> map = new HashMap<String, Object>();
					map.put("deviceId", deviceId);
					map.put("mainDeviceId", mainDeviceId);
					list.add(map);
					input_DeviceId.setText("");
					input_MainDeviceId.setText("");
					listView.setAdapter(adapter);
					listAndBottom_bar_install.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	@Override
	public void onBackPressed() {
		OnBack();
	}

	private void OnBack() {
		if (list.isEmpty()) {
			finish();
		} else {
			Builder alertDialog = new AlertDialog.Builder(InstallActivity.this);
			alertDialog
					.setTitle("提示")
					.setMessage("是否放弃此次操作？")
					.setNegativeButton("取消", null)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									adapter.notifyDataSetChanged();
									finish();
								}
							})
					.setNeutralButton("保存",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									saveFunction();
									saveHistory();
									finish();
								}
							}).show();
		}
	}

	public void saveHistory() {
		for (int i = 0; i < list.size(); i++) {
			HashMap<String, Object> map1 = new HashMap<String, Object>();
			map1 = (HashMap<String, Object>) list.get(i);
			int deviceIdInt;
			int mainDeviceIdInt;

			deviceIdInt = Integer.parseInt((String) map1.get("deviceId"));
			mainDeviceIdInt = Integer.parseInt((String) map1
					.get("mainDeviceId"));

			HashMap<String, String> mapHistory = new HashMap<String, String>();
			SimpleDateFormat dateFormat = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			Date curDate = new Date(System.currentTimeMillis());
			time = dateFormat.format(curDate);
			mapHistory = getHistoryMap(time, 2, userId, 0, deviceIdInt,
					mainDeviceIdInt, uploadFlag, "", "", "");
			historyDao.addHistory(mapHistory);
			if (historyDao.findRecordByDeviceId(deviceIdInt + "")) {

				Toast.makeText(getApplicationContext(), "内容已保存",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	public void saveFunction() {
		for (int i = 0; i < list.size(); i++) {
			HashMap<String, Object> map1 = new HashMap<String, Object>();
			map1 = (HashMap<String, Object>) list.get(i);
			int deviceIdInt;
			int mainDeviceIdInt;

			deviceIdInt = Integer.parseInt((String) map1.get("deviceId"));
			mainDeviceIdInt = Integer.parseInt((String) map1
					.get("mainDeviceId"));

			HashMap<String, String> map = new HashMap<String, String>();
			map = getDeviceMap(deviceIdInt, null, userId, 0, mainDeviceIdInt, 0);
			if (deviceDao.findDeviceById(deviceIdInt)) {
				deviceDao.updateData(map);
			} else {
				deviceDao.addDevice(map);
			}
		}
		saveFlag = 1;

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

	class uploadThread implements Runnable {

		@Override
		public void run() {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Message msg = Message.obtain();
			msg.what = 3;
			handler.sendMessage(msg);
		}
	}

	class readMainDeviceThread implements Runnable {

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

	class readDeviceThread implements Runnable {

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

	class MyAdapter extends BaseAdapter {
		private List<Map<String, Object>> list;
		private Context context;
		private LayoutInflater inflater;

		public MyAdapter(Context context, List<Map<String, Object>> list) {
			// TODO Auto-generated constructor stub
			this.list = list;
			this.inflater = LayoutInflater.from(context);
			this.context = context;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.listview_item_main,
						null);
				holder.item_tv_left = (TextView) convertView
						.findViewById(R.id.item_tv_left);
				holder.item_tv_right = (TextView) convertView
						.findViewById(R.id.item_tv_right);
				holder.delete = (Button) convertView.findViewById(R.id.delete);
				convertView.setTag(holder);
			}
			holder = (ViewHolder) convertView.getTag();
			holder.item_tv_left.setText("设备ID："
					+ list.get(position).get("deviceId") + "");
			holder.item_tv_right.setText("主设备ID："
					+ list.get(position).get("mainDeviceId") + "");
			holder.delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					list.remove(position);
					adapter.notifyDataSetChanged();
					if (list.size() == 0) {
						listAndBottom_bar_install.setVisibility(View.INVISIBLE);
					}
				}
			});

			return convertView;
		}

		public class ViewHolder {
			public TextView item_tv_left;
			public TextView item_tv_right;
			public Button delete;
		}

	}
}
