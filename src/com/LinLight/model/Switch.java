package com.LinLight.model;


/**
 * 无线开关
 * 
 * @author Administrator
 * 
 */
public class Switch {

	/**
	 * 开关ID
	 */
	public int id;

	/**
	 * 开关名字
	 */
	public String name;

	/**
	 * 开关类型， 1=1键开关 2=2键开关 4=4键开关
	 */
	public int type;

	/**
	 * 灯列表json信息 [{mac,name,x,y,color,bright},{},{}]
	 */
	public String lightsJson;

	/**
	 * 场景 0=<item>卧室</item> 1=<item>客厅 </item> 2=<item>小饭厅</item>
	 * 3=<item>书房</item>
	 */
	public int scene;

	/**
	 * 控制方式 0=open light 1=close light 2=twinkle light 3=scene change
	 * 若是场景切换，则对象为全选
	 */
	public int action;

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
