package com.LinLight.app.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;

import com.LinLight.app.BaseActivity;
import com.LinLight.app.MyApplication;
import com.LinLight.app.R;
import com.LinLight.app.adapter.ListPopAdapter;
import com.LinLight.ble.ConnectionManager;
import com.LinLight.ble.LinlightDevice;
import com.LinLight.dao.DBUtils;
import com.LinLight.global.Constants;
import com.LinLight.global.MenuItem;
import com.LinLight.model.Scene;
import com.LinLight.util.LogUtils;
import com.LinLight.util.SendThread;
import com.LinLight.view.SlidingMenu.SlidingMenu;
import com.LinLight.view.titlebar.TitleBar;
import com.LinLight.ble.ConnectionManager;
import com.LinLight.util.SendThread;
import com.LinLight.app.ui.HomePageActivity.MyOnGestureListener;
import com.LinLight.app.ui.HomePageActivity;
//import com.LinLight.constants.Constants;
import com.LinLight.model.OpenClose;
import com.LinLight.util.ActionUtil;
//import com.LinLight.util.SysApplication;
import com.LinLight.ble.LinlightDevice;

public class HomePageActivity extends BaseActivity {

	//private List<LinlightDevice> connected_light;
	private GestureDetector mGestureDetector = null;// 手势识别
	private com.LinLight.view.SlidingMenu.SlidingMenu menu;
	private BluetoothAdapter mBluetoothAdapter;
	private static final int REQUEST_ENABLE_BT = 1;
//	public static ConnectionManager connectionManager1;// 连接管理，是一个单实例
	// 10秒后停止查找搜索.
	private static final long SCAN_PERIOD = 100000;
//	private Handler mHandler;// 消息传递
//	public static Queue<Integer> queue;// 存放所有控制命令的队列
//	private SendThread sThread;// 线程类，专门处理发送命令
	private MainFragment mainFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		SysApplication.getInstance().addActivity(this);// 保存所有Activity，退出
		mGestureDetector = new GestureDetector(new MyOnGestureListener());		
		setAbContentView(R.layout.sliding_menu_content);

		TitleBar mAbTitleBar = this.getTitleBar();
//		mAbTitleBar.setTitleText(R.string.app_name);
		mAbTitleBar.setLogo(R.drawable.button_selector_menu);
		mAbTitleBar.setTitleBarBackground(R.drawable.title_bar_background);
		mAbTitleBar.setTitleTextMargin(10, 0, 0, 0);
//		mAbTitleBar.setLogoLine(R.drawable.line);

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		if (dm.widthPixels < dm.heightPixels)// 视具体情况而定
		{
			Constants.SCREEN_WIDTH = dm.widthPixels;// 屏幕宽度
			Constants.SCREEN_HEIGHT = dm.heightPixels;// 屏幕高度
		} else {
			Constants.SCREEN_WIDTH = dm.heightPixels;
			Constants.SCREEN_HEIGHT = dm.widthPixels;
		}

		// 主视图的Fragment添加
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, new MainFragment()).commit();

		// SlidingMenu的配置
		menu = new SlidingMenu(this);
		menu.setMode(SlidingMenu.LEFT);

		// 屏蔽左右滑动
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setShadowDrawable(R.drawable.shadow);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.35f);
		menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);

		// menu视图的Fragment添加
		menu.setMenu(R.layout.sliding_menu_menu);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.menu_frame, new LeftFragment()).commit();

		mAbTitleBar.getLogoView().setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (menu.isMenuShowing()) {
					menu.showContent();
				} else {
					menu.showMenu();
				}
			}
		});

		initRightViews();
//		queue = new LinkedList<Integer>();// 队列初始化，用于存储命令

		//mHandler = new Handler();
     
//		connectionManager1 = ConnectionManager.getInstance(HomePageActivity.this);// 获取单实例
//		this.sThread = new SendThread(this);// 初始化发送命令的线程
//		this.sThread.start();// 开始发送命令的线程		
		
