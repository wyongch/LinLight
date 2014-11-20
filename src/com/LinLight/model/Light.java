package com.LinLight.model;

import android.graphics.Bitmap;


public final class Light {

	/**
	 * 灯的ID
	 */
	public int id;
	
	/**
	 * 灯的mac地址
	 */
	public String mac;
	
	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	/**
	 * 灯的名字
	 */
	public String name;
	
	/**
	 * 灯的颜色
	 */
	public int color;
	
	/**
	 * 灯的亮度
	 */
	public int bright;
	
	/**
	 * 灯的X轴坐标
	 */
	public float x;
	
	/**
	 * 灯的Y轴坐标
	 */
	public float y;
	
	/**
	 * 灯所采用的图片
	 */
	public Bitmap bitmap;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public int getBright() {
		return bright;
	}

	public void setBright(int bright) {
		this.bright = bright;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	
	
}
