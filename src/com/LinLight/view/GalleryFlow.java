package com.LinLight.view;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.Gallery;

//自定义显示场景的画廊(Gallery)
public class GalleryFlow extends Gallery {
	// 最初按下的坐标
	private float x;
	// 移动结束时的坐标
	private float endx;
	// 按下去时选中的项的下标
	private int selectedIndex;
	private float mxwidth;

	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		if (action == MotionEvent.ACTION_DOWN) {
			x = event.getX();
			endx = 0;
			selectedIndex = getSelectedItemPosition();
			if (selectedIndex == 0)
				mxwidth = getWidth() - x;
			if (selectedIndex == 0)
				mxwidth = x;
		} else if (action == MotionEvent.ACTION_MOVE) {
			if (selectedIndex == 0 || selectedIndex == getCount() - 1) {
				float mx = event.getX();
				Animation translate = null;
				endx = mx - x;
				if (selectedIndex == 0) {
					if (endx > 0 && endx < getWidth() && mx > x - mxwidth) {
						translate = new TranslateAnimation(endx, endx, 0, 0);
						translate.setDuration(25);
						translate.setFillAfter(true);
						startAnimation(translate);
					} else {
						endx = 0;
						selectedIndex = getSelectedItemPosition();
					}
				} else {
					if (endx < 0 && endx > -getWidth() * 2 && mx < x + mxwidth) {
						translate = new TranslateAnimation(endx, endx, 0, 0);
						translate.setDuration(25);
						translate.setFillAfter(true);
						startAnimation(translate);
					} else {
						endx = 0;
						selectedIndex = getSelectedItemPosition();
					}
				}
			}
		} else if (action == MotionEvent.ACTION_UP) {
			if ((selectedIndex == 0 || selectedIndex == getCount() - 1)) {
				int index = getSelectedItemPosition();
				if (index == 0 || index == getCount() - 1) {
					if (endx != 0) {
						Animation translate = new TranslateAnimation(endx, 0,
								0, 0);
						translate.setDuration(300);
						translate.setFillAfter(true);
						startAnimation(translate);
					}
				}
			}
		}
		return super.onTouchEvent(event);
	}

	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		int kEvent;
		if (e2.getX() > e1.getX()) {
			kEvent = KeyEvent.KEYCODE_DPAD_LEFT;
		} else {
			kEvent = KeyEvent.KEYCODE_DPAD_RIGHT;
		}
		onKeyDown(kEvent, null);
		return true;
	}

	// 以上代码实现尽头反弹效果
	/**
	 * The camera class is used to 3D transformation matrix.
	 */
	private Camera mCamera = new Camera();

	/**
	 * The max rotation angle.
	 */
	private int mMaxRotationAngle = 60;

	/**
	 * The max zoom value (Z axis).
	 */
	private int mMaxZoom = -120;

	/**
	 * The center of the gallery.
	 */
	private int mCoveflowCenter = 0;

	public GalleryFlow(Context context) {
		this(context, null);
	}

	public GalleryFlow(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public GalleryFlow(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// Enable set transformation.
		this.setStaticTransformationsEnabled(true);
		// Enable set the children drawing order.
		this.setChildrenDrawingOrderEnabled(true);
	}

	public int getMaxRotationAngle() {
		return mMaxRotationAngle;
	}

	public void setMaxRotationAngle(int maxRotationAngle) {
		mMaxRotationAngle = maxRotationAngle;
	}

	public int getMaxZoom() {
		return mMaxZoom;
	}

	public void setMaxZoom(int maxZoom) {
		mMaxZoom = maxZoom;
	}

	@Override
	protected int getChildDrawingOrder(int childCount, int i) {
		// Current selected index.
		int selectedIndex = getSelectedItemPosition()
				- getFirstVisiblePosition();
		if (selectedIndex < 0) {
			return i;
		}

		if (i < selectedIndex) {
			return i;
		} else if (i >= selectedIndex) {
			return childCount - 1 - i + selectedIndex;
		} else {
			return i;
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		mCoveflowCenter = getCenterOfCoverflow();
		super.onSizeChanged(w, h, oldw, oldh);
	}

	private int getCenterOfView(View view) {
		return view.getLeft() + view.getWidth() / 2;
	}

	@Override
	protected boolean getChildStaticTransformation(View child, Transformation t) {
		super.getChildStaticTransformation(child, t);

		final int childCenter = getCenterOfView(child);
		final int childWidth = child.getWidth();

		int rotationAngle = 0;
		t.clear();
		t.setTransformationType(Transformation.TYPE_MATRIX);

		// If the child is in the center, we do not rotate it.
		if (childCenter == mCoveflowCenter) {
			transformImageBitmap(child, t, 0);
		} else {
			// Calculate the rotation angle.
			rotationAngle = (int) (((float) (mCoveflowCenter - childCenter) / childWidth) * mMaxRotationAngle);

			// Make the angle is not bigger than maximum.
			if (Math.abs(rotationAngle) > mMaxRotationAngle) {
				rotationAngle = (rotationAngle < 0) ? -mMaxRotationAngle
						: mMaxRotationAngle;
			}

			transformImageBitmap(child, t, rotationAngle);
		}

		return true;
	}

	private int getCenterOfCoverflow() {
		return (getWidth() - getPaddingLeft() - getPaddingRight()) / 2
				+ getPaddingLeft();
	}

	private void transformImageBitmap(View child, Transformation t,
			int rotationAngle) {
		mCamera.save();

		final Matrix imageMatrix = t.getMatrix();
		final int imageHeight = child.getHeight();
		final int imageWidth = child.getWidth();
		final int rotation = Math.abs(rotationAngle);

		// Zoom on Z axis.
		mCamera.translate(0, 0, mMaxZoom);

		if (rotation < mMaxRotationAngle) {
			float zoomAmount = (float) (mMaxZoom + rotation * 1.5f);
			mCamera.translate(0, 0, zoomAmount);
		}

		// Rotate the camera on Y axis.
		mCamera.rotateY(rotationAngle);
		// Get the matrix from the camera, in fact, the matrix is S (scale)
		// transformation.
		mCamera.getMatrix(imageMatrix);

		// The matrix final is T2 * S * T1, first translate the center point to
		// (0, 0),
		// then scale, and then translate the center point to its original
		// point.
		// T * S * T

		// S * T1
		imageMatrix.postTranslate((imageWidth / 2), (imageHeight / 2));
		// (T2 * S) * T1
		imageMatrix.preTranslate(-(imageWidth / 2), -(imageHeight / 2));

		mCamera.restore();
	}
}