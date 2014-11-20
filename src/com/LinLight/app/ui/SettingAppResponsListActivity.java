package com.LinLight.app.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.LinLight.app.BaseActivity;
import com.LinLight.app.R;
import com.LinLight.app.adapter.SettingAppResponsAdapter;
import com.LinLight.dao.DBUtils;
import com.LinLight.global.AppData;
import com.LinLight.model.AppLinkage;

/**
 * 无线开关设置列表
 * @author Administrator
 *
 */
public class SettingAppResponsListActivity extends BaseActivity {
	
	private ArrayList<HashMap<String, Object>> appRespons = new ArrayList<HashMap<String,Object>>();
	private ListView listView = null;
	private SettingAppResponsAdapter adapter = null;
	private Button settingAdd = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.setting_app_respons_list);
		
		listView = (ListView) findViewById(R.id.setting_app_respons_listview);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				System.out.println("postion:"+position);
				Intent intent = new Intent(SettingAppResponsListActivity.this, SettingAppResponActivity.class);
				
				startActivityForResult(intent, 1);
			}
		});
		
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				
				showDialog("温馨提示：", "您确认删除此项吗？", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						DBUtils db = new DBUtils(SettingAppResponsListActivity.this);
						
						int id = (Integer) appRespons.get(position).get("id");
						if (db.queryAppLinkageByID(id+"")) {
							db.deleteAppLinkageById(id);
						}
						
						appRespons.remove(position);
						adapter.notifyDataSetChanged();
					}
				});
				
				return false;
			}
			
		});
		
		((Button) findViewById(R.id.setting_app_respons_back)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		settingAdd = (Button) findViewById(R.id.setting_app_respons_add);
		settingAdd.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				List<AppLinkage> list = new DBUtils(SettingAppResponsListActivity.this).getAllAppLinkages();
				
				if(list.size() < 8){
				
				startActivityForResult(new Intent(SettingAppResponsListActivity.this, SettingAppResponActivity.class), 0);
				}else{
					Toast.makeText(SettingAppResponsListActivity.this, "注意：应用联动设置不能超过8个", Toast.LENGTH_LONG).show();
				}
			}
		});
		
		if (AppData.TEST) {
			
//			initData();
		}
	}
	
	private Handler wirelessSwitchHandler =  new Handler(){
		
		public void handleMessage(android.os.Message msg) {
			
			switch (msg.what) {
			case 0:
				
				adapter = new SettingAppResponsAdapter(SettingAppResponsListActivity.this, appRespons);
				listView.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				
				break;
			default:
				break;
			}
		};
	};
	
	protected void onResume() {
		
		super.onResume();
		
		initData();
	};
	
	private void initData(){
		
		appRespons.clear();
		
		List<AppLinkage> list = new DBUtils(this).getAllAppLinkages();
		
		if(list.size() > 0){
			
			for(AppLinkage app : list) {
				
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("id", app.getId());
				map.put("name", getResources().getStringArray(R.array.spinner_change_app)[app.type]);
				map.put("lights", "两个灯");
				map.put("status", true);
				map.put("action", getResources().getStringArray(R.array.spinner_change_control)[app.action]);
				
				appRespons.add(map);
			}
		}
		
		
		wirelessSwitchHandler.sendEmptyMessage(0);
	}
	
	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
		
		switch (arg0) {
		case 0:
			//增加一个新开关返回数据
			
			if (arg2 == null) {
				return;
			}
			
			Bundle bundle = arg2.getExtras();
			String name = bundle.getString("name");
			String obj = bundle.getString("obj");
			String action = bundle.getString("action");
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("name", name);
			map.put("action", action);
			map.put("lights", obj);
			appRespons.add(map);
			adapter.notifyDataSetChanged();
			
			break;
		case 1:
			//编辑一个开关返回数据
			
			
			
			break;
			
		case -1:
			break;
		}
	}

}
