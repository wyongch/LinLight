package com.LinLight.app.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.LinLight.app.R;

public class SettingWirelessSwitchAdapter extends BaseAdapter {

	private Context mContext = null;
	private ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String,Object>>();

	public SettingWirelessSwitchAdapter(Context context, ArrayList<HashMap<String, Object>> list) {

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
		final ViewHolderWirelessSwitch viewHolder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.wireless_switch_list_item,
					null);
			viewHolder = new ViewHolderWirelessSwitch();
			viewHolder.wirelessSwitchName = (TextView) convertView
					.findViewById(R.id.wireless_switch_textview_name);
			viewHolder.wirelessSwitchImg = (ImageView) convertView.findViewById(R.id.wireless_switch_img);
			viewHolder.wirelessSwitchId = (TextView) convertView.findViewById(R.id.wireless_switch_textview_id);
			viewHolder.wirelessSwitchType = (TextView) convertView.findViewById(R.id.wireless_switch_textview_type);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolderWirelessSwitch) convertView.getTag();
		}

		viewHolder.wirelessSwitchName.setText(this.list.get(position).get("name").toString());
		viewHolder.wirelessSwitchId.setText(this.list.get(position).get("id").toString());
		viewHolder.wirelessSwitchType.setText(this.list.get(position).get("type").toString());
		
		
		int type = (Integer) this.list.get(position).get("type");
		if(1 == type)
			viewHolder.wirelessSwitchImg.setImageResource(R.drawable.switch1);
		else if(2 == type)
			viewHolder.wirelessSwitchImg.setImageResource(R.drawable.switch2);
		else if(4 == type)
			viewHolder.wirelessSwitchImg.setImageResource(R.drawable.switch4);
		return convertView;
	}
}

// 自定义显示闹钟列表
class ViewHolderWirelessSwitch {
	public TextView wirelessSwitchName;
	public ImageView wirelessSwitchImg;
	public TextView wirelessSwitchId;
	public TextView wirelessSwitchType;
}
