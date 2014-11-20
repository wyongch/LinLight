package com.LinLight.app.ui;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.LinLight.app.MyApplication;
import com.LinLight.app.R;
import com.LinLight.app.adapter.ImageAdapter;
import com.LinLight.ble.ConnectionManager;
import com.LinLight.ble.LinlightDevice;
import com.LinLight.dao.DBUtils;
import com.LinLight.global.Constants;
import com.LinLight.model.Light;
import com.LinLight.model.Scene;
import com.LinLight.util.ActionUtil;
import com.LinLight.util.BitmapUtil;
import com.LinLight.util.BrightUtils;
import com.LinLight.util.LogUtils;
import com.LinLight.util.SendThread;
import com.LinLight.util.WidgetUtils;
import com.LinLight.ble.ConnectionManager;
import com.LinLight.ble.LinlightDevice;
import com.LinLight.ble.ConnectionManager;
public class MainFragment extends Fragment {

	private static final String TAG = "MainFragment";
	private com.LinLight.app.MyApplication application;
	private Activity mActivity = null;
	private static final long SCAN_PERIOD = 5000;
	private TextView lightName, lightColor;
	private RelativeLayout rlImage = null;
	private Gallery gallery = null;
	public static ImageAdapter imageAdapter = null;
	private ArrayList<Bitmap> imgList = new ArrayList<Bitmap>();
	private ArrayList<String> textList = new ArrayList<String>();
	private int[] imgIdArray = null;
	private String[] textIdArray = null;

	private ArrayList<Scene> sceneList = new ArrayList<Scene>();

	private MainSceneElementView mainSceneElementView;
	private GestureDetector mGestureDetector = null; // 手势识别

	private DBUtils dbUtils;

	private int sceneWidth;
	private int sceneHeight;

	private int pointerWidth;
	private int pointerHeight;

	// about light's x y and color, start.
	private int lightGroup[][] = new int[][]{{1,0,0,0,0},{0,1,0,0,0},{0,0,1,0,0},{0,0,0,1,0},{0,0,0,0,1}};
	private int[]lightX = new int[] {100,100,100};		//初始化三个灯的x坐标
	private int[]lightY = new int[] {100,300,500};		//初始化三个灯的y坐标
	private boolean[]flagLight = new boolean[] {true,true,true};			//灯的组合关系
	private boolean[]clickLight = new boolean[] {false,false,false};		//灯是否被点击
	private int[]lightDefaultColor = new int[] {Color.GRAY,Color.GRAY,Color.GRAY};  //灯的默认颜色

	
//	private int saveGroup[][] = new int[][]{{0,0,0,0},{0,0,0,0},{0,0,0,0},{0,0,0,0}} ;
	private int saveGroup[] = new int[]{0,0,0,0,0} ;
	
	// 第一次的默认的坐标
//	private int lightRedX = 100;
//	private int lightRedY = 100;
//	private int lightBlueX = 100;
//	private int lightBlueY = 300;
//	private int lightGreenX = 100;
//	private int lightGreenY = 500;
//	private int light123X = 240;
//	private int light123Y = 250;
	
//	private int redLightGroup = 0;
//	private int blueLightGroup = 0;
//	private int greenLightGroup = 0;
	
	// 灯的组合关系
//	private boolean flagLight1 = true;
//	private boolean flagLight2 = true;
//	private boolean flagLight3 = true;
//	private boolean flagLight123 = false;

	// 灯是否被点击
//	private boolean clickLight1 = false;
//	private boolean clickLight2 = false;
//	private boolean clickLight3 = false;
//	private boolean clickLight123 = false;

	// 灯开与关
	private boolean light1Close = false;
	private boolean light2Close = false;
	private boolean light3Close = false;
	private boolean light123Close = false;

	// 灯的亮度
	private int light1BrightProgress = 30, light2BrightProgress = 40,
			light3BrightProgress = 50;
	private int light123BrightProgress = 60;

	// 三个灯的画笔
	private Paint paintRed, paintBlue, paintGreen, paint123;
	
	private Paint magnifierPaint;
	private Bitmap rlImgBack;

	private final String[] sLight = new String[] { "1号灯", "2号灯", "3号灯", "合并灯" };    //定义当前选定的定
	private boolean[] bLight = new boolean[] { false, false, false, false };		//定义灯的状态
	
//	private int redLightColor = Color.GRAY, blueLightColor = Color.GRAY,	//定义灯的颜色
//			greenLightColor = Color.GRAY, light123Color = Color.GRAY;
	
	private int lastLight1Color, lastLight2Color, lastLight3Color, lastLight123Color;
	
	private static int magnifierShowStatus = -1;
	private static final int STATUS_NONE = 0;
	private static final int STATUS_LIGHT_1 = 1;
	private static final int STATUS_LIGHT_2 = 2;
	private static final int STATUS_LIGHT_3 = 3;
	private static final int STATUS_LIGHT_123 = 123;
	
	
	public static ConnectionManager mf_connectionManager;// 连接管理，是一个单实例
	
	Thread mThread ;
	
	// about light's x y and color, end.

