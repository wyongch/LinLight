package com.LinLight.model;


/**
 * 应用联动
 * 
 * @author Administrator
 * 
 */

public class AppLinkage {

	/**
	 * 应用类型
	 */
	public int id;
	/**
	 * 应用类型 0=来短信 1=来电话
	 */
	public int type;

	/**
	 * 灯列表json信息 [{mac,name,x,y,color,bright},{},{}]
	 */
	public String lightsJson;
	/**
	 * 动作设置
	 */
	public int action;
	/**
	 * 场景选择
	 */
	/**
	 * 场景 0=<item>卧室</item> 1=<item>客厅 </item> 2=<item>小饭厅</item>
	 * 3=<item>书房</item>
	 */
	public int scene;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getAction() {
		return action;
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

	public void setAction(int action) {
		this.action = action;
	}
}
