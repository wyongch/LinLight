package com.LinLight.ble;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

//蓝牙4.0连接管理类
@TargetApi(18)
public class ConnectionManager {
	public static final String DEVICE_FOUND = "com.linlight.DEVICE_FOUND";
	private static ConnectionManager connectionManager;
	private BluetoothAdapter adapter;
	private boolean isScanning = false;
	private BluetoothManager manager;
	private Context context;
	public static List<LinlightDevice> devices = new ArrayList<LinlightDevice>();
	public static List<LinlightDevice> connected_devices = new ArrayList<LinlightDevice>();
	private String str1_cn = "设备不支持蓝牙";
	private String str2_cn = "设备不支持BLE";
	private String str1_en = "Bluetooth not supported.";
	private String str2_en = "BLE is not supported";

	/**
	 * Return a list of {@link LinlightDevice} that has found. Connected and
	 * unconncted returns.
	 * 
	 * @return list of LinlightDevice.
	 * */

	public List<LinlightDevice> getDevices() {
		return devices;
	}

	/**
	 * Return a list of {@link LinlightDevice} that has connected.
	 * 
	 * @return list of LinlightDevice.
	 * */
	public List<LinlightDevice> getConnected() {
		return connected_devices;
	}

	// 产生单实例
	public static ConnectionManager getInstance(Context context) {
		if (connectionManager == null) {
			connectionManager = new ConnectionManager(context);
		}
		return connectionManager;
	}

	public ConnectionManager(Context context) {
		this.context = context;
		manager = (BluetoothManager) context
				.getSystemService(Context.BLUETOOTH_SERVICE);
		adapter = manager.getAdapter();
	}

