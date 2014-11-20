package com.LinLight.app.ui;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.LinLight.app.BaseActivity;
import com.LinLight.app.R;
import com.LinLight.app.ui.MainFragment.MainSceneElementView;
import com.LinLight.dao.DBUtils;
import com.LinLight.model.Scene;
import com.LinLight.util.LogUtils;

public class SettingWirelessActivity extends BaseActivity implements
		View.OnClickListener {

	/**
	 * 对象选择、控制方式、触发场景
	 */
	private Spinner spinnerLight, spinnerControl, spinnerScene;  //定义下拉列表
	private Button wirelessCancel, wirelessOk;	//定义确定，取消按钮

	private final int RESULT_OK = 1;
	private RelativeLayout relay_3 = null;

	private int mLight, mcontrol, mScene;

	private String id, type;
	private int[] switchStatus ={0,0,0,0} ;
	
	//四个开关
	private ImageView iv_switch4_type1;
	private ImageView iv_switch4_type2;
	private ImageView iv_switch4_type3;
	private ImageView iv_switch4_type4;
	private ImageView wireless_switch_img;
	//两个开关
	private ImageView iv_switch2_type1;
	private ImageView iv_switch2_type2;
	//一个开关
	private ImageView iv_switch1_type1;
	
	//开关描述，上下左右
	private TextView tv_switch_type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.setting_wireless_switch);

		spinnerLight = (Spinner) findViewById(R.id.wireless_spinner_change_light);
		spinnerControl = (Spinner) findViewById(R.id.wireless_spinner_change_control);
		spinnerScene = (Spinner) findViewById(R.id.wireless_spinner_change_scene);
		
		tv_switch_type = (TextView) findViewById(R.id.wireless_switch_textview_direction);
		
		//一个开关view
		iv_switch1_type1 = (ImageView) findViewById(R.id.wireless_switch1_img1);
		
		//两个开关view
		iv_switch2_type1 = (ImageView) findViewById(R.id.wireless_switch2_img1);
		iv_switch2_type2 = (ImageView) findViewById(R.id.wireless_switch2_img2);
		
		//四个开关view
		iv_switch4_type1 = (ImageView) findViewById(R.id.wireless_switch4_img1);
		iv_switch4_type2 = (ImageView) findViewById(R.id.wireless_switch4_img2);
		iv_switch4_type3 = (ImageView) findViewById(R.id.wireless_switch4_img3);
		iv_switch4_type4 = (ImageView) findViewById(R.id.wireless_switch4_img4);

		id = getIntent().getExtras().getString("id");
		type = getIntent().getExtras().getString("type");
		
//		Toast.makeText(this, "" + type,
//				Toast.LENGTH_SHORT).show();

		LogUtils.i("id:" + id);
		LogUtils.i("type:" + type);
		
//		wireless_switch_img = (ImageView)findViewById(R.id.wireless_switch);	
//		wireless_switch_img.setImageResource(R.drawable.switch1);
//		wireless_switch_img.setBackgroundResource(R.drawable.switch1);

		 if("1".equals(type)){	 

//			 setContentView(R.layout.setting_wireless_switch);  
		        final LayoutInflater inflater = LayoutInflater.from(this);  
		        // 获取需要被添加控件的布局  
		        final RelativeLayout relay_5 = (RelativeLayout) findViewById(R.id.relay_5);  
		        // 获取需要添加的布局  
		        RelativeLayout layout = (RelativeLayout) inflater.inflate(  
		                R.layout.switch_1, null).findViewById(R.id.wireless_switch1);  
		        // 将布局加入到当前布局中  
		        relay_5.addView(layout);  
			 
			
			 
//		 wireless_switch_img.setImageResource(R.drawable.switch1);
		 LogUtils.i("setImage111111:"+id);
		 } else if("2".equals(type)){
			 
//			 setContentView(R.layout.setting_wireless_switch);  
		        final LayoutInflater inflater = LayoutInflater.from(this);  
		        // 获取需要被添加控件的布局  
		        final RelativeLayout relay_5 = (RelativeLayout) findViewById(R.id.relay_5);  
		        // 获取需要添加的布局  
		        LinearLayout layout = (LinearLayout) inflater.inflate(  
		                R.layout.switch_2, null).findViewById(R.id.wireless_switch2);  
		        // 将布局加入到当前布局中  
		        relay_5.addView(layout);  
			 
//			 iv_switch2_type1 = (ImageView) findViewById(R.id.wireless_switch2_img1);
//			 iv_switch2_type1.setImageResource(R.drawable.switch2_left);
//			 iv_switch2_type2 = (ImageView) findViewById(R.id.wireless_switch2_img2);
//			 iv_switch2_type2.setImageResource(R.drawable.switch2_right);
			
			 
			
			 
//		 wireless_switch_img.setImageResource(R.drawable.switch2);
		 
		 LogUtils.i("setImage22222:"+id);
		 } else if("4".equals(type)){
			 
//			 setContentView(R.layout.setting_wireless_switch);  
		        final LayoutInflater inflater = LayoutInflater.from(this);  
		        // 获取需要被添加控件的布局  
		        final RelativeLayout relay_5 = (RelativeLayout) findViewById(R.id.relay_5);  
		        // 获取需要添加的布局  
		        LinearLayout layout = (LinearLayout) inflater.inflate(  
		                R.layout.switch_4, null).findViewById(R.id.wireless_switch4);  
		        // 将布局加入到当前布局中  
		        relay_5.addView(layout);  

//		 wireless_switch_img.setImageResource(R.drawable.switch4);
		 LogUtils.i("setImage44444:"+id);
		 }

		relay_3 = (RelativeLayout) findViewById(R.id.relay_3);
    

		wirelessCancel = (Button) findViewById(R.id.wireless_switch_cancel);
		wirelessOk = (Button) findViewById(R.id.wireless_switch_ok);

		String[] mItemSpinnerLight = getResources().getStringArray(
				R.array.spinner_change_light);
		ArrayAdapter<String> spinnerLightAdapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item, mItemSpinnerLight);
		spinnerLight.setAdapter(spinnerLightAdapter);
		spinnerLight.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				mLight = arg2;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});

		final String[] mItemSpinnerControl = getResources().getStringArray(
				R.array.spinner_change_control);
		ArrayAdapter<String> spinnerControlAdapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item, mItemSpinnerControl);
		spinnerControl.setAdapter(spinnerControlAdapter);
		spinnerControl.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {

				mcontrol = position;

				if ("场景切换".equals(mItemSpinnerControl[position])) {
					System.out.println("changjingqiehuan.");
					relay_3.setVisibility(View.VISIBLE);
				} else {
					relay_3.setVisibility(View.GONE);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		
		String[] mItemSpinnerScene = null;
		
		DBUtils dbUtils = new DBUtils(this);
		ArrayList<Scene> scenes = dbUtils.getAllScene();
		int sceneListSize = scenes.size();
		
		if (sceneListSize > 0) {
			
			mItemSpinnerScene = new String[sceneListSize];
			
			for (int i = 0; i < sceneListSize; i++) {
				mItemSpinnerScene[i] = scenes.get(i).getName();
			}
			
		} else {
			
			mItemSpinnerScene = getResources().getStringArray(R.array.spinner_change_scene);
		}
		
		ArrayAdapter<String> sceneAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, mItemSpinnerScene);
		spinnerScene.setAdapter(sceneAdapter);
		spinnerScene.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				mScene = arg2;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}

		});

		wirelessCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				finish();
			}
		});

		wirelessOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// setResult(RESULT_OK, intent);

				finish();
			}
		});
	}

	/**
	 * <p>
	 * Title: onClick
	 * </p>
	 * <p>
	 * Description:
	 * </p>
	 * 
	 * @param v
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		//一个开关的事件响应
		case R.id.wireless_switch1_img1:
			iv_switch1_type1 = (ImageView) findViewById(R.id.wireless_switch1_img1);
			
			if(iv_switch1_type1.getDrawable()==null){
				iv_switch1_type1.setImageResource(R.drawable.finger1);
			}else {
				iv_switch1_type1.setImageResource(0);
				iv_switch1_type1.setImageDrawable(null);
			}
			break;
		
		//两个开关事件响应
		case R.id.wireless_switch2_img1:
			
			iv_switch2_type1 = (ImageView) findViewById(R.id.wireless_switch2_img1);
			iv_switch2_type2 = (ImageView) findViewById(R.id.wireless_switch2_img2);
			if(iv_switch2_type1.getDrawable()==null){
				iv_switch2_type1.setImageResource(R.drawable.finger2);
				
				iv_switch2_type2.setImageResource(0);
				iv_switch2_type2.setImageDrawable(null);
			}else {
				iv_switch2_type1.setImageResource(0);
				iv_switch2_type1.setImageDrawable(null);
			}
			
			
			
			break ;
			
		case R.id.wireless_switch2_img2:
			iv_switch2_type2 = (ImageView) findViewById(R.id.wireless_switch2_img2);
			iv_switch2_type1 = (ImageView) findViewById(R.id.wireless_switch2_img1);
			if(iv_switch2_type2.getDrawable()==null){
				iv_switch2_type2.setImageResource(R.drawable.finger2);
				
				iv_switch2_type1.setImageResource(0);
				iv_switch2_type1.setImageDrawable(null);
			}else {
				iv_switch2_type2.setImageResource(0);
				iv_switch2_type2.setImageDrawable(null);
			}
			
			
			break ;
			
			
		//四个开关的事件响应
		case R.id.wireless_switch4_img1:
			tv_switch_type.setText("左上");	
			
			iv_switch4_type1 = (ImageView) findViewById(R.id.wireless_switch4_img1);
			iv_switch4_type2 = (ImageView) findViewById(R.id.wireless_switch4_img2);
			iv_switch4_type3 = (ImageView) findViewById(R.id.wireless_switch4_img3);
			iv_switch4_type4 = (ImageView) findViewById(R.id.wireless_switch4_img4);
		
			
			if (iv_switch4_type1.getDrawable()==null){	
			 iv_switch4_type1.setImageResource(R.drawable.finger);
			 
			 iv_switch4_type2.setImageResource(0);
			iv_switch4_type2.setImageDrawable(null);
			 
			iv_switch4_type3.setImageResource(0);
			iv_switch4_type3.setImageDrawable(null);	
			
			iv_switch4_type4.setImageResource(0);
			iv_switch4_type4.setImageDrawable(null);
			 
			}else{
				iv_switch4_type1.setImageResource(0);
				iv_switch4_type1.setImageDrawable(null);				
			}			

			break;
		case R.id.wireless_switch4_img2:
			
			tv_switch_type.setText("右上");
			
			iv_switch4_type1 = (ImageView) findViewById(R.id.wireless_switch4_img1);
			iv_switch4_type2 = (ImageView) findViewById(R.id.wireless_switch4_img2);
			iv_switch4_type3 = (ImageView) findViewById(R.id.wireless_switch4_img3);
			iv_switch4_type4 = (ImageView) findViewById(R.id.wireless_switch4_img4);
			
			
			if (iv_switch4_type2.getDrawable()==null){	
			 iv_switch4_type2.setImageResource(R.drawable.finger);
			 
			 iv_switch4_type1.setImageResource(0);
				iv_switch4_type1.setImageDrawable(null);
				 
				iv_switch4_type3.setImageResource(0);
				iv_switch4_type3.setImageDrawable(null);	
				
				iv_switch4_type4.setImageResource(0);
				iv_switch4_type4.setImageDrawable(null);
			 
			}else{
				iv_switch4_type2.setImageResource(0);
				iv_switch4_type2.setImageDrawable(null);				
			}

			break;
		case R.id.wireless_switch4_img3:
			
			tv_switch_type.setText("左下");
			iv_switch4_type1 = (ImageView) findViewById(R.id.wireless_switch4_img1);
			iv_switch4_type2 = (ImageView) findViewById(R.id.wireless_switch4_img2);
			iv_switch4_type3 = (ImageView) findViewById(R.id.wireless_switch4_img3);
			iv_switch4_type4 = (ImageView) findViewById(R.id.wireless_switch4_img4);
			if (iv_switch4_type3.getDrawable()==null){	
			 iv_switch4_type3.setImageResource(R.drawable.finger);
			 
			 iv_switch4_type2.setImageResource(0);
				iv_switch4_type2.setImageDrawable(null);
				 
				iv_switch4_type1.setImageResource(0);
				iv_switch4_type1.setImageDrawable(null);	
				
				iv_switch4_type4.setImageResource(0);
				iv_switch4_type4.setImageDrawable(null);
			 
			}else{
				iv_switch4_type3.setImageResource(0);
				iv_switch4_type3.setImageDrawable(null);				
			}
			
//			 iv_switch4_type3 = (ImageView) findViewById(R.id.wireless_switch4_img3);
//			 iv_switch4_type3.setImageResource(R.drawable.finger);
//			
//			Toast.makeText(this, id+"左下", Toast.LENGTH_SHORT).show();
//			iv_switch4_type3.setImageResource(R.drawable.finger);
//			tv_switch_type.setText("nihao");

			break;
		case R.id.wireless_switch4_img4:
			
			tv_switch_type.setText("右下");
			
			iv_switch4_type1 = (ImageView) findViewById(R.id.wireless_switch4_img1);
			iv_switch4_type2 = (ImageView) findViewById(R.id.wireless_switch4_img2);
			iv_switch4_type3 = (ImageView) findViewById(R.id.wireless_switch4_img3);
			iv_switch4_type4 = (ImageView) findViewById(R.id.wireless_switch4_img4);
			if (iv_switch4_type4.getDrawable()==null){	
			 iv_switch4_type4.setImageResource(R.drawable.finger);
			 
			 iv_switch4_type2.setImageResource(0);
				iv_switch4_type2.setImageDrawable(null);
				 
				iv_switch4_type3.setImageResource(0);
				iv_switch4_type3.setImageDrawable(null);	
				
				iv_switch4_type1.setImageResource(0);
				iv_switch4_type1.setImageDrawable(null);
			 
			}else{
				iv_switch4_type4.setImageResource(0);
				iv_switch4_type4.setImageDrawable(null);				
			}

			break;
		default:
			break;
		}
	}

}
