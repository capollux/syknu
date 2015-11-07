package com.hwang.sy_knu.adapter;

import java.util.ArrayList;

import com.hwang.sy_knu.R;
import com.hwang.sy_knu.Data.MenuData;
import com.hwang.sy_knu.R.id;
import com.hwang.sy_knu.R.layout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class Adapter extends BaseAdapter{
		
		private LayoutInflater inflater;
		private ArrayList<MenuData> infoList;
		private ViewHolder viewHolder;
		
		public Adapter(Context c , ArrayList<MenuData> array){
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
			
			if(v == null){
				viewHolder = new ViewHolder();
				v = inflater.inflate(R.layout.main_sub_list_row, null);
				viewHolder.title = (TextView)v.findViewById(R.id.main_sub_title);
				v.setTag(viewHolder);
				
			}else {
				viewHolder = (ViewHolder)v.getTag();
			}
			
			viewHolder.title.setText(infoList.get(position).getMainSub());

			return v;
		}
		
		public void setArrayList(ArrayList<MenuData> arrays){
			this.infoList = arrays;
		}
		
		public ArrayList<MenuData> getArrayList(){
			return infoList;
		}
		
		
		/*
		 * ViewHolder
		 */
		class ViewHolder{
			TextView title;

		}
	}

