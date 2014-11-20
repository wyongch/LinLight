package com.LinLight.model;

//对应数据库中开关的数据模型
public class OpenClose {
	private int id;// id
	private int isScan;// 是否允许扫描
	private String lightName;// 控制灯的名字
	private String mac;// MAC地址
	private String openCloseName;// 开关的名字
	private String openCloseWays;// 开关的控制方式

	public OpenClose() {
		super();

	}

	public OpenClose(int id, String lightName, String openCloseName,
			String openCloseWays, int isScan, String mac) {
		super();
		this.id = id;
		this.lightName = lightName;
		this.openCloseName = openCloseName;
		this.openCloseWays = openCloseWays;
		this.isScan = isScan;
		this.mac = mac;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLightName() {
		return lightName;
	}

	public void setLightName(String lightName) {
		this.lightName = lightName;
	}

	public String getOpenCloseName() {
		return openCloseName;
	}

	public void setOpenCloseName(String openCloseName) {
		this.openCloseName = openCloseName;
	}

	public String getOpenCloseWays() {
		return openCloseWays;
	}

	public void setOpenCloseWays(String openCloseWays) {
		this.openCloseWays = openCloseWays;
	}

	public int getScan() {
		return isScan;
	}

	public void setScan(int isScan) {
		this.isScan = isScan;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

}
