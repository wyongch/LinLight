package com.LinLight.app.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import com.LinLight.app.MyApplication;
import com.LinLight.app.R;
import com.LinLight.dao.DBUtils;
import com.LinLight.global.AppData;
import com.LinLight.global.TestData;
import com.LinLight.model.Scene;

public final class WelcomeActivity extends Activity {

	private DBUtils dbUtils = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final View view = View.inflate(this, R.layout.welcome, null);
		setContentView(view);

//		dbUtils = new DBUtils(this);

		// 渐变展示启动屏
		AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
		aa.setDuration(3000);
		view.startAnimation(aa);
		aa.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation arg0) {
				startActivity(new Intent(WelcomeActivity.this,
						HomePageActivity.class));
				WelcomeActivity.this.finish();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {

			}
		});

		// 场景测试数据
//		if (AppData.TEST) {
//
//			if (dbUtils.getAllScene().size() <= 0) {
//
//				initScene(getSceneData());
//			}
//		}
	}

	// ////////////////////Test
	// Data//////////////////////////////////////////////////////////

//	private List<Scene> getSceneData() {
//
//		List<Scene> list = new ArrayList<Scene>();
//
//		int size = TestData.SCENE_NAME_ARRAY.length;
//
//		for (int i = 0; i < size; i++) {
//
//			Scene scene = new Scene();
//			scene.setName(TestData.SCENE_NAME_ARRAY[i]);
//			scene.setBitmap(BitmapFactory.decodeResource(getResources(), TestData.SCENE_IMAGE_ID_ARRAY[i]));
//			scene.setLightsJson(new String());
//			list.add(scene);
//		}
//
//		return list;
//	}

//	private void initScene(List<Scene> sceneList) {
//
//		if (sceneList == null) {
//			return;
//		}
//
//		int listSize = sceneList.size();
//
//		if (listSize > 0) {
//
//			for (int i = 0; i < listSize; i++) {
//
//				dbUtils.insertScene(sceneList.get(i));
//			}
//		}
//	}

}
