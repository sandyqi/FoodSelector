package com.whattoeat;

import java.util.ArrayList;
import java.util.List;

import com.whattoeat.adapter.CompleteListAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class CompleteListActivity extends Activity implements OnClickListener{
	private ListView mCompleteListView;
	private Button mAddItemToList,confirm;
	private List<String> mItems;
	private String[] menu = new String[]{"Cheese Burger", "Italian Soup","Roasted Chicken","Lobsters","Spegetti", 
			"Ribs", "Steak", "Vegetable","Chinese Food", 
			"Vienam Noodle Soup", "Thai Curry","Italian Sausage","Peking Roasted Duck","Y.X.F.S.soup","Roasted Shrinp"};
	private CompleteListAdapter mListAdapter;
	private static final int MIN =0,MAX=100;
	RelativeLayout relativeLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_complete_list);
		initial();
		mItems = new ArrayList<String>();
		// this constructor connected this class and CompleteListAdapter with mItems(a list)
		mListAdapter = new CompleteListAdapter(this,mItems);
		mCompleteListView.setAdapter(mListAdapter);	
	}
	public void initial(){
		mCompleteListView = (ListView)findViewById(R.id.list);
		mAddItemToList = (Button)findViewById(R.id.add);
		confirm = (Button)findViewById(R.id.confirm);
		mAddItemToList.setOnClickListener(this);
		confirm.setOnClickListener(this);
		//find the layout I want to add pic to
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.add:
			addItemsToList();
			break;
		case R.id.confirm:
			break;
		}
	}
	public void addItemsToList(){
		int rand = (int)(Math.random()*(menu.length));
		mItems.add(menu[rand]);
		//ImageView foodView= new ImageView(CompleteListActivity.this);
	
		//the method below is not applicatble for my app. I should make moves in CompleteListAdapter.java
		// Because the notifyDataSetChanged() method make it work in CompleteListAdapter class
		//	foodView.setImageResource(R.drawable.a26);
	//	relativeLayout = (RelativeLayout)findViewById(R.id.rl);
	//	RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
	//			RelativeLayout.LayoutParams.WRAP_CONTENT);
		//set the position of the imageview
		//layoutParams.addRule(RelativeLayout.ALIGN_LEFT,R.id.text2);
		//layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		//relativeLayout.addView(foodView, layoutParams);
		mListAdapter.notifyDataSetChanged();
	}
}
