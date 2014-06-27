package com.whattoeat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.whattoeat.adapter.test2;
import com.whattoeat.database.dbAdapter;
import com.whattoeat.database.dbHelper;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class MainPage extends Activity implements OnClickListener {
	private ListView mCompleteListView;
	private Button mAddItemToList, clear;
	private TextView count;
	private List<String[]> mItems;
	private LayoutInflater inflater;
	private ImageButton openphoto;
	final int ACTIVITY_SELECT_IMAGE = 1234;
	public static final String KEY_TITLE = "title";
	public static final String KEY_BODY = "body";
	public static final String KEY_ROWID = "_id";
	public static final String KEY_TIME = "time";
	public static final String KEY_PHOTO = "photo";
	private static final String DATABASE_TABLE = "tb_foodinfo";
	private test2 mListAdapter;
	private List<String> food_title;
	private List<String> food_body;
	private List<String> food_time;
	private List<String> food_photo;
	private List<String> food_id;
	private dbAdapter adapter;
	private Cursor mCursor;
	int fromclicktotouch;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_complete_list);
		initial();
		adapter = new dbAdapter(this);
		adapter.open();
		mItems = new ArrayList<String[]>(); // mItems is a very magic stuff. I
											// can use it as a new stuff to
											// locate, and then to change or get
											// the information
		// this constructor connected this class and CompleteListAdapter with
		// mItems(a list)
		mListAdapter = new test2(this, mItems);
		mCompleteListView.setAdapter(mListAdapter);
		mCompleteListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {
				// TODO Auto-generated method stub
				fromclicktotouch = position;
				inflater = (LayoutInflater) MainPage.this
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				View view = inflater.inflate(R.layout.pop_up, null, false);
				view.setOnTouchListener(new OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						switch (event.getAction()) {
						case MotionEvent.ACTION_DOWN:

							Intent in = new Intent(MainPage.this, Note.class);
							in.putExtra("BACKGROUND",
									mItems.get(fromclicktotouch)[1]);
							in.putExtra("BODY", mItems.get(fromclicktotouch)[2]);
							in.putExtra("ID", mItems.get(fromclicktotouch)[3]);
							in.putExtra("TITLE",
									mItems.get(fromclicktotouch)[0]);
							in.putExtra("TIME", mItems.get(fromclicktotouch)[4]);
							startActivity(in);
							return true;
						}
						return false;
					}

				});
				PopupWindow window = new PopupWindow(view, 600, 500, true);
				Cursor c = mCursor;
				c.moveToPosition(fromclicktotouch);
				try {
					Bitmap bp = BitmapFactory.decodeFile(mItems
							.get(fromclicktotouch)[1]);
					Drawable draw = new BitmapDrawable(getResources(), bp);
					window.setBackgroundDrawable(draw);
					window.showAtLocation(findViewById(R.id.list),
							Gravity.CENTER, 10, 10);
				} catch (Exception e) {
					e.printStackTrace();
					Log.i("test class",
							"Can not save image from drawable folder into local folderp");
				}
			}
		});
	}

	public void initial() {
		mCompleteListView = (ListView) findViewById(R.id.list);
		mAddItemToList = (Button) findViewById(R.id.add);
		clear = (Button) findViewById(R.id.confirm);
		mAddItemToList.setOnClickListener(this);
		clear.setOnClickListener(this);
		count = (TextView) findViewById(R.id.count);
		openphoto = (ImageButton) findViewById(R.id.handle);
		openphoto.setOnClickListener(this);

		// find the layout I want to add pic to

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.add:
			addItemsToList();
			break;
		case R.id.confirm:
			mItems.clear();
			count.setText("0");
			updateAllArray();
			break;
		case R.id.handle:
			// create a intent with a given action + a given data url
			// ACTION_PICK means select something and return it.
			// INTERNAL_CONTENT_URI means the style URI for the internal storage
			// 1. Opent the Gallery
			Intent i = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);

			startActivityForResult(i, ACTIVITY_SELECT_IMAGE);
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (data == null)
			return;
		if (requestCode == ACTIVITY_SELECT_IMAGE) {
			// get the uri of the touched image.

			Uri selectedImage = data.getData();
			// the data stream for file
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			// getContentResolver: return a ContentResolver instance
			// ContentResolver.query() rquery the given uri, returning a cursor
			// over the result set.
			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();

			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String filePath = cursor.getString(columnIndex);
			cursor.close();

			Intent in = new Intent(MainPage.this, Note.class);
			in.putExtra("GALLERY", filePath);
			startActivity(in);

		} else {

		}
	}

	public void updateAllArray() {
		mCursor = adapter.getAll();
		// if I do not put them here, it will not update the information unless
		// I restart the app
		startManagingCursor(mCursor);
		food_title = new ArrayList<String>();
		food_body = new ArrayList<String>();
		food_time = new ArrayList<String>();
		food_photo = new ArrayList<String>();
		food_id = new ArrayList<String>();

		while (mCursor.moveToNext()) {
			food_title.add(mCursor.getString(mCursor.getColumnIndex("title")));
			food_body.add(mCursor.getString(mCursor.getColumnIndex("body")));
			food_time.add(mCursor.getString(mCursor.getColumnIndex("time")));
			food_photo.add(mCursor.getString(mCursor.getColumnIndex("photo")));
			food_id.add(mCursor.getString(mCursor.getColumnIndex("_id")));

		}
		food_title.size();
	}

	public void initialValue() {
		// the string string arrays are from system. They are default. The
		// values will be add to the database when the database is created at
		// the first time.
		String[] f_title = getResources().getStringArray(R.array.food_title);
		String[] f_body = getResources().getStringArray(R.array.food_body);
		String[] f_photo = new String[10]; // save images from drawable into
											// local folder.
		// Path:
		// Environment.getExternalStorageDirectory()+"/WhatToEat/myImages";
		Bitmap cb = ((BitmapDrawable) getResources().getDrawable(
				R.drawable.cheeseburger)).getBitmap();
		f_photo[0] = saveImageToFile(cb, f_title[0]);
		Bitmap cf = ((BitmapDrawable) getResources().getDrawable(
				R.drawable.chinesefood)).getBitmap();
		f_photo[1] = saveImageToFile(cf, f_title[1]);
		Bitmap is = ((BitmapDrawable) getResources().getDrawable(
				R.drawable.italiansausage)).getBitmap();
		f_photo[2] = saveImageToFile(is, f_title[2]);
		Bitmap isp = ((BitmapDrawable) getResources().getDrawable(
				R.drawable.italiansoup)).getBitmap();
		f_photo[3] = saveImageToFile(isp, f_title[3]);
		Bitmap lb = ((BitmapDrawable) getResources().getDrawable(
				R.drawable.lobsters)).getBitmap();
		f_photo[4] = saveImageToFile(lb, f_title[4]);
		Bitmap rb = ((BitmapDrawable) getResources().getDrawable(
				R.drawable.ribs)).getBitmap();
		f_photo[5] = saveImageToFile(rb, f_title[5]);
		Bitmap rc = ((BitmapDrawable) getResources().getDrawable(
				R.drawable.roastedchicken)).getBitmap();
		f_photo[6] = saveImageToFile(rc, f_title[6]);
		Bitmap sg = ((BitmapDrawable) getResources().getDrawable(
				R.drawable.spegetti)).getBitmap();
		f_photo[7] = saveImageToFile(sg, f_title[7]);
		Bitmap sk = ((BitmapDrawable) getResources().getDrawable(
				R.drawable.steak)).getBitmap();
		f_photo[8] = saveImageToFile(sk, f_title[8]);
		Bitmap tc = ((BitmapDrawable) getResources().getDrawable(
				R.drawable.thaicurry)).getBitmap();
		f_photo[9] = saveImageToFile(tc, f_title[9]);
		for (int i = 0; i < f_title.length; i++) {
			adapter.insertMessage(f_title[i], f_body[i], f_photo[i]);
		}

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

	public void addItemsToList() {
		Cursor cc = adapter.getAll();
		if (cc.moveToNext() == false) {
			initialValue();
		}
		updateAllArray();
		int rand = (int) (Math.random() * food_title.size());
		mItems.add(new String[] { food_title.get(rand), food_photo.get(rand), // mItems is used to conncet two class
				food_body.get(rand), food_id.get(rand) , food_time.get(rand)});
		count.setText(String.valueOf(mItems.size()));
		mListAdapter.notifyDataSetChanged();
	}

}
