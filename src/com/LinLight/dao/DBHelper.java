package com.LinLight.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.LinLight.global.Gloable;



/**
 * 创建数据库，创建数据表
 * @author Administrator
 *
 */
public class DBHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "linlight_db";
	private static final int DB_VERSION = 1;
	
	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
	}

	/**
	 * 创建数据库
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		String sql_scene = "CREATE TABLE IF NOT EXISTS "
				+ Gloable.TABLE_SCENE
				+ "(_id integer primary key autoincrement,name text,bitmap blob,lightJson text)";
		
		String sql_timer = "CREATE TABLE IF NOT EXISTS "
				+ Gloable.TABLE_TIMER
				+ "(_id integer primary key autoincrement,hm text,week integer,lightJson text,action integer,scene integer,isOpen text)";
		
		String sql_switch = "CREATE TABLE IF NOT EXISTS "
				+ Gloable.TABLE_SWITCH
				+ "(_id integer primary key autoincrement,name text,type integer,lightJson text,action integer,scene integer)";
		
		String sql_apprespons = "CREATE TABLE IF NOT EXISTS "
				+ Gloable.TABLE_APPRESPONS
				+ "(_id integer primary key autoincrement,type integer,lightJson text,action integer,scene integer)";
		
        db.execSQL(sql_scene);
        db.execSQL(sql_timer);
        db.execSQL(sql_switch);
        db.execSQL(sql_apprespons);
	}

	/**
	 * 升级数据库操作
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int currentVersion) {
		
		  db.execSQL("DROP TABLE IF EXISTS " + Gloable.TABLE_SCENE);
		  db.execSQL("DROP TABLE IF EXISTS " + Gloable.TABLE_TIMER);
		  db.execSQL("DROP TABLE IF EXISTS " + Gloable.TABLE_SWITCH);
		  db.execSQL("DROP TABLE IF EXISTS " + Gloable.TABLE_APPRESPONS);
		  
          onCreate(db);
	}

}
