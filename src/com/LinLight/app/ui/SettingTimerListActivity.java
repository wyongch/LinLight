package com.LinLight.app.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.LinLight.app.BaseActivity;
import com.LinLight.app.R;
import com.LinLight.app.adapter.SettingTimerAdapter;
import com.LinLight.dao.DBUtils;
import com.LinLight.model.Timer;
import com.LinLight.util.LogUtils;

/**
 * 定时器设置列表
 * @author Administrator
 *
 */
public class SettingTimerListActivity extends BaseActivity {
	
	private static final int STATUS_LOADING_DATA_SUCCESS = 1;
	private static final int STATUS_LOADING_DATA_FAILURE = STATUS_LOADING_DATA_SUCCESS << 1;
	
	private ArrayList<HashMap<String,Object>> timerList = new ArrayList<HashMap<String,Object>>();
	private ListView listView = null;
	private SettingTimerAdapter adapter = null;
	private Button settingAddTimer = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.setting_timer_list);
		
		listView = (ListView) findViewById(R.id.setting_timer_listview);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Intent intent = new Intent(SettingTimerListActivity.this, SettingTimerActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("type", "onItemClick");
				bundle.putString("hour", timerList.get(position).get("hour").toString());
				bundle.putString("minute", timerList.get(position).get("minute").toString());
				bundle.putBoolean("isOpen", (Boolean) timerList.get(position).get("isopen"));
				intent.putExtras(bundle);
				startActivity(intent);
				System.out.println("postion:"+position);
			}
		});
		
		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				
				showDialog("温馨提示：", "您确认删除此项吗？", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						
						DBUtils db = new DBUtils(SettingTimerListActivity.this);
						
						int id = (Integer) timerList.get(position).get("id");
						if (db.queryTimerByID(id+"")) {
							db.deleteTimerById(id);
						}
						
						timerList.remove(position);
						adapter.notifyDataSetChanged();
					}
				});
				
				return false;
			}
			
		});
		
		((Button) findViewById(R.id.setting_timer_back)).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		settingAddTimer = (Button) findViewById(R.id.setting_timer_add);
		
		settingAddTimer.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				DBUtils db = new DBUtils(SettingTimerListActivity.this);
				List<Timer> list = db.getAllTimer();
				LogUtils.i("timerlsit's size:"+list.size());																					
				if (list.size() < 8) {
				
				Intent intent = new Intent(SettingTimerListActivity.this, SettingTimerActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("type", "onItemClick");
				bundle.putString("hour", "");
				bundle.putString("minute", "");
				bundle.putBoolean("isOpen", false);
				intent.putExtras(bundle);
				startActivity(intent);
				}else{
					Toast.makeText(SettingTimerListActivity.this, "提示：定时器个数不能超过8个", 3000).show();
				}
				
					
			}
		});
	}
	
	private Handler timerHandler =  new Handler(){
		
		public void handleMessage(android.os.Message msg) {
			
			switch (msg.what) {
			case STATUS_LOADING_DATA_SUCCESS:
				
				adapter = new SettingTimerAdapter(SettingTimerListActivity.this, timerList);
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
	
	private void initData() {

		timerList.clear();
		DBUtils db = new DBUtils(this);
		List<Timer> list = db.getAllTimer();
		LogUtils.i("timerlsit's size:"+list.size());																					
		if (list.size() > 0) {

			for (int i = 0; i < list.size(); i++) {

				HashMap<String, Object> map = new HashMap<String, Object>();

				if (!TextUtils.isEmpty(list.get(i).hm)) {
					map.put("hour", list.get(i).hm.split(":")[0]);
					map.put("minute", list.get(i).hm.split(":")[1]);
				}
				
				map.put("id", list.get(i).getId());
				map.put("isopen", list.get(i).isOpen);
				map.put("weeks", list.get(i).week);
				map.put("lightJson", list.get(i).lightsJson);
				map.put("obj", "灯1");
				map.put("action", list.get(i).action);
				map.put("scene", list.get(i).scene);

				timerList.add(map);

			}
			timerHandler.sendEmptyMessage(STATUS_LOADING_DATA_SUCCESS);
		}

	}

}
