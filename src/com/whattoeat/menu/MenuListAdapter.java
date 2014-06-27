package com.whattoeat.menu;


import java.util.List;

import com.whattoeat.R;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MenuListAdapter extends BaseAdapter {
	List<String> list;
	Activity context;

	public MenuListAdapter(Activity context, List<String> list) {
		this.list = list;
		this.context = context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub

		View v = convertView;
		CompleteListViewHolder viewHolder;
		if (convertView == null) {
			LayoutInflater li = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = li.inflate(R.layout.mainmenu_listitem, null);
			viewHolder = new CompleteListViewHolder(v);
			v.setTag(viewHolder);
		} else {
			viewHolder = (CompleteListViewHolder) v.getTag();
		}
		viewHolder.mTVItem.setText(list.get(arg0));
		return v;
	}

	class CompleteListViewHolder {
		public TextView mTVItem;
		public ImageView i;

		public CompleteListViewHolder(View base) {
			mTVItem = (TextView) base.findViewById(R.id.text);
			i = (ImageView) base.findViewById(R.id.i);
		}
	}
}
