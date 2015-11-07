package com.hwang.sy_knu;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.hwang.sy_knu.Data.DetailContext;
import com.hwang.sy_knu.adapter.DetailPageFragmentList;

public class Detail extends FragmentActivity {

	private int detailPageNum;
	private ViewPager detailViewPager;
	private PagerAdapter detailPagerAdapter;
	private LinearLayout detailPageMark;
	private int detailPrevPosition = 0; // Slide Position
	
	// 받는 정보
	private String str;
	
	
	private String detailIndex;
	private String detail01;
	private String detail02;
	private String detail03;
	private String detail04;
	private TextView title; 
	
	
	// 주는 정보
	private ArrayList<DetailContext> detailIdx01;
	private ArrayList<DetailContext> detailIdx02;
	private ArrayList<DetailContext> detailIdx03;
	private ArrayList<DetailContext> detailIdx04;

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sy_context_detail);
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setTitle("강의 내용 및 일정");
		
		Intent get_info = getIntent();
		str = get_info.getStringExtra("htmlsource");
		
		
		detailIdx01=new ArrayList<DetailContext>();
		detailIdx02=new ArrayList<DetailContext>();
		detailIdx03=new ArrayList<DetailContext>();
		detailIdx04=new ArrayList<DetailContext>();
		title=(TextView)findViewById(R.id.detail_title);
		title.setText("강의 요목 및 수업 목표");

		
		Make_Context_Detail();
		
		
		// Veiw Pager
		
		detailPageNum = 4;
		detailPageMark = (LinearLayout) findViewById(R.id.detail_pagemark);
		detailViewPager = (ViewPager) findViewById(R.id.detail_pager);
		detailPagerAdapter = new PagerAdapter(getSupportFragmentManager());
		detailViewPager.setAdapter(detailPagerAdapter);

		detailViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// 여기다가 제목 설정
				if(arg0==0){
					title.setText("강의 요목 및 수업 목표");
				} else if (arg0==1) {
					title.setText("과제 및 연구문제");
				} else if (arg0==2) {
					title.setText("교재 및 참고자료");
				} else if (arg0==3) {
					title.setText("비고");
				}
				
				detailPageMark.getChildAt(detailPrevPosition).setBackgroundResource(R.drawable.point_off);
				detailPageMark.getChildAt(arg0).setBackgroundResource(R.drawable.point_on);
				detailPrevPosition = arg0;
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}

		});
		
		initPageMark();
		
	}
	
	private void initPageMark() {
		for (int j = 0; j < detailPageNum; j++) {
			ImageView iv = new ImageView(getApplicationContext());
			iv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
			if (j == 0)
				iv.setBackgroundResource(R.drawable.point_on);
			else
				iv.setBackgroundResource(R.drawable.point_off);
			detailPageMark.addView(iv);
		}
		detailPrevPosition = 0;
	}

	// Pager Adapter
	private class PagerAdapter extends FragmentStatePagerAdapter {

		public PagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			return detailPageNum;
		}

		@Override
		public Fragment getItem(int position) { // Load each Slide


			if(position==0){
				
				return new DetailPageFragmentList(detailIdx01);
			} else if (position==1){

				return new DetailPageFragmentList(detailIdx02);
			} else if (position==2){

				return new DetailPageFragmentList(detailIdx03);
			} else if (position==3){

				return new DetailPageFragmentList(detailIdx04);
			} else {
				
				return new DetailPageFragmentList(detailIdx04);
			}
		}

	}

	
	public void Make_Context_Detail(){
		Document doc = Jsoup.parse(str);    	
		  
	          
        Elements rows = doc.select("table.table1 tbody tr");

        for (Element row : rows) {
	           Iterator<Element> iterElem = row.getElementsByTag("td").iterator();
	           //StringBuilder makeContext = new StringBuilder();
	          	 try{
		        	   if(iterElem.hasNext()){
		        		   detailIndex=iterElem.next().text();
		        		   detail01=iterElem.next().text();
		        		   detail02=iterElem.next().text();
		        		   detail03=iterElem.next().text();
		        		   detail04=iterElem.next().text();
			          		
		        		   detailIdx01.add(new DetailContext(detailIndex+"주차",detail01));
		        		   detailIdx02.add(new DetailContext(detailIndex+"주차",detail02));
		        		   detailIdx03.add(new DetailContext(detailIndex+"주차",detail03));
		        		   detailIdx04.add(new DetailContext(detailIndex+"주차",detail04));	
			          		
			      		}
			           	    
	           	
	          	 } catch(NoSuchElementException e){
	          		
	          	 }
	           
		           	   	
	
       }
                
		
	}

}
