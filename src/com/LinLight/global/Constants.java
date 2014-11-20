package com.LinLight.global;


import com.LinLight.util.ActionUtil;

import android.content.res.Resources;
import android.graphics.Bitmap;

//定义所有用到常量的工具类
public class Constants {
	
    public static String RGB="";//数据库中初始化rgb的值
	public static float xZoom = 1F;// 缩放比例
	public static float yZoom = 1F;
	public static float sXtart = 10;// 棋盘的起始坐标
	public static float sYtart = 10;
	public static float Zoom = 1.5F;
	public static int color = 100;// 保存像素点的颜色值

	public static int SCREEN_WIDTH; // 屏幕宽度
	public static int SCREEN_HEIGHT; // 屏幕高度

	public static float SCREEN_WIDTH_STANDARD = 960; // 屏幕标准宽度
	public static float SCREEN_HEIGHT_STANDARD = 540; // 屏幕标准高度

	public static float xMainRatio = 1f;// X缩放比例
	public static float yMainRatio = 1f;// Y缩放比例

	// 预加载的图片数组
	public static Bitmap[] PIC_ARRAY;

	// 预加载的图片ID数组列表
	public static int[] PIC_ID = {

	};
	// 是否已经执行过changeRatio方法
	public static boolean changeRatioOkFlag = false;

	// 动态自适应屏幕的方法（这个方法很重要）
	public static void changeRatio() {
		if (changeRatioOkFlag) {
			return;
		}
		changeRatioOkFlag = true;

		xMainRatio = SCREEN_WIDTH / SCREEN_WIDTH_STANDARD;// 屏幕宽度除以屏幕标准宽度等于X缩放比例
		yMainRatio = SCREEN_HEIGHT / SCREEN_HEIGHT_STANDARD;
	}

	// 加载图片的方法
	public static void loadPic(Resources res) {
		PIC_ARRAY = new Bitmap[PIC_ID.length];
		for (int i = 0; i < PIC_ID.length - 1; i++) {
			PIC_ARRAY[i] = ActionUtil.loadBM(res, PIC_ID[i]);
			PIC_ARRAY[i] = ActionUtil.scaleToFitXYRatio(PIC_ARRAY[i],
					xMainRatio, yMainRatio);
		}
		PIC_ARRAY[3] = ActionUtil.loadBM(res, PIC_ID[3]);
		PIC_ARRAY[3] = ActionUtil.scaleToFitXYRatio(PIC_ARRAY[3], xMainRatio,
				xMainRatio);
	}
}