package com.whattoeat;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Align;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

public class SplashSurface extends SurfaceView implements Runnable {
	Thread thread = null;
	SurfaceHolder holder;
	boolean isRunning = true;
	Canvas canvas;
	Bitmap food1, food2, food3, food4, food5;
	int x1, x2, x3, x4, x5, y1, y2, y3, y4, y5;
	int width, height;
	int count;
	Paint paint; 

	public SplashSurface(Context context) {
		// TODO Auto-generated constructor stub
		super(context);
		initialize();
	}

	public void initialize() {
		food1 = BitmapFactory.decodeResource(getResources(), R.drawable.food);
		food2 = BitmapFactory.decodeResource(getResources(), R.drawable.food2);
		food3 = BitmapFactory.decodeResource(getResources(), R.drawable.food7);
		food4 = BitmapFactory.decodeResource(getResources(), R.drawable.food4);
		food5 = BitmapFactory.decodeResource(getResources(), R.drawable.food5);
		holder = getHolder();
		width = 600;
		height = 800;
		x1 = 0;
		y1 = 0;
		x2 = width;
		y2 = 0;
		x3 = 0;
		y3 = height;
		x4 = width;
		y4 = height;
		x5 = width / 3;
		y5 = height / 2;
		count = 0;
	
	}

	public void pause() {
		isRunning = false;

		while (true) {
			try {
				thread.join();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		}
		thread = null;
	}

	public void resume() {
		count = 0;
		isRunning = true;
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		paint = new Paint();
		paint.setTextAlign(Align.CENTER);
		paint.setTextSize(100);
		paint.setColor(Color.GRAY);
		while (isRunning) {
			if (!holder.getSurface().isValid()) {
				continue;
			}
			canvas = holder.lockCanvas();
			canvas.drawRGB(128, 0, 0);
			if (y1 < height) {
				x1 += 12;
				y1 += 16;
				x2 -= 12;
				y2 += 16;
				x3 += 12;
				y3 -= 16;
				x4 -= 12;
				y4 -= 16;
				canvas.drawBitmap(food1, x1, y1, null);
				canvas.drawBitmap(food2, x2, y2, null);
				canvas.drawBitmap(food3, x3, y3, null);
				canvas.drawBitmap(food4, x4, y4, null);
				holder.unlockCanvasAndPost(canvas);
			} else {
				canvas.drawBitmap(food1, x1, y1, null);
				canvas.drawBitmap(food2, x2, y2, null);
				canvas.drawBitmap(food3, x3, y3, null);
				canvas.drawBitmap(food4, x4, y4, null);
				holder.unlockCanvasAndPost(canvas);
			}
			if (y1 >= height) {
				try{
				canvas = holder.lockCanvas();
				}catch(Exception e){
					e.printStackTrace();
					Log.i("Canvas", "Canvas has been locked already!");
				}
				canvas.drawRGB(0, 0, 50);
				try {
					Thread.sleep(300);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (count % 2 == 0) {
					canvas.drawText("Hungry", 400, 600, paint);
				} else {
					canvas.drawText("Now", 400, 600, paint);
				}
				holder.unlockCanvasAndPost(canvas);
				count++;
				if(count == 6){
					break;
				}
			}
		}
	}
}
