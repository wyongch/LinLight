package com.LinLight.app.ui;

import java.util.ArrayList;
import java.util.Random;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.LinLight.app.BaseActivity;
import com.LinLight.app.R;
import com.LinLight.dao.DBUtils;
import com.LinLight.model.Scene;
import com.LinLight.model.Timer;
import com.LinLight.util.LogUtils;

/**
 * 
 * @ClassName:  SettingTimerActivity   
 * @Description:TODO
 * @author: jack hooke  
 * @date:   2014年9月3日 下午11:06:09   
 *
 */
public class SettingTimerActivity extends BaseActivity {
	
	/**
	 * 时间选择器
	 */
	private TimePicker timePicker;
	
	private RelativeLayout relayRepetition;
	/**
	 * 重复
	 */
	private TextView tvRepetition;
	/**
	 * 对象选择，动作选择
	 */
	private Spinner spinnerObject, spinnerActon, spinnerScene;
	private Button timerCancel, timerOk;
	
	private String type;
	private String hour, minute;
	private boolean isOpen;
	
	private String[] mItemRepeat = null;
	
	private RelativeLayout relay4;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.setting_timer);

		type = getIntent().getExtras().getString("type");
		
		if(type.equals("onItemClick")){
			
			hour = getIntent().getExtras().getString("hour");
			minute = getIntent().getExtras().getString("minute");
			isOpen = getIntent().getExtras().getBoolean("isOpen");
		}
		
		timePicker = (TimePicker) findViewById(R.id.timePicker1);
		timePicker.setIs24HourView(true);
		
		mItemRepeat = getResources().getStringArray(R.array.spinner_change_repeat);
		relayRepetition = (RelativeLayout) findViewById(R.id.relay_repetition);
		relayRepetition.setOnClickListener(new CheckBoxClickListener());
		
		tvRepetition = (TextView) findViewById(R.id.textView_change_repeat_tv);
		spinnerObject = (Spinner) findViewById(R.id.spinner_change_object);
		spinnerActon = (Spinner) findViewById(R.id.spinner_change_action);
		spinnerScene = (Spinner) findViewById(R.id.timer_spinner_scene);
		
		relay4 = (RelativeLayout) findViewById(R.id.timer_relay_4);
		
		timerCancel = (Button) findViewById(R.id.timer_cancel);
		timerOk = (Button) findViewById(R.id.timer_ok);
		
		if(!TextUtils.isEmpty(hour) && !TextUtils.isEmpty(minute)) {
			timePicker.setCurrentHour(Integer.valueOf(hour));
			timePicker.setCurrentMinute(Integer.valueOf(minute));
		}
		
		String[] mItemStrings = getResources().getStringArray(R.array.spinner_change_obj);
		ArrayAdapter<String> _Adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mItemStrings);
		spinnerObject.setAdapter(_Adapter);
		
		final String[] actionStrings = getResources().getStringArray(R.array.spinner_change_control);
		ArrayAdapter<String> actionAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, actionStrings);
		spinnerActon.setAdapter(actionAdapter);
		
		spinnerActon.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				if ("场景切换".equals(actionStrings[position])) {
					System.out.println("changjingqiehuan.");
					relay4.setVisibility(View.VISIBLE);
				} else {
					relay4.setVisibility(View.GONE);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
		
		//查看数据库中是否存在场景数据
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
		
		ArrayAdapter<String> sceneAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mItemScene);
		spinnerScene.setAdapter(sceneAdapter);
		
		timerCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				finish();
			}
		});
		
		timerOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				DBUtils db = new DBUtils(SettingTimerActivity.this);
				
				Timer timer = new Timer();
				
				LogUtils.i("timerPicker.getCurrentHour:"+timePicker.getCurrentHour());
				LogUtils.i("timePicker.getCurrentMinute():"+timePicker.getCurrentMinute());
				
				timer.setId(timer.hashCode());
				timer.setHm(timePicker.getCurrentHour()+":"+timePicker.getCurrentMinute());
				timer.setOpen(isOpen);
				timer.setWeek(1);
				timer.setAction(2);
				timer.setScene(1);
				
				db.insertTimer(timer);
				
				finish();
				
			}
		});
	}
	
	 private ListView areaCheckListView;
	 private boolean[] areaState=new boolean[]{true, false, false, false, false, false,false };
	
	 
	class CheckBoxClickListener implements OnClickListener{
		   @Override
		   public void onClick(View v) {
		    AlertDialog ad = new AlertDialog.Builder(SettingTimerActivity.this)
		    .setTitle("重复")
		    .setMultiChoiceItems(mItemRepeat,areaState,new DialogInterface.OnMultiChoiceClickListener(){
		       public void onClick(DialogInterface dialog,int whichButton, boolean isChecked){
		        //点击某个区域
		       }
		      }).setPositiveButton("确定",new DialogInterface.OnClickListener(){
		       public void onClick(DialogInterface dialog,int whichButton){
		        String s = "您选择了:";
		        for (int i = 0; i < mItemRepeat.length; i++){
		         if (areaCheckListView.getCheckedItemPositions().get(i)){
		          s += i + ":"+ areaCheckListView.getAdapter().getItem(i)+ "  ";
		         }else{
		          areaCheckListView.getCheckedItemPositions().get(i,false);
		         }
		        }
		        if (areaCheckListView.getCheckedItemPositions().size() > 0){
		         Toast.makeText(SettingTimerActivity.this, s, Toast.LENGTH_LONG).show();
		        }else{
		          //没有选择
		        }
		        dialog.dismiss();
		       }
		      }).setNegativeButton("取消", null).create();
		    areaCheckListView = ad.getListView();
		    ad.show();
		   }
		}

}
