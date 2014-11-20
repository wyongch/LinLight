package com.LinLight.util;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

//定义所有公用方法的工具类
public class ActionUtil {
	public static float hue, sat, bri;// HSB(色调，饱和度，亮度)，HSB用float数据类型表示
	public static int iHue, iSat, iBri;// HSB用int数据类型表示
	public static int r, g, b;// 颜色r,g,b

	// 最大值
	private static int max(int x, int y) {
		return x > y ? x : y;
	}

	// 最小值
	private static int min(int x, int y) {
		return x > y ? y : x;
	}

	// HSB用float数据类型表示
	public static void rgbTohsb1(int r, int g, int b) {
		int minval;
		int maxval;
		minval = min(r, min(g, b));
		maxval = max(r, max(g, b));
		// bri值
		bri = (float) (maxval + minval) / 510;
		// sat值
		if (maxval == minval) {
			sat = 0.0f;
		} else {
			int sum = maxval + minval;
			if (sum > 255) {
				sum = 510 - sum;
			}
			sat = (float) (maxval - minval) / sum;
		}
		// hue值
		if (maxval == minval) {
			hue = 0.0f;
		} else {
			float diff = (float) (maxval - minval);
			float rnorm = (maxval - r) / diff;
			float gnorm = (maxval - g) / diff;
			float bnorm = (maxval - b) / diff;
			hue = 0.0f;
			if (r == maxval) {
				hue = 60.0f * (6.0f + bnorm - gnorm);
			}
			if (g == maxval) {
				hue = 60.0f * (2.0f + rnorm - bnorm);
			}
			if (b == maxval) {
				hue = 60.0f * (4.0f + gnorm - rnorm);
			}
			if (hue > 360.0f) {
				hue = hue - 360.0f;
			}
		}
	}

	// HSB用int数据类型表示，同Winform中的ColorDialog中HSB的表示
	public static void rgbTohsb(int r, int g, int b) {
		rgbTohsb1(r, g, b);
		iHue = (int) ((hue / 360.0f) * 240 + 0.5);
		iSat = (int) (sat * 241 + 0.5);
		iBri = (int) (bri * 241 + 0.5);

		if (iHue > 239) {
			iHue = 239;
		}

		if (iSat > 240) {
			iSat = 240;
		}

		if (iBri > 240) {
			iBri = 240;
		}
	}

	// 传入的HSB用int型表示，如果用float型表示，可将下面的转换成float过程去掉
	public static void hsbTorgb(int hue, int sat, int bri) {
		// begin:HSB转换为float
		if (hue > 239) {
			hue = 239;
		} else {
			if (hue < 0) {
				hue = 0;
			}
		}

		if (sat > 240) {
			sat = 240;
		} else {
			if (sat < 0) {
				sat = 0;
			}
		}
		if (bri > 240) {
			bri = 240;
		} else {
			if (bri < 0) {
				bri = 0;
			}
		}
		float H = hue / 239.0f;
		float S = sat / 240.0f;
		float L = bri / 240.0f;

		// end:HSB转换为float

		float red = 0, green = 0, blue = 0;
		float d1, d2;
		if (L == 0) {
			red = green = blue = 0;
		} else {
			if (S == 0) {
				red = green = blue = L;
			} else {
				d2 = (L <= 0.5f) ? L * (1.0f + S) : L + S - (L * S);
				d1 = 2.0f * L - d2;

				float d3[] = { H + 1.0f / 3.0f, H, H - 1.0f / 3.0f };
				float rgb[] = { 0, 0, 0 };

				for (int i = 0; i < 3; i++) {
					if (d3[i] < 0) {
						d3[i] += 1.0f;
					}

					if (d3[i] > 1.0f) {
						d3[i] -= 1.0f;
					}

					if (6.0f * d3[i] < 1.0f) {
						rgb[i] = d1 + (d2 - d1) * d3[i] * 6.0f;
					} else {
						if (2.0f * d3[i] < 1.0f) {
							rgb[i] = d2;
						} else {
							if (3.0f * d3[i] < 2.0f) {
								rgb[i] = (d1 + (d2 - d1)
										* ((2.0f / 3.0f) - d3[i]) * 6.0f);
							} else {
								rgb[i] = d1;
							}
						}
					}
				}
				red = rgb[0];
				green = rgb[1];
				blue = rgb[2];
			}
		}
		red = 255.0f * red;
		green = 255.0f * green;
		blue = 255.0f * blue;
		if (red < 1) {
			red = 0.0f;
		} else {
			if (red > 255.0f) {
				red = 255.0f;
			}
		}
		if (green < 1) {
			green = 0.0f;
		} else {
			if (green > 255.0f) {
				green = 255.0f;
			}
		}
		if (blue < 1) {
			blue = 0.0f;
		} else {
			if (blue > 255.0f) {
				blue = 255.0f;
			}
		}
		r = (int) (red + 0.5);
		g = (int) (green + 0.5);
		b = (int) (blue + 0.5);
	}

	// 判断点击了那个图片，计算两个点是否重合
	public static boolean isEqual(int currentX, int currentY, int lastX,
			int lastY) {
		if (Math.abs(currentX - lastX) < 72 && Math.abs(currentY - lastY) < 72) {
			return true;
		}
		return false;
	}
	
	public static boolean isEqual(float currentX, float currentY, float lastX,
			float lastY) {
		if (Math.abs(currentX - lastX) < 72 && Math.abs(currentY - lastY) < 72) {
			return true;
		}
		return false;
	}

	// 判断点击了那个图片，计算两个点是否重合
	public static boolean isEqualLarge(int currentX, int currentY, int lastX,
			int lastY) {
		if (Math.abs(currentX - lastX) < 100 && Math.abs(currentY - lastY) < 100) {
			return true;
		}
		return false;
	}
	// 判断点击了那个图片，计算两个点是否重合
		public static boolean isEqualLarge2(int currentX, int currentY, int lastX,
				int lastY) {
			if (Math.abs(currentX - lastX) < 100 && Math.abs(currentY - lastY) < 100) {
				return true;
			}
			return false;
		}
	// 加载图片资源
	public static Bitmap loadBM(Resources res, int id) {
		return BitmapFactory.decodeResource(res, id);// 返回Bitmap对象
	}

	// 缩放图片的方法
	public static Bitmap scaleToFit(Bitmap bm, float fwRatio)// 缩放图片的方法
	{
		int width = bm.getWidth(); // 图片宽度
		int height = bm.getHeight();// 图片高度
		Matrix matrix = new Matrix();
		matrix.postScale((float) fwRatio, (float) fwRatio);// 图片等比例缩小为原来的fblRatio倍
		Bitmap bmResult = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
				true);// 声明位图
		return bmResult;
	}

	// 缩放图片的方法
	public static Bitmap scaleToFitXYRatio(Bitmap bm, float xRatio, float yRatio)// 缩放图片的方法
	{
		Matrix m1 = new Matrix();
		m1.postScale(xRatio, yRatio);
		Bitmap bmResult = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(),
				bm.getHeight(), m1, true);// 声明位图
		return bmResult;
	}
}