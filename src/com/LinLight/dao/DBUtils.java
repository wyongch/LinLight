package com.LinLight.dao;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.LinLight.global.AppData;
import com.LinLight.global.Gloable;
import com.LinLight.model.AppLinkage;
import com.LinLight.model.Scene;
import com.LinLight.model.Switch;
import com.LinLight.model.Timer;
import com.LinLight.util.LogUtils;

/**
 *  数据库操作类
 */
public class DBUtils {
	
	private DBHelper dbHelper;

	public DBUtils(Context context) {
		
		dbHelper = new DBHelper(context);
	}


	//////////////  Scene  ////////////////////////////////////////////////////
	
	/**
	 * 插入场景
	 * @param value
	 * @return
	 */
	public long insertScene(Scene scene) {

		long row = 0;
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		dbHelper.onCreate(db);
		ContentValues values = null;
		try {
			values = new ContentValues();
			values.put("_id", scene.getId());
			values.put("name", scene.getName());
			values.put("bitmap", bitmapTobyte(scene.getBitmap()));
			values.put("lightJson", scene.getLightsJson());
			row = db.insert(Gloable.TABLE_SCENE, null, values);
		} catch (Exception e) {
		} finally {
			if (db.isOpen()) {
				db.close();
			}
		}
		return row;
	}
	
	/**
	 * 取出所有场景
	 * @return
	 */
	public ArrayList<Scene> getAllScene() {

		SQLiteDatabase db = dbHelper.getReadableDatabase();
		dbHelper.onCreate(db);
		ArrayList<Scene> sceneList = new ArrayList<Scene>();

		try {
			Cursor cursor = db.query(Gloable.TABLE_SCENE, null, null, null,
					null, null, null);
			while (cursor.moveToNext()) {
				Scene scene = new Scene();
				scene.setId(cursor.getInt(cursor.getColumnIndex("_id")));
				scene.setName(cursor.getString(cursor.getColumnIndex("name")));
				scene.setBitmap(byteTobitmap(cursor.getBlob(cursor
						.getColumnIndex("bitmap"))));
				scene.setLightsJson(cursor.getString(cursor
						.getColumnIndex("lightJson")));
				sceneList.add(scene);
			}
			cursor.close();
			db.close();
		} catch (Exception e) {
		}
		return sceneList;
	}
	
	
	/**
	 * 按id查询数据表中是否存在场景
	 * @param id
	 * @return
	 */
	public boolean querySceneByID(String id) {
		Cursor cursor;
		try {

			SQLiteDatabase db = dbHelper.getWritableDatabase();
			dbHelper.onCreate(db);
			cursor = db.query(Gloable.TABLE_SCENE, null, "_id=?",
					new String[] { id }, null, null, null);
			cursor.moveToFirst();
			int count = cursor.getCount();
			db.close();
			if (count > 0) {
				cursor.close();
				return true;
			}
			cursor.close();
		} catch (Exception e) {
		}
		return false;
	}
	
	/**
	 * 删除场景
	 * @param id
	 */
	public void deleteSceneById(int id) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String sql = "DELETE FROM " + Gloable.TABLE_SCENE + " WHERE _id=" + id;
		db.execSQL(sql);
		db.close();
	}
	
	
	//////////////////  Timer  ////////////////////////////////////////////////
	
	
	/**
	 * 插入定时器
	 * @param value
	 * @return
	 */
	public long insertTimer(Timer timer) {

		long row = 0;
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		dbHelper.onCreate(db);
		ContentValues values = null;
		try {
			values = new ContentValues();
			values.put("_id", timer.getId());
			values.put("hm", timer.getHm());
			values.put("week", timer.getWeek());
			values.put("lightJson", timer.getLightsJson());
			values.put("action", timer.getAction());
			values.put("scene", timer.scene);
			values.put("isOpen", String.valueOf(timer.isOpen()));
			row = db.insert(Gloable.TABLE_TIMER, null, values);
		} catch (Exception e) {
		} finally {
			if (db.isOpen()) {
				db.close();
			}
		}
		
		LogUtils.i("insertTimer long:"+row);
		return row;
	}
	
	/**
	 * 取出所有定时器
	 * @return
	 */
	public List<Timer> getAllTimer() {

		SQLiteDatabase db = dbHelper.getReadableDatabase();
		dbHelper.onCreate(db);
		List<Timer> timerList = new ArrayList<Timer>();

		try {
			Cursor cursor = db.query(Gloable.TABLE_TIMER, null, null, null,
					null, null, null);
			while (cursor.moveToNext()) {
				
				Timer timer = new Timer();
				timer.setId(cursor.getInt(cursor.getColumnIndex("_id")));
				timer.setHm(cursor.getString(cursor.getColumnIndex("hm")));
				timer.setWeek(cursor.getInt(cursor.getColumnIndex("week")));
				timer.setLightsJson(cursor.getString(cursor.getColumnIndex("lightJson")));
				timer.setAction(cursor.getInt(cursor.getColumnIndex("action")));
				timer.setScene(cursor.getInt(cursor.getColumnIndex("scene")));
				timer.setOpen(Boolean.valueOf(cursor.getString(cursor.getColumnIndex("isOpen"))));
				timerList.add(timer);
			}
			cursor.close();
			db.close();
		} catch (Exception e) {
		}
		return timerList;
	}
	
	
	/**
	 * 按id查询数据表中是否存在定时器
	 * @param id
	 * @return
	 */
	public boolean queryTimerByID(String id) {
		Cursor cursor;
		try {

			SQLiteDatabase db = dbHelper.getWritableDatabase();
			dbHelper.onCreate(db);
			cursor = db.query(Gloable.TABLE_TIMER, null, "_id=?",
					new String[] { id }, null, null, null);
			cursor.moveToFirst();
			int count = cursor.getCount();
			db.close();
			if (count > 0) {
				cursor.close();
				return true;
			}
			cursor.close();
		} catch (Exception e) {
		}
		return false;
	}
	
	/**
	 * 删除场景
	 * @param id
	 */
	public void deleteTimerById(int id) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String sql = "DELETE FROM " + Gloable.TABLE_TIMER + " WHERE _id=" + id;
		db.execSQL(sql);
		db.close();
	}
	
	
	
	
	///////////////////Switch///////////////////////////////////////////////
	/**
	 * 插入开关
	 * @param value
	 * @return
	 */
	public long insertSwitch(Switch sw) {

		long row = 0;
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		dbHelper.onCreate(db);
		ContentValues values = null;
		try {
			values = new ContentValues();
			values.put("_id", sw.getId());
			values.put("name", sw.getName());
			values.put("type", sw.getType());
			values.put("lightJson", sw.getLightsJson());
			values.put("scene", sw.getScene());
			values.put("action", sw.getAction());
			row = db.insert(Gloable.TABLE_SWITCH, null, values);
		} catch (Exception e) {
		} finally {
			if (db.isOpen()) {
				db.close();
			}
		}
		return row;
	}
	
	/**
	 * 取出所有定时器
	 * @return
	 */
	public List<Switch> getAllSwitch() {

		SQLiteDatabase db = dbHelper.getReadableDatabase();
		dbHelper.onCreate(db);
		List<Switch> switchList = new ArrayList<Switch>();

		try {
			Cursor cursor = db.query(Gloable.TABLE_SWITCH, null, null, null,
					null, null, null);
			while (cursor.moveToNext()) {
				
				Switch sw = new Switch();
				sw.setId(cursor.getInt(cursor.getColumnIndex("_id")));
				sw.setName(cursor.getString(cursor.getColumnIndex("name")));
				sw.setLightsJson(cursor.getString(cursor.getColumnIndex("lightJson")));
				sw.setType(cursor.getInt(cursor.getColumnIndex("type")));
				sw.setAction(cursor.getInt(cursor.getColumnIndex("action")));
				sw.setScene(cursor.getInt(cursor.getColumnIndex("scene")));
				switchList.add(sw);
			}
			cursor.close();
			db.close();
		} catch (Exception e) {
		}
		return switchList;
	}
	
	
	/**
	 * 按id查询数据表中是否存在定时器
	 * @param id
	 * @return
	 */
	public boolean querySwitchByID(String id) {
		Cursor cursor;
		try {

			SQLiteDatabase db = dbHelper.getWritableDatabase();
			dbHelper.onCreate(db);
			cursor = db.query(Gloable.TABLE_SWITCH, null, "_id=?",
					new String[] { id }, null, null, null);
			cursor.moveToFirst();
			int count = cursor.getCount();
			db.close();
			if (count > 0) {
				cursor.close();
				return true;
			}
			cursor.close();
		} catch (Exception e) {
		}
		return false;
	}
	
	/**
	 * 删除开关
	 * @param id
	 */
	public void deleteSwitchById(int id) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String sql = "DELETE FROM " + Gloable.TABLE_SWITCH + " WHERE _id=" + id;
		db.execSQL(sql);
		db.close();
	}
	
	
	//////////////////APPRESPONS////////////////////////////
	/**
	 * 插入应用联动
	 * @param value
	 * @return
	 */
	public long insertApprespons(AppLinkage applinkage) {

		long row = 0;
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		dbHelper.onCreate(db);
		ContentValues values = null;
		try {
			values = new ContentValues();
			values.put("_id", applinkage.getId());
			values.put("type", applinkage.getType());
			values.put("lightJson", applinkage.getLightsJson());
			values.put("scene", applinkage.getScene());
			values.put("action", applinkage.getAction());
			row = db.insert(Gloable.TABLE_APPRESPONS, null, values);
		} catch (Exception e) {
		} finally {
			if (db.isOpen()) {
				db.close();
			}
		}
		return row;
	}
	
	/**
	 * 取出所有定时器
	 * @return
	 */
	public List<AppLinkage> getAllAppLinkages() {

		SQLiteDatabase db = dbHelper.getReadableDatabase();
		dbHelper.onCreate(db);
		List<AppLinkage> appLinkageList = new ArrayList<AppLinkage>();

		try {
			Cursor cursor = db.query(Gloable.TABLE_APPRESPONS, null, null, null,
					null, null, null);
			while (cursor.moveToNext()) {
				
				AppLinkage app = new AppLinkage();
				app.setId(cursor.getInt(cursor.getColumnIndex("_id")));
				app.setLightsJson(cursor.getString(cursor.getColumnIndex("lightJson")));
				app.setType(cursor.getInt(cursor.getColumnIndex("type")));
				app.setAction(cursor.getInt(cursor.getColumnIndex("action")));
				app.setScene(cursor.getInt(cursor.getColumnIndex("scene")));
				appLinkageList.add(app);
			}
			cursor.close();
			db.close();
		} catch (Exception e) {
		}
		return appLinkageList;
	}
	
	
	/**
	 * 按id查询数据表中是否存在定时器
	 * @param id
	 * @return
	 */
	public boolean queryAppLinkageByID(String id) {
		Cursor cursor;
		try {

			SQLiteDatabase db = dbHelper.getWritableDatabase();
			dbHelper.onCreate(db);
			cursor = db.query(Gloable.TABLE_APPRESPONS, null, "_id=?",
					new String[] { id }, null, null, null);
			cursor.moveToFirst();
			int count = cursor.getCount();
			db.close();
			if (count > 0) {
				cursor.close();
				return true;
			}
			cursor.close();
		} catch (Exception e) {
		}
		return false;
	}
	
	/**
	 * 删除开关
	 * @param id
	 */
	public void deleteAppLinkageById(int id) {
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		String sql = "DELETE FROM " + Gloable.TABLE_APPRESPONS + " WHERE _id=" + id;
		db.execSQL(sql);
		db.close();
	}
	
	
	/////////////////Bitmap convert to byte array///////////////////////////////
	

	/*
	 * 将图片转化为位图
	 */
	private byte[] bitmapTobyte(Bitmap bitmap) {
		int size = bitmap.getWidth() * bitmap.getHeight() * 4;
		// 创建一个字节数组输出流,流的大小为size
		ByteArrayOutputStream baos = new ByteArrayOutputStream(size);
		// 设置位图的压缩格式，质量为100%，并放入字节数组输出流中
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		// 将字节数组输出流转化为字节数组byte[]
		byte[] imagedata = baos.toByteArray();
		return imagedata;
	}

	/*
	 * 将字节数组转化为位图
	 */
	private Bitmap byteTobitmap(byte[] imagedata) {
		Bitmap imagebitmap = BitmapFactory.decodeByteArray(imagedata, 0,
				imagedata.length);
		return imagebitmap;
	}
}
