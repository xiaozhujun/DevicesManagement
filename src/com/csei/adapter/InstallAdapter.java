package com.csei.adapter;

import java.util.ArrayList;
import java.util.Collections;
import com.csei.database.entity.Contract;
import com.csei.database.entity.Device;
import com.csei.devicesmanagement.CameraActivity;
import com.csei.devicesmanagement.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

public class InstallAdapter extends BaseExpandableListAdapter {

	private Context context;
	private String[] groupName;
	private ArrayList<Device> deviceList;
	private ArrayList<Contract> contractList;
	private String installType;
	private String installMan;
	private String installStatus;
	private LayoutInflater inflater = null;
	private int contractSelected;

	private EditText installTypeEdt;
	private EditText installManEdt;
	private EditText installStatusEdt;
	
	private Activity mActivity;
	
	public InstallAdapter(Activity activity){
		this.mActivity=activity;
	}

	public void setDevicelist(ArrayList<Device> deviceList) {
		Collections.reverse(deviceList);
		this.deviceList = deviceList;
	}

	public ArrayList<Device> getDeviceList() {
		Collections.reverse(deviceList);
		return deviceList;
	}

	public int getContractSelected() {
		return contractSelected;
	}

	public String getInstallType() {
		return installType;
	}

	public String getInstallMan() {
		return installMan;
	}

	public String getInstallStatus() {
		return installStatus;
	}

	public InstallAdapter(Context context, String[] groupName,
			ArrayList<Contract> contractList, ArrayList<Device> deviceList,
			String installType, String installMan, String installStatus,
			int contractSelected) {
		this.context = context;
		this.mActivity=(Activity) context;
		this.groupName = groupName;
		this.contractList = contractList;
		Collections.reverse(deviceList);
		this.deviceList = deviceList;
		inflater = LayoutInflater.from(context);
		this.installType = installType;
		this.installMan = installMan;
		this.installStatus = installStatus;

		this.contractSelected = contractSelected;
	}

	@Override
	public int getGroupCount() {
		return groupName.length;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if (groupPosition == 0)
			return contractList.size();
		else if (groupPosition == 1)
			return 1;
		else if (deviceList.isEmpty())
			return 1;
		else
			return deviceList.size();
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		convertView = inflater.inflate(R.layout.listitem_stock_group, null);
		TextView groupnametv = (TextView) convertView
				.findViewById(R.id.groupnametv);

		groupnametv.setText(groupName[groupPosition]);
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		if (groupPosition == 0) {
			convertView = inflater.inflate(R.layout.listitem_stock_store, null);
			TextView cangkuxinxitv = (TextView) convertView
					.findViewById(R.id.cangkuxinxitv);
			TextView cangkuidtv = (TextView) convertView
					.findViewById(R.id.cangkuidtv);
			TextView cangkunametv = (TextView) convertView
					.findViewById(R.id.cangkunametv);
			TextView cangkuaddresstv = (TextView) convertView
					.findViewById(R.id.cangkuaddresstv);
			TextView cangkutelephonetv = (TextView) convertView
					.findViewById(R.id.cangkutelephonetv);
			CheckBox cangkucb = (CheckBox) convertView
					.findViewById(R.id.cangkucb);

			if (contractSelected == childPosition) {
				cangkucb.setChecked(true);
			}

			cangkucb.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					if (isChecked) {
						contractSelected = childPosition;
					} else {
						contractSelected = -1;
					}
					InstallAdapter.this.notifyDataSetChanged();
				}
			});

			cangkuxinxitv.setText("合同信息");
			cangkuidtv.setText("合同Id:  "
					+ contractList.get(childPosition).getId() + "");
			cangkunametv.setText("合同客户:  "
					+ contractList.get(childPosition).getCustomerName());
			cangkuaddresstv.setText("合同期限:  "
					+ contractList.get(childPosition).getStartTime() + "至"
					+ contractList.get(childPosition).getEndTime());
			cangkutelephonetv.setText("签署日期:  "
					+ contractList.get(childPosition).getSignTime());

			return convertView;
		} else if (groupPosition == 1) {
			convertView = inflater.inflate(R.layout.listitem_install_install,
					null);
			installTypeEdt = (EditText) convertView
					.findViewById(R.id.installtypeedt);
			installManEdt = (EditText) convertView
					.findViewById(R.id.installmanedt);
			installStatusEdt = (EditText) convertView
					.findViewById(R.id.installstatusedt);
			if (!"".equals(installType)) {
				installTypeEdt.setText(installType);
			}
			if (!"".equals(installMan)) {
				installManEdt.setText(installMan);
			}
			if (!"".equals(installStatus)) {
				installStatusEdt.setText(installStatus);
			}

			installTypeEdt
					.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
						@Override
						public void onFocusChange(View v, boolean hasFocus) {
							if (hasFocus) {
								// 此处为得到焦点时的处理内容
							} else {
								installType = installTypeEdt.getText()
										.toString();
							}
						}
					});

			installManEdt
					.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
						@Override
						public void onFocusChange(View v, boolean hasFocus) {
							if (hasFocus) {
								// 此处为得到焦点时的处理内容
							} else {
								installMan = installManEdt.getText().toString();
							}
						}
					});

			installStatusEdt
					.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
						@Override
						public void onFocusChange(View v, boolean hasFocus) {
							if (hasFocus) {
								// 此处为得到焦点时的处理内容
							} else {
								installStatus = installStatusEdt.getText()
										.toString();
							}
						}
					});

			return convertView;
		} else {
			if (deviceList.isEmpty()) {
				convertView = inflater.inflate(R.layout.listitem_stock_device,
						null);
				ImageView shebeiimg = (ImageView) convertView
						.findViewById(R.id.shebeiimg);
				Button shebeishanchubtn = (Button) convertView
						.findViewById(R.id.shebeishanchubtn);

				shebeishanchubtn.setVisibility(View.GONE);
				shebeiimg.setImageResource(R.drawable.button_my_login_down);
			} else {
				convertView = inflater.inflate(R.layout.listitem_stock_device,
						null);
				TextView shebeixinxitv = (TextView) convertView
						.findViewById(R.id.shebeixinxitv);
				TextView shebeiidtv = (TextView) convertView
						.findViewById(R.id.shebeiidtv);
				TextView shebeinametv = (TextView) convertView
						.findViewById(R.id.shebeinametv);
				Button shebeishanchubtn = (Button) convertView
						.findViewById(R.id.shebeishanchubtn);
				Button photobtn = (Button) convertView
						.findViewById(R.id.photobtn);
				photobtn.setText("拍照");
				photobtn.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// 拍照操作
						Intent intent = new Intent(context,
								CameraActivity.class);
						intent.putExtra("i", childPosition);
						intent.putExtra("activity", "InstallActivity");
						intent.putExtra("activityName", "install");
						mActivity.startActivityForResult(intent, 0);
					}
				});
				shebeixinxitv.setText("设备信息");
				shebeishanchubtn.setText("删除");
				shebeiidtv.setText("设备Id:  "
						+ deviceList.get(childPosition).getId() + "");
				shebeinametv.setText("设备名称:  "
						+ deviceList.get(childPosition).getName());
				shebeishanchubtn.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						deviceList.remove(childPosition);
						InstallAdapter.this.notifyDataSetChanged();
					}
				});
			}
			return convertView;
		}
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return false;
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return 0;
	}

}
