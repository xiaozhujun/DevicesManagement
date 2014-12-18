package com.csei.adapter;

import java.util.ArrayList;
import java.util.Collections;

import com.csei.database.entity.Contract;
import com.csei.database.entity.Device;
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
import android.widget.EditText;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class StockListAdapter extends BaseExpandableListAdapter{

	private Context context;
	private String[] groupName;
	private ArrayList<Contract> contractList;
	private int contractSelected;
	private String driverName;
	private String driverPhone;
	private String carNumber;
	private ArrayList<Store> storeList;
	private int storeSelected ;
	private ArrayList<Device> deviceList;
	private LayoutInflater inflater = null;
	
	private EditText driverNameEdt;
	private EditText driverPhoneEdt;
	private EditText carNumberEdt;
	
	public void setStoreList(ArrayList<Store> storeList){
		this.storeList = storeList;
	}
	
	public ArrayList<Store> getStoreList(){
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
	
	public int getContractSelected(){
		return contractSelected;
	}
	
	public int getStoreSelected(){
		return storeSelected;
	}

	public String getDriverName(){
		return driverName;
	}
	
	public String getDriverPhone(){
		return driverPhone;
	}
	
	public String getCarNumber(){
		return carNumber;
	}
	
	public StockListAdapter(Context context,String[] groupName,ArrayList<Contract> contractList,ArrayList<Store> storeList,ArrayList<Device> deviceList,int contractSelected,int storeSelected,String driverName,String driverPhone,String carNumber){
		this.context = context;
		this.groupName = groupName;
		this.contractList = contractList;
		this.storeList = storeList;
		Collections.reverse(deviceList);
		this.deviceList = deviceList;
		inflater = LayoutInflater.from(context);
		this.contractSelected = contractSelected;
		this.storeSelected = storeSelected;
		this.driverName = driverName;
		this.driverPhone = driverPhone;
		this.carNumber = carNumber;
	}

	@Override
	public int getGroupCount() {
		return groupName.length;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if(groupPosition==0)
			return contractList.size();
		else if(groupPosition==1)
			return 1;
		else if(groupPosition==2)
			return storeList.size();
		else if (deviceList.isEmpty()) 
			return 1;
		else
			return deviceList.size();
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
			
			if(contractSelected==childPosition){
				cangkucb.setChecked(true);
			}
			
			cangkucb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(isChecked){
						contractSelected = childPosition;
					}else {
						contractSelected = -1;
					} 
				}
			});

			cangkuxinxitv.setText("合同信息");
			cangkuidtv.setText("合同Id:  "+contractList.get(childPosition).getId()+"");
			cangkunametv.setText("合同客户:  "+contractList.get(childPosition).getCustomerName());
			cangkuaddresstv.setText("合同期限:  "+contractList.get(childPosition).getStartTime()+"至"+contractList.get(childPosition).getEndTime());
			cangkutelephonetv.setText("签署日期:  "+contractList.get(childPosition).getSignTime());

			return convertView;
		}
		else if (groupPosition==1){
			convertView = inflater.inflate(R.layout.listitem_transport_driver, null);
			driverNameEdt = (EditText) convertView.findViewById(R.id.drivernameedt);
			driverPhoneEdt = (EditText) convertView.findViewById(R.id.driverphoneedt);
			carNumberEdt = (EditText) convertView.findViewById(R.id.carnumberedt);
			
			if(!"".equals(driverName)){
				driverNameEdt.setText(driverName);
			}if(!"".equals(driverPhone)){
				driverPhoneEdt.setText(driverPhone);
			}if(!"".equals(carNumber)){
				carNumberEdt.setText(carNumber);
			}
			
			driverNameEdt.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {  
			    @Override  
			    public void onFocusChange(View v, boolean hasFocus) {  
			        if(hasFocus) {
			        	// 此处为得到焦点时的处理内容
			        } else {
			        	driverName = driverNameEdt.getText().toString();
			        }
			    	}
			});
			
			driverPhoneEdt.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {  
			    @Override  
			    public void onFocusChange(View v, boolean hasFocus) {  
			        if(hasFocus) {
			        	// 此处为得到焦点时的处理内容
			        } else {
			        	driverPhone = driverPhoneEdt.getText().toString();
			        }
			    	}
			});
			
			carNumberEdt.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {  
			    @Override  
			    public void onFocusChange(View v, boolean hasFocus) {  
			        if(hasFocus) {
			        	// 此处为得到焦点时的处理内容
			        } else {
			        	carNumber = carNumberEdt.getText().toString();
			        }
			    	}
			});
			return convertView;
		}
		else if(groupPosition==2){
			convertView = inflater.inflate(R.layout.listitem_stock_store, null);
			TextView cangkuxinxitv = (TextView) convertView.findViewById(R.id.cangkuxinxitv);
			TextView cangkuidtv = (TextView) convertView.findViewById(R.id.cangkuidtv);
			TextView cangkunametv = (TextView) convertView.findViewById(R.id.cangkunametv);
			TextView cangkuaddresstv = (TextView) convertView.findViewById(R.id.cangkuaddresstv);
			TextView cangkutelephonetv = (TextView) convertView.findViewById(R.id.cangkutelephonetv);
			CheckBox cangkucb = (CheckBox) convertView.findViewById(R.id.cangkucb);
			
			if(storeSelected==childPosition){
				cangkucb.setChecked(true);
			}
			
			cangkucb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(isChecked){
						storeSelected = childPosition;
					}else {
						storeSelected = -1;
					}
					StockListAdapter.this.notifyDataSetChanged(); 
				}
			});

			cangkuxinxitv.setText("仓库信息");
			cangkuidtv.setText("仓库Id:  "+storeList.get(childPosition).getId()+"");
			cangkunametv.setText("仓库名称:  "+storeList.get(childPosition).getName());
			cangkuaddresstv.setText("仓库地址:  "+storeList.get(childPosition).getAddress());
			cangkutelephonetv.setText("仓库电话:  "+storeList.get(childPosition).getTelephone());

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
						StockListAdapter.this.notifyDataSetChanged();
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

}
