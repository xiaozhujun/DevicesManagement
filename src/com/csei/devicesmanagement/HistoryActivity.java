package com.csei.devicesmanagement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.security.auth.PrivateCredentialPermission;

import com.csei.adapter.HistoryAdapter;
import com.csei.application.MyApplication;
import com.csei.database.entity.Install;
import com.csei.database.entity.StockIn;
import com.csei.database.entity.StockOut;
import com.csei.database.entity.Transport;
import com.csei.database.entity.Uninstall;
import com.csei.database.entity.service.imple.InstallServiceDao;
import com.csei.database.entity.service.imple.StockInServiceDao;
import com.csei.database.entity.service.imple.StockOutServiceDao;
import com.csei.database.entity.service.imple.TransportServiceDao;
import com.csei.database.entity.service.imple.UninstallServiceDao;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.Toast;

public class HistoryActivity extends Activity{
	
	private int userId; 
	private String[] groupName;
	
	private ExpandableListView expandableListView;
	
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
	
	private StockInServiceDao stockInServiceDao;
	private StockOutServiceDao stockOutServiceDao;
	private TransportServiceDao transportServiceDao;
	private InstallServiceDao installServiceDao;
	private UninstallServiceDao uninstallServiceDao;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		MyApplication.getInstance().addActivity(this);
		setContentView(R.layout.activity_history);
		userId = Integer.parseInt(getIntent().getStringExtra("userId"));
		
		expandableListView = (ExpandableListView) findViewById(R.id.expandlistview_history);
		
		initData();
		
		HistoryAdapter historyAdapter = new HistoryAdapter(HistoryActivity.this, groupName, stockInList, stockOutList, transportList, installList, uninstallList);
		expandableListView.setAdapter(historyAdapter);
	}

	private void initData() {
		
		groupName = new String []{"入库记录","出库记录","运输记录","安装记录","卸载记录"};
		stockInList = new ArrayList<HashMap<String, String>>();
		stockOutList = new ArrayList<HashMap<String, String>>();
		transportList = new ArrayList<HashMap<String, String>>();
		installList = new ArrayList<HashMap<String, String>>();
		uninstallList = new ArrayList<HashMap<String, String>>();
		
		stockInServiceDao = new StockInServiceDao(HistoryActivity.this);
		stockOutServiceDao = new StockOutServiceDao(HistoryActivity.this);
		transportServiceDao = new TransportServiceDao(HistoryActivity.this);
		installServiceDao = new InstallServiceDao(HistoryActivity.this);
		uninstallServiceDao = new UninstallServiceDao(HistoryActivity.this);
		
		stockInSelected = new ArrayList<Integer>();
		stockOutSelected = new ArrayList<Integer>();
		transportSelected = new ArrayList<Integer>();
		installSelected = new ArrayList<Integer>();
		uninstallSelected = new ArrayList<Integer>();
		
		stockInList = stockInServiceDao.findHistory(userId);
		stockOutList = stockOutServiceDao.findHistory(userId);
		transportList = transportServiceDao.findHistory(userId);
		installList = installServiceDao.findHistory(userId);
		uninstallList = uninstallServiceDao.findHistory(userId);
		
		
	}
}
