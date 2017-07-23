package com.hwang.sy_knu;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hwang.sy_knu.DB.DbOpenHelper;
import com.hwang.sy_knu.Data.Data;
import com.hwang.sy_knu.Data.HttpClient;
import com.hwang.sy_knu.adapter.SyAdapter;
import com.hwang.sy_knu.info.AppInfo;

public class SyList extends Activity implements OnItemClickListener, Observer {

	private DbOpenHelper mDbOpenHelper;
	private Cursor mCursor;
	
	private ProgressBar loading;
	private TextView errorMessage;

	private HttpClient getSyList;
	
	private ArrayList<Data> syMenu;
	private ListView syList;
	private SyAdapter syAdapter;

	// 받는 정보
	private String syTitle;
	private int sySearch;
	private int sySaved;
	private String syTerm;
	private String syListUrl;
	
	// 주는 정보
	private List<String> syContextUrl;
	private List<String> Remark;
	private List<String> SY_Num;
	
//	private TextView SY_Cour_Name;
	
	// 새로고침 연속 방지
	private boolean mFlag = true;
	
	
	// 변수
	private int cntrow=0;
	private int cnt=0;
	

	// 액션바
		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			if(sySaved==1){
				getMenuInflater().inflate(R.menu.overflow_menu_saved, menu);
			} else {
				getMenuInflater().inflate(R.menu.overflow_menu_list, menu);
			}
			return true;
		}
		
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			
			switch(item.getItemId()){
			case android.R.id.home:
				break;
				
			case R.id.list_refresh:
				// 오픈이 몇인지 분기해야함
				
				if (!mFlag) {
					return false;
				} else {
					
					mFlag = false;
					
					if(sySaved==1){
						syMenu.clear();
						syContextUrl.clear();
						Remark.clear();
						cnt=0;
						loading.setVisibility(View.VISIBLE);
						doWhileCursorToArray();
					} else {
						syMenu.clear();
						syContextUrl.clear();
						Remark.clear();
						
						loading.setVisibility(View.VISIBLE);
						getSyList = new HttpClient(HttpClient.HTTP_GET_SY_LIST,syListUrl);
						getSyList.addObserver(this);
						getSyList.connect();
					}
				}
				break;
			
				// '수집한'에서만 보이는 메뉴
//			case R.id.saved_sy:
//				Intent Go_Sy_Saved = new Intent(SyList.this,SyList.class);
//				Go_Sy_Saved.putExtra("title", "즐겨찾기");
//				Go_Sy_Saved.putExtra("open_saved",1); // Saved 에서 여는거 = 1
//				startActivity(Go_Sy_Saved);
//				break;

				
				// '수집한'에서만 보이는 메뉴 끝
			
			case R.id.app_info:
				Intent Go_App_Info = new Intent(getApplicationContext(),AppInfo.class);
				startActivity(Go_App_Info);
				break;
	
			default:
				return false;
			}
			
			return true;
		}
		// 액션바 끝
	
		// Long Click
