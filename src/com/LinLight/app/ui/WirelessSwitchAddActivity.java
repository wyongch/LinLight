package com.LinLight.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.Toast;

import com.LinLight.app.BaseActivity;
import com.LinLight.app.R;
import com.LinLight.dao.DBUtils;
import com.LinLight.global.AppData;
import com.LinLight.model.Switch;

/**
 * 
 * 增加一个无线开关
 *
 */
public class WirelessSwitchAddActivity extends BaseActivity {

	private ImageView imgBackground = null;
	private Button btnCancel, btnConfirm;
	
	/**
	 * 开关ID
	 */
	private String switchID;
	
	/**
	 * 开关类型，1=1键，2=2键，4=4键
	 */
	private int switchType = 1;
	
	private final int RESULT_OK = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_wireless_switch_add);
		
		imgBackground = (ImageView) findViewById(R.id.tab_img);
		btnCancel = (Button) findViewById(R.id.wireless_switch_cancel);
		btnConfirm = (Button) findViewById(R.id.wireless_switch_ok);
		
		TabHost tabHost = (TabHost) findViewById(R.id.tabhost);
		tabHost.setup();

		tabHost.addTab(tabHost.newTabSpec("switch1").setIndicator("单键")
				.setContent(R.id.view1));
		tabHost.addTab(tabHost.newTabSpec("switch2").setIndicator("双键")
				.setContent(R.id.view2));
		tabHost.addTab(tabHost.newTabSpec("switch4").setIndicator("四建")
				.setContent(R.id.view3));
		
		btnConfirm.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
		
//				if (AppData.TEST) {
//					switchID = "switch1";
//					showToast("请确定按下了实体开关键");
//				}
				
				Intent intent = new Intent(WirelessSwitchAddActivity.this, SettingWirelessSwitchListActivity.class);
				
				Bundle b = new Bundle();
				b.putString("switchID", switchID);
				b.putInt("switchType", switchType);
				
//				Toast.makeText(WirelessSwitchAddActivity.this,"" + switchType, Toast.LENGTH_SHORT).show();
				
				intent.putExtras(b);
				
				DBUtils db = new DBUtils(WirelessSwitchAddActivity.this);
				
				Switch sw = new Switch();
				sw.setId(sw.hashCode());
				sw.setType(switchType);
				
				db.insertSwitch(sw);
				
				setResult(RESULT_OK, intent);
				
				finish();
			}
		});
		
		btnCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
						
				finish();
			}
		});
		
		tabHost.setOnTabChangedListener(new OnTabChangeListener() {
			
			@Override
			public void onTabChanged(String tabId) {
				
				if ("switch1".equals(tabId)) {
					imgBackground.setImageResource(R.drawable.switch1);
					switchType = 1;
					if (AppData.TEST) {
						switchID = "switch1";
					}
					
				} else if ("switch2".equals(tabId)) {
					imgBackground.setImageResource(R.drawable.switch2);
					switchType = 2;
					if (AppData.TEST) {
						switchID = "switch2";
					}
					
				} else if ("switch4".equals(tabId)) {
					imgBackground.setImageResource(R.drawable.switch4);
					switchType = 4;
					if (AppData.TEST) {
						switchID = "switch4";
					}
				}				
			}
		});
	}
	
	/**
	 * 监听实体开关按下后返回的ID
	 */
	private void requestSwitchID() {
		
		//添加请求或接收代码

	}

}
