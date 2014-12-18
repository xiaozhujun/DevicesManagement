package com.csei.adapter;

import java.util.ArrayList;
import java.util.Collections;

import com.csei.database.entity.Device;
import com.csei.devicesmanagement.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class TransportAdapter extends BaseExpandableListAdapter{
	
	private Context context;
	private String[] groupName;
	private ArrayList<Device> deviceList;
	private LayoutInflater inflater ;
	
	private EditText driverNameEdt;
	private EditText driverPhoneEdt;
	private EditText carNumberEdt;
	private EditText addressEdt;
	private EditText destinationEdt;
	
	private String driverName;
	private String driverPhone;
	private String carNumber;
	private String address;
	private String destination;
	
	public TransportAdapter(Context context,String[] groupName,ArrayList<Device> deviceList,String driverName,String driverPhone,String carNumber,String address,String destination){
		this.context = context;
		this.groupName = groupName;
		Collections.reverse(deviceList);
		this.deviceList = deviceList;
		this.inflater = LayoutInflater.from(context);
		this.driverName = driverName;
		this.driverPhone =  driverPhone;
		this.carNumber = carNumber;
		this.address = address;
		this.destination = destination;
	}

	public ArrayList<Device> getDeviceList(){
		Collections.reverse(deviceList);
		return deviceList;
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
	
	public String getAddress(){
		return address;
	}
	
	public String getDestination(){
		return destination;
	}
	
	@Override
	public int getGroupCount() {
		return groupName.length;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if(groupPosition==0)
			return 1;
		else if(deviceList.isEmpty())
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
			convertView = inflater.inflate(R.layout.listitem_transport_driver, null);
			driverNameEdt = (EditText) convertView.findViewById(R.id.drivernameedt);
			driverPhoneEdt = (EditText) convertView.findViewById(R.id.driverphoneedt);
			carNumberEdt = (EditText) convertView.findViewById(R.id.carnumberedt);
			addressEdt = (EditText) convertView.findViewById(R.id.addressedt);
			destinationEdt = (EditText) convertView.findViewById(R.id.destinationedt);
			
			if(!"".equals(driverName)){
				driverNameEdt.setText(driverName);
			}
			if(!"".equals(driverPhone)){
				driverPhoneEdt.setText(driverPhone);
			}
			if(!"".equals(carNumber)){
				carNumberEdt.setText(carNumber);
			}
			if(!"".equals(address)){
				carNumberEdt.setText(address);
			}
			if(!"".equals(destination)){
				carNumberEdt.setText(destination);
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
			
			addressEdt.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {  
			    @Override  
			    public void onFocusChange(View v, boolean hasFocus) {  
			        if(hasFocus) {
			        	// 此处为得到焦点时的处理内容
			        } else {
			        	address = addressEdt.getText().toString();
			        }
			    	}
			});
			
			destinationEdt.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {  
			    @Override  
			    public void onFocusChange(View v, boolean hasFocus) {  
			        if(hasFocus) {
			        	// 此处为得到焦点时的处理内容
			        } else {
			        	destination = destinationEdt.getText().toString();
			        }
			    	}
			});
			return convertView;
		}else{
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
						TransportAdapter.this.notifyDataSetChanged();
					}
				});
			}
			return convertView;
		}
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