//		 @Override
//		 public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//			 super.onCreateContextMenu(menu, v, menuInfo);
//			 if(sySaved==1){
//				 menu.add(0, Menu.FIRST, Menu.NONE, "삭제");
//				 menu.add(0, 2, Menu.NONE, "수강신청 인원조회");
//			 } else {
//				 menu.add(0, Menu.FIRST, Menu.NONE, "즐겨찾기 추가");
//				 menu.add(0, 2, Menu.NONE, "수강신청 인원조회");
//			 }
//
//		 }
//
//		 @Override
//		 public boolean onContextItemSelected(MenuItem item) {
//			 super.onContextItemSelected(item);
//
//			 AdapterView.AdapterContextMenuInfo menuInfo;
//			 int index;
//
//			 switch(item.getItemId()) {
//
//
//			 case Menu.FIRST:
//				 menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//				 index = menuInfo.position;
//				 if(sySaved==1){
//					 mDbOpenHelper.deleteColumn(SY_Num.get(index).replace("\n", ""));
//					 syMenu.clear();
//					 syContextUrl.clear();
//					 Remark.clear();
//					 cnt=0;
//					 doWhileCursorToArray();
//				 } else {
//					 mDbOpenHelper.insertColumnSaved(SY_Num.get(index).replace("\n", ""), syContextUrl.get(index).substring(syContextUrl.get(index).indexOf("searchOpenYrTrm='")+17,syContextUrl.get(index).indexOf("searchOpenYrTrm='")+22));
//					 Toast.makeText(getApplicationContext(), "즐겨찾기 추가 완료", Toast.LENGTH_SHORT).show();
//				 }
//				 break;
//			 case 2:
//				 menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//				 index = menuInfo.position;
//				Intent goStuScan = new Intent(SyList.this,StuScan.class);
//
//
//				goStuScan.putExtra("code", syMenu.get(index).getSubjectNum());
//				startActivity(goStuScan);
//				break;
//
//			 }
//
//		 return false;
//
//		 }
		 
		// Long Click End

		
		
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sy_list);
		getActionBar().setDisplayShowHomeEnabled(false);
		
		mDbOpenHelper = new DbOpenHelper(this);
		mDbOpenHelper.open();
		
		loading = (ProgressBar)findViewById(R.id.loading_sy);
		errorMessage = (TextView)findViewById(R.id.check_network_sy);
		
		Intent getInfo = getIntent();
		
		syTitle = getInfo.getStringExtra("title");
		sySaved = getInfo.getIntExtra("open_saved", -1);
		sySearch = getInfo.getIntExtra("search", -1);
		syListUrl = getInfo.getStringExtra("url");
		
		
		if(syTitle.equals("계절학기")){
			syTerm = Data.seasonTerm;
		} else {
			syTerm = Data.yearTerm;
		}
		
		getActionBar().setTitle(syTitle);
			
		//SY_Cour_Name = (TextView)findViewById(R.id.sy_cour_title);
		syList = (ListView)findViewById(R.id.sy_list);
		
		syMenu = new ArrayList<Data>();
		syContextUrl = new ArrayList<String>();
		Remark = new ArrayList<String>();
		SY_Num = new ArrayList<String>();
		
