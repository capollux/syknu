package com.hwang.sy_knu.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hwang.sy_knu.R;
import com.hwang.sy_knu.Data.DetailContext;
import com.hwang.sy_knu.R.id;
import com.hwang.sy_knu.R.layout;

public class ContextAdapter extends BaseAdapter {
	
	private LayoutInflater inflater;
	private ArrayList<DetailContext> infoList;
	private ViewHolder viewHolder;

	public ContextAdapter(Context c, ArrayList<DetailContext> array) {
		inflater = LayoutInflater.from(c);
		infoList = array;
	}

	
	@Override
	public int getCount() {
		return infoList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertview, ViewGroup parent) {

		View v = convertview;

		if (v == null) {
			viewHolder = new ViewHolder();
			v = inflater.inflate(R.layout.sy_context_row, null);
			viewHolder.syHead = (TextView) v.findViewById(R.id.sy_heads);
			viewHolder.syContext = (TextView) v.findViewById(R.id.sy_context);
			v.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) v.getTag();
		}

		viewHolder.syHead.setText(infoList.get(position).getHead());
		viewHolder.syContext.setText(infoList.get(position).getContext());


		return v;
	}

	public void setArrayList(ArrayList<DetailContext> arrays) {
		this.infoList = arrays;
	}

	public ArrayList<DetailContext> getArrayList() {
		return infoList;
	}

	/*
	 * ViewHolder
	 */
	class ViewHolder {
		TextView syHead;
		TextView syContext;

	}
}
