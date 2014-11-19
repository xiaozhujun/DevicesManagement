package com.csei.adapter;

import java.util.ArrayList;
import java.util.Collections;

import com.csei.database.entity.Device;
import com.csei.database.entity.Site;
import com.csei.database.entity.Store;
import com.csei.devicesmanagement.R;

import android.R.integer;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class UninstallAdapter extends BaseExpandableListAdapter{

	private Context context;
	private String[] groupName;
	private ArrayList<Site> storeList;
	private ArrayList<Device> deviceList;
	private LayoutInflater inflater = null;
	private static int isSelected ;
	
	public void setStoreList(ArrayList<Site> storeList){
		this.storeList = storeList;
	}
	public ArrayList<Site> getStoreList(){
		return storeList;
	}
	
	public void setDevicelist(ArrayList<Device> deviceList){
		Collections.reverse(deviceList);
		this.deviceList = deviceList;
	}
	
	public ArrayList<Device> getDeviceList(){
		Collections.reverse(deviceList);
		return deviceList;
	}
	
	public int getIsSelected(){
		return isSelected;
	}

	public UninstallAdapter(Context context,String[] groupName,ArrayList<Site> storeList,ArrayList<Device> deviceList,int isSelected){
		this.context = context;
		this.groupName = groupName;
		this.storeList = storeList;
		Collections.reverse(deviceList);
		this.deviceList = deviceList;
		inflater = LayoutInflater.from(context);
		this.isSelected = isSelected;
	}

	@Override
	public int getGroupCount() {
		return groupName.length;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if(groupPosition==0)
			return storeList.size();
		else if (deviceList.isEmpty()) 
			return 1;
		else
			return deviceList.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		if (groupPosition==0)
			return storeList;
		else 
			return deviceList;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		if(groupPosition==0)
			return storeList.get(childPosition);
		else 
			return deviceList.get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		convertView = inflater.inflate(R.layout.listitem_stock_group, null);
		TextView groupnametv = (TextView) convertView.findViewById(R.id.groupnametv);
		
		groupnametv.setText(groupName[groupPosition]);
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		if(groupPosition==0){
			convertView = inflater.inflate(R.layout.listitem_stock_store, null);
			TextView cangkuxinxitv = (TextView) convertView.findViewById(R.id.cangkuxinxitv);
			TextView cangkuidtv = (TextView) convertView.findViewById(R.id.cangkuidtv);
			TextView cangkunametv = (TextView) convertView.findViewById(R.id.cangkunametv);
			TextView cangkuaddresstv = (TextView) convertView.findViewById(R.id.cangkuaddresstv);
			TextView cangkutelephonetv = (TextView) convertView.findViewById(R.id.cangkutelephonetv);
			CheckBox cangkucb = (CheckBox) convertView.findViewById(R.id.cangkucb);
			
			if(isSelected==childPosition){
				cangkucb.setChecked(true);
			}
			
			cangkucb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(isChecked){
						isSelected = childPosition;
					}else {
						isSelected = -1;
					}
					UninstallAdapter.this.notifyDataSetChanged(); 
				}
			});

			cangkuxinxitv.setText("工地信息");
			cangkuidtv.setText("工地Id:  "+storeList.get(childPosition).getId()+"");
			cangkunametv.setText("工地名称:  "+storeList.get(childPosition).getName());
			cangkuaddresstv.setText("工地地址:  "+storeList.get(childPosition).getAddress());
			cangkutelephonetv.setText("工地电话:  "+storeList.get(childPosition).getTelephone());

			return convertView;
		}else {
			if (deviceList.isEmpty()) {
				convertView = inflater.inflate(R.layout.listitem_stock_device, null);
				ImageView shebeiimg = (ImageView) convertView.findViewById(R.id.shebeiimg);
				Button shebeishanchubtn = (Button) convertView.findViewById(R.id.shebeishanchubtn);
				
				shebeishanchubtn.setVisibility(View.GONE);
				shebeiimg.setImageResource(R.drawable.button_my_login_down);
			}else {
				convertView = inflater.inflate(R.layout.listitem_stock_device, null);
				TextView shebeixinxitv = (TextView) convertView.findViewById(R.id.shebeixinxitv);
				TextView shebeiidtv = (TextView) convertView.findViewById(R.id.shebeiidtv);
				TextView shebeinametv = (TextView) convertView.findViewById(R.id.shebeinametv);
				Button shebeishanchubtn = (Button) convertView.findViewById(R.id.shebeishanchubtn);
				
				shebeixinxitv.setText("设备信息");
				shebeishanchubtn.setText("删除");
				shebeiidtv.setText("设备Id:  "+deviceList.get(childPosition).getId()+"");
				shebeinametv.setText("设备名称:  "+deviceList.get(childPosition).getName());
				shebeishanchubtn.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						deviceList.remove(childPosition);
						UninstallAdapter.this.notifyDataSetChanged();
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

}

