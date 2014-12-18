package com.csei.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.csei.devicesmanagement.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

@SuppressLint("UseSparseArrays")
public class HistoryAdapter extends BaseExpandableListAdapter {

	private String[] groupName;
	
	private List<HashMap<String, String>> stockInList;
	private List<HashMap<String, String>> stockOutList;
	private List<HashMap<String, String>> transportList;
	private List<HashMap<String, String>> installList;
	private List<HashMap<String, String>> uninstallList;
	
	private List<Integer> stockInSelected;
	private List<Integer> stockOutSelected;
	private List<Integer> transportSelected;
	private List<Integer> installSelected;
	private List<Integer> uninstallSelected;
	
	private LayoutInflater inflater = null;

	public List<Integer> getStockInSelected(){
		return stockInSelected;
	}
	
	public List<Integer> getStockOutSelected(){
		return stockOutSelected;
	} 
	
	public List<Integer> getTransportSelected(){
		return transportSelected;
	}
	
	public List<Integer> getInstallSelected(){
		return installSelected;
	}
	
	public List<Integer> getUninstallSelected(){
		return uninstallSelected;
	}
	
	public HistoryAdapter(Context context,String[] groupName,List<HashMap<String, String>> stockInList,List<HashMap<String, String>> stockOutList,List<HashMap<String, String>> transportList,List<HashMap<String, String>> installList,List<HashMap<String, String>> uninstallList) {
		this.groupName = groupName;
		this.stockInList = stockInList;
		this.stockOutList = stockOutList;
		this.transportList = transportList;
		this.installList = installList;
		this.uninstallList = uninstallList;
		inflater = LayoutInflater.from(context);
		stockInSelected = new ArrayList<Integer>();
		stockOutSelected = new ArrayList<Integer>();
		transportSelected = new ArrayList<Integer>();
		installSelected = new ArrayList<Integer>();
		uninstallSelected = new ArrayList<Integer>();
	}

