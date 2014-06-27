package com.whattoeat.database;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class dbAdapter {
	public static final String KEY_TITLE="title";
	public static final String KEY_BODY="body";
	public static final String KEY_ROWID="_id";
	public static final String KEY_TIME="time";
	public static final String KEY_PHOTO="photo";
	
	private dbHelper helper;
	private SQLiteDatabase database;
	private static final String DATABASE_TABLE="tb_foodinfo";
	private final Context mContext;
	
	public dbAdapter(Context context){
		mContext = context;
	}
	public dbAdapter open() throws SQLException{
		helper = new dbHelper(mContext);
		database = helper.getWritableDatabase();
		return this;
	}
	public void close(){
		helper.close();
	}
	public long insertMessage(String title, String body,String photo){
		ContentValues values = new ContentValues();
		values.put(KEY_TITLE, title);
		values.put(KEY_BODY, body);
		String time = getCreateTime();
		values.put(KEY_TIME, time);
		values.put(KEY_PHOTO, photo);
		return database.insert(DATABASE_TABLE, null, values);
	}
	public String getCreateTime(){
		Calendar rightNow = Calendar.getInstance();
		String text = "Created time: "+rightNow.get(Calendar.MONTH)+"/ "+
		rightNow.get(Calendar.DAY_OF_MONTH)+"   "+rightNow.get(Calendar.HOUR_OF_DAY)+": "+
		rightNow.get(Calendar.MINUTE)+": "+rightNow.get(Calendar.SECOND);
		return text;
	}
	public boolean deleteMessage(long id){
		return database.delete(DATABASE_TABLE, KEY_ROWID+"="+id, null)>0;
	}
	public Cursor getAll(){
		
		return database.query(DATABASE_TABLE, new String[]{KEY_ROWID, KEY_TITLE, KEY_BODY,KEY_TIME,KEY_PHOTO},null, null, null, null, null);
	}
	public Cursor getOne(long id) throws SQLException{
		
			Cursor list=database.query(DATABASE_TABLE, 
					new String[]{KEY_ROWID,KEY_TITLE,KEY_BODY,KEY_TIME}, KEY_ROWID+"="+id, 
					null, null, null, null);
			if(list!=null){
				list.moveToFirst();
			}
			return list;
	}
	public boolean update(long id,String title,String body, String photo){
		ContentValues values=new ContentValues();
		values.put(KEY_TITLE, title);
		values.put(KEY_BODY, body);
		values.put(KEY_TIME, getCreateTime());
		values.put(KEY_PHOTO, photo);
		
		return database.update(DATABASE_TABLE, values, KEY_ROWID+"="+id, null)>0;
	}
}
