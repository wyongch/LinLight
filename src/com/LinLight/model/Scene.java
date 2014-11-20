package com.LinLight.model;

import android.graphics.Bitmap;

public class Scene {
	
	/**
	 * 场景ID
	 */
	public int id;
	
	/**
	 * 场景名字
	 */
	public String name;
	
	/**
	 * 场景的背景界面
	 */
	public Bitmap bitmap;
	
	/**
	 * 灯列表json信息
	 * [{mac,name,x,y,color,bright},{},{}]
	 */
	public String lightsJson;

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

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	public String getLightsJson() {
		return lightsJson;
	}

	public void setLightsJson(String lightsJson) {
		this.lightsJson = lightsJson;
	}
	
}
