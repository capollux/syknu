package com.hwang.sy_knu.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hwang.sy_knu.R;
import com.hwang.sy_knu.Data.Data;
import com.hwang.sy_knu.R.id;
import com.hwang.sy_knu.R.layout;

public class SyAdapter extends BaseAdapter {

		private LayoutInflater inflater;
		private ArrayList<Data> infoList;
		private ViewHolder viewHolder;
	
		public SyAdapter(Context c, ArrayList<Data> array) {
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
	
		@SuppressLint("InflateParams")
		@Override
		public View getView(int position, View convertview, ViewGroup parent) {
	
			View v = convertview;
	
			if (v == null) {
				viewHolder = new ViewHolder();
				v = inflater.inflate(R.layout.sy_list_row,null);
				viewHolder.subject_num = (TextView) v.findViewById(R.id.subject_num);
				viewHolder.subject_title = (TextView) v.findViewById(R.id.subject_title);
				viewHolder.subject_pro = (TextView) v.findViewById(R.id.subject_pro);
				viewHolder.stu_max = (TextView) v.findViewById(R.id.subject_max_stu);
				viewHolder.stu_curr = (TextView) v.findViewById(R.id.subject_curr_stu);
				viewHolder.stu_resr = (TextView) v.findViewById(R.id.subject_resr_stu);

				v.setTag(viewHolder);
	
			} else {
				viewHolder = (ViewHolder) v.getTag();
			}
	
			viewHolder.subject_num.setText(infoList.get(position).getSubjectNum());
			viewHolder.subject_title.setText(infoList.get(position).getSubjectTitle());
			viewHolder.subject_pro.setText(infoList.get(position).getProfessor());
			viewHolder.stu_max.setText(infoList.get(position).getMaxStu());
			viewHolder.stu_curr.setText(infoList.get(position).getCurrStu());
			viewHolder.stu_resr.setText(infoList.get(position).getResrStu());

	
			return v;
		}
	
		public void setArrayList(ArrayList<Data> arrays) {
			this.infoList = arrays;
		}
	
		public ArrayList<Data> getArrayList() {
			return infoList;
		}
	
		/*
		 * ViewHolder
		 */
		class ViewHolder {
			TextView subject_num;
			TextView subject_title;
			TextView subject_pro;
			TextView stu_max;
			TextView stu_curr;
			TextView stu_resr;
	
		}
}
