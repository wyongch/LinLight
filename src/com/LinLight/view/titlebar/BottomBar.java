package com.LinLight.view.titlebar;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.LinLight.util.ViewUtil;

public class BottomBar extends LinearLayout {
	
	/** The m context. */
	private Activity mActivity;
	
	/** 副标题栏布局ID. */
	public int mBottomBarID = 2;
	
	/** 全局的LayoutInflater对象，已经完成初始化. */
	public LayoutInflater mInflater;
	
	
	/** 下拉选择. */
	private PopupWindow popupWindow;
	
	/** Window 管理器. */
	private WindowManager mWindowManager = null;
	
	/** 屏幕宽度. */
	public int diaplayWidth  = 320;
	

	public BottomBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		ininBottomBar(context);
	}

	public BottomBar(Context context) {
		super(context);
		ininBottomBar(context);
		
	}
	
	@SuppressWarnings("deprecation")
	public void ininBottomBar(Context context){
		
		mActivity  = (Activity)context;
		//水平排列
		this.setOrientation(LinearLayout.HORIZONTAL);
		this.setId(mBottomBarID);
		this.setPadding(0, 0, 0, 0);
		
		mInflater = LayoutInflater.from(context);
		
		mWindowManager = mActivity.getWindowManager();
		Display display = mWindowManager.getDefaultDisplay();
		diaplayWidth = display.getWidth();
		
	}
	

	/**
	 * 描述：标题栏的背景图.
	 * @param res  背景图资源ID
	 */
	public void setBottomBarBackground(int res) {
		this.setBackgroundResource(res);
	}
	
	
	/**
	 * 描述：标题栏的背景图.
	 * @param color  背景颜色值
	 */
	public void setBottomBarBackgroundColor(int color) {
		this.setBackgroundColor(color);
	}

	
	/**
	 * 描述：设置标题背景.
	 * @param d  背景图
	 */
	@SuppressWarnings("deprecation")
	public void setBottomBarBackgroundDrawable(Drawable d) {
		this.setBackgroundDrawable(d);
	}
	
	
	
	/**
	 * 描述：下拉菜单的的实现方法
	 * @param parent
	 * @param view 要显示的View
	 * @param offsetMode 不填满的模式
	 */
	private void showWindow(View parent,View view,boolean offsetMode) {
		ViewUtil.measureView(view);
		int popWidth = parent.getMeasuredWidth();
		if(view.getMeasuredWidth()>parent.getMeasuredWidth()){
			popWidth = view.getMeasuredWidth();
		}
		int popMargin = this.getMeasuredHeight();
		
		if(offsetMode){
			popupWindow = new PopupWindow(view,popWidth, LayoutParams.WRAP_CONTENT, true);
		}else{
			popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, true);
		}

		int[] location = new int[2];
		parent.getLocationInWindow(location);
		int startX = location[0]-parent.getLeft();
		if(startX + popWidth >= diaplayWidth){
			startX = diaplayWidth-popWidth-2;
		}
		
		// 使其聚集
		popupWindow.setFocusable(true);
		// 设置允许在外点击消失
		popupWindow.setOutsideTouchable(true);
		// 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
		popupWindow.setBackgroundDrawable(new ColorDrawable(android.R.color.transparent));
		
		popupWindow.showAtLocation(parent,Gravity.BOTTOM|Gravity.LEFT,startX,popMargin+2);
	}
	
	/**
	 * 
	 * 描述：设置下拉的View
	 * @param view
	 * @throws 
	 */
	public void setDropDown(final View parent,final View view){
		 if(parent==null || view == null){
			   return;
		 }
		 parent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				showWindow(parent,view,true);
			}
		});
		
	}

	/**
	 * 
	 * 描述：设置副标题栏界面显示
	 * @param view
	 * @throws 
	 */
	public void setBottomView(View view) {
		removeAllViews();
		addView(view,new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
	}
	
	/**
	 * 描述：用指定资源ID表示的View填充主界面.
	 * @param resId  指定的View的资源ID
	 */
	public void setBottomView(int resId) {
		setBottomView(mInflater.inflate(resId, null));
	}
	
}