//		if(sySaved==1){
//			SY_Cour_Name.setText(null);
//		} else {
//			SY_Cour_Name.setText(syTerm+" 학기");
//		}

		registerForContextMenu(syList);
		

		
		syList.setOnItemClickListener(this);
		
		
		if (sySaved==1){
			cnt=0;
			doWhileCursorToArray();
			
		} else {
			loading.setVisibility(View.VISIBLE);
			getSyList = new HttpClient(HttpClient.HTTP_GET_SY_LIST,syListUrl);
			getSyList.addObserver(this);
			getSyList.connect();
		}
		
	}
	
	
	private void parseHtml(String str) {
		
		 Document doc = Jsoup.parse(str);    	
		  
         Elements rows = doc.select("table.courTable tbody tr a");
	      
         for (Element row : rows) {  
        	 syContextUrl.add("http://my.knu.ac.kr"+row.attr("href"));
         }	

         rows = doc.select("table.courTable tbody tr");
         
       
         List<String> heads = new ArrayList<String>();
         String head="";
         
         // position info
         int position=1;
         
		int gradePosition=0;
		int subjectTypePosition=0;
		int openMajorPosition=0;
		int subjectNumPosition=0;
		int subjectTitlePosition=0;

		int subjectUnitNumPosition=0;
		int subjectUnitLecPosition=0;
		int subjectUnitPraPosition=0;
		int professorPosition=0;
		int schedulePosition=0;

		int placePosition=0;
		int maxStuPosition=0;
		int currStuPosition=0;
		int resrStuPosition=0;
		int canResrPosition=0;

		int remarkPosition=0;
         
         
         
         for (Element row : rows) {
        	 
        	 position=1;
        	 Iterator<Element> iterElem = row.getElementsByTag("th").iterator();

        	 heads.add("-");

        	 while (true){
        		 
        		 if(iterElem.hasNext()){

        			 String tmp=iterElem.next().text();
        			 head=tmp.replace(" ", "");
        			 heads.add(head);
        			 
        			 
        			 if(head.equals("학년")){
        				 gradePosition=position;
        			 } else if(head.equals("교과구분")) {
        				 subjectTypePosition=position;
        			 } else if(head.equals("개설대학")){ 
        				 openMajorPosition=position;
        			 } else if(head.equals("교과목번호")){
        				 subjectNumPosition=position;
        			 } else if(head.equals("교과목명")){
        				 subjectTitlePosition=position;
        			 } else if(head.equals("학점")){
        				 subjectUnitNumPosition=position;
        			 } else if(head.equals("강의")){
        				 subjectUnitLecPosition=position;
        			 } else if(head.equals("실습")){
        				 subjectUnitPraPosition=position;
        			 } else if(head.equals("담당교수")){
        				 professorPosition=position;
        			 } else if(head.equals("강의시간")){
        				 schedulePosition=position;
        			 } else if(head.equals("강의실")){
        				 placePosition=position;
        			 } else if(head.equals("수강정원")){
        				 maxStuPosition=position;
        			 } else if(head.equals("수강신청")){
        				 currStuPosition=position;
        			 } else if(head.equals("수강꾸러미신청")){
        				 resrStuPosition=position;
        			 } else if(head.equals("수강꾸러미신청가능여부")){
        				 canResrPosition=position;
        			 } else if(head.equals("비고")){
        				 remarkPosition=position;
        			 } else {
        				 
        			 }
       
        			 position++;
        			 
        		 } else {
        			 break;
        		 }        		 
        	 }

         }
         
         
         rows = doc.select("table.courTable tbody tr");

         
         for (Element row : rows) {
        	 List<String> context = new ArrayList<String>();
        	 	Iterator<Element> iterElem = row.getElementsByTag("td").iterator();
        	 	
        	 	context.add("-");
        	 	
	            for(int i=1 ; i<heads.size()+1 ; i++ ){
		            	if(iterElem.hasNext()){
		            		if(i==subjectNumPosition){
		            			String tmp=iterElem.next().text();
		            			String subjectNum=tmp.substring(0,4)+"\n"+tmp.substring(4);
		            			context.add(subjectNum);
		            		} else {
		            			context.add(iterElem.next().text());
		            		}
		            	}
	             }
           
           
	            if(context.size()>=3){
	          
	            	 Remark.add(context.get(remarkPosition)+" ("+context.get(gradePosition)+" / "+context.get(subjectTypePosition)+")");

	  	           SY_Num.add(context.get(subjectNumPosition));
	  	            
	  		       syMenu.add(new Data(context.get(gradePosition),context.get(subjectTypePosition),context.get(openMajorPosition),context.get(subjectNumPosition),context.get(subjectTitlePosition),
	  		       			context.get(subjectUnitNumPosition),context.get(subjectUnitLecPosition),context.get(subjectUnitPraPosition),context.get(professorPosition),context.get(schedulePosition),
	  		       			context.get(placePosition),context.get(maxStuPosition),context.get(currStuPosition),context.get(resrStuPosition),context.get(canResrPosition),context.get(remarkPosition)));
	            }
	          
			
        }
         
         /*
         rows = doc.select("table.courTable tbody tr");

         
         for (Element row : rows) {
	       	    String[] strings = {"-","-","-","-","-",   "-","-","-","-","-",   "-","-","-","-","-",   "-"};
	            Iterator<Element> iterElem = row.getElementsByTag("td").iterator();
	            for(int i=0 ; i<16 ;i++ ){
		            	if(iterElem.hasNext()){
		            		
		            		
		            		if(i==(3+syOpen)){
		            			String tmp=iterElem.next().text();
		            			String newstr=tmp.substring(0,4)+"\n"+tmp.substring(4);
		            			strings[i]=newstr;
		            		} else {
		            			strings[i]=iterElem.next().text();
		            		}
		            		
		            	}
	             }
           
           
				if(strings[12+syOpen].replaceAll("\\d", "").equals("") && !strings[12+syOpen].equals("")){ // 12번 자리에 숫자이면 (신청인원, 꾸러미인원 테이블에 있을떄)
					Remark.add(strings[15+syOpen]+" ("+strings[0]+" / "+strings[1]+")");
					
					if(strings[14+syOpen].equals("N")){
		            	strings[13+syOpen]=strings[14+syOpen];
		            }
				} else {
					if(!strings[3+syOpen].equals("-")){
	   				Remark.add(strings[12+syOpen]+" ( "+strings[0]+" / "+strings[1]+" )");
					}
					
				}

		       	if(syOpen==0){
		       		if(!strings[3].equals("-")){
		
		       			if(strings[12].replaceAll("\\d", "").equals("") && !strings[12].equals("")){ // 12번 자리에 숫자이면 (신청인원, 꾸러미인원 테이블에 있을떄)
		       				syMenu.add(new SY_KNU_Data(strings[0],strings[1],strings[2],strings[3],strings[4],
					            		strings[5],strings[6],strings[7],strings[8],strings[9],
					            		strings[10],strings[11],strings[12],strings[13],strings[14],strings[15]));
			            		
		       			} else {
		       				syMenu.add(new SY_KNU_Data(strings[0],strings[1],strings[2],strings[3],strings[4],
					            		strings[5],strings[6],strings[7],strings[8],strings[9],
					            		strings[10],strings[11],"-","-","-",strings[12])); // 임시
			            		
		       			}
		       			
		           		SY_Num.add(strings[3]);
		       		}
		       	} else {
		       		if(!strings[2].equals("-")){
		       			if(strings[11].replaceAll("\\d", "").equals("") && !strings[11].equals("")){
		       				syMenu.add(new SY_KNU_Data(strings[0],strings[1],"",strings[2],strings[3],strings[4],
					            		strings[5],strings[6],strings[7],strings[8],strings[9],
					            		strings[10],strings[11],strings[12],strings[13],strings[14]));
					            		
		       			} else {
		       				syMenu.add(new SY_KNU_Data(strings[0],strings[1],"",strings[2],strings[3],strings[4],
					            		strings[5],strings[6],strings[7],strings[8],strings[9],
					            		strings[10],"-","-","-",strings[11]));
		       			}
		       			
				            SY_Num.add(strings[2]);
		       		}
		       	}
           
        }
         */	
         
         

         cnt++;
         
         if(sySaved==1 && cntrow>cnt){
   		  
         } else {
        	syAdapter = new SyAdapter(getApplicationContext(),syMenu);
         	syList.setAdapter(syAdapter);
	  		
	  		loading.setVisibility(View.INVISIBLE);
	  		
	  		syAdapter.notifyDataSetChanged();
	  		mFlag=true;
	  		
	  		 if(syMenu.isEmpty()){
	  			 
	  			 
			  			 if(sySearch==1){
			  				errorMessage.setText("검색 결과가 없습니다.");
			  			 } else if (sySaved==1){
			  				errorMessage.setText("즐겨찾는 강의계획서가 없습니다.");
			  			 } else {
			  				 if(!str.equals("")){ // 접속은 되었지만 정상적인 SY사이트가 아닌 경우에 ( str 값은 있다 )
			  					errorMessage.setText("내용이 없습니다."); // 서버 접속 오류?
			  				 }
		            	}
		            	
			  			errorMessage.setVisibility(View.VISIBLE);
				} else {
					errorMessage.setVisibility(View.INVISIBLE);
				}
         }
		
		
	}		
	
	
	@Override
	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub
		parseHtml((String)data);
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		Intent goSyContext = new Intent(SyList.this,Sy.class);

		goSyContext.putExtra("title", syMenu.get(position).getSubjectNum()+"  "+syMenu.get(position).getSubjectTitle());
		goSyContext.putExtra("position", position);
		goSyContext.putExtra("url", syContextUrl.get(position));
		goSyContext.putExtra("remark", Remark.get(position));
		startActivity(goSyContext);
	}
	
	
	private void doWhileCursorToArray() {

		String num;
		String term;
		mCursor = null;
		mCursor = mDbOpenHelper.getAllColumns();

		cntrow = mCursor.getCount();
		
		if(cntrow==0){
			if(syAdapter!=null){
				syAdapter.notifyDataSetChanged();
			}
			errorMessage.setText("수집한 강의계획서가 없습니다.");	
			errorMessage.setVisibility(View.VISIBLE);
		}
		
		if(!SY_Num.isEmpty()){
			SY_Num.clear();
		}
		
		
		while (mCursor.moveToNext()) {

			num = mCursor.getString(mCursor.getColumnIndex("Sub_Num"));
			term = mCursor.getString(mCursor.getColumnIndex("Sub_Term"));
			
			loading.setVisibility(View.VISIBLE);
			getSyList = new HttpClient(HttpClient.HTTP_GET_SAVED_SY_LIST, "http://yes.knu.ac.kr/cour/cour/course/listLectPln/list.action?search_subj_sub_class_cde="+num+"&search_open_yr_trm="+term);
			getSyList.addObserver(this);
			getSyList.connect();
			
		}
	
		mCursor.close();
	}

	

	
	
	
}