//		final BluetoothManager bluetoothManager =
//                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
//        mBluetoothAdapter = bluetoothManager.getAdapter();
//        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
//        // fire an intent to display a dialog asking the user to grant permission to enable it.
//        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
//            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
//            finish();
//        }     				
//		scanLeDevice(true);// 开始搜索BLE设备
//		queue.offer(0);
		Log.i("wwwwwww", "启动系统") ;
	}

	@Override
	protected void onResume() {
		super.onResume();
		initRightViews();
//		if (!mBluetoothAdapter.isEnabled()) {
//            if (!mBluetoothAdapter.isEnabled()) {
//               Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//               startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
//               System.out.println("error_bluetooth_not_enabled1");
//               //        	mBluetoothAdapter.enable();//also pop-up dialog box
//            }
//        }	
//		
		
		
		
		
		
		System.out.println("onResume");
	//	queue.offer(0); 
		//queue.offer(1);	
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		if (requestCode == REQUEST_ENABLE_BT){
//			if( resultCode == Activity.RESULT_CANCELED) {
//		    	System.out.println("error_bluetooth_not_enabled2");
//		    	finish(); //HomePageActivity是栈顶部的Activity，直接使用finish方法结束
//		        return;
//			}
//			else{
//				System.out.println("bluetooth_is_enabled");
//				 return;
//			}
//		}
	}	
	
	
	private void initRightViews(){
		
		mAbTitleBar.clearRightView();
		View rightViewApp = mInflater.inflate(R.layout.app_btn, null);
		mAbTitleBar.addRightView(rightViewApp);
		final Button appBtn = (Button) rightViewApp.findViewById(R.id.appBtn);

		appBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				View popView = mInflater.inflate(R.layout.list_pop, null);
				ListView popListView = (ListView) popView
						.findViewById(R.id.pop_list);
				List<MenuItem> list = new ArrayList<MenuItem>();
				list.add(new MenuItem("定时开关设置"));
				list.add(new MenuItem("无线开关设置"));
				list.add(new MenuItem("应用联动设置"));
				ListPopAdapter mListPopAdapter = new ListPopAdapter(
						HomePageActivity.this, list, R.layout.list_pop_item);
				popListView.setAdapter(mListPopAdapter);

				popListView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {

//						Toast.makeText(HomePageActivity.this, "" + position,
//								Toast.LENGTH_SHORT).show();

						switch (position) {
						case 0:

							startActivity(new Intent(HomePageActivity.this,
									SettingTimerListActivity.class));

							break;
						case 1:

							startActivity(new Intent(HomePageActivity.this,
									SettingWirelessSwitchListActivity.class));

							break;
						case 2:

							startActivity(new Intent(HomePageActivity.this,
									SettingAppResponsListActivity.class));

							break;
						default:
							break;
						}

					}
				});

				mAbTitleBar.showWindow(appBtn, popView, true);
			}
		});
	}

	@Override
	public void onBackPressed() {
		if (menu.isMenuShowing()) {
			menu.showContent();
		} else {
			super.onBackPressed();
		}
	}

	// 设置扫描
//	private void scanLeDevice(final boolean enable) {
//		if (enable) {
//			// Stops scanning after a pre-defined scan period.
//			mHandler.postDelayed(new Runnable() {
//				@Override
//				public void run() {
//					connectionManager1.stopLEScan();
//				}
//			}, SCAN_PERIOD);
//			System.out.println("start_scanLeDevice");
//			connectionManager1.startLEScan();
//		} else {
//			System.out.println("stop_scanLeDevice");
//			connectionManager1.stopLEScan();
//		}
//	}

	class MyOnGestureListener extends SimpleOnGestureListener {
		// 双击的第二下Touch down时触发
		public boolean onDoubleTap(MotionEvent event) {
			return true;
		}
	}
}
