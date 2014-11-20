package com.LinLight.model;


/**
 * 定时器
 * @author Administrator
 *
 */
public class Timer {
	
	public int id;
	
	/*
	 * 小时分钟
	 */
	public String hm;
	
	/**
	 * 星期1-7
	 */
	public int week;
	
	/**
	 * 灯列表json信息
	 * [{mac,name,x,y,color,bright},{},{}]
	 */
	public String lightsJson;
	
	/**
	 * 定时器动作，控制方式
	 * 0=open light
	 * 1=close light
	 * 2=twinkle light
	 * 3=scene change
	 */
	public int action;
	
	/**
	 * 场景
	 * 0=<item>卧室</item>
       1=<item>客厅 </item>
       2=<item>小饭厅</item>
       3=<item>书房</item>
	 */
	public int scene;
	
	/**
	 * 定时器是否开启
	 */
	public boolean isOpen;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getWeek() {
		return week;
	}

	public void setWeek(int week) {
		this.week = week;
	}

	public String getHm() {
		return hm;
	}

	public void setHm(String hm) {
		this.hm = hm;
	}

	public String getLightsJson() {
		return lightsJson;
	}

	public void setLightsJson(String lightsJson) {
		this.lightsJson = lightsJson;
	}

	public int getScene() {
		return scene;
	}

	public void setScene(int scene) {
		this.scene = scene;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}

	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}
}
