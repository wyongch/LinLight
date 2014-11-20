package com.LinLight.app.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.LinLight.app.R;

public class SettingAppResponsAdapter extends BaseAdapter {

	private Context mContext = null;
	private ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();

	public SettingAppResponsAdapter(Context context, ArrayList<HashMap<String, Object>> list) {

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
		final ViewHolderAppRespons viewHolder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.app_respons_list_item,
					null);
			viewHolder = new ViewHolderAppRespons();
			viewHolder.appResponsSMS = (TextView) convertView.findViewById(R.id.app_respons_textview_sms);
			viewHolder.appResponsSwitchImg = (Switch) convertView.findViewById(R.id.app_respons_switch);
			viewHolder.appResponsLights = (TextView) convertView.findViewById(R.id.app_respons_textview_lights);
			viewHolder.appResponsAction = (TextView) convertView.findViewById(R.id.app_respons_textview_action);
			viewHolder.appResponsStatus = (TextView) convertView.findViewById(R.id.app_respons_textview_status);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolderAppRespons) convertView.getTag();
		}

		viewHolder.appResponsSMS.setText(this.list.get(position).get("name").toString());
		viewHolder.appResponsLights.setText(this.list.get(position).get("lights").toString());
		viewHolder.appResponsAction.setText(this.list.get(position).get("action").toString());
		boolean status = (Boolean) this.list.get(position).get("status");
		
		if(false == status){
			
			viewHolder.appResponsSwitchImg.setChecked(false);
			viewHolder.appResponsStatus.setText("未开启");
			
		} else {
			
			viewHolder.appResponsSwitchImg.setChecked(true);
			viewHolder.appResponsStatus.setText("已开启");
		}
		
		viewHolder.appResponsSwitchImg.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
				if(isChecked){
					
					viewHolder.appResponsStatus.setText("已开启");
					
				} else {
					
					viewHolder.appResponsStatus.setText("已关闭");
					
				}
			}
		});
		
		return convertView;
	}
}

// 自定义显示应用联动列表
class ViewHolderAppRespons {
	public TextView appResponsSMS;
	public TextView appResponsStatus;
	public Switch appResponsSwitchImg;
	public TextView appResponsLights;
	public TextView appResponsAction;
	
}
