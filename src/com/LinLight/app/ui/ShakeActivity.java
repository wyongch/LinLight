package com.LinLight.app.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import com.LinLight.app.BaseActivity;
import com.LinLight.app.R;
import com.LinLight.view.listener.ShakeListener;
import com.LinLight.view.listener.ShakeListener.OnShakeListener;

public class ShakeActivity extends BaseActivity {

	private ImageView imageView;
	private ShakeListener mShakeListener = null;
	private Button btnBack = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.shake);
		imageView = (ImageView) findViewById(R.id.shake_img);
		btnBack = (Button) findViewById(R.id.shake_cancel);

		mShakeListener = new ShakeListener(this);
		mShakeListener.setOnShakeListener(new OnShakeListener() {

			@Override
			public void onShake() {

				startAnim();
				mShakeListener.stop();
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run() {

						mShakeListener.start();
					}
				}, 2000);
			}
		});
		
		btnBack.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				ShakeActivity.this.finish();
			}
		});
	}
	
	//定义摇一摇动画动画
	public void startAnim() { 

		AnimationSet anim = new AnimationSet(true);

		TranslateAnimation translateAnimation = new TranslateAnimation(-100f, 100f, 0, 0);
		translateAnimation.setDuration(1000);
		translateAnimation.setRepeatCount(1);
		translateAnimation.setRepeatMode(Animation.REVERSE);
		
		anim.addAnimation(translateAnimation);
		imageView.startAnimation(anim);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		this.finish();
	}
}
