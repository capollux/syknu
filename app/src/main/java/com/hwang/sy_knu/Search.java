package com.hwang.sy_knu;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class Search extends Activity implements OnItemSelectedListener, View.OnClickListener {

	private Spinner termSpinner;
	
	private String searchTitle;

	private LinearLayout touchInterceptor;
	
	// 받는 정보
	private String searchWhat;

	private EditText termYear;
	private EditText searchNum;
	private EditText searchName;
	private EditText searchPro;
	
	private String searchNameUTF8;
	private String searchProUTF8;

	
	private Button searchStartBtnNum;
	private Button searchStartBtnName;
	private Button searchStartBtnPro;
	
	// 주는 정보
	private String term_1;
	private String term_2;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		getActionBar().setDisplayShowHomeEnabled(false);

		Intent get_info = getIntent();
		searchTitle = get_info.getStringExtra("title");
		getActionBar().setTitle(searchTitle);
		
		termYear = (EditText)findViewById(R.id.search_term_year_1);

		// initalize
		searchNum = (EditText)findViewById(R.id.search_subj_num);
		searchName= (EditText)findViewById(R.id.search_subj_title);
		searchPro= (EditText)findViewById(R.id.search_subj_pro);	
		
		searchStartBtnNum= (Button)findViewById(R.id.search_subj_num_btn);
		searchStartBtnName= (Button)findViewById(R.id.search_subj_title_btn);
		searchStartBtnPro= (Button)findViewById(R.id.search_subj_pro_btn);

		searchStartBtnNum.setOnClickListener(this);
		searchStartBtnName.setOnClickListener(this);
		searchStartBtnPro.setOnClickListener(this);
		

		String[] optionLavala=getResources().getStringArray(R.array.spinnerArrayTerm);
		ArrayAdapter<String> Term_Adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,optionLavala);
		termSpinner = (Spinner)findViewById(R.id.search_term_year_2);
		termSpinner.setAdapter(Term_Adapter);
		
		termSpinner.setOnItemSelectedListener(this);
		
		
		touchInterceptor = (LinearLayout)findViewById(R.id.search_layout);
		touchInterceptor.setOnTouchListener(new OnTouchListener() {
		    @SuppressLint("ClickableViewAccessibility")
			@Override
		    public boolean onTouch (View v, MotionEvent event) {
		    	InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(v.getApplicationWindowToken(),InputMethodManager.SHOW_FORCED);
		        return false;
		    }
		});
		
	}
	
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		term_2=(String)termSpinner.getSelectedItem();

	}



	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		term_2="";
	}



	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.search_subj_num_btn:
			if (searchNum.getText().toString().length()<7){
				Toast. makeText(getApplicationContext(), "7자 이상 입력해주세요.", Toast.LENGTH_SHORT).show();
			} else {
				searchWhat = "search_subj_sub_class_cde='"+searchNum.getText().toString().toUpperCase();
			}
			
			break;
			
		case R.id.search_subj_title_btn:
			if (searchName.getText().toString().equals("")){
				Toast. makeText(getApplicationContext(), "교과목명을 입력해주세요.", Toast.LENGTH_SHORT).show();
			} else {
				try {
					searchNameUTF8 = new String(URLEncoder.encode(searchName.getText().toString(),"UTF-8").getBytes("UTF-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					searchNameUTF8 = searchName.getText().toString();
				}

				searchWhat = "search_subj_nm='"+searchNameUTF8;
			}
			break;
			
		case R.id.search_subj_pro_btn:
			if (searchPro.getText().toString().equals("")){
				Toast. makeText(getApplicationContext(), "교수명을 입력해주세요.", Toast.LENGTH_SHORT).show();
			} else {
				try {
					searchProUTF8 = new String(URLEncoder.encode(searchPro.getText().toString(),"UTF-8").getBytes("UTF-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					searchProUTF8 = searchPro.getText().toString();
				}
				searchWhat = "search_prof_nm='"+searchProUTF8;
			}
			break;

		}

		term_1 = termYear.getText().toString();

		if(searchWhat!=null && term_1.length()==4){
			Intent Go_Sy_List = new Intent(Search.this,SyList.class);
			Go_Sy_List.putExtra("title", "검색 결과");
			Go_Sy_List.putExtra("url", "http://yes.knu.ac.kr/cour/cour/course/listLectPln/list.action?"+searchWhat+"'&search_open_yr_trm='"+term_1+term_2+"'");
			Go_Sy_List.putExtra("search", 1);
			Go_Sy_List.putExtra("term", term_1+term_2);
			startActivity(Go_Sy_List);
		} else {
			Toast. makeText(getApplicationContext(), "년도를 올바르게 입력해주세요.", Toast.LENGTH_SHORT).show();
		}
		
		//Search_Num.setText(null);
		//Search_Name.setText(null);
		//Search_Pro.setText(null);
		searchWhat=null;
		
	}
	
	
}
