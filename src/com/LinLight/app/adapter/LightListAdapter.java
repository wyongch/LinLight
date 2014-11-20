package com.LinLight.app.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.LinLight.app.R;
import com.LinLight.model.Light;
import com.LinLight.model.Picture;

//灯列表适配器类
public class LightListAdapter extends BaseAdapter {
	
	List<Light> lightList;
	Context context;

	public LightListAdapter(List<Light> lightList,Context context) {
		super();
		this.lightList = lightList;
		this.context = context;
	}

	@Override
	public int getCount() {
		if (null != lightList) {
			return lightList.size();
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		return lightList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		LightListViewHolder viewHolder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.light_list_item, null);
			viewHolder = new LightListViewHolder();
			viewHolder.lightName = (TextView) convertView.findViewById(R.id.light_name);
			viewHolder.lightMac = (TextView) convertView.findViewById(R.id.light_mac);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (LightListViewHolder) convertView.getTag();
		}
		viewHolder.lightName.setText(lightList.get(position).name);
		viewHolder.lightMac.setText(lightList.get(position).mac);
		return convertView;
	}
}

class LightListViewHolder {
	public TextView lightName;
	public TextView lightMac;
}
