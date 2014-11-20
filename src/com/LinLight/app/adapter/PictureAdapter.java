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
import com.LinLight.model.Picture;

//场景的适配器类
public class PictureAdapter extends BaseAdapter {
	List<Picture> pictures;
	List<Bitmap> reflectedBitmaps;
	Context context;

	public PictureAdapter(List<Picture> pictures,List<Bitmap> reflectedBitmaps, Context context) {
		super();
		this.pictures = pictures;
		this.context = context;
		this.reflectedBitmaps=reflectedBitmaps;
	}

	@Override
	public int getCount() {
		if (null != pictures) {
			return pictures.size();
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		return pictures.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = LayoutInflater.from(context);
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.picture_item, null);
			viewHolder = new ViewHolder();
			viewHolder.title = (TextView) convertView.findViewById(R.id.title);
			viewHolder.image = (ImageView) convertView.findViewById(R.id.image);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.title.setText((CharSequence) pictures.get(position)
				.getTitle());
		viewHolder.title.setTextColor(Color.BLACK);
		viewHolder.image.setImageBitmap((reflectedBitmaps.get(position)));
		return convertView;
	}
}

class ViewHolder {
	public TextView title;
	public ImageView image;
}
