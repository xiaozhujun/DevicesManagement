package com.csei.adapter;

import java.util.HashMap;
import java.util.List;

import com.csei.devicesmanagement.R;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

@SuppressLint("UseSparseArrays")
public class MyListAdapter extends BaseAdapter{

	private List<HashMap<String,String>> list;
	private static HashMap<Integer,Boolean> isSelected;
	private LayoutInflater inflater = null; 
	private int visibility;
	
	public void setVisibility(int visibility){
		this.visibility = visibility;
	}

	public MyListAdapter(List<HashMap<String,String>> list,Context context){
		this.list = list;
		inflater = LayoutInflater.from(context);
		isSelected = new HashMap<Integer, Boolean>();	
		visibility = View.INVISIBLE;
		initData();
	}

	private void initData(){
		for(int i=0;i<list.size();i++){
			getIsSelected().put(i,false);
		}
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if(convertView == null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.listitem_history, null);
			holder.optionName = (TextView) convertView.findViewById(R.id.content_name);
			holder.upLoadFlag = (TextView) convertView.findViewById(R.id.upload);
			holder.cb = (CheckBox) convertView.findViewById(R.id.check_box);
			holder.deviceName = (TextView) convertView.findViewById(R.id.deviceName);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.userName = (TextView) convertView.findViewById(R.id.userName);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
			
		}
		switch(Integer.parseInt(list.get(position).get("optionType"))){
		case 0:
			holder.optionName.setText("设备入库");
			break;
		case 1:
			holder.optionName.setText("设备出库");
			break;
		case 2:
			holder.optionName.setText("设备安装");
			break;
		case 3:
			holder.optionName.setText("设备卸载");
			break;
		case 4:
			holder.optionName.setText("设备运输");
			break;
		}
		Log.i("msg", list.get(position).get("upLoadFlag"));
		switch(Integer.parseInt(list.get(position).get("upLoadFlag"))){
		case 0://未上传
			holder.upLoadFlag.setText("未上传");
			holder.upLoadFlag.setBackgroundResource(R.color.coral);
			break;
		case 1:
			holder.upLoadFlag.setText("已上传");
			holder.upLoadFlag.setBackgroundResource(R.color.green);
			break;
		}
		
		holder.cb.setVisibility(visibility);
		
		holder.deviceName.setText("设备ID："+list.get(position).get("deviceId"));
		holder.time.setText("操作时间："+list.get(position).get("time"));
		holder.userName.setText("操作人员ID："+list.get(position).get("userId"));
		
		
		/*此处一定要注意，先设置监听，再设置checkbox的状态！不然ListView下滑时会
		*造成已选中的checkbox的状态丢失，上方图片设置同理！
		*
		*
		*/
		holder.cb.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				getIsSelected().put(position, isChecked);
			}
		});
		
		holder.cb.setChecked(getIsSelected().get(position));
		
		return convertView;
	}


	public static HashMap<Integer, Boolean> getIsSelected() {  
		return isSelected;  
	}
	
	public static void getIsSelected(HashMap<Integer,Boolean> isSelected){
		MyListAdapter.isSelected = isSelected;
	}
	
	public static class ViewHolder {  
		TextView optionName; 
		TextView upLoadFlag;
		CheckBox cb;
		TextView deviceName;
		TextView time;
		TextView userName;

	} 

}
