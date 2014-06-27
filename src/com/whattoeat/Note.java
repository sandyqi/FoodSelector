package com.whattoeat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import com.whattoeat.database.dbAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Note extends Activity implements OnClickListener {
	private Button edit, confirm;
	private EditText editnote, editfoodname;
	private TextView date, note, foodname;
	private ImageView background;
	private LinearLayout rl;
	boolean editButtonClicked;
	View vtitle,vnote;
	Long id;
	String title,body, newTitle,newBody,newPhoto;
	String path;
	private dbAdapter adapter;
	int galleryPhotoHeight,galleryPhotoWidth;
	String galleryPhotoType;
	Bitmap yourSelectedImage;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.memo);
		adapter = new dbAdapter(this);
		adapter.open();
		initial();
	}

	public void initial() {
		edit = (Button) findViewById(R.id.confirm_button);
		confirm = (Button) findViewById(R.id.edit_button);
		editnote = (EditText) findViewById(R.id.editnote);
		note = (TextView) findViewById(R.id.note);
		date = (TextView) findViewById(R.id.date);
		foodname = (TextView)findViewById(R.id.foodname);
		editfoodname =(EditText)findViewById(R.id.editfoodname);
		background = (ImageView) findViewById(R.id.backg);
		rl = (LinearLayout) findViewById(R.id.layout);
		id = null;
		edit.setOnClickListener(this);
		confirm.setOnClickListener(this);
		vtitle = findViewById(R.id.editfoodname);
		vnote = findViewById(R.id.editnote);
		vtitle.getBackground().setAlpha(100);
		vnote.getBackground().setAlpha(100);
		Bundle b = getIntent().getExtras();
		
		try {  // check if the user opened a photo in gallery and then came into this page
			if (b.getString("GALLERY").isEmpty() == false) {
				editButtonClicked=false;
				String photo = b.getString("GALLERY");																						
				BitmapFactory.Options options = new BitmapFactory.Options();// get dimension of photos from gallery
				options.inSampleSize= calculateInSampleSize(options, 500, 500);
				//options.inJustDecodeBounds=true;
				Bitmap bp =BitmapFactory.decodeFile(photo);
				Drawable fromGallery = new BitmapDrawable(getResources(),
						bp);
				yourSelectedImage = bp;
				background.setBackgroundDrawable(fromGallery);
				foodname.setText("Click Edit Button to edit");
				note.setText("Click edit button to edit");				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try { // check if the user opened the main list and then came into this page
			if (b.getString("BACKGROUND").isEmpty() == false) {
				editButtonClicked=false;
				//drawable = Integer.parseInt(b.getString("BACKGROUND")); //get BACKGROUND from bundle
				path = b.getString("BACKGROUND");
				Bitmap bp =BitmapFactory.decodeFile(path);
				Drawable fromBackground = new BitmapDrawable(getResources(),
						bp);
				background.setBackgroundDrawable(fromBackground);
				body = b.getString("BODY");  //get BODY from bundle
				editnote.setText(body);
				note.setText(body);
				id = (long) Integer.parseInt(b.getString("ID")); //get ID from bundle
				title = b.getString("TITLE"); //get TITLE from bundle
				editfoodname.setText(title);
				foodname.setText(title);
				String time = b.getString("TIME");
				date.setText(time);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.confirm_button:
			note.setVisibility(View.INVISIBLE);
			editnote.setVisibility(View.VISIBLE);
			foodname.setVisibility(View.INVISIBLE);
			editfoodname.setVisibility(View.VISIBLE);
			editButtonClicked = true;
			break;
		case R.id.edit_button: // this is confirm button
			if(editButtonClicked = true){   // if user did not click edit but clicked confirm button, nothing should happen
			newBody = String.valueOf(editnote.getText());
			newTitle = String.valueOf(editfoodname.getText());
			title = newTitle;
			body = newBody;
			note.setText(newBody);
			note.setVisibility(View.VISIBLE);
			editnote.setVisibility(View.INVISIBLE);
			foodname.setText(newTitle);
			foodname.setVisibility(View.VISIBLE);
			editfoodname.setVisibility(View.INVISIBLE);
			if(id==null){
			
			adapter.insertMessage(title, body, saveImageToFile(yourSelectedImage, title));
			}else{
			adapter.update(id, title, body, path);
			}
			editButtonClicked = false;
			}
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setIcon(android.R.drawable.btn_dialog);
			builder.setTitle("Hi");
			builder.setMessage("Memo saved successfully");
			builder.setNeutralButton("OK",null);
			builder.show();
			break;
		}
	}
	
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight){
		int inSampleSize = 1;
		final int height = options.outHeight;
		final int width = options.outWidth;
		if(height>reqHeight || width > reqWidth){
			final int halfHeight = height /2;
			final int halfWidth = width/2;
			while((halfHeight/inSampleSize)>reqHeight && (halfWidth/inSampleSize)>reqWidth){
				inSampleSize *= 2;
			}
		}
		return inSampleSize;
	}
	
	public String saveImageToFile(Bitmap image, String fileName) {
		// get path
		String iconsStoragePath = Environment.getExternalStorageDirectory()
				+ "/WhatToEat/myImages";
		File direct = new File(iconsStoragePath);
		if (direct.exists() == false) {
			direct.mkdirs();
		}
		File file = new File(direct.getPath() + "/" + fileName + ".png");
		if (file.exists() == false) {
			try {
				file.createNewFile();
				FileOutputStream out = new FileOutputStream(file);
				image.compress(Bitmap.CompressFormat.PNG, 100, out);
				out.flush();
				out.close();
			} catch (FileNotFoundException e) {
				Log.w("TAG", "Error saving image file: FileNotFoundException"
						+ e.getMessage());
				return "";
			} catch (IOException e) {
				Log.w("TAG",
						"Error saving image file: IOException" + e.getMessage());
				return "";
			}
		}
		return file.getPath();
	}
	
    
	
}