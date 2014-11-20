package com.LinLight.ble;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.content.Context;

/**
 * Represents Linlight device.The {@link LinlightDevice} can read and write
 * equipment directly through the API18+.
 */
// Linlight的实体类主要用于Linlight对象的表示 包括Linlight用到的服务UUID、特征UUID BluetoothDevice对象
@TargetApi(18)
public class LinlightDevice {
	// 用与广播中查找rssi的Key
	public static final String EXTRA_RSSI = "rssi";
	// 用与广播中查找BluetoothDeivce对象的Key
	public static final String EXTRA_DEVICE = "device";
	public static final int STATE_ON = 1;
	public static final int STATE_OFF = 0;
	/**
	 * The UUID of Linlight Service
	 */
	// Linlight所用的BLE服务UUID，用于查找Alight服务
	public static final String LINLIGHT_SERVICE = "0000fff0-0000-1000-8000-00805f9b34fb";
	// 用于改变灯颜色的UUID
	public static final String CHARACTERISTIC_CONTORL = "0000fff1-0000-1000-8000-00805f9b34fb";
	// 查询灯的状态UUID
	public static final String CHARACTERISTIC_STATE_QUERY = "0000fff2-0000-1000-8000-00805f9b34fb";
	// 灯的状态发生改变时给手机发出通知UUID
	public static final String CHARACTERISTIC_STATE_NOTIFICATION = "0000fff3-0000-1000-8000-00805f9b34fb";
	// 设置灯的学习模式的UUID
	public static final String CHARACTERISTIC_LEARN_MODE = "0000fff4-0000-1000-8000-00805f9b34fb";

	// 保留UUID
	// public static final String CHARACTERISTIC_STATUS_QUERY =
	// "0000fff5-0000-1000-8000-00805f9b34fb";

	// public static final String CHARACTERISTIC_STATE_NOTIFICATION =
	// "0000fff6-0000-1000-8000-00805f9b34fb";

	// Add an ID entry to the ID entry table of the light
	public static final String CHARACTERISTIC_ADD_ID = "0000fff7-0000-1000-8000-00805f9b34fb";

	// 删除开关配对的UUID
	public static final String CHARACTERISTIC_DELETE_ID = "0000fff8-0000-1000-8000-00805f9b34fb";

	// Modify the ID entry with a specified number in the table.
	public static final String CHARACTERISTIC_MODIFY_ID = "0000fff9-0000-1000-8000-00805f9b34fb";

	// Get the length of whole the ID entry table of the light
	public static final String CHARACTERISTIC_TABLE_LENGTH = "0000fffa-0000-1000-8000-00805f9b34fb";

	// Query the ID entry with a specified number in the table.
	public static final String CHARACTERISTIC_QUERY_ID = "0000fffb-0000-1000-8000-00805f9b34fb";
	
	// Response of the Query ID Entry 
	public static final String CHARACTERISTIC_QUERY_NOTIFICATION = "0000fffc-0000-1000-8000-00805f9b34fb";
	// 设置手机与灯的时间同步的UUID
	public static final String CHARACTERISTIC_CURRENT_TIME = "0000fffd-0000-1000-8000-00805f9b34fb";

	// 设置灯定时操作的UUID
	public static final String CHARACTERISTIC_ALARM_CLOCK = "0000fffe-0000-1000-8000-00805f9b34fb";

	public static final String NOTIFICATION_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";

	// 蓝牙设备address
	private String address;

	// Android蓝牙设备类
	private BluetoothDevice device;

	// 保存连接后的gatt对象
	private BluetoothGatt gatt;

	// 保存所有特征
	private List<BluetoothGattCharacteristic> characteristics = new ArrayList<BluetoothGattCharacteristic>();

	// 用于发送广播
	private Context context;

	// 连接回调
	private LinlightCallBack callBack;

	// Android蓝牙适配器类，单实例
	private BluetoothAdapter adapter;

	/**
	 * 构造函数
	 * */
	public LinlightDevice(Context context, String address,
			BluetoothDevice device, BluetoothAdapter adapter) {
		this.context = context;
		callBack = new LinlightCallBack(context, this);
		this.address = address;
		this.device = device;
		this.adapter = adapter;
	}

