package com.LinLight.app.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.LinLight.app.BaseActivity;
import com.LinLight.app.R;
import com.LinLight.app.adapter.LightListAdapter;
import com.LinLight.ble.ConnectionManager;
import com.LinLight.ble.LinlightDevice;
import com.LinLight.model.Light;


public class LightListActivity extends BaseActivity {
	
	private Button scan_light = null;	
	private TextView list_light1= null;
	private Handler mHandler;// 消息传递	
	private BluetoothAdapter mBluetoothAdapter;	
	public static ConnectionManager connectionManager;// 连接管理，是一个单实例
	private static final long SCAN_PERIOD = 100000;	
	private static final int REQUEST_ENABLE_BT = 1;	
	String  MacAddr = "";
	
	private Button btnButton;
	private ListView lightListView;
	private LightListAdapter adapter;
	private ArrayList<Light> data = new ArrayList<Light>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.light_list);
		lightListView = (ListView) findViewById(R.id.light_listview);
		btnButton = (Button) findViewById(R.id.light_back);
		scan_light = (Button)findViewById(R.id.scan_light);
		
		mHandler = new Handler();
	     
//		init();
			
		adapter = new LightListAdapter(data, this);
		lightListView.setAdapter(adapter);
//		connectionManager = ConnectionManager.getInstance(LightListActivity.this);// 获取单实例	
//		final BluetoothManager bluetoothManager =
//                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
//        mBluetoothAdapter = bluetoothManager.getAdapter();
		
		btnButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
 	    				
		scan_light.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				System.out.println("scan light");
				list_light1.setVisibility(View.VISIBLE);
			//	light_list_scan.scanLeDevice(true);
				scanLeDevice(true);
				
			}
		});		
	}
	// 设置扫描
			private void scanLeDevice(final boolean enable) {
				if (enable) {
					// Stops scanning after a pre-defined scan period.
					mHandler.postDelayed(new Runnable() {
						@Override
						public void run() {
							connectionManager.stopLEScan();
						}
					}, SCAN_PERIOD);
					System.out.println("start_scanLeDevice");
					connectionManager.startLEScan();
				} else {
					System.out.println("stop_scanLeDevice");
					connectionManager.stopLEScan();
				}
			}
}
