package com.LinLight.model;

//对应数据库中闹钟的数据模型
public class Alarm {
	private int id;// id
	private int isSet;// 是否设置定时
	private String time;// 定时时间
	private String repeat;// 定时周期
	private String timeName;// 定时器名字
	private String target;// 定时对象
	private String action;// 定时动作

	public Alarm() {
		super();
	}

	public Alarm(int id, int isSet, String time, String repeat,
			String timeName, String target, String action) {
		super();
		this.id = id;
		this.isSet = isSet;
		this.time = time;
		this.repeat = repeat;
		this.timeName = timeName;
		this.target = target;
		this.action = action;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIsSet() {
		return isSet;
	}

	public void setIsSet(int isSet) {
		this.isSet = isSet;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getRepeat() {
		return repeat;
	}

	public void setRepeat(String repeat) {
		this.repeat = repeat;
	}

	public String getTimeName() {
		return timeName;
	}

	public void setTimeName(String timeName) {
		this.timeName = timeName;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

}
