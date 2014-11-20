package com.LinLight.app.adapter;

import java.util.ArrayList;

import android.R.color;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;

import com.LinLight.app.R;
import com.LinLight.global.Constants;
import com.LinLight.model.Scene;
import com.LinLight.util.BitmapUtil;

public class ImageAdapter extends BaseAdapter {

	private Context mContext; // 上下文对象
	private ArrayList<Scene> imgList = null;

	// 构造方法
	public ImageAdapter(Context context, ArrayList<Scene> mImgList) {
		this.mContext = context;
		this.imgList = mImgList;
	}

	// 获取图片的个数
	public int getCount() {
		return this.imgList.size();
	}

	// 获取图片在库中的位置
	public Object getItem(int position) {
		return this.imgList.get(position);
	}

	// 获取图片在库中的位置
	public long getItemId(int position) {
		return position;
	}

	// 获取适配器中指定位置的视图对象
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolderGallery holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.main_scene_gallery_item, null);
			holder = new ViewHolderGallery();
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.gallery_item_imageview);
			holder.textView = (TextView) convertView
					.findViewById(R.id.gallery_item_textview);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolderGallery) convertView.getTag();
		}

		Bitmap finalBitmap = BitmapUtil.zoomBitmap(imgList.get(position).bitmap, Constants.SCREEN_WIDTH >>2, Constants.SCREEN_WIDTH >>2);
		holder.imageView.setImageBitmap(finalBitmap);
		holder.textView.setText(imgList.get(position).name);

		return convertView;
	}

	private class ViewHolderGallery {

		ImageView imageView;
		TextView textView;
	}

}