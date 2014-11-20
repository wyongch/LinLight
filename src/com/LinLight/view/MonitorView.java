/*
 * Copyright (C) 2013 www.418log.org
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.LinLight.view;

import com.LinLight.global.AppData;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Typeface;
import android.os.SystemClock;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * 描述：阻塞测试View(显示页面停留时间，测试主界面阻塞情况).
 */
public class MonitorView extends View  {
	
	/** The tag. */
	private final static String TAG = "MonitorView";
	
	/** The Constant D. */
	private static final boolean D = AppData.DEBUG;

	/** The m start time. */
	private long mStartTime = -1;
	
	/** The m counter. */
	private int mCounter;
	
	/** The m fps. */
	private int mFps;
	
	/** The m paint. */
	private final Paint mPaint; 

	/**
	 * 构造阻塞测试View.
	 *
	 * @param context the context
	 */
	public MonitorView(Context context) {
		this(context, null);
	}

	/**
	 * 构造阻塞测试View.
	 *
	 * @param context the context
	 * @param attributeset the attributeset
	 */
	public MonitorView(Context context, AttributeSet attributeset) {
		super(context);
		if(D)
			Log.d(TAG,"MonitorView");
		mPaint = new Paint(); 
        mPaint.setColor(Color.WHITE);
        mPaint.setAntiAlias(true); 
        mPaint.setTypeface(Typeface.DEFAULT);
        mPaint.setTextSize(16);
	}

	/**
	 * 描述：TODO.
	 *
	 * @param canvas the canvas
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 * @author: zhaoqp
	 * @date：2013-6-17 上午9:04:49
	 * @version v1.0
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		
		//if(D)Log.d(TAG, "--AbMonitorView onDraw--");
		canvas.drawColor(Color.argb(80, 0, 0, 0));
		if (mStartTime == -1) {
			mStartTime = SystemClock.elapsedRealtime();
			mCounter = 0;
		}

		long now = SystemClock.elapsedRealtime();
		long delay = now - mStartTime;
		
		if(delay!=0){
			// 计算帧速率
	        mFps = (int)(mCounter * 1000 / delay);
		}
      
        String text = mFps + " fps";
        //获取值的文本的高度
        TextPaint mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTypeface(Typeface.DEFAULT);
        mTextPaint.setTextSize(16);
        FontMetrics fm  = mTextPaint.getFontMetrics();
        //得到行高
        int textHeight = (int)Math.ceil(fm.descent - fm.ascent)+2;
        int textWidth = (int)Graphical.getStringWidth(text,mTextPaint);
        
        
		canvas.drawText(text,(this.getWidth()-textWidth)/2, textHeight, mPaint);
		if (delay > 1000L) {
			mStartTime = now;
			mFps = mCounter;
			mCounter = 0;
		}
		mCounter++;
		super.onDraw(canvas);
	}

}