	private final static int CROP = 200;
	private final static String FILE_SAVEPATH = Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/linlight/";
	private Uri cropUri;
	private File protraitFile;
	private String protraitPath;
	private Handler mHandler;// 消息传递
	private static final int REQUEST_ENABLE_BT = 1;
	private List<LinlightDevice> connected_light;
	private BluetoothAdapter mBluetoothAdapter;
	//public static ConnectionManager connectionManager1;// 连接管理，是一个单实例
	//public static Queue<Integer> queue;// 存放所有控制命令的队列	
	String inputString = null;
	//HomePageActivity homePageActivity;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		LogUtils.i("onCreateView");
		//homePageActivity = this.getActivity();
		//homePageActivity.queue.offer(0); 
		//homePageActivity.queue.offer(1);
	
//		public SendThread(HomePageActivity homePageActivity) {
//			this.homePageActivity = homePageActivity;
//		}
		mActivity=this.getActivity();
		//queue = new LinkedList<Integer>();// 队列初始化，用于存储命令
		mf_connectionManager = ConnectionManager.getInstance(this.getActivity());// 获取单实例
//		this.sThread = new SendThread(this);// 初始化发送命令的线程
//		this.sThread.start();// 开始发送命令的线程		
		Log.i("wwwwwww", "启动系统1") ;
		final BluetoothManager bluetoothManager =
                (BluetoothManager) this.getActivity().getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!this.getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this.getActivity(), R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            this.getActivity().finish();
        }
        Log.i("wwwwwww", "启动系统2") ;
		
 
        //	mf_connectionManager.startLEScan();
        	
  
        
        //mf_connectionManager.stopLEScan();
		//queue.offer(0);		
		mThread = new Thread(){
			public void run() {
				
				Log.i("wwwwwww", "启动线程") ;

				for (LinlightDevice yeelightDevice : ConnectionManager.devices) {
				  mf_connectionManager.connect(yeelightDevice
							.getAddress());
					Log.i("wwwwwww", "connet all light---启动线程111111") ;	
				System.out.println("connet all light---"+yeelightDevice.getAddress());	
		//		Toast.makeText("connet all light----", str1, Toast.LENGTH_SHORT).show();
					try {
						Thread.sleep(80);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				if (ConnectionManager.connected_devices.size() >= 1) {
					Log.i("wwwwwww", "启动线程111111") ;
					mf_connectionManager.writeBrightAndColor(
//							MainFragment.b1Color,
							//lightDefaultColor[0],	
							lightDefaultColor[0] ,
							ConnectionManager.connected_devices.get(0));
					System.out.println("send cmd to light1---"+ConnectionManager.connected_devices.get(0).getAddress());												
				}
				try {
					Log.i("wwwwwww", "启动线程13331") ;
					Thread.sleep(80);
				} catch (InterruptedException e) {
					e.printStackTrace();
					Log.i("wwwwwww", "启动线程111eeee1") ;
				}
				
					
		}
	};
	mHandler = new Handler();
	scanLeDevice(true);// 开始搜索BLE设备
	 mThread.start(); 

		
		
	 
		mActivity = this.getActivity();
		application = (MyApplication) mActivity.getApplication();
		mGestureDetector = new GestureDetector(mActivity,
				new MainSceneGestureListener());

		View view = inflater.inflate(R.layout.main_scene, null);

		lightName = (TextView) view.findViewById(R.id.lightname);
//		lightColor = (TextView) view.findViewById(R.id.lightcolor);
		rlImage = (RelativeLayout) view.findViewById(R.id.main_scene_img);
		mainSceneElementView = new MainSceneElementView(mActivity);
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				-1, -1);
		layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
		rlImage.addView(mainSceneElementView, layoutParams);

		mainSceneElementView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				
				if (event.getAction() == MotionEvent.ACTION_UP) {
					magnifierShowStatus = STATUS_NONE;
					mainSceneElementView.invalidate();
				}

				return mGestureDetector.onTouchEvent(event);
			}
		});

		dbUtils = new DBUtils(mActivity);    //新建一个数据库操作对象 dbUtils

		gallery = (Gallery) view.findViewById(R.id.gallery);

		// 本地存储数据初始化
		imgIdArray = new int[] { R.drawable.i1, R.drawable.i2, R.drawable.i3,
				R.drawable.i4, R.drawable.i5, R.drawable.i6, R.drawable.i7, };
		textIdArray = getResources().getStringArray(R.array.gallery_text);

		for (int i = 0; i < imgIdArray.length; i++) {

			Scene scene = new Scene();
			scene.name = textIdArray[i];
			scene.bitmap = BitmapFactory.decodeResource(getResources(),
					imgIdArray[i]);

			scene.lightsJson = new String("");
			scene.id = i;

			sceneList.add(scene);
		}
		
		//定义场景链表对象list
		List<Scene> list = dbUtils.getAllScene();
		LogUtils.i("list's size:" + list.size());
		if (list.size() > 0) {		//将数据库中的场景添加到场景链表sceneList中

			for (Scene scene : list) {

				sceneList.add(scene);
			}
		}
		
		//定义个增加新场景对象
		Scene addScene = new Scene();
		addScene.bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.add);
		addScene.name = "增加新场景";
		addScene.lightsJson = "";
		sceneList.add(addScene);
		
		//压缩位图
		rlImgBack = BitmapUtil.zoomBitmap(
				sceneList.get(sceneList.size() / 2 - 1).bitmap,
				Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT * 2 / 3);

		imageAdapter = new ImageAdapter(mActivity, sceneList);
		gallery.setAdapter(imageAdapter);
		gallery.setSelection(sceneList.size() / 2 - 1);
		//设置单击场景切换
		gallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				// 添加新场景事件响应
				if (sceneList.get(position).name.equals("增加新场景")) {

					startImagePick();

				} else {// 其他场景点击事件处理

					//压缩图片
					rlImgBack = BitmapUtil.zoomBitmap(
							sceneList.get(position).bitmap,
							Constants.SCREEN_WIDTH,
							Constants.SCREEN_HEIGHT * 2 / 3);

					//设置测试场景灯的坐标和颜色
					if (sceneList.get(position).name.contains("测试")) {
						
//						lightRedX = 100;
//						lightRedY = 100;
//						lightBlueX = 100;
//						lightBlueY = 250;
//						lightGreenX = 100;
//						lightGreenY = 400;
						
						lightX[0] = 100 ;
						lightX[1] = 100 ;
						lightX[2] = 100 ;
						lightY[0] = 100 ;
						lightY[1] = 300 ;
						lightY[2] = 500 ;

						lightDefaultColor[0] = Color.GRAY;
						lightDefaultColor[1] = Color.GRAY;
						lightDefaultColor[2] = Color.GRAY;
						
						//设置测试场景灯的开关状态
						flagLight[0] = true;
						flagLight[1] = true;
						flagLight[2] = true;

						mainSceneElementView.invalidate();

					} else {

						LogUtils.i("lightJson:"
								+ sceneList.get(position).getLightsJson());

						//判断场景列表是sceneList否为空
						if (TextUtils.isEmpty(sceneList.get(position)
								.getLightsJson())) {

//							lightRedX = 100;
//							lightRedY = 100;
//							lightBlueX = 100;
//							lightBlueY = 250;
//							lightGreenX = 100;
//							lightGreenY = 400;
							
							lightX[0] = 100 ;
							lightX[1] = 100 ;
							lightX[2] = 100 ;
							lightY[0] = 100 ;
							lightY[1] = 250 ;
							lightY[2] = 400 ;

							lightDefaultColor[0] = Color.GRAY;
							lightDefaultColor[1] = Color.GRAY;
							lightDefaultColor[2] = Color.GRAY;

							mainSceneElementView.invalidate();

						} else {

							//场景列表不为空时
							LogUtils.i("lightJson:"
									+ sceneList.get(position).getLightsJson());

							JSONArray jsonArray;
							//得到数据库中保存的json信息，即灯的坐标，颜色，亮度
							try {
								jsonArray = new JSONArray(sceneList.get(
										position).getLightsJson());

								lightX[0] = jsonArray.getJSONObject(0).optInt(
										"x");
								LogUtils.i("lightRedX:" + lightX[0]);
								if (lightX[0] == 0)
									lightX[0] = 30;

								lightY[0] = jsonArray.getJSONObject(0).optInt(
										"y");
								LogUtils.i("lightRedY:" + lightY[0]);
								if (lightY[0] == 0)
									lightY[0] = 50;

								// redLightColor = jsonArray.getJSONObject(0)
								// .optInt("color");
								lightDefaultColor[0] = Color.GRAY;

								light1BrightProgress = jsonArray.getJSONObject(
										0).optInt("bright");

								lightX[1] = jsonArray.getJSONObject(1).optInt(
										"x");
								if (lightX[1] == 0)
									lightX[1] = 30;

								lightY[1] = jsonArray.getJSONObject(1).optInt(
										"y");
								if (lightY[1] == 0)
									lightY[1] = 150;

								// blueLightColor = jsonArray.getJSONObject(1)
								// .optInt("color");
								lightDefaultColor[1] = Color.GRAY;

								light2BrightProgress = jsonArray.getJSONObject(
										1).optInt("bright");

								lightX[2] = jsonArray.getJSONObject(2)
										.optInt("x");
								if (lightX[2] == 0)
									lightX[2] = 30;

								lightY[2] = jsonArray.getJSONObject(2)
										.optInt("y");
								if (lightY[2] == 0)
									lightY[2] = 250;

								// greenLightColor = jsonArray.getJSONObject(2)
								// .optInt("color");
								lightDefaultColor[2] = Color.GRAY;
								light3BrightProgress = jsonArray.getJSONObject(
										2).optInt("bright");
								
								//得到json灯组的信息
								lightGroup[0][0] = jsonArray.getJSONObject(0).optInt("g00");
								lightGroup[0][1] = jsonArray.getJSONObject(0).optInt("g01");
								lightGroup[0][2] = jsonArray.getJSONObject(0).optInt("g02");
								lightGroup[0][3] = jsonArray.getJSONObject(0).optInt("g03");
								lightGroup[0][4] = jsonArray.getJSONObject(0).optInt("g04");
								
								lightGroup[1][0] = jsonArray.getJSONObject(1).optInt("g10");
								lightGroup[1][1] = jsonArray.getJSONObject(1).optInt("g11");
								lightGroup[1][2] = jsonArray.getJSONObject(1).optInt("g12");
								lightGroup[1][3] = jsonArray.getJSONObject(1).optInt("g13");
								lightGroup[1][4] = jsonArray.getJSONObject(1).optInt("g14");
								
								lightGroup[2][0] = jsonArray.getJSONObject(2).optInt("g20");
								lightGroup[2][1] = jsonArray.getJSONObject(2).optInt("g21");
								lightGroup[2][2] = jsonArray.getJSONObject(2).optInt("g22");
								lightGroup[2][3] = jsonArray.getJSONObject(2).optInt("g23");
								lightGroup[2][4] = jsonArray.getJSONObject(2).optInt("g24");
								
								LogUtils.i("得到初始状态json灯组：" + lightGroup[0][0]+ lightGroup[0][1]+ lightGroup[0][2]);
								LogUtils.i("得到初始状态json灯组：" + lightGroup[1][0]+ lightGroup[1][1]+ lightGroup[1][2]);
								LogUtils.i("得到初始状态json灯组：" + lightGroup[2][0]+ lightGroup[2][1]+ lightGroup[2][2]);
								
								/****************************************************************/
			//					light123X = jsonArray.getJSONObject(3).optInt("x");
		//						LogUtils.i("light123X:" + light123X);
		//						if (light123X == 0)
		//							light123X = 30;

		//						light123Y = jsonArray.getJSONObject(3).optInt("y");
		//						LogUtils.i("light123Y:" + light123Y);
		//						if (light123Y == 0)
		//							lightRedY = 50;
//
//								light123Color = Color.GRAY;

//								light123BrightProgress = jsonArray.getJSONObject(3).optInt("bright");
								/****************************************************************/
								

								//数据库中得到灯的开关状态
								flagLight[0] = jsonArray.getJSONObject(0).optBoolean("flag");
								flagLight[1] = jsonArray.getJSONObject(1).optBoolean("flag");
								flagLight[2] = jsonArray.getJSONObject(2).optBoolean("flag");
//								flagLight123 = jsonArray.getJSONObject(3).optBoolean("flag");

								//原始开关状态
//								flagLight[0] = true;
//								flagLight[1] = true;
//								flagLight[2] = true;

								mainSceneElementView.invalidate();

							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}

				}
			}

		});

		// 场景长按处理
		gallery.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {

				if (sceneList.get(position).name.equals("增加新场景")) {

					return true;

				} else {

					final EditText input = new EditText(mActivity);// 这一句话挺重要的
					AlertDialog builder = new AlertDialog.Builder(mActivity)
							.create();
					builder.setTitle("修改标题");
					builder.setView(input);
					input.setText(sceneList.get(position).name);// 初始化显示原标题
					// 为对话框添加"保存"按钮
					builder.setButton(DialogInterface.BUTTON_POSITIVE, "保存",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									inputString = input.getText().toString();
									if (inputString != null) {// 修改场景标题

										sceneList.get(position).setName(
												inputString);

										// [{mac,name,x,y,color,bright},{},{}]

										if (!sceneList.get(position).name
												.contains("测试")
												&& !sceneList.get(position).name
														.contains("增加新场景")) {

											Scene beforeScene = sceneList
													.get(position);
											if (dbUtils.querySceneByID(""
													+ beforeScene.id)) {
												dbUtils.deleteSceneById(beforeScene.id);
											}
											Scene modifyScene = new Scene();
											modifyScene.bitmap = beforeScene.bitmap;
											modifyScene.id = beforeScene.id;
											modifyScene.name = inputString;
											JSONArray jsonArray = new JSONArray();
											JSONObject jsonObject = new JSONObject();
											JSONObject jsonObject2 = new JSONObject();
											JSONObject jsonObject3 = new JSONObject();
	//										JSONObject jsonObject4 = new JSONObject();
											
//											JSONObject group1 = new JSONObject();
//											JSONObject group2 = new JSONObject();
//											JSONObject group3 = new JSONObject();
//											JSONObject group4 = new JSONObject();
//											JSONObject group5 = new JSONObject();
											
//											group1.put("g0", lightGroup[0][0]);
//											group1.put("g1", lightGroup[0][1]);
//											group1.put("g2", lightGroup[0][2]);
//											group1.put("g3", lightGroup[0][3]);
//											group1.put("g4", lightGroup[0][4]);
											
											
											try {
												jsonObject.put("name", "灯1");
												jsonObject.put("flag", flagLight[0]);
												jsonObject.put("x", lightX[0]);
												jsonObject.put("y", lightY[0]);
												jsonObject.put("color",
														lightDefaultColor[0]);
												jsonObject.put("bright",
														light1BrightProgress);
//												jsonObject.put("group", group1);
												jsonObject.put("g00", lightGroup[0][0]);
												jsonObject.put("g01", lightGroup[0][1]);
												jsonObject.put("g02", lightGroup[0][2]);
												jsonObject.put("g03", lightGroup[0][3]);
												jsonObject.put("g04", lightGroup[0][4]);
												
												

												jsonObject2.put("name", "灯2");
												jsonObject2.put("flag", flagLight[1]);
												jsonObject2
														.put("x", lightX[1]);
												jsonObject2
														.put("y", lightY[1]);
												jsonObject2.put("color",
														lightDefaultColor[1]);
												jsonObject2.put("bright",
														light2BrightProgress);
												jsonObject2.put("g10", lightGroup[1][0]);
												jsonObject2.put("g11", lightGroup[1][1]);
												jsonObject2.put("g12", lightGroup[1][2]);
												jsonObject2.put("g13", lightGroup[1][3]);
												jsonObject2.put("g14", lightGroup[1][4]);

												jsonObject3.put("name", "灯3");
												jsonObject3.put("flag", flagLight[2]);
												jsonObject3.put("x",
														lightX[2]);
												jsonObject3.put("y",
														lightY[2]);
												jsonObject3.put("color",
														lightDefaultColor[2]);
												jsonObject3.put("bright",
														light3BrightProgress);
												jsonObject3.put("g20", lightGroup[2][0]);
												jsonObject3.put("g21", lightGroup[2][1]);
												jsonObject3.put("g22", lightGroup[2][2]);
												jsonObject3.put("g23", lightGroup[2][3]);
												jsonObject3.put("g24", lightGroup[2][4]);
												
												LogUtils.i("灯分组" + lightGroup[0][0]+ lightGroup[0][1]+ lightGroup[0][2]);
												LogUtils.i("灯分组" + lightGroup[1][0]+ lightGroup[1][1]+ lightGroup[1][2]);
												LogUtils.i("灯分组" + lightGroup[2][0]+ lightGroup[2][1]+ lightGroup[2][2]);
												
	//											jsonObject4.put("name", "灯123");
	//											jsonObject4.put("flag", flagLight123);
	//											jsonObject4.put("x",
	//													light123X);
	//											jsonObject4.put("y",
	//													light123Y);
	//											jsonObject4.put("color",
	//													light123Color);
	//											jsonObject4.put("bright",
	//													light123BrightProgress);

												jsonArray.put(jsonObject);
												jsonArray.put(jsonObject2);
												jsonArray.put(jsonObject3);
		//										jsonArray.put(jsonObject4);

											} catch (JSONException e) {
												e.printStackTrace();
											}
											modifyScene.lightsJson = jsonArray
													.toString();
											dbUtils.insertScene(modifyScene);

											sceneList.get(position).lightsJson = jsonArray
													.toString();

										} else {

											Toast.makeText(mActivity,
													"预设场景不可操作!",
													Toast.LENGTH_SHORT).show();
										}

										imageAdapter.notifyDataSetChanged();
										mainSceneElementView.invalidate();
									}
								}
							});
					// 为对话框添加"取消"按钮
					builder.setButton(DialogInterface.BUTTON_NEGATIVE, "取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {

								}
							});
					// 为对话框添加"删除"按钮
					builder.setButton(DialogInterface.BUTTON_NEUTRAL, "删除",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {

									int id = sceneList.get(position).getId();

									if (dbUtils.querySceneByID("" + id)) {
										dbUtils.deleteSceneById(id);
									}
									
									sceneList.remove(position);

									imageAdapter.notifyDataSetChanged();
								}
							});
					builder.show();
				}

				return false;
			}

		});

		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		LogUtils.i("onStart");

		sceneWidth = WidgetUtils.getWidth(rlImage);
		sceneHeight = WidgetUtils.getHeight(rlImage);
		LogUtils.i("width:" + WidgetUtils.getWidth(rlImage));
		LogUtils.i("height:" + WidgetUtils.getHeight(rlImage));
	}

	@Override
	public void onResume() {
		super.onResume();
		LogUtils.i("onResume");
		imageAdapter.notifyDataSetChanged();
//		if (!mBluetoothAdapter.isEnabled()) {
//            if (!mBluetoothAdapter.isEnabled()) {
//               Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//               startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
//               System.out.println("error_bluetooth_not_enabled1");
//               //        	mBluetoothAdapter.enable();//also pop-up dialog box
//            }
//        }	
		for (LinlightDevice yeelightDevice : ConnectionManager.devices) {
			  mf_connectionManager.connect(yeelightDevice
						.getAddress());
				Log.i("wwwwwww", "connet all light---启动线程111111") ;	
			System.out.println("connet all light---"+yeelightDevice.getAddress());	
	//		Toast.makeText("connet all light----", str1, Toast.LENGTH_SHORT).show();
				try {
					Thread.sleep(80);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
	}

	@Override
	public void onDestroy() {
		LogUtils.i("onDestroy");
		super.onDestroy();
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		LogUtils.i("onActivityCreated");
	}

	private Bitmap btPointer;
	
	//圆圈之坐标偏移以及半径
	private int offsetX, offsetY, offsetRadius;
	
	private float light1Radius, light2Radius, light3Radius, light123Radius;
	
	//定义最小半径和最大半径
	private static final float MIN_RADIUS = 2;
	private static final float MAX_RADIUS = 26;
	
	//相较手指灯图片偏移之坐标
	private int fingerOffsetX, fingerOffsetY;
	
	private int magnifierRadius;
	
	class MainSceneElementView extends View {

		public MainSceneElementView(Context context) {
			super(context);

			initLightStatus();
		}

		private void initLightStatus() {

			paintRed = new Paint();
			paintBlue = new Paint();
			paintGreen = new Paint();
			paint123 = new Paint();
			magnifierPaint = new Paint();
			
			paintRed.setAntiAlias(true);
			paintBlue.setAntiAlias(true);
			paintGreen.setAntiAlias(true);
			paint123.setAntiAlias(true);
			magnifierPaint.setAntiAlias(true);

			btPointer = BitmapFactory.decodeResource(getResources(),
					R.drawable.pointer);
			//得到滑动灯图的宽高
			pointerWidth = btPointer.getWidth();
			pointerHeight = btPointer.getHeight();

			offsetX = pointerWidth >> 1;
			offsetY = pointerHeight / 3;
//			offsetRadius = pointerWidth / 3;
			
			light1Radius = 10;
			light2Radius = 10;
			light3Radius = 10;
			light123Radius = 10;
			
			magnifierRadius = pointerWidth >> 1;
			
//			fingerOffsetX = pointerWidth;
//			fingerOffsetY = pointerHeight;

			LogUtils.i("pointerWidth:" + pointerWidth);
			LogUtils.i("pointerHeight:" + pointerHeight);
			
			magnifierShowStatus = STATUS_NONE;
			
		}

		@Override
		protected void onDraw(Canvas canvas) {
			super.onDraw(canvas);

			//压缩之后的图片
			canvas.drawBitmap(rlImgBack, 5, 5, null);

//			if (flagLight123) {
				
//				if (light123Close) {
//					light123Color = Color.GRAY;
//				}

				//设置画笔
//				paint123.setColor(light123Color);
				//画灯
//				canvas.drawBitmap(btPointer, light123X - fingerOffsetX, light123Y - fingerOffsetY, paint123);
				//画小圆
//				canvas.drawCircle(light123X + offsetX - fingerOffsetX, light123Y + offsetY - fingerOffsetY,
//						light123Radius, paint123);
				
//				if (magnifierShowStatus == STATUS_LIGHT_123) {
//					magnifierPaint.setColor(light123Color);
					//画大圆
//					canvas.drawCircle(light123X - fingerOffsetX + magnifierRadius, light123Y - fingerOffsetY - magnifierRadius -2 , magnifierRadius, magnifierPaint);
//				}
//			}

			if (flagLight[0]) {
				
				if (light1Close) {
					lightDefaultColor[0] = Color.GRAY;
				}
				
				paintRed.setColor(lightDefaultColor[0]);
				canvas.drawBitmap(btPointer, lightX[0] - fingerOffsetX, lightY[0] - fingerOffsetY, paintRed);
				canvas.drawCircle(lightX[0] + offsetX - fingerOffsetX, lightY[0] + offsetY - fingerOffsetY,
						light1Radius, paintRed);
				
				if (magnifierShowStatus == STATUS_LIGHT_1) {
					magnifierPaint.setColor(lightDefaultColor[0]);
					canvas.drawCircle(lightX[0] - fingerOffsetX + magnifierRadius, lightY[0] - fingerOffsetY - magnifierRadius -2 , magnifierRadius, magnifierPaint);
				}
			}

			if (flagLight[1]) {
				
				if (light2Close) {
					lightDefaultColor[1] = Color.GRAY;
				}

				paintBlue.setColor(lightDefaultColor[1]);
				canvas.drawBitmap(btPointer, lightX[1] - fingerOffsetX, lightY[1] - fingerOffsetY, paintBlue);
				canvas.drawCircle(lightX[1] + offsetX - fingerOffsetX, lightY[1] + offsetY - fingerOffsetY,
						light2Radius, paintBlue);
				
				if (magnifierShowStatus == STATUS_LIGHT_2) {
					magnifierPaint.setColor(lightDefaultColor[1]);
					canvas.drawCircle(lightX[1] - fingerOffsetX + magnifierRadius, lightY[1] - fingerOffsetY - magnifierRadius -2 , magnifierRadius, magnifierPaint);
				}
			}

			if (flagLight[2]) {
				
				if (light3Close) {
					lightDefaultColor[2] = Color.GRAY;
				}

				paintGreen.setColor(lightDefaultColor[2]);
				canvas.drawBitmap(btPointer, lightX[2] - fingerOffsetX, lightY[2] - fingerOffsetY,
						paintGreen);
				canvas.drawCircle(lightX[2] + offsetX - fingerOffsetX, lightY[2] + offsetY - fingerOffsetY,
						light3Radius, paintGreen);
				
				if (magnifierShowStatus == STATUS_LIGHT_3) {
					magnifierPaint.setColor(lightDefaultColor[2]);
					canvas.drawCircle(lightX[2] - fingerOffsetX + magnifierRadius, lightY[2] - fingerOffsetY - magnifierRadius -2 , magnifierRadius, magnifierPaint);
				}
			}
		}
	}

	class MainSceneGestureListener extends
			GestureDetector.SimpleOnGestureListener {

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			
			return super.onSingleTapUp(e);
		}

		@Override
		public void onShowPress(MotionEvent e) {

		}

		/**
		 * 长按灯操作
		 */
		@Override
		public void onLongPress(MotionEvent e) {

			final SeekBar seekBar = new SeekBar(mActivity);
			seekBar.setMax(255);
			AlertDialog builder = new AlertDialog.Builder(mActivity).create();
			builder.setView(seekBar);

			if (flagLight[0]
					&& ActionUtil.isEqual((int) e.getX(), (int) e.getY(),
							(int) lightX[0] + pointerWidth/2, (int) lightY[0] + pointerHeight/2)) {

				seekBar.setProgress(light1BrightProgress);
				seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {

					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {

					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						light1BrightProgress = progress;

//						new BrightUtils(mActivity)
//								.setScreenBrightness(light1BrightProgress);
						// 执行亮度具体操作
						changeLightBright(new Light());
						//设置灯的半径
						light1Radius = light1BrightProgress / 255.0f * MAX_RADIUS;
						if(light1Radius <= MIN_RADIUS)
							light1Radius = MIN_RADIUS;
						
						mainSceneElementView.invalidate();
					}
				});
				builder.show();

			} else if (flagLight[1]
					&& ActionUtil.isEqual((int) e.getX(), (int) e.getY(),
							(int) lightX[1] + pointerWidth/2, (int) lightY[1] + pointerHeight/2)) {

				seekBar.setProgress(light2BrightProgress);
				seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {

					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {

					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						light2BrightProgress = progress;

//						new BrightUtils(mActivity)
//								.setScreenBrightness(light2BrightProgress);
						// 执行亮度具体操作
						changeLightBright(new Light());
						
						light2Radius = light2BrightProgress / 255.0f * MAX_RADIUS;
						if(light2Radius <= MIN_RADIUS)
							light2Radius = MIN_RADIUS;
						
						mainSceneElementView.invalidate();
					}
				});
				builder.show();

			} else if (flagLight[2]
					&& ActionUtil.isEqual((int) e.getX(), (int) e.getY(),
							(int) lightX[2] + pointerWidth/2, (int) lightY[2] + pointerHeight/2)) {

				seekBar.setProgress(light3BrightProgress);
				seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {

					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {

					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						light3BrightProgress = progress;
//						new BrightUtils(mActivity)
//								.setScreenBrightness(light3BrightProgress);
						// 执行亮度具体操作
						changeLightBright(new Light());
						
						light3Radius = light3BrightProgress / 255.0f * MAX_RADIUS;
						if(light3Radius <= MIN_RADIUS)
							light3Radius = MIN_RADIUS;
						
						mainSceneElementView.invalidate();
					}
				});
				builder.show();
			} 
//			else if (flagLight123
//					&& ActionUtil.isEqual((int) e.getX(), (int) e.getY(),
//							(int) light123X, (int) light123Y)) {
//
//				seekBar.setProgress(light123BrightProgress);
//				seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
//
//					@Override
//					public void onStopTrackingTouch(SeekBar seekBar) {
//
//					}
//
//					@Override
//					public void onStartTrackingTouch(SeekBar seekBar) {
//
//					}
//
//					@Override
//					public void onProgressChanged(SeekBar seekBar,
//							int progress, boolean fromUser) {
//						light123BrightProgress = progress;
//
//						new BrightUtils(mActivity)
//								.setScreenBrightness(light123BrightProgress);
//						// 执行亮度具体操作
//						changeLightBright(new Light());
//						
//						light123Radius = light123BrightProgress / 255.0f * MAX_RADIUS;
//						if(light123Radius <= MIN_RADIUS)
//							light123Radius = MIN_RADIUS;
//						
//						mainSceneElementView.invalidate();
//					}
//				});
//				builder.show();
//			}
		}

		/**
		 * 灯的滑动事件
		 */
		
		/*注释
		initLightGroup
		形参 LighNo 当前需要初始化的灯分组，即表示二维数组中的行数
		*/
		public boolean initLightGroup(int LightNo)   //初始化灯数组
		{
			if(LightNo>2){		//判断灯的个数是否大于2
				return false;
			}
			
			for(int i=0; i < 3;i++){
				if(LightNo == i){
					lightGroup[LightNo][i] = 1;
				}
				else{
					lightGroup[LightNo][i] = 0;
				}
			}
			
			return true;
		}
		/*
		注释expandLightGroup()
		形参 int Light 表示当前需要展开的第几个灯分组，即表示二维数组中的行数
		*/
		public boolean expandLightGroup(int Light) //展开灯
		{
			LogUtils.i("lightGroup组的信息" + Light);
			if(Light>2){
				return false;
			}
			
			for(int i=0; i < 3;i++){
				if((i != Light)&&(1 == lightGroup[Light][i])){
					flagLight[i] = true;		//将Light分组中第i个灯显示
					lightX[Light] = 100;		//设置Light灯，即滑动灯x，y坐标 ，这里是将滑动灯的坐标设置为隐藏灯的初始化坐标
					lightY[Light] = 100+150*i;
					LogUtils.i("lightGroup组的信息" + lightGroup[Light][i]);
				}
			}
			LogUtils.i("lightGroup组的信息" + lightGroup[Light][0]+ lightGroup[Light][1]+ lightGroup[Light][2]+ lightGroup[Light][3]);
			initLightGroup(Light);
			return true;
		}
		
		
		public boolean isSingleLight(int Light)   //判断是否是一个灯
		{
			if(Light>2){
				return false;
			}
			
			boolean bIsSingle = true;
			for(int i=0; i < 3;i++){
				if((i != Light)&&(1 == lightGroup[Light][i])){
					bIsSingle = false;
				}
			}
			return bIsSingle;
		}
		//scrollLight 滑动的灯；   toMergeLight 将要合并的灯 ； MotionEvent e滑动事件
		public boolean mergeLight(int scrollLight, int toMergeLight, MotionEvent e)   //是否合并灯操作
		{
			if(scrollLight>2){		//判断滑动的灯是否为0,1,2
				return false;
			}
			if(toMergeLight>2){
				return false;
			}
			
			if ((false == flagLight[scrollLight]) || (false == flagLight[toMergeLight])){
				return false;
			}
			
			flagLight[scrollLight] = false;  	//将滑动的灯置为隐藏
			for(int i=0; i < 3;i++){
				if(0 == lightGroup[toMergeLight][i]){    //将要合并灯的分组中，第i个灯为0
					if(1 == lightGroup[scrollLight][i]){	//滑动灯的分组中，第i个灯为1
						lightGroup[toMergeLight][i] = lightGroup[scrollLight][i];  //将滑动灯分组中的1赋值给合并灯分组中，即两个灯合并
						lightGroup[scrollLight][i] = 0;		//合并之后将滑动灯分组设为0，即隐藏滑动的灯分组
					}
				}
			}
			
			
//			JSONArray jsonArray = new JSONArray();
//			JSONObject _obj = new JSONObject();
//			_obj.put("num1", lightGroup[toMergeLight][0]);
//			_obj.put("num1", saveGroup[0]); 
//			for(int i=0; i<5 ;i++){
//				saveGroup[i] = lightGroup[toMergeLight][i] ;
//				JSONArray jsonArray = new JSONArray();
//				JSONObject _obj = new JSONObject();
//				_obj.put("num"+i, saveGroup[i]); 
//			}
			
			lightGroup[scrollLight][scrollLight] = 1;   //将灯分组中scrollLight号灯置1 ，即用scrollLight表示第几号灯

			lightX[scrollLight] = (int) e.getX();		//设置滑动灯的x坐标
			lightY[scrollLight] = (int) e.getY();		//设置滑动灯的y坐标			
			lightDefaultColor[scrollLight] = Color.GRAY;
			
			LogUtils.i("设置滑动灯的坐标" + lightX[scrollLight]);
			LogUtils.i("设置滑动灯的坐标" + lightY[scrollLight]);
			
			LogUtils.i("灯分组" + lightGroup[0][0]+ lightGroup[0][1]+ lightGroup[0][2]);
			LogUtils.i("灯分组" + lightGroup[1][0]+ lightGroup[1][1]+ lightGroup[1][2]);
			LogUtils.i("灯分组" + lightGroup[2][0]+ lightGroup[2][1]+ lightGroup[2][2]);
			
//			lightX[toMergeLight] = (int) e.getX();		//设置滑动灯的x坐标
//			lightY[toMergeLight] = (int) e.getY();		//设置滑动灯的y坐标
//			LogUtils.i("设置滑动灯的坐标toMergeLight" + lightX[toMergeLight]);
//			LogUtils.i("设置滑动灯的坐标toMergeLight" + lightY[toMergeLight]);
//			lightDefaultColor[toMergeLight] = Color.GRAY;
			
			return true;
		}
		
		
		
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {

			// 确定灯的滑动范围不出界
			if ((e2.getX() > 0 && e2.getX() <= (Constants.SCREEN_WIDTH - pointerWidth/2))
					&& (e2.getY() > 0 && e2.getY() <= ((Constants.SCREEN_HEIGHT * 2 / 3)
							- pointerHeight - 20 - 80))) {

				// 滑动前提：单击点中才能有滑动操作
				if (clickLight[0]) {

					lightX[0] = (int) e2.getX() - pointerWidth/2;
					lightY[0] = (int) e2.getY() - pointerHeight/2;

					//设置rgb文本颜色
//					lightColor
//							.setTextColor(backColorToRGB(getBackgroundColor(e2)));
					lightDefaultColor[0] = backColorToRGB(getBackgroundColor(e2));
					
					//
					
//					Message rgbmsg = new Message() ;
//					rgbmsg.what = 123 ;
//					rgbmsg.obj = lightDefaultColor[0] ;
//					
//					mHandler.sendMessage(rgbmsg) ;
					
					
 	//				lightColor.setText("RGB:" + lightDefaultColor[0]);
					LogUtils.i("redLightColor:" + lightDefaultColor[0]);
					Log.i("wwwwwww", "启动线程onscroll") ;
					
					mf_connectionManager.writeBrightAndColor(
							lightDefaultColor[0] ,
							ConnectionManager.connected_devices.get(0));
					magnifierShowStatus = STATUS_LIGHT_1;

				}

				if (clickLight[1]) {

					lightX[1] = (int) e2.getX() - pointerWidth/2;
					lightY[1] = (int) e2.getY() - pointerHeight/2;

					//设置rgb文本颜色
//					lightColor
//							.setTextColor(backColorToRGB(getBackgroundColor(e2)));
					lightDefaultColor[1] = backColorToRGB(getBackgroundColor(e2));
					//设置文本内容
//					lightColor.setText("RGB:" + lightDefaultColor[1]);
					
					magnifierShowStatus = STATUS_LIGHT_2;
				}

				if (clickLight[2]) {

					lightX[2] = (int) e2.getX() - pointerWidth/2;
					lightY[2] = (int) e2.getY() - pointerHeight/2; 

					//设置rgb文本颜色
//					lightColor
//							.setTextColor(backColorToRGB(getBackgroundColor(e2)));
					lightDefaultColor[2] = backColorToRGB(getBackgroundColor(e2));
					//设置rgb文本内容
//					lightColor.setText("RGB" + lightDefaultColor[2]);
					
					magnifierShowStatus = STATUS_LIGHT_3;
				}
				

//				//设置三个灯合并的滑动操作
//				if (clickLight123) {
//
//					light123X = (int) e2.getX() - pointerWidth/2;
//					light123Y = (int) e2.getY() - pointerHeight/2;
////					lightColor
////							.setTextColor(backColorToRGB(getBackgroundColor(e2)));
//					light123Color = backColorToRGB(getBackgroundColor(e2));
////					lightColor.setText("RGB:" + light123Color);
//					
//					magnifierShowStatus = STATUS_LIGHT_123;
//
//				}

				// 如果在滑动过程中，三个灯坐标有重合，即做合并灯操作
//				if (ActionUtil.isEqual((int) lightRedX, (int) lightRedY,
//						(int) lightGreenX, (int) lightGreenY)
//						&& ActionUtil.isEqual((int) lightRedX, (int) lightRedY,
//								(int) lightBlueX, (int) lightBlueY)
//						&& ActionUtil.isEqual((int) lightGreenX,
//								(int) lightGreenY, (int) lightBlueX,
//								(int) lightBlueY)) {
//
//					setLightSwitch(3);
//					lightName.setText(sLight[3]);
//
//					flagLight1 = false;
//					flagLight2 = false;
//					flagLight3 = false;
//					flagLight123 = true;
//
//					clickLight1 = false;
//					clickLight2 = false;
//					clickLight3 = false;
//					clickLight123 = true;
//
//					light123X = (int) e2.getX();
//					light123Y = (int) e2.getY();
//					light123Color = Color.GRAY;
//
//					mainSceneElementView.invalidate();
//				}
				
				//判断一号灯坐标与三号灯坐标是否相等
				if (ActionUtil.isEqual((int) lightX[0], (int) lightY[0],
						(int) lightX[2], (int) lightY[2])) {
					setLightSwitch(3);				//设置灯的开关状态
					lightName.setText(sLight[3]);		//灯名设置为合并灯
					if(clickLight[0]){					//如果一号灯被点击
						mergeLight(0,2,e2);				//将一号灯与三号灯合并
					}else{
						mergeLight(2,0,e2);				//将三号灯与一号灯合并
					}
					
					mainSceneElementView.invalidate();
				}
				//判断一号灯坐标与二号灯坐标是否相等
				else if(ActionUtil.isEqual((int) lightX[0], (int) lightY[0],
  						(int) lightX[1], (int) lightY[1])){
					setLightSwitch(3);
					lightName.setText(sLight[3]);
					if(clickLight[0]){
						mergeLight(0,1,e2);
					}else{
						mergeLight(1,0,e2);
					}
					mainSceneElementView.invalidate();
				}
				//判断二号灯坐标与三号灯坐标是否相等
				else if(ActionUtil.isEqual((int) lightX[2],
 						(int) lightY[2], (int) lightX[1],
  						(int) lightY[1])){
					setLightSwitch(3);
					lightName.setText(sLight[3]);
					if(clickLight[1]){
						mergeLight(1,2,e2);
					}else{
						mergeLight(2,1,e2);
					}
					mainSceneElementView.invalidate();
				}
				
				mainSceneElementView.invalidate();
			}
			
			System.out.println("e2.getAction:"+e2.getAction());

			return true;
		}

		/**
		 * 点击事件
		 */
		@Override
		public boolean onDown(MotionEvent e) {

			if (flagLight[0]
					&& ActionUtil.isEqual((int) e.getX(), (int) e.getY(),
							(int) lightX[0] + pointerWidth/2, (int) lightY[0] + pointerHeight/2)) {

				setLightSwitch(0);		//设置一号灯打开
				
//				if(isSingleLight(0)){	//修改，判断是否是合并灯，设置不同的灯显示
				lightName.setText(sLight[0]);

				clickLight[0] = true;
				clickLight[1] = false;
				clickLight[2] = false;
//				}
//				else{
//					lightName.setText(sLight[3]);
//					clickLight[0] = true;
//					clickLight[1] = false;
//					clickLight[2] = false;
//				}

				if (light1Close) {

				} else {

				}

			} else if (flagLight[1]
					&& ActionUtil.isEqual((int) e.getX(), (int) e.getY(),
							(int) lightX[1] + pointerWidth/2, (int) lightY[1] + pointerHeight/2)) {

				setLightSwitch(1);
				lightName.setText(sLight[1]);

				clickLight[0] = false;
				clickLight[1] = true;
				clickLight[2] = false;

			} else if (flagLight[2]
					&& ActionUtil.isEqual((int) e.getX(), (int) e.getY(),
							(int) lightX[2] + pointerWidth/2, (int) lightY[2]+pointerHeight/2)) {

				setLightSwitch(2);
				lightName.setText(sLight[2]);

				clickLight[0] = false;
				clickLight[1] = false;
				clickLight[2] = true;

			} else {
			}

			return true;
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			
			return true;
		}

		@Override 
		public boolean onDoubleTapEvent(MotionEvent e) {
			return true;
		}

		/**
		 * 双击事件
		 */
		@Override
		public boolean onDoubleTap(MotionEvent e) {

			LogUtils.i("双击操作");

			// 双击三号灯
			// 双击在组合灯上时,拆散灯操作
//			if (flagLight123
//				&& ActionUtil.isEqual((int) e.getX(), (int) e.getY(),
//				light123X, light123Y)) {
//	
//				flagLight123 = false;
//				flagLight1 = true;
//				flagLight2 = true;
//				flagLight3 = true;
//	
//				lightRedX = 100;
//				lightRedY = 100;
//				lightBlueX = 100;
//				lightBlueY = 250;
//				lightGreenX = 100;
//				lightGreenY = 400;
//	
//				mainSceneElementView.invalidate();
//				return true;
//			}
			//为三号灯设置双击事件响应
			if ((flagLight[2] && ActionUtil.isEqual((int) e.getX(),
					(int) e.getY(), (int) lightX[2] + pointerWidth/2, (int) lightY[2] + pointerHeight/2))) {
				
				if(isSingleLight(2)){
					if (light3Close) {
						
						Toast.makeText(mActivity, "双击3号灯，灯将打开", Toast.LENGTH_SHORT).show();
						
					} else {
						
						Toast.makeText(mActivity, "双击3号灯，灯将关闭", Toast.LENGTH_SHORT).show();
					}
					
					light3Close = !light3Close;
				}else{
					expandLightGroup(2);
				}
				

			}
			
			//为一号灯设置双击事件响应
			else if ((flagLight[0] && ActionUtil.isEqual((int) e.getX(),
					(int) e.getY(), (int) lightX[0]+pointerWidth/2, (int) lightY[0]+pointerHeight/2))) {
				
				if(isSingleLight(0)){
					if (light1Close) {
						mf_connectionManager.writeBrightAndColor(
								lightDefaultColor[0] ,
								ConnectionManager.connected_devices.get(0));
						Toast.makeText(mActivity, "双击1号灯，灯将打开", Toast.LENGTH_SHORT).show();
						
					} else {
						
						mf_connectionManager.writeBlackAndColor(
								lightDefaultColor[0] ,
								ConnectionManager.connected_devices.get(0));
						Toast.makeText(mActivity, "双击1号灯，灯将关闭", Toast.LENGTH_SHORT).show();
					}
					
					light1Close = !light1Close;
				}
				else{
					expandLightGroup(0);
				}
				
			}
			
			//为二号灯设置双击事件响应
			else if ((flagLight[1] && ActionUtil.isEqual((int) e.getX(),
					(int) e.getY(), (int) lightX[1]+pointerWidth/2, (int) lightY[1]+pointerHeight/2))) {
				if(isSingleLight(1)){
					if (light2Close) {
						
						Toast.makeText(mActivity, "双击2号灯，灯将打开", Toast.LENGTH_SHORT).show();
						
					} else {
						
						Toast.makeText(mActivity, "双击2号灯，灯将关闭", Toast.LENGTH_SHORT).show();
					}
					
					light2Close = !light2Close;
				}else{
					expandLightGroup(1);
				}
			}

			// 双击在空白的地方
			else {

				LogUtils.i("双击在空白区域。");

				// 执行双击在空白地方的操作
				if (light123Close) {

					// 向蓝牙设备发送指令，所有的灯关闭
					mf_connectionManager.writeBlackAndColor(
							lightDefaultColor[0] ,
							ConnectionManager.connected_devices.get(0));
					Toast.makeText(mActivity, "向蓝牙设备发送指令，所有的灯关闭",
							Toast.LENGTH_SHORT).show();

//					{
//						// 发送蓝牙指令控制所有灯关闭
////						new BrightUtils(mActivity).setScreenBrightness(10);
//					}

				} else {

					// 向蓝牙设备发送指令，所有的灯打开
					mf_connectionManager.writeBrightAndColor(
							lightDefaultColor[0] ,
							ConnectionManager.connected_devices.get(0));
					Toast.makeText(mActivity, "向蓝牙设备发送指令，所有的灯打开",
							Toast.LENGTH_SHORT).show();

//					{
//						// 发送蓝牙指令控制所有灯打开
//						new BrightUtils(mActivity).setScreenBrightness(250);
//					}
				}

				light123Close = !light123Close;
			}
			
			mainSceneElementView.invalidate();

			return true;
		}
	}

	public boolean getLightSwitch(int postion) {

		return bLight[postion];

	}

	public void setLightSwitch(int postion) {

		for (int i = 0; i < bLight.length; i++) {

			if (i == postion) {

				bLight[i] = true;

			} else {

				bLight[i] = false;
			}
		}
	}

	/**
	 * 获取手势点上的颜色
	 * 
	 * @param event
	 * @return
	 */
	public int getBackgroundColor(MotionEvent event) {

		int x = (int) (event.getX() );
		int y = (int) (event.getY() + (pointerHeight/2));

		int color = rlImgBack.getPixel(x, y);
		ActionUtil.r = Color.red(color);
		ActionUtil.g = Color.green(color);
		ActionUtil.b = Color.blue(color);
		return color;
	}

	/**
	 * Integer type color convert to RGB color value.
	 * 
	 * @param backgroundColor
	 * @return
	 */
	private int backColorToRGB(int backgroundColor) {

		int clr = backgroundColor;
		int red = (clr & 0x00ff0000) >> 16; // 取高两位
		int green = (clr & 0x0000ff00) >> 8; // 取中两位
		int blue = clr & 0x000000ff; // 取低两位
		return Color.rgb(red, green, blue);
	}

	/**
	 * 改变灯的亮度
	 */
	private void changeLightBright(Light light) {

		int lightID = light.id;
		int lightBright = light.bright;

		{
			// 发送灯的id以及亮度给指定的蓝牙设备
		}
	}

	// ////////////从相册或相机获取图片////////////////////////////

	/**
	 * 通过uri获取文件的绝对路径
	 * 
	 * @param uri
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String getAbsoluteImagePath(Activity context, Uri uri) {
		String imagePath = "";
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = context.managedQuery(uri, proj, // Which columns to
														// return
				null, // WHERE clause; which rows to return (all rows)
				null, // WHERE clause selection arguments (none)
				null); // Order-by clause (ascending by name)

		if (cursor != null) {
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			if (cursor.getCount() > 0 && cursor.moveToFirst()) {
				imagePath = cursor.getString(column_index);
			}
		}

		return imagePath;
	}

	/**
	 * 获取文件扩展名
	 * 
	 * @param fileName
	 * @return
	 */
	public static String getFileFormat(String fileName) {
		if (TextUtils.isEmpty(fileName))
			return "";

		int point = fileName.lastIndexOf('.');
		return fileName.substring(point + 1);
	}

	// 裁剪头像的绝对路径
	private Uri getUploadTempFile(Uri uri) {
		String storageState = Environment.getExternalStorageState();
		if (storageState.equals(Environment.MEDIA_MOUNTED)) {
			File savedir = new File(FILE_SAVEPATH);
			if (!savedir.exists()) {
				savedir.mkdirs();
			}
		} else {
			return null;
		}
		String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss")
				.format(new Date());
		String thePath = getAbsolutePathFromNoStandardUri(uri);

		// 如果是标准Uri
		if (TextUtils.isEmpty(thePath)) {
			thePath = getAbsoluteImagePath(mActivity, uri);
		}
		String ext = getFileFormat(thePath);
		ext = TextUtils.isEmpty(ext) ? "jpg" : ext;
		// 照片命名
		String cropFileName = "linlight_" + timeStamp + "." + ext;
		// 裁剪头像的绝对路径
		protraitPath = FILE_SAVEPATH + cropFileName;
		protraitFile = new File(protraitPath);

		cropUri = Uri.fromFile(protraitFile);
		return this.cropUri;
	}

	/**
	 * 选择图片裁剪
	 * 
	 * @param output
	 */
	private void startImagePick() {
		Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		intent.addCategory(Intent.CATEGORY_OPENABLE);
		intent.setType("image/*");
		startActivityForResult(Intent.createChooser(intent, "选择图片"),
				REQUEST_CODE_GETIMAGE_BYCROP);
	}

	/**
	 * 裁剪
	 * 
	 * @param data
	 *            原始图片
	 * @param output
	 *            裁剪后图片
	 */
	private void startActionCrop(Uri data) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(data, "image/*");
		intent.putExtra("output", this.getUploadTempFile(data));
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);// 裁剪框比例
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", Constants.SCREEN_WIDTH);// 输出图片大小
		intent.putExtra("outputY", Constants.SCREEN_HEIGHT*2/3);
		intent.putExtra("scale", true);// 去黑边
		intent.putExtra("scaleUpIfNeeded", true);// 去黑边
		startActivityForResult(intent, REQUEST_CODE_GETIMAGE_BYSDCARD);
	}

	@Override
	public void onActivityResult(final int requestCode, final int resultCode,
			final Intent data) {
		if (resultCode != Activity.RESULT_OK)
			return;

		switch (requestCode) {
		case REQUEST_CODE_GETIMAGE_BYCROP:
			startActionCrop(data.getData());// 选图后裁剪
			break;
		case REQUEST_CODE_GETIMAGE_BYSDCARD:

			Bitmap photo = null;
			photo = getBitmapByFile(new File(protraitPath));

			Scene scene = new Scene();
			scene.setId(new SimpleDateFormat("yyyyMMddHHmmss").format(
					new Date()).hashCode());
			scene.setName("新加场景"
					+ (new SimpleDateFormat("HHmmss").format(new Date())));
			scene.setBitmap(photo);

			JSONArray jsonArray = new JSONArray();

			try {
//			 flagLight1 = true;
//			 flagLight2 = true;
//			 flagLight3 = true;
//			 flagLight123 = false;
//			private int lightGroup[][] = new int[][]{{1,0,0,0,0},{0,1,0,0,0},{0,0,1,0,0},{0,0,0,1,0},{0,0,0,0,1}};
//			lightGroup =int[][] {{1,0,0,0,0},{0,1,0,0,0},{0,0,1,0,0},{0,0,0,1,0},{0,0,0,0,1}};
			
				
				JSONArray jsonLight1 = new JSONArray() ;
				
				lightGroup[0][0] = 1 ;
				lightGroup[0][1] = 0 ;
				lightGroup[0][2] = 0 ;
				lightGroup[0][3] = 0 ;
				
				lightGroup[1][0] = 0 ;
				lightGroup[1][1] = 1 ;
				lightGroup[1][2] = 0 ;
				lightGroup[1][3] = 0 ;
				
				lightGroup[2][0] = 0 ;
				lightGroup[2][1] = 0 ;
				lightGroup[2][2] = 1 ;
				lightGroup[2][3] = 0 ;
				

				JSONObject obj1 = new JSONObject();
				obj1.put("x", 100);
				obj1.put("y", 100);
				obj1.put("mac", "90:59:AF:27:B8:B9");
				obj1.put("name", "灯1");
				obj1.put("bright", 79);
				obj1.put("flag", flagLight[0]);
				obj1.put("saveGroup", saveGroup) ;
				
				//初始化灯组信息
				obj1.put("g00", lightGroup[0][0]);
				obj1.put("g01", lightGroup[0][1]);
				obj1.put("g02", lightGroup[0][2]);
				obj1.put("g03", lightGroup[0][3]);
				obj1.put("g04", lightGroup[0][4]);
				
//				objTmp .put("1", 0) ;
//				objTmp .put("1", 0) ;
//				objTmp .put("1", 0) ;
//				objTmp .put("1", 0) ;
//				objTmp .put("1", 0) ;

				JSONObject obj2 = new JSONObject();
				obj2.put("x", 100);
				obj2.put("y", 300);
				obj2.put("mac", "90:59:AF:27:B8:B2");
				obj2.put("name", "灯2");
				obj2.put("bright", 69);
				obj2.put("flag", flagLight[1]);
				
				obj2.put("g10", lightGroup[1][0]);
				obj2.put("g11", lightGroup[1][1]);
				obj2.put("g12", lightGroup[1][2]);
				obj2.put("g13", lightGroup[1][3]);
				obj2.put("g14", lightGroup[1][4]);

				JSONObject obj3 = new JSONObject();
				obj3.put("x", 100);
				obj3.put("y", 500);
				obj3.put("mac", "90:59:AF:27:B8:B3");
				obj3.put("name", "灯3");
				obj3.put("bright", 99);
				obj3.put("flag", flagLight[2]);
							
				obj3.put("g20", lightGroup[2][0]);
				obj3.put("g21", lightGroup[2][1]);
				obj3.put("g22", lightGroup[2][2]);
				obj3.put("g23", lightGroup[2][3]);
				obj3.put("g24", lightGroup[2][4]);
				
				
	//			JSONObject obj4 = new JSONObject();
	//			obj4.put("x", 100);
	//			obj4.put("y", 100);
	//			obj4.put("mac", "90:59:AF:27:B8:B9");
	//			obj4.put("name", "灯123");
	//			obj4.put("bright", 79);
	//			obj4.put("flag", flagLight123);

				jsonArray.put(obj1);
				jsonArray.put(obj2);
				jsonArray.put(obj3);
	//			jsonArray.put(obj4);

			} catch (JSONException e) {
				e.printStackTrace();
			}

			LogUtils.i("jsonArray：" + jsonArray.toString());
			scene.lightsJson = jsonArray.toString();

			DBUtils db = new DBUtils(mActivity);
			long l = db.insertScene(scene);
			LogUtils.i("long l:" + l);

			sceneList.add(sceneList.size() - 1, scene);
			imageAdapter.notifyDataSetChanged();
			mainSceneElementView.invalidate();

			break;
		}
	}

	public static Bitmap getBitmapByFile(File file) {
		FileInputStream fis = null;
		Bitmap bitmap = null;
		try {
			fis = new FileInputStream(file);
			bitmap = BitmapFactory.decodeStream(fis);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
			} catch (Exception e) {
			}
		}
		return bitmap;
	}
	
	private void scanLeDevice(final boolean enable) {
		if (enable) {
			// Stops scanning after a pre-defined scan period.
			Log.i("wwwwwww", "启动scanLeDevice统2") ;
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					//Log.i("wwwwwww", "scanLeDevice统失败") ;
					mf_connectionManager.stopLEScan();
				}
			}, SCAN_PERIOD);
			System.out.println("start_scanLeDevice");
			mf_connectionManager.startLEScan();
		} else {
			System.out.println("stop_scanLeDevice");
			mf_connectionManager.stopLEScan();
		}
	}

	/**
	 * 判断当前Url是否标准的content://样式，如果不是，则返回绝对路径
	 */
	public static String getAbsolutePathFromNoStandardUri(Uri mUri) {
		String filePath = null;

		String mUriString = mUri.toString();
		mUriString = Uri.decode(mUriString);

		String pre1 = "file://" + SDCARD + File.separator;
		String pre2 = "file://" + SDCARD_MNT + File.separator;

		if (mUriString.startsWith(pre1)) {
			filePath = Environment.getExternalStorageDirectory().getPath()
					+ File.separator + mUriString.substring(pre1.length());
		} else if (mUriString.startsWith(pre2)) {
			filePath = Environment.getExternalStorageDirectory().getPath()
					+ File.separator + mUriString.substring(pre2.length());
		}
		return filePath;
	}

	public final static String SDCARD_MNT = "/mnt/sdcard";
	public final static String SDCARD = "/sdcard";

	/** 请求相册 */
	public static final int REQUEST_CODE_GETIMAGE_BYSDCARD = 0;
	/** 请求裁剪 */
	public static final int REQUEST_CODE_GETIMAGE_BYCROP = 2;
}