	private LeScanCallback callback = new LeScanCallback() {
		private boolean contains = false;

		@Override
		public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
			contains = false;
			for (LinlightDevice device2 : devices) {
				if (device2.getAddress().equals(device.getAddress())) {
					contains = true;
					break;
				}
			}
			if (!contains) {
				LinlightDevice yeelightDevice = new LinlightDevice(context,
						device.getAddress(), device, adapter);
				devices.add(yeelightDevice);
			}
			Intent intent = new Intent(DEVICE_FOUND);
			intent.putExtra(LinlightDevice.EXTRA_RSSI, rssi);
			intent.putExtra(LinlightDevice.EXTRA_DEVICE, device);
			context.sendBroadcast(intent);
		}
	};

	/**
	 * Starts a scan for Bluetooth LE devices.
	 * */
	// 开始搜索BLE设备
	public void startLEScan() {
		Resources res = context.getResources();
		String str1 = "";
		String str2 = "";
		Configuration config = res.getConfiguration();
//		if (!config.locale.equals(Locale.CHINA)) {
//			str1 = str1_en;
//			str2 = str2_en;
//		} else {
//			str1 = str1_cn;
//			str2 = str2_cn;
//		}
		System.out.println("开始搜索BLE设备");
		if (adapter == null) {
			Toast.makeText(context, str1, Toast.LENGTH_SHORT).show();
			return;
		}
		if (!context.getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_BLUETOOTH_LE)) {
			Toast.makeText(context, str2, Toast.LENGTH_SHORT).show();
			return;
		}
		adapter.startLeScan(callback);
		isScanning = true;

	}

	/**
	 * Stops an ongoing Bluetooth LE device scan.
	 * */
	// 停止搜索BLE设备
	public void stopLEScan() {
		adapter.stopLeScan(callback);
		isScanning = false;
	}

	/**
	 * Return true if the local Bluetooth adapter is currently in the Bluetooth
	 * LE device discovery process
	 * 
	 * @return true if scaning
	 * */
	// * 返回程序是否正在搜索BLE设备
	public boolean isScanning() {
		return isScanning;
	}

	/**
	 * Conncet all Bluetooth LE devices
	 * */
	// 连接全部ble设备
	public void connect() {
		for (LinlightDevice device : devices) {
			device.connect();
		}
	}

	/**
	 * Connect a a single device.
	 * 
	 * @param address
	 *            a given MAC address
	 * */
	// 根据提供的MAC地址连接特定的设备
	public void connect(String address) {
		for (LinlightDevice device : devices) {
			if (device.getAddress().equals(address)) {
				device.connect();
				break;
			}
		}
	}

	/**
	 * Control the brightness and color of a single device Format :
	 * <Type>,<On>,<Hue/R>,<Sat/G>,<Bri/B>,,,,,
	 * 
	 * @param color
	 *            a given color <br>
	 * @param device
	 *            a given device<br>
	 *            Type The indicator of the color type
	 * */
	// 控制单个设备的颜色和亮度
	public void writeBrightAndColor(int color, LinlightDevice device) {
//	public void writeBrightAndColor(int color_r, int color_g,int color_b,LinlightDevice device) {
		String data = "";
		int r = Color.red(color);
		int g = Color.green(color);
		int b = Color.blue(color);
//		int r = color_r;
//		int g = color_g;
//		int b = color_b;		
		// Format : <Type>,<On>,<Hue/R>,<Sat/G>,<Bri/B>,,,,,(改变颜色时传给灯的数据格式)
		data = 2 + "," + 1 + "," + r + "," + g + "," + b + "," + ",";
		while (data.length() < 18) {
			data += ",";
		}
		if (device == null) {
			Log.i("wwwwwww", "device == null") ;
			for (LinlightDevice device1 : connected_devices) {
				device1.write(LinlightDevice.CHARACTERISTIC_CONTORL, data);
			}
		} else {
			Log.i("wwwwwww", "device != null") ;
			for (LinlightDevice device1 : connected_devices) {
				if (device1.equals(device)) {
					device1.write(LinlightDevice.CHARACTERISTIC_CONTORL, data);
					break;
				}
			}
		}
	}
	
	public void writeBlackAndColor(int color,LinlightDevice device) {
		String data = "";
		int r = Color.red(color);
		int g = Color.green(color);
		int b = Color.blue(color);
	
		// Format : <Type>,<On>,<Hue/R>,<Sat/G>,<Bri/B>,,,,,(改变颜色时传给灯的数据格式)
		data = 2 + "," + 0 + "," + r + "," + g + "," + b + "," + ",";
		while (data.length() < 18) {
			data += ",";
		}
		if (device == null) {
			for (LinlightDevice device1 : connected_devices) {
				device1.write(LinlightDevice.CHARACTERISTIC_CONTORL, data);
			}
		} else {
			for (LinlightDevice device1 : connected_devices) {
				if (device1.equals(device)) {
					device1.write(LinlightDevice.CHARACTERISTIC_CONTORL, data);
					break;
				}
			}
		}
	}

	/**
	 * Control brightness and color of all devices
	 * 
	 * @param color
	 *            a given color<br>
	 * */
	// 控制全部设备的颜色和亮度
	public void writeBrightAndColor(int color) {
		writeBrightAndColor(color, null);
	}


	/**
	 * Set time delay ON/OFF of all deivces
	 * 
	 * @param time
	 *            delay time(give 0 if you want cancel delay )<br>
	 * @param state
	 *            delay on({@link com.linlight.blue.SDK.LinlightDevice#STATE_ON}
	 *            )or delay off(
	 *            {@link com.linlight.blue.SDK.LinlightDevice#STATE_OFF})
	 * 
	 * */
	// 发送命令，删除开关配对
	public void writeDeleteId(int mode) {
		writeDeleteId(mode, null);
	}

	// 发送命令，删除开关配对
	public void writeDeleteId(int mode, LinlightDevice device) {
		String data = "";
		data = mode + ",";
		while (data.length() < 18) {
			data += ",";
		}
		if (device == null) {
			for (LinlightDevice device1 : connected_devices) {
				device1.write(LinlightDevice.CHARACTERISTIC_DELETE_ID, data);
			}
		} else {
			for (LinlightDevice device1 : connected_devices) {
				if (device1.equals(device)) {
					device1.write(LinlightDevice.CHARACTERISTIC_DELETE_ID, data);
					break;
				}
			}
		}
	}

	// 设置全部灯为学习模式
	public void writeLearn(int mode) {
		writeLearn(mode, null);
	}

	// 设置单个灯为学习模式
	public void writeLearn(int mode, LinlightDevice device) {
		String data = "";
		data = mode + ",";
		while (data.length() < 18) {
			data += ",";
		}
		if (device == null) {
			for (LinlightDevice device1 : connected_devices) {
				device1.write(LinlightDevice.CHARACTERISTIC_LEARN_MODE, data);
			}
		} else {
			for (LinlightDevice device1 : connected_devices) {
				if (device1.equals(device)) {
					device1.write(LinlightDevice.CHARACTERISTIC_LEARN_MODE,
							data);
					break;
				}
			}
		}
	}

	// 设置手机与全部设备时间同步
	public void writeCurrentTime(String time) {
		writeCurrentTime(time, null);
	}

	// 设置手机与设备时间同步
	public void writeCurrentTime(String time, LinlightDevice device) {
		String data = "";
		data = time;
		while (data.length() < 18) {
			data += ",";
		}
		if (device == null) {
			for (LinlightDevice device1 : connected_devices) {
				device1.write(LinlightDevice.CHARACTERISTIC_CURRENT_TIME, data);
			}
		} else {
			for (LinlightDevice device1 : connected_devices) {
				if (device1.equals(device)) {
					device1.write(LinlightDevice.CHARACTERISTIC_CURRENT_TIME,
							data);
					break;
				}
			}
		}
	}

	// 设置全部设备定时操作
	public void writeAlarm(String time, String repeat, String action,
			String state) {
		writeAlarm(time, repeat, action, state, null);
	}

	// 设置给定设备定时操作
	public void writeAlarm(String time, String repeat, String action,
			String state, LinlightDevice device) {
		String data = "";
		data = time + "," + repeat + "," + action + "," + state;
		while (data.length() < 18) {
			data += ",";
		}
		if (device == null) {
			for (LinlightDevice device1 : connected_devices) {
				device1.write(LinlightDevice.CHARACTERISTIC_ALARM_CLOCK, data);
			}
		} else {
			for (LinlightDevice device1 : connected_devices) {
				if (device1.equals(device)) {
					device1.write(LinlightDevice.CHARACTERISTIC_ALARM_CLOCK,
							data);
					break;
				}
			}
		}
	}

	/**
	 * Read the status of a connected device. This is an asynchronous call.
	 * Register for
	 * {@link com.linlight.blue.SDK.LinlightDevice#STATUS_NOTIFICATION} to be
	 * notified when the result is returned.
	 * 
	 * @param device
	 *            a given device
	 * */
	/*
	 * //查询灯的状态 查询结果会异步返回。 通过广播{@link
	 * com.linlight.blue.SDK.LinlightDevice#STATUS_NOTIFICATION}返回<br>
	 * 改广播中总是会包含两个数据项 : {@link
	 * com.linlight.blue.SDK.LinlightDevice#EXTRA_ADDRESS}和{@link
	 * com.linlight.blue.SDK.LinlightDevice#EXTRA_RESULT}即设备的mac地址和返回的数据 <br>
	 */
	public void getDeviceState(final LinlightDevice device) {
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				query(LinlightDevice.CHARACTERISTIC_STATE_QUERY, "QRY", device);
			}
		}, 100);

	}

	private void query(final String uuid, final String data,
			LinlightDevice device) {
		for (LinlightDevice device1 : connected_devices) {
			if (device1.equals(device)) {
				final LinlightDevice device2 = device1;
				BluetoothGattCharacteristic notificationCharac = null;
				if (uuid.equals(LinlightDevice.CHARACTERISTIC_STATE_QUERY)) {
					notificationCharac = device1
							.getCharacteristic(LinlightDevice.CHARACTERISTIC_STATE_NOTIFICATION);
				} else {
				}
				if (notificationCharac != null) {
					System.out.println(device1.enableNotification(true,
							notificationCharac));
					;
					System.out.println(device1.enableNotification(true,
							notificationCharac));
					;
				}
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						device2.write(uuid, data);
					}
				}, 200);
				break;
			}
		}
	}

	/**
	 * Disconncet all devices
	 * */
	// * 断开所有设备连接
	public void disconnect() {
		for (LinlightDevice device : connected_devices) {
			device.disconnect();
		}
	}

	/**
	 * Disconncet a single device
	 * 
	 * @param address
	 *            a given MAC address
	 * 
	 * */
	// 断开单个设备连接 @param address 某个要断开的设备MAC地址
	public void disconnect(String address) {
		for (LinlightDevice device : connected_devices) {
			if (device.getAddress().equals(address)) {
				device.disconnect();
				break;
			}
		}
	}

	/**
	 * Read the RSSI of a remote device. This is an asynchronous call. Register
	 * for {@link com.linlight.blue.SDK.LinlightDevice#REMOTERSSI} to be
	 * notified when the result is returned.
	 * 
	 * @param device
	 *            a given device MAC address
	 * 
	 * */
	public void getRemoteRSSI(String address) {
		for (LinlightDevice device : connected_devices) {
			if (device.getAddress().equals(address)) {
				device.readRemoteRSSI();
				break;
			}
		}
	}

	// ////////////////////////////////////////////////////////////
	// 以下是功能测试代码
	// 查询全部灯状态的命令
	public void writeQueryState() {
		writeQueryState(null);
	}

	// 查询给定灯状态的命令
	public void writeQueryState(LinlightDevice device) {
		String data = "";
		data = "QRY" + "";
		while (data.length() < 18) {
			data += ",";
		}
		if (device == null) {
			for (LinlightDevice device1 : connected_devices) {
				device1.write(LinlightDevice.CHARACTERISTIC_STATE_QUERY, data);
			}
		} else {
			for (LinlightDevice device1 : connected_devices) {
				if (device1.equals(device)) {
					device1.write(LinlightDevice.CHARACTERISTIC_STATE_QUERY,
							data);
					break;
				}
			}
		}
	}

	// Query ID Entry
	public void writeQueryID() {
		writeQueryState(null);
	}

	// Query ID Entry
	public void writeQueryID(LinlightDevice device) {
		String data = "";
		data = "QRY" + "";
		while (data.length() < 18) {
			data += ",";
		}
		if (device == null) {
			for (LinlightDevice device1 : connected_devices) {
				device1.write(LinlightDevice.CHARACTERISTIC_QUERY_ID, data);
			}
		} else {
			for (LinlightDevice device1 : connected_devices) {
				if (device1.equals(device)) {
					device1.write(LinlightDevice.CHARACTERISTIC_QUERY_ID,
							data);
					break;
				}
			}
		}
	}

	public void readTableLength(){
		readTableLength(null);
	}
	// Get ID Table Length
	public void readTableLength(LinlightDevice device) {
		String data = "";
		data = "QRY" + "";
		while (data.length() < 18) {
			data += ",";
		}
		if (device == null) {
			for (LinlightDevice device1 : connected_devices) {
				device1.read(LinlightDevice.CHARACTERISTIC_TABLE_LENGTH);
			}
		} else {
			for (LinlightDevice device1 : connected_devices) {
				if (device1.equals(device)) {
					device1.read(LinlightDevice.CHARACTERISTIC_TABLE_LENGTH);
					break;
				}
			}
		}
	}
}
