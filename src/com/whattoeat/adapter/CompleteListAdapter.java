package com.whattoeat.adapter;

import java.util.List;

import com.whattoeat.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class CompleteListAdapter extends BaseAdapter{
	private Activity mContext;//use this to find layout?
	private List<String>mList;
	private LayoutInflater mLayoutInflater = null;
	public CompleteListAdapter(Activity context, List<String>list){
		mContext = context;
		mList = list;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		// get a view that displays the data at the specified position in the data set. 
		//either crete a view manually or inflate it from an XML layout file. 
		View v = convertView;
		CompleteListViewHolder viewHolder;  
		// if it is not possible to convert this view to display the correct data, this method can create a new view.
		// so it is necessary to check if this view is non-nnull an dof an appropriate type before using
		if(convertView==null){
			// instantiates a layout XML file into its corresponding view objects. 
			//use getLayoutInflater() or getSystemService(String) to retrieve a standart layoutinflater instance.
			LayoutInflater li = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			//inflate a new view hierarchy from the specified xml resource.
			// two parameters : int, viewgroup
			v = li.inflate(R.layout.list_layout, null);
			viewHolder = new CompleteListViewHolder(v);
			v.setTag(viewHolder);
		}else{
			viewHolder = (CompleteListViewHolder)v.getTag();
		}
		viewHolder.mTVItem.setText(mList.get(position));
	//	viewHolder.i.setAlpha(Integer.parseInt(mList.get(position)));
		return v;
	}
	class CompleteListViewHolder{
		public TextView mTVItem;
		public ImageView i;
		public CompleteListViewHolder(View base){
			mTVItem = (TextView)base.findViewById(R.id.text);
		i = (ImageView)base.findViewById(R.id.i);
		}
	}

}
