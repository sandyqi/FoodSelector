package com.whattoeat.database;


import com.whattoeat.R;
import com.whattoeat.test;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbHelper extends SQLiteOpenHelper{
	private static final String DATABASE_NAME = "db_foodmenu";
	private static final int DATABASE_VERSION=1;
	private static final String DATABASE_CREATE="create table tb_foodinfo (_id integer primary key autoincrement, "+
	"title text not null, body text not null, time text not null, photo text not null);";

	public dbHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(DATABASE_CREATE);
		
	}

	

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS timetodrop");
		onCreate(db);
	}
	

}
