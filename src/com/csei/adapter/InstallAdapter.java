package com.csei.adapter;

import java.util.ArrayList;
import java.util.Collections;

import com.csei.database.entity.Device;
import com.csei.database.entity.Site;
import com.csei.database.entity.Store;
import com.csei.devicesmanagement.R;

import android.content.Context;
import android.provider.VoicemailContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class InstallAdapter extends BaseExpandableListAdapter{
	
	private Context context;
	private String[] groupName;
	private ArrayList<Site> siteList;
	private ArrayList<Device> deviceList;
	private LayoutInflater inflater = null;
	private static int isSelected ;
	private Device mainDevice;
	
	public InstallAdapter(Context context,String[] groupName,ArrayList<Site> siteList,ArrayList<Device> deviceList,int isSelected,Device mainDevice){
		this.context = context;
		this.groupName = groupName;
		Collections.reverse(siteList);
		this.siteList = siteList;
		Collections.reverse(deviceList);
		this.deviceList = deviceList;
		inflater = LayoutInflater.from(context);
		this.isSelected = isSelected;
		this.mainDevice = mainDevice;
	} 
	
	public void setSiteList(ArrayList<Site> siteList){
		Collections.reverse(siteList);
		this.siteList = siteList;
	}

	public ArrayList<Site> getSiteList(){
		Collections.reverse(siteList);
		return siteList;
	}
	
	public void setDeviceList(ArrayList<Device> deviceList){
		Collections.reverse(deviceList);
		this.deviceList = deviceList;
	}
	
	public ArrayList<Device> getDeviceList(){
		Collections.reverse(deviceList);
		return deviceList;
	}
	
	public void setIsSelected(int isSelected){
		this.isSelected = isSelected;
	}
	
	public int getIsSelected(){
		return isSelected;
	}
	
	public void setMainDevice(Device mainDevice){
		this.mainDevice = mainDevice;
		InstallAdapter.this.notifyDataSetChanged();
	}
	
	public Device getMainDevice(){
		return mainDevice;
	}
	
	@Override
	public int getGroupCount() {
		return groupName.length;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if(groupPosition==0)
			return siteList.size();
		else if(groupPosition==1)
			return 1;
		else
			return deviceList.size();
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

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
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
					InstallAdapter.this.notifyDataSetChanged(); 
				}
			});

			cangkuxinxitv.setText("工地信息");
			cangkuidtv.setText("工地Id:  "+siteList.get(childPosition).getId()+"");
			cangkunametv.setText("工地名称:  "+siteList.get(childPosition).getName());
			cangkuaddresstv.setText("工地地址:  "+siteList.get(childPosition).getAddress());
			cangkutelephonetv.setText("工地电话:  "+siteList.get(childPosition).getTelephone());
			
		}else if(groupPosition==1){
			convertView = inflater.inflate(R.layout.listitem_stock_device, null);
			TextView shebeixinxitv = (TextView) convertView.findViewById(R.id.shebeixinxitv);
			TextView shebeiidtv = (TextView) convertView.findViewById(R.id.shebeiidtv);
			TextView shebeinametv = (TextView) convertView.findViewById(R.id.shebeinametv);
			Button shebeishanchubtn = (Button) convertView.findViewById(R.id.shebeishanchubtn);
			
			shebeishanchubtn.setText("删除");
			shebeixinxitv.setText("主设备信息");
			shebeiidtv.setText("设备Id:  "+mainDevice.getId()+"");
			shebeinametv.setText("设备名称:  "+mainDevice.getName());
			
		}else if(groupPosition==2){
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
					InstallAdapter.this.notifyDataSetChanged();
				}
			});
		}
		return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
