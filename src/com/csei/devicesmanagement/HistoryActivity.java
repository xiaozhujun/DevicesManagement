package com.csei.devicesmanagement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.csei.adapter.MyListAdapter;
import com.csei.application.MyApplication;
import com.csei.database.entity.service.imple.HistoryServiceImple;
import com.csei.devicesmanagement.R.color;

public class HistoryActivity extends Activity{
	
//jfakfdsaklfkalflk


	private MyListAdapter adapter;
	private HistoryServiceImple dao;

	private List<HashMap<String,String>> list;

	//topBar
	private ImageView left_back;
	private RelativeLayout tv_topbar_right_map_layout;
	private TextView tv_topbar_right_edit;
	private boolean ButtonsOn =false;

	//Content
	private ListView listView;
	private RelativeLayout no_collection;
	private FrameLayout bottom_bar;
	private Button btn_select_all;
	private Button btn_cancel_all;
	private Button btn_upload;
	private Button btn_delete;
	private Handler handler;
	private boolean select_all = false;


	private String userId ;

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(ButtonsOn){
			bottom_bar.setVisibility(View.GONE);
			tv_topbar_right_edit.setText("编辑");
			adapter.setVisibility(View.GONE);
			adapter.notifyDataSetChanged();
			ButtonsOn = false;
		}else{
			HistoryActivity.this.finish();
		}
	}



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		MyApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_history);
		
		userId = getIntent().getStringExtra("userId");
		Log.i("msg", userId);
	
		//控件初始化
		left_back = (ImageView) findViewById(R.id.iv_topbar_left_back1);
		tv_topbar_right_map_layout = (RelativeLayout) findViewById(R.id.tv_topbar_right_map_layout_history1);
		tv_topbar_right_edit = (TextView) findViewById(R.id.tv_topbar_right_edit1);
		listView = (ListView) findViewById(R.id.listView_history);
		no_collection = (RelativeLayout) findViewById(R.id.no_collection);
		bottom_bar = (FrameLayout) findViewById(R.id.bottom_bar);
		btn_select_all = (Button) findViewById(R.id.btnSelAll);
		btn_cancel_all = (Button) findViewById(R.id.btnCancelAll);
		btn_upload = (Button) findViewById(R.id.btnUpload);
		btn_delete = (Button) findViewById(R.id.btnDelAll);


		left_back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});


		dao = new HistoryServiceImple(HistoryActivity.this);


		
	//	list = dao.(userId);
		
		list = dao.findHistory(Integer.parseInt(userId));
		Collections.reverse(list);
		Log.i("msg", list.toString());
		
		

		if(list.size()!=0){
			no_collection.setVisibility(View.GONE);
			adapter = new MyListAdapter(list,HistoryActivity.this);
			listView.setAdapter(adapter);

			tv_topbar_right_map_layout.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					TextView view = (TextView)tv_topbar_right_map_layout.findViewById(R.id.tv_topbar_right_edit1);

					if(view.getText().toString().equals("编辑")){
						bottom_bar.setVisibility(View.VISIBLE);
						view.setText("完成");
						adapter.setVisibility(View.VISIBLE);
						ButtonsOn = true;
						adapter.notifyDataSetChanged(); 
					}else{
						bottom_bar.setVisibility(View.GONE);
						view.setText("编辑");
						adapter.setVisibility(View.INVISIBLE);
						ButtonsOn = false;
						adapter.notifyDataSetChanged(); 
					}
				}
			});

			btn_select_all.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(select_all==false){
						select_all = true;
						for(int i=0;i<list.size();i++){
							MyListAdapter.getIsSelected().put(i, select_all);
						}
					}else{
						select_all = false ;
						for(int i=0;i<list.size();i++){
							MyListAdapter.getIsSelected().put(i, select_all);
						}
					}
					adapter.notifyDataSetChanged();			
				}
			});


			btn_cancel_all.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					for(int i=0;i<list.size();i++){
						MyListAdapter.getIsSelected().put(i, false);
					}
					adapter.notifyDataSetChanged();
				}
			});

			btn_delete.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Builder alertDialog = new AlertDialog.Builder(HistoryActivity.this);
					alertDialog.setTitle("提示").setMessage("删除会记录且无法恢复，是否删除？").setPositiveButton("确定", new DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							changeData();
							adapter.notifyDataSetChanged();
						}

					}).setNegativeButton("取消",null).show();



				}
			});

			btn_upload.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//批量上传
					List<Integer> selected = new ArrayList<Integer>();
					int location=0;
					for(int i=0;i<list.size();i++){
						if(MyListAdapter.getIsSelected().get(i)){
							location = i;
							selected.add(location);
						}
					}
					for (int j = 0; j < selected.size(); j++) {
						
						new Thread(new MyThread(selected.get(j))).start();
						
					}
					adapter.notifyDataSetChanged();					
				}
			});


		}else{
			listView.setVisibility(View.GONE);
			no_collection.setVisibility(View.VISIBLE);
			tv_topbar_right_map_layout.setClickable(false);
			tv_topbar_right_edit.setClickable(false);
			tv_topbar_right_edit.setTextColor(color.gray);
		}
		handler=new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				super.handleMessage(msg);
				switch (msg.what) {
				case 0:
					Toast.makeText(getApplicationContext(), "第"+msg.arg1+"项上传成功", Toast.LENGTH_SHORT).show();
					dao.updateUpLoadFlagHistoryById(Integer.parseInt(list.get(msg.arg1-1).get("id")));
					//list = dao.findHistory(Integer.parseInt(userId));
					//Collections.reverse(list);
					//adapter.notifyDataSetChanged();
					list = new ArrayList<HashMap<String,String>>();
					list = dao.findHistory(Integer.parseInt(userId));
					Collections.reverse(list);
					
					adapter = new MyListAdapter(list, HistoryActivity.this);
					adapter.setVisibility(View.VISIBLE);
					listView.setAdapter(adapter);

					if(list.size()==0){
						no_collection.setVisibility(View.VISIBLE);
						tv_topbar_right_map_layout.setClickable(false);
						tv_topbar_right_edit.setClickable(false);
						tv_topbar_right_edit.setText("编辑");
						tv_topbar_right_edit.setTextColor(color.gray);
						bottom_bar.setVisibility(View.GONE);
					}
					break;

				default:
					break;
				}
			}
			
		};
	}



	private void changeData(){
		List<Integer> selectedItem = new ArrayList<Integer>();
		for(int i=0;i<list.size();i++){
			if(MyListAdapter.getIsSelected().get(i)){
				selectedItem.add(i);
			}
		}
		
		for(int j=0;j<selectedItem.size();j++){
			
			String id = list.get(selectedItem.get(j)).get("id");
			dao.deleteHistoryById(Integer.parseInt(id));
		}

		list = new ArrayList<HashMap<String,String>>();
		list = dao.findHistory(Integer.parseInt(userId));
		Collections.reverse(list);
		
		adapter = new MyListAdapter(list, HistoryActivity.this);
		adapter.setVisibility(View.VISIBLE);
		listView.setAdapter(adapter);

		if(list.size()==0){
			no_collection.setVisibility(View.VISIBLE);
			tv_topbar_right_map_layout.setClickable(false);
			tv_topbar_right_edit.setClickable(false);
			tv_topbar_right_edit.setText("编辑");
			tv_topbar_right_edit.setTextColor(color.gray);
			bottom_bar.setVisibility(View.GONE);
		}
	}
	
	class MyThread implements Runnable{

		int i;
		
		public MyThread(int i){
			this.i = i+1;
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message msg=Message.obtain();
			msg.what= 0;
			msg.arg1= i;
			handler.sendMessage(msg);
		}
		
	}

}