	/**
	 * Connect device;
	 * */
	public void connect() {
		device = adapter.getRemoteDevice(address);
		if (gatt != null) {
			gatt.connect();
		} else {
			device.connectGatt(context, false, callBack);
		}
	}

	/**
	 * Disconnect
	 * */
	public void disconnect() {
		gatt.disconnect();
		gatt = null;
	}

	/**
	 * Return if the device is connceted;
	 * 
	 * @return true if device has connceted.
	 * */
	public boolean isConnected() {
		return gatt == null;
	}

	/**
	 * Write data to characteristic.
	 * 
	 * @param uuid
	 *            a given characteristic
	 * @param data
	 *            value
	 * */
	public void write(String uuid, String data) {
		if (gatt == null) {
			return;
		}
		BluetoothGattCharacteristic characteristic = null;
		for (int i = 0; i < characteristics.size(); i++) {
			if (characteristics.get(i).getUuid().toString().equals(uuid)) {
				characteristic = characteristics.get(i);
				break;
			}
		}
		if (characteristic != null) {
			characteristic.setValue(data.getBytes());
			gatt.writeCharacteristic(characteristic);
		}
	}
	/**
	 * read data from characteristic.
	 * 
	 * @param uuid
	 *            a given characteristic
	 * @param data
	 *            value
	 * */
	//读取到的数据通过onCharacteristicRead()回调函数返回
	public void read(String uuid) {
		if (gatt == null) {
			return;
		}
		BluetoothGattCharacteristic characteristic = null;
		for (int i = 0; i < characteristics.size(); i++) {
			if (characteristics.get(i).getUuid().toString().equals(uuid)) {
				characteristic = characteristics.get(i);
				break;
			}
		}
		if (characteristic != null) {
			gatt.readCharacteristic(characteristic);
		}
	}
	/*
	 * 设置蓝牙Adapter
	 */
	public void setAdapter(BluetoothAdapter adapter) {
		this.adapter = adapter;
	}

	/**
	 * The characteristics which belongs to service fff0;
	 * */
	public void setCharacteristics(
			List<BluetoothGattCharacteristic> characteristics) {
		this.characteristics = characteristics;
	}

	/**
	 * Set BluetoothGatt
	 * */
	public void setGatt(BluetoothGatt gatt) {
		this.gatt = gatt;
	}

	/**
	 * Get MAC address
	 * 
	 * @return MAC address
	 * */
	public String getAddress() {
		return address;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		if (o == this) {
			return true;
		}
		if (o instanceof LinlightDevice) {
			LinlightDevice d = (LinlightDevice) o;
			return d.getAddress().equals(address);
		}
		return false;
	}

	/**
	 * setCharacteristicNotification(characteristic, enabled)
	 * ：设置当指定characteristic值变化时，发出通知。 Enable or disable notifications for a
	 * given characteristic.
	 * 
	 * @param enable
	 *            Set to true to enable notifications/indications
	 * @param characteristic
	 *            The characteristic for which to enable notifications
	 * */
	public boolean enableNotification(boolean enable,
			BluetoothGattCharacteristic characteristic) {
		boolean flag = false;
		gatt.setCharacteristicNotification(characteristic, enable);
		BluetoothGattDescriptor descriptor = characteristic.getDescriptor(UUID
				.fromString(NOTIFICATION_CONFIG));
		if (descriptor != null) {
			descriptor
					.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
			flag = gatt.writeDescriptor(descriptor);
		}
		return flag;
	}

	/**
	 * Get BlueToothGattCharacteristic Object by a given UUID.
	 * 
	 * @param uuid
	 * a given uuid
	 * @return GATT characteristic object or null if no characteristic with the
	 *         given UUID was found.
	 * 
	 * */
	public BluetoothGattCharacteristic getCharacteristic(String uuid) {
		for (BluetoothGattCharacteristic characteristic : characteristics) {
			if (characteristic.getUuid().toString().equals(uuid)) {
				return characteristic;
			}
			;
		}
		return null;
	}

	public void readRemoteRSSI() {
		gatt.readRemoteRssi();
	}
}