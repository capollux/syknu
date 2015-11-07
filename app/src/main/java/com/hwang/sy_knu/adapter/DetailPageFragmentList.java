package com.hwang.sy_knu.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.hwang.sy_knu.R;
import com.hwang.sy_knu.Data.DetailContext;
import com.hwang.sy_knu.R.id;
import com.hwang.sy_knu.R.layout;

public class DetailPageFragmentList extends Fragment{
	
	private TextView checkNetwork;
	
	private ArrayList<DetailContext> eachContext;
	private Context mContext;

	private View displayView;
	private ViewHolder viewHolder = null;
	
	public DetailPageFragmentList (ArrayList<DetailContext> Data) {
		
		eachContext = Data;
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

		if (displayView == null) {
			viewHolder = new ViewHolder();
			
			displayView = (View) inflater.inflate(R.layout.sy_context ,container, false);
			checkNetwork = (TextView)displayView.findViewById(R.id.check_network_sy_context);
			
			mContext = displayView.getContext();
			viewHolder.detailContextList = (ListView) displayView.findViewById(R.id.sy_context_list);

			displayView.setTag(viewHolder);
		} else {

			viewHolder = (ViewHolder) displayView.getTag();

		}
		
		viewHolder.syContextAdapter = new ContextAdapter(mContext,eachContext);
	 	viewHolder.detailContextList.setAdapter(viewHolder.syContextAdapter);
	 	viewHolder.syContextAdapter.notifyDataSetChanged();
  		
  		if(eachContext.isEmpty()){
  			checkNetwork.setText("내용이 없습니다.");
  			checkNetwork.setVisibility(View.VISIBLE);
		} else {
			checkNetwork.setVisibility(View.INVISIBLE);
		}
		
		return displayView;
	}

	class ViewHolder {

		public ListView detailContextList;
		public ContextAdapter syContextAdapter;
		
	}

}