	@Override
	public int getGroupCount() {
		return groupName.length;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if(groupPosition == 0)
			return stockInList.size();
		else if(groupPosition == 1)
			return stockOutList.size();
		else if(groupPosition == 2)
			return transportList.size();
		else if(groupPosition == 3)
			return installList.size();
		else 
			return uninstallList.size();
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
		if(groupPosition == 0){
			
			convertView = inflater.inflate(R.layout.listitem_history_stockin, null);
			
			((TextView) convertView.findViewById(R.id.stockintv)).setText("入库记录");
			((TextView) convertView.findViewById(R.id.storehouseidtv)).setText("仓库编号:"+stockInList.get(childPosition).get("storehouseId"));
			((TextView) convertView.findViewById(R.id.deviceidtv)).setText("设备编号:"+stockInList.get(childPosition).get("deviceId"));
			((TextView) convertView.findViewById(R.id.contractidtv)).setText("合同编号:"+stockInList.get(childPosition).get("contractId"));
			((TextView) convertView.findViewById(R.id.drivertv)).setText("司机姓名:"+stockInList.get(childPosition).get("driver"));
			((TextView) convertView.findViewById(R.id.carnumbertv)).setText("车牌号码:"+stockInList.get(childPosition).get("carNumber"));
			((TextView) convertView.findViewById(R.id.descriptiontv)).setText("描述:"+stockInList.get(childPosition).get("description"));
			
			for(int i:stockInSelected){
				if(childPosition==i){
					((CheckBox) convertView.findViewById(R.id.stockincb)).setChecked(true);
				}
			}
			
			((CheckBox) convertView.findViewById(R.id.stockincb)).setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(isChecked){
						stockInSelected.add(childPosition);
					}else {
						for(int i=0;i<stockInSelected.size();i++){
							if(stockInSelected.get(i)==childPosition)
								stockInSelected.remove(i);
						}
					}
				}
			});
			return convertView;
		}else if(groupPosition == 1){

			convertView = inflater.inflate(R.layout.listitem_history_stockin, null);
			
			((TextView) convertView.findViewById(R.id.stockintv)).setText("出库记录");
			((TextView) convertView.findViewById(R.id.storehouseidtv)).setText("仓库编号:"+stockOutList.get(childPosition).get("storehouseId"));
			((TextView) convertView.findViewById(R.id.deviceidtv)).setText("设备编号:"+stockOutList.get(childPosition).get("deviceId"));
			((TextView) convertView.findViewById(R.id.contractidtv)).setText("合同编号:"+stockOutList.get(childPosition).get("contractId"));
			((TextView) convertView.findViewById(R.id.drivertv)).setText("司机姓名:"+stockOutList.get(childPosition).get("driver"));
			((TextView) convertView.findViewById(R.id.carnumbertv)).setText("车牌号码:"+stockOutList.get(childPosition).get("carNumber"));
			((TextView) convertView.findViewById(R.id.descriptiontv)).setText("描述:"+stockOutList.get(childPosition).get("description"));
			
			for(int i:stockOutSelected){
				if(childPosition==i){
					((CheckBox) convertView.findViewById(R.id.stockincb)).setChecked(true);
				}
			}
			
			((CheckBox) convertView.findViewById(R.id.stockincb)).setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(isChecked){
						stockOutSelected.add(childPosition);
					}else {
						for(int i=0;i<stockOutSelected.size();i++){
							if(stockOutSelected.get(i)==childPosition)
								stockOutSelected.remove(i);
						}
					}
				}
			});
			return convertView;
		}else if(groupPosition == 2){

			convertView = inflater.inflate(R.layout.listitem_history_transport, null);
			
			((TextView) convertView.findViewById(R.id.transporttv)).setText("运输记录");
			((TextView) convertView.findViewById(R.id.deviceidtv)).setText("设备编号:"+transportList.get(childPosition).get("deviceId"));
			((TextView) convertView.findViewById(R.id.drivertv)).setText("司机姓名:"+transportList.get(childPosition).get("driver"));
			((TextView) convertView.findViewById(R.id.telephonetv)).setText("司机电话:"+transportList.get(childPosition).get("telephone"));
			((TextView) convertView.findViewById(R.id.destinationtv)).setText("目的地:"+transportList.get(childPosition).get("destination"));
			((TextView) convertView.findViewById(R.id.addresstv)).setText("出发地:"+transportList.get(childPosition).get("address"));
			
			for(int i:transportSelected){
				if(childPosition==i){
					((CheckBox) convertView.findViewById(R.id.transportcb)).setChecked(true);
				}
			}
			
			((CheckBox) convertView.findViewById(R.id.transportcb)).setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(isChecked){
						transportSelected.add(childPosition);
					}else {
						for(int i=0;i<transportSelected.size();i++){
							if(transportSelected.get(i)==childPosition)
								transportSelected.remove(i);
						}
					}
				}
			});
			return convertView;
		}else if(groupPosition == 3){
			
			convertView = inflater.inflate(R.layout.listitem_history_install, null);
			
			((TextView) convertView.findViewById(R.id.installtv)).setText("安装记录");
			((TextView) convertView.findViewById(R.id.deviceidtv)).setText("设备编号:"+installList.get(childPosition).get("deviceId"));
			((TextView) convertView.findViewById(R.id.contractidtv)).setText("合同编号:"+installList.get(childPosition).get("contractId"));
			((TextView) convertView.findViewById(R.id.typetv)).setText("type:"+installList.get(childPosition).get("type"));
			((TextView) convertView.findViewById(R.id.installmantv)).setText("安装人姓名："+installList.get(childPosition).get("installMan"));
			((TextView) convertView.findViewById(R.id.installstatustv)).setText("安装status:"+installList.get(childPosition).get("installStatus"));
			
			for(int i:installSelected){
				if(childPosition==i){
					((CheckBox) convertView.findViewById(R.id.installcb)).setChecked(true);
				}
			}
			
			((CheckBox) convertView.findViewById(R.id.installcb)).setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(isChecked){
						installSelected.add(childPosition);
					}else {
						for(int i=0;i<installSelected.size();i++){
							if(installSelected.get(i)==childPosition)
								installSelected.remove(i);
						}
					}
				}
			});
			return convertView;
		}else if(groupPosition == 4){

			convertView = inflater.inflate(R.layout.listitem_history_uninstall, null);
			
			((TextView) convertView.findViewById(R.id.uninstalltv)).setText("卸载记录");
			((TextView) convertView.findViewById(R.id.deviceidtv)).setText("设备编号:"+uninstallList.get(childPosition).get("deviceId"));
			((TextView) convertView.findViewById(R.id.contractidtv)).setText("合同编号:"+uninstallList.get(childPosition).get("contractId"));
			((TextView) convertView.findViewById(R.id.removemantv)).setText("卸载人姓名："+uninstallList.get(childPosition).get("removeMan"));
			((TextView) convertView.findViewById(R.id.removestatustv)).setText("卸载status:"+uninstallList.get(childPosition).get("removeStatus"));
			
			for(int i:uninstallSelected){
				if(childPosition==i){
					((CheckBox) convertView.findViewById(R.id.uninstallcb)).setChecked(true);
				}
			}
			
			((CheckBox) convertView.findViewById(R.id.uninstallcb)).setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(isChecked){
						uninstallSelected.add(childPosition);
					}else {
						for(int i=0;i<uninstallSelected.size();i++){
							if(uninstallSelected.get(i)==childPosition)
								uninstallSelected.remove(i);
						}
					}
				}
			});
			return convertView;
		}
		return null;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}

}
