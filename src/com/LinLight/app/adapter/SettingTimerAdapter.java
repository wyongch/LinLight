package com.LinLight.app.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;

import com.LinLight.app.R;

public class SettingTimerAdapter extends BaseAdapter {

	private Context mContext = null;
	private ArrayList<HashMap<String, Object>> list = null;

	public SettingTimerAdapter(Context context,
			ArrayList<HashMap<String, Object>> list) {

		this.mContext = context;
		this.list = list;

	}

	@Override
	public int getCount() {
		return this.list.size();
	}

	@Override
	public Object getItem(int position) {
		return this.list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		LayoutInflater inflater = LayoutInflater.from(mContext);
		final ViewHolderTime viewHolder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.timer_list_item, null);
			viewHolder = new ViewHolderTime();
			viewHolder.timerTitle = (TextView) convertView.findViewById(R.id.timer_textview_time);
			viewHolder.timerStatusStr  = (TextView) convertView.findViewById(R.id.timer_textview_status);
			viewHolder.timerSwitch = (Switch) convertView.findViewById(R.id.timer_switch);
			viewHolder.timerWeeks = (TextView) convertView.findViewById(R.id.timer_weeks);
			viewHolder.timerObj = (TextView) convertView.findViewById(R.id.timer_obj);
			viewHolder.timerAction = (TextView) convertView.findViewById(R.id.tiemr_action);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolderTime) convertView.getTag();
		}
		
		viewHolder.timerTitle.setText(this.list.get(position).get("hour").toString()+":"+this.list.get(position).get("minute").toString());
		boolean isOpen = Boolean.getBoolean(this.list.get(position).get("isopen").toString());
		
		if(isOpen) {
			viewHolder.timerStatusStr.setText("已开启");
			viewHolder.timerSwitch.setChecked(true);
		} else {
			viewHolder.timerStatusStr.setText("未开启");
			viewHolder.timerSwitch.setChecked(false);
		}
		
		
		viewHolder.timerWeeks.setText(mContext.getResources().getStringArray(R.array.spinner_change_repeat)[position%mContext.getResources().getStringArray(R.array.spinner_change_repeat).length]);
		viewHolder.timerObj.setText(this.list.get(position).get("obj").toString());
		viewHolder.timerAction.setText(mContext.getResources().getStringArray(R.array.spinner_change_control)[position%mContext.getResources().getStringArray(R.array.spinner_change_control).length]);
		
		viewHolder.timerSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
				if(isChecked){
					
					viewHolder.timerStatusStr.setText("已开启");
					
				} else {
					
					viewHolder.timerStatusStr.setText("已关闭");
					
				}
			}
		});
		
		return convertView;
	}
}

// 自定义显示闹钟列表
class ViewHolderTime {
	public TextView timerTitle;
	public TextView timerStatusStr;
	public TextView timerWeeks;
	public TextView timerObj;
	public TextView timerAction;
	public Switch timerSwitch;
}

