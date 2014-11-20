package com.LinLight.model;

import android.graphics.Bitmap;

//对应数据库中场景的数据模型
public class Picture {
	private String title;// 图片名称
	private int imageId;// 图片ID(位置)
    private Bitmap bitmap;//图片
    private String rgb;//图片中三个圆的rgb值
    private String xy;//三个圆的x,y的坐标
	public Picture() {
		super();
	}

	public Picture(String title, int imageId) {
		super();
		this.title = title;
		this.imageId = imageId;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getImageId() {
		return imageId;
	}

	public void setImageId(int imageId) {
		this.imageId = imageId;
	}

	public String getRgb() {
		return rgb;
	}

	public void setRgb(String rgb) {
		this.rgb = rgb;
	}

	public String getXy() {
		return xy;
	}

	public void setXy(String xy) {
		this.xy = xy;
	}

}
