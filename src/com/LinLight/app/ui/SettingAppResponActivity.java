package com.LinLight.app.ui;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.LinLight.app.BaseActivity;
import com.LinLight.app.R;
import com.LinLight.dao.DBUtils;
import com.LinLight.model.AppLinkage;
import com.LinLight.model.Scene;

/**
 * 应用联动设置
 * @author Administrator
 *
 */
public class SettingAppResponActivity extends BaseActivity {
	
	/**
	 * 应用类型，对象，动作
	 */
	private Spinner spinnerApp, spinnerobj, spinnerAction, spinnerScene;
	private Button appResponCancel, appResponOk;
	private RelativeLayout relay4 = null;
	
	private int type, obj, action,scene;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.setting_app_respon);
		
		spinnerApp = (Spinner) findViewById(R.id.app_respon_spinner_change_app);
		spinnerAction = (Spinner) findViewById(R.id.app_respon_spinner_action);
		spinnerobj = (Spinner) findViewById(R.id.app_respon_spinner_change_obj);
		spinnerScene = (Spinner) findViewById(R.id.app_respon_spinner_scene);
		appResponCancel = (Button) findViewById(R.id.app_respon_cancel);
		appResponOk = (Button) findViewById(R.id.app_respon_ok);
		
		relay4 = (RelativeLayout) findViewById(R.id.relay_4);
		
		String[] mItemApp = getResources().getStringArray(R.array.spinner_change_app);
		ArrayAdapter<String> _Adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mItemApp);
		spinnerApp.setAdapter(_Adapter);
		spinnerApp.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				type = arg2;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
			
		});
		
		final String[] mItemAction = getResources().getStringArray(R.array.spinner_change_control);
		ArrayAdapter<String> actionAdapter =new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mItemAction);
		spinnerAction.setAdapter(actionAdapter);
		
		String[] mItemObj = getResources().getStringArray(R.array.spinner_change_obj );
		ArrayAdapter<String> objAdapter =new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mItemObj);
		spinnerobj.setAdapter(objAdapter);
		
		spinnerAction.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				
				action = position;

				if ("场景切换".equals(mItemAction[position])) {
					System.out.println("changjingqiehuan.");
					relay4.setVisibility(View.VISIBLE);
				} else {
					relay4.setVisibility(View.GONE);
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		String[] mItemScene = null;
		
		DBUtils dbUtils = new DBUtils(this);
		ArrayList<Scene> scenes = dbUtils.getAllScene();
		int sceneListSize = scenes.size();
		
		if (sceneListSize > 0) {
			
			mItemScene = new String[sceneListSize];
			
			for (int i = 0; i < sceneListSize; i++) {
				mItemScene[i] = scenes.get(i).getName();
			}
			
		} else {
			
			mItemScene = getResources().getStringArray(R.array.spinner_change_scene);
		}
		
		ArrayAdapter<String> sceneAdapter =new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mItemScene);
		spinnerScene.setAdapter(sceneAdapter);
		spinnerScene.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				scene  = arg2;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
			
			
		});
		
		appResponCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				finish();
			}
		});
		
		appResponOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				DBUtils db = new DBUtils(SettingAppResponActivity.this);
				
				AppLinkage app = new AppLinkage();
				app.setId(app.hashCode());
				app.setType(type);
				app.setScene(scene);
				app.setAction(action);
				
				db.insertApprespons(app);
				
				finish();
				
			}
		});
	}

}
