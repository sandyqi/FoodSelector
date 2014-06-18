package com.whattoeat;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.whattoeat.database.dbAdapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Note extends Activity implements OnClickListener {
	private Button edit, confirm;
	private EditText editnote, editfoodname;
	private TextView date, note, foodname;
	private ImageView background;
	private RelativeLayout rl;
	Drawable drawable;
	boolean editButtonClicked;
	View vtitle,vnote;
	Long id;
	String title,body, newTitle,newBody,newPhoto;
	byte[] photo;
	private dbAdapter adapter;
	int galleryPhotoHeight,galleryPhotoWidth;
	String galleryPhotoType;
	Bitmap yourSelectedImage;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.note);
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
		rl = (RelativeLayout) findViewById(R.id.layout);
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
				String path = b.getString("GALLERY");																						
				BitmapFactory.Options options = new BitmapFactory.Options();// get dimension of photos from gallery
			//	options.inJustDecodeBounds=true; // do not allocate memory
				yourSelectedImage=BitmapFactory.decodeFile(path, options); // I have to put options here, then I can get height, width, type. It is a connection
			//	galleryPhotoHeight = options.outHeight;  //1280 see it in debug
			//	galleryPhotoWidth = options.outWidth;		//800
			//	galleryPhotoType = options.outMimeType; // they are included in the method calculateInSampleSize()
				options.inSampleSize= calculateInSampleSize(options, 500, 500);
			//	options.inJustDecodeBounds=true;
				yourSelectedImage = BitmapFactory.decodeFile(path, options);
				Drawable fromGallery = new BitmapDrawable(getResources(),
						yourSelectedImage);
				photo = encodeTobase6(yourSelectedImage);
				background.setBackgroundDrawable(fromGallery);
				foodname.setText("Click Edit Button to edit");
				note.setText("Click edit button to edit");
			//	editfoodname.setText("Name");
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try { // check if the user opened the main list and then came into this page
			if (b.getString("BACKGROUND").isEmpty() == false) {
				editButtonClicked=false;
				//drawable = Integer.parseInt(b.getString("BACKGROUND")); //get BACKGROUND from bundle
				String photo2 = b.getString("BACKGROUND");
				Bitmap bp = decodeBase64(photo2);
				photo = encodeTobase6(bp);
				drawable = new BitmapDrawable(getResources(),bp);
				background.setBackgroundDrawable(drawable);
				body = b.getString("BODY");  //get BODY from bundle
				editnote.setText(body);
				note.setText(body);
				id = (long) Integer.parseInt(b.getString("ID")); //get ID from bundle
				title = b.getString("TITLE"); //get TITLE from bundle
				editfoodname.setText(title);
				foodname.setText(title);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.confirm_button: // this is edit button
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
			saveImageToFile(yourSelectedImage, title);
			adapter.insertMessage(title, body, photo);
			}else{
			adapter.update(id, title, body, photo);
			}
			editButtonClicked = false;
			}
			break;
		}
	}
	public static String encodeTobase64(Bitmap image){
		Bitmap imagex = image;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		imagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] b = baos.toByteArray();
		String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
		Log.e("LOOK", imageEncoded);
		return imageEncoded;
	}
	public static Bitmap decodeBase64(String input){
		byte[] decodedByte = Base64.decode(input, 0);
		return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
	}
	public static byte[] encodeTobase6(Bitmap image){
		Bitmap imagex = image;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		imagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] b = baos.toByteArray();
		return b;
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
	
	public boolean saveImageToFile(Bitmap image, String filename){
		// get path
		String iconsStoragePath =Environment.getDataDirectory()+"/WhatToEat/myImages/";
		File sdIconStorageDir = new File(iconsStoragePath,filename);
		//create storage directories, if they don't exist
		sdIconStorageDir.mkdir();
		
		try{
			String filePath = sdIconStorageDir.toString();
			FileOutputStream outstream = new FileOutputStream(sdIconStorageDir);
			BufferedOutputStream bos = new BufferedOutputStream(outstream);
			image.compress(CompressFormat.PNG, 100, bos);
		//	Uri uri = Uri.parse(filePath);
		//	Bitmap bitmap = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(uri));
        //    photo = encodeTobase6(bitmap);
			bos.flush();
			bos.close();
		}catch(FileNotFoundException e){
			Log.w("TAG", "Error saving image file: "+ e.getMessage());
			return false;
		}catch(IOException e){
			Log.w("TAG", "Error saving image file: "+ e.getMessage());
			return false;
		}
		return true;
	}
	
    
	
}