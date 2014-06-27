package com.whattoeat;
import com.whattoeat.menu.MenuList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class test extends Activity{
	SplashSurface surface;
	Intent intent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		surface = new SplashSurface(this);
		setContentView(surface);
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		surface.pause();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		surface.resume();
		Thread thread = new Thread(){
			@Override
			public void run(){
				try{
					sleep(3000);
				}catch (InterruptedException e){
					e.printStackTrace();
				}finally{
					intent = new Intent(test.this,MenuList.class);
					startActivity(intent);   			
				}						
			}
		};
		thread.start();

	}
	

	
}
