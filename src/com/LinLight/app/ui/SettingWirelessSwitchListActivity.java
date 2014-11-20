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
import com.LinLight.app.adapter.SettingWirelessSwitchAdapter;
import com.LinLight.dao.DBUtils;
import com.LinLight.global.AppData;
import com.LinLight.model.Switch;

/**
 * 无线开关设置列表
 * @author Administrator
 *
 */
public class SettingWirelessSwitchListActivity extends BaseActivity {
	
	private static final int STATUS_LOADING_DATA_SUCCESS = 1;
	private static final int STATUS_LOADING_DATA_FAILURE = STATUS_LOADING_DATA_SUCCESS << 1;
	
	private ArrayList<HashMap<String, Object>> wirelessSwitchList = new ArrayList<HashMap<String,Object>>();
	private ListView listView = null;
	private SettingWirelessSwitchAdapter adapter = null;
	private Button settingAddWirelessSwitch = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.setting_wireless_switch_list);
		
		listView = (ListView) findViewById(R.id.setting_wireless_switch_listview);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				System.out.println("postion:"+position);
				Intent intent = new Intent(SettingWirelessSwitchListActivity.this, SettingWirelessActivity.class);
				Bundle b = new Bundle();
				b.putString("id", wirelessSwitchList.get(position).get("id").toString());
				b.putString("type", wirelessSwitchList.get(position).get("type").toString());
				intent.putExtras(b);
//				Toast.makeText(SettingWirelessSwitchListActivity.this, "" + wirelessSwitchList.get(position).get("type").toString(),
//						Toast.LENGTH_SHORT).show();
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
						
						DBUtils db = new DBUtils(SettingWirelessSwitchListActivity.this);
						
						int id = (Integer) wirelessSwitchList.get(position).get("id");
						if (db.querySwitchByID(id+"")) {
							db.deleteSwitchById(id);
						}
						
						wirelessSwitchList.remove(position);
						adapter.notifyDataSetChanged();
					}
				});
				
				return false;
			}
			
		});
		
		settingAddWirelessSwitch = (Button) findViewById(R.id.setting_wireless_switch_add);
		settingAddWirelessSwitch.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				DBUtils db = new DBUtils(SettingWirelessSwitchListActivity.this);
				
				List<Switch> list = db.getAllSwitch();
				
				if(list.size() < 6){
				
				startActivityForResult(new Intent(SettingWirelessSwitchListActivity.this, WirelessSwitchAddActivity.class), 0);
				}
				else{
					Toast.makeText(SettingWirelessSwitchListActivity.this, "提示：无线开关个数不能超过6个", Toast.LENGTH_LONG).show();
				}
			}
		});
		
		((Button) findViewById(R.id.setting_wireless_switch_back)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		if (AppData.TEST) {
			
			
		}
	}
	
	private Handler wirelessSwitchHandler =  new Handler(){
		
		public void handleMessage(android.os.Message msg) {
			
			switch (msg.what) {
			case STATUS_LOADING_DATA_SUCCESS:
				
				adapter = new SettingWirelessSwitchAdapter(SettingWirelessSwitchListActivity.this, wirelessSwitchList);
				listView.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				
				break;
			case STATUS_LOADING_DATA_FAILURE:
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
		
		wirelessSwitchList.clear();
		
		DBUtils db = new DBUtils(SettingWirelessSwitchListActivity.this);
		
		List<Switch> list = db.getAllSwitch();
		
		if(list.size() > 0) {
			
			for(int i=0;i<list.size();i++){
				
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("name", "灯"+1);
				map.put("id", list.get(i).getId());
				map.put("type", list.get(i).getType());
				wirelessSwitchList.add(map);
				
			}
		}
		
		wirelessSwitchHandler.sendEmptyMessage(STATUS_LOADING_DATA_SUCCESS);
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
			String id = bundle.getString("switchID");
			String name = id;
			int type = bundle.getInt("switchType");
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("name", name);
			map.put("id", id);
			map.put("type", type);
			wirelessSwitchList.add(map);
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
