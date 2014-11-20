package com.LinLight.util;

import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import com.LinLight.app.ui.HomePageActivity;
import com.LinLight.app.ui.MainFragment;
import com.LinLight.ble.ConnectionManager;
import com.LinLight.ble.LinlightDevice;

//发送命令的线程
public class SendThread extends Thread {

	private static final String TAG = "SendThread";
	HomePageActivity homePageActivity;
	private int command = 0;// 存放命令

	public SendThread(HomePageActivity homePageActivity) {
		this.homePageActivity = homePageActivity;
	}

	@Override
	public void run() {
		Integer order;
		while (true) {
//			while (!homePageActivity.queue.isEmpty()) {
//				try {
//					command = homePageActivity.queue.poll();//
//				} catch (Exception e1) {
//					e1.printStackTrace();
//					Log.i(TAG, "-------" + "出现了NoSuchElementException异常");
//				}
//				Log.i(TAG, "-------" + "size"
//						+ ConnectionManager.connected_devices.size());
//				switch (command) {
//				case 0: {// 0代表连接所有的灯
//					for (LinlightDevice yeelightDevice : ConnectionManager.devices) {
//						homePageActivity.connectionManager1.connect(yeelightDevice
//								.getAddress());
//					System.out.println("connet all light---"+yeelightDevice.getAddress());	
//			//		Toast.makeText("connet all light----", str1, Toast.LENGTH_SHORT).show();
//						try {
//							Thread.sleep(80);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
//					}
////			System.out.println("connet all light 0");
//					break;
//				}
//				case 1: {// 1代表给1号灯发送命令
//					if (ConnectionManager.connected_devices.size() >= 1) {
//						homePageActivity.connectionManager1.writeBrightAndColor(
////								MainFragment.b1Color,
//								//lightDefaultColor[0],	
//								Color.RED,
//								ConnectionManager.connected_devices.get(0));
//						System.out.println("send cmd to light1---"+ConnectionManager.connected_devices.get(0).getAddress());												
//					}
//					try {
//						Thread.sleep(80);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//					break;
//				}
//				case 2: {// 2代表给2号灯发送命令
//					if (ConnectionManager.connected_devices.size() >= 2) {
//						homePageActivity.connectionManager1.writeBrightAndColor(
//								//MainFragment.b2Color,
//								Color.RED,
//								ConnectionManager.connected_devices.get(1));
//					}
//
//					try {
//						Thread.sleep(80);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//					break;
//				}
//				case 3: {// 3代表给3号灯发送命令
//					if (ConnectionManager.connected_devices.size() >= 3) {
//						homePageActivity.connectionManager1.writeBrightAndColor(
//								//MainFragment.b3Color,
//								Color.RED,
//								ConnectionManager.connected_devices.get(2));
//					}
//
//					try {
//						Thread.sleep(80);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//					break;
//				}
//				case 12: {// 12代表给12号灯发送命令
////					if (ConnectionManager.connected_devices.size() >= 1) {
////						homePageActivity.connectionManager1.writeBrightAndColor(
////								MainFragment.b2Color,
////								ConnectionManager.connected_devices.get(1));
////					}
////
////					try {
////						Thread.sleep(80);
////					} catch (InterruptedException e) {
////						e.printStackTrace();
////					}
////					break;
////				}
////				case 3: {// 3代表给3号灯发送命令
////					if (ConnectionManager.connected_devices.size() >= 3) {
////						homePageActivity.connectionManager1.writeBrightAndColor(
////								MainFragment.b3Color,
////								ConnectionManager.connected_devices.get(2));
////					}
////
////					try {
////						Thread.sleep(80);
////					} catch (InterruptedException e) {
////						e.printStackTrace();
////					}
//
//					
//					if (ConnectionManager.connected_devices.size() >= 2) {
//						homePageActivity.connectionManager1.writeBrightAndColor(
//						//	MainFragment.b12Color,
//								Color.RED,
//							ConnectionManager.connected_devices.get(0));
//						try {
//							Thread.sleep(80);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}	
//						homePageActivity.connectionManager1.writeBrightAndColor(
//							//MainFragment.b12Color,
//								Color.RED,	
//							ConnectionManager.connected_devices.get(1));						
//					}
//					else if (ConnectionManager.connected_devices.size() >= 1) {
//						homePageActivity.connectionManager1.writeBrightAndColor(
//							//	MainFragment.b12Color,
//								Color.RED,
//								ConnectionManager.connected_devices.get(0));
//					}
//					
//					try {
//						Thread.sleep(80);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//						
//						break;
//				}
//				case 13: {// 13代表给13号灯发送命令
////					if (ConnectionManager.connected_devices.size() >= 1) {
////						homePageActivity.connectionManager1.writeBrightAndColor(
////								MainFragment.b12Color,
////								ConnectionManager.connected_devices.get(0));
////					}
////					
////					try {
////						Thread.sleep(80);
////					} catch (InterruptedException e) {
////						e.printStackTrace();
////					}
////					if (ConnectionManager.connected_devices.size() >= 3) {
////						homePageActivity.connectionManager1.writeBrightAndColor(
////								MainFragment.b13Color,
////								ConnectionManager.connected_devices.get(2));
////					}
////					try {
////						Thread.sleep(80);
////					} catch (InterruptedException e) {
////						e.printStackTrace();
////					}
//
//					if (ConnectionManager.connected_devices.size() >= 3) {
//						homePageActivity.connectionManager1.writeBrightAndColor(
//							//	MainFragment.b13Color,
//								Color.RED,	
//								ConnectionManager.connected_devices.get(2));
//						try {
//							Thread.sleep(80);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
//						homePageActivity.connectionManager1.writeBrightAndColor(
//							//	MainFragment.b13Color,
//								Color.RED,	
//								ConnectionManager.connected_devices.get(0));						
//					}
//					else if (ConnectionManager.connected_devices.size() >= 1) {
//						homePageActivity.connectionManager1.writeBrightAndColor(
//								//MainFragment.b13Color,
//								Color.RED,
//								ConnectionManager.connected_devices.get(0));
//					}
//					try {
//						Thread.sleep(80);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}					
//
//					break;
//				}
//				case 23: {// 23代表给23号灯发送命令
//					if (ConnectionManager.connected_devices.size() >= 2) {
//						homePageActivity.connectionManager1.writeBrightAndColor(
//							//	MainFragment.b23Color,
//								Color.RED,
//								ConnectionManager.connected_devices.get(1));
//					}
//					try {
//						Thread.sleep(80);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//					if (ConnectionManager.connected_devices.size() >= 3) {
//						homePageActivity.connectionManager1.writeBrightAndColor(
//							//	MainFragment.b23Color,
//								Color.RED,
//								ConnectionManager.connected_devices.get(2));
//					}
//					try {
//						Thread.sleep(80);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//
//					break;
//				}
//				case 123: {// 123代表给123号灯发送命令
//					if (ConnectionManager.connected_devices.size() >= 1) {
//						homePageActivity.connectionManager1.writeBrightAndColor(
//								//MainFragment.b123Color,
//								Color.RED,
//								ConnectionManager.connected_devices.get(0));
//					}
//					try {
//						Thread.sleep(80);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//					if (ConnectionManager.connected_devices.size() >= 2) {
//						homePageActivity.connectionManager1.writeBrightAndColor(
//								//MainFragment.b123Color,
//								Color.RED,
//								ConnectionManager.connected_devices.get(1));
//					}
//					try {
//						Thread.sleep(80);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//					if (ConnectionManager.connected_devices.size() >= 3) {
//						homePageActivity.connectionManager1.writeBrightAndColor(
//								//MainFragment.b123Color,
//								Color.RED,
//								ConnectionManager.connected_devices.get(2));
//					}
//					try {
//						Thread.sleep(80);
//					} catch (InterruptedException e) {
//						e.printStackTrace();
//					}
//
//					break;
//				}
//				default:
//					break;
//				}
//				try {
//					Thread.sleep(50);// 每条指令之间休息100ms
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
		}
	}
}