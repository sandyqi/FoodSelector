package com.whattoeat.menu;

import java.util.ArrayList;
import java.util.List;

import com.whattoeat.MainPage;
import com.whattoeat.R;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MenuList extends ListActivity{
	List<String> helpers=new ArrayList<String>();
	MenuListAdapter adapter;
	Typeface font;
	final static int INSTRUCTION_ID = Menu.FIRST;
	final static int INFORMATION_ID = Menu.FIRST+1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mainmenu);
		ImageView pup = (ImageView)findViewById(R.id.imageView1pup);
		pup.setBackgroundResource(R.drawable.image_border);
		helpers.add("Select Food");
		helpers.add("Open Food Gallery");
		helpers.add("Games");
		adapter = new MenuListAdapter(this, helpers);
		setListAdapter(adapter);
		font = Typeface.createFromAsset(this.getAssets(), "goodchoice.ttf");
		TextView title = (TextView)findViewById(R.id.titleInListview);
		title.setTypeface(font);

	}
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
		Log.i("PPP", "the "+position);
		if (position == 0){
			Intent in = new Intent(MenuList.this, MainPage.class);
			startActivity(in);
		}
	}
	public boolean onCreateOptionsMenu(Menu menu) {  //create the menu. 
		super.onCreateOptionsMenu(menu);
		menu.add(0, INSTRUCTION_ID, 0, "Instruction");
		menu.add(0, INFORMATION_ID, 0, "More Information");
		//menu.add(0, DELETE_ID, 0, R.string.menu_delete);
		return true;
	}
	/*
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stu
		switch(item.getItemId()){
		case INSTRUCTION_ID:
			popWindow1();
			break;
		case INFORMATION_ID:
			popWindow2();
			break;
		}
		return super.onMenuItemSelected(featureId, item);
		
	}
	public void popWindow1(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle("Instruction");
		builder.setMessage(R.string.instruction);
		builder.setNeutralButton("OK", null);
		builder.show();
	}
	public void popWindow2(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle("Do you know...");
		builder.setMessage(R.string.information);
		builder.setNeutralButton("OK", null);
		builder.show();
	}
	*/
}
