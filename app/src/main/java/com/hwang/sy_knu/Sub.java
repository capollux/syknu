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
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hwang.sy_knu.Data.HttpClient;
import com.hwang.sy_knu.Data.MenuData;
import com.hwang.sy_knu.adapter.Adapter;
import com.hwang.sy_knu.info.AppInfo;

public class Sub extends Activity implements OnItemClickListener, Observer {
	
	private ProgressBar loading;
	private TextView errorMessage;
	
	
	private ArrayList<MenuData> subMenu;
	private ListView subList;
	private Adapter subAdapter;

	private String subTitle;
	private int subIndex;
    private String subValue;

	private List<String> syListUrl;
	private List<String> majorListUrl;

	
	// 액션바
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.overflow_menu_main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch(item.getItemId()){
		case android.R.id.home:
			break;

//		case R.id.saved_sy:
//			Intent goSySaved = new Intent(Sub.this,SyList.class);
//			goSySaved.putExtra("title", "즐겨찾기");
//			goSySaved.putExtra("open_saved",1); // Saved 에서 여는거 = 1
//			startActivity(goSySaved);
//
//			break;
			
		case R.id.app_info:
			Intent goAppInfo = new Intent(getApplicationContext(),AppInfo.class);
			startActivity(goAppInfo);
			
			break;
			
		default:
			return false;
		}
		
		return true;
	}
	// 액션바 끝

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_sub_list);
		getActionBar().setDisplayShowHomeEnabled(false);
		
		
		loading = (ProgressBar)findViewById(R.id.loading_main);
		errorMessage = (TextView)findViewById(R.id.check_network_main);
		
		Intent get_info = getIntent();
		subTitle = get_info.getStringExtra("title");
		subIndex = get_info.getIntExtra("index", -1);
		subValue = get_info.getStringExtra("url");

		getActionBar().setTitle(subTitle);
			
		subList = (ListView)findViewById(R.id.main_sub_list);
		subList.setOnItemClickListener(this);

		subMenu = new ArrayList<MenuData>();
		syListUrl = new ArrayList<String>();
		majorListUrl = new ArrayList<String>();
		
  		loading.setVisibility(View.VISIBLE);
		HttpClient getSub = new HttpClient(HttpClient.HTTP_GET_SUB, Main.SY_SERVER_URL);
		getSub.addObserver(this);
		getSub.connect();
		
	}
	
	
	private void parseHtml(String str) {
	
        Document doc = Jsoup.parse(str);
        Elements rows = doc.select("select#sub"+subValue+" option");
        for (Element row : rows) {
            if(subIndex == 0){
              syListUrl.add(row.attr("value"));
            } else {
              majorListUrl.add(row.attr("value"));
            }
        }


        if (subIndex != 0) {

        }

        rows = doc.select("select#sub"+subValue+" option");

        for (Element row : rows) {
            Iterator<Element> iterElem = row.getElementsByTag("option").iterator();
            if(iterElem.hasNext()){
                subMenu.add(new MenuData(iterElem.next().text()));
            }
        }

          	subAdapter = new Adapter(getApplicationContext(),subMenu);
          	subList.setAdapter(subAdapter);
	  		
	  		loading.setVisibility(View.INVISIBLE);

	  		
	  		subAdapter.notifyDataSetChanged();
	  		
	  		if(subMenu.isEmpty()){
	  			errorMessage.setText("네트워크 연결 오류\n또는 내용이 없습니다.");
	  			errorMessage.setVisibility(View.VISIBLE);
			} else {
				errorMessage.setVisibility(View.INVISIBLE);
			}
	  		
	}
	
	@Override
	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub
		parseHtml((String)data);
		
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		TextView selectedTitle = (TextView)view.findViewById(R.id.main_sub_title);
		
		// Main 1,2,3,4
		if(subIndex<=3 && subIndex>=1){
			Intent goMajorList = new Intent(Sub.this,Major.class);
			goMajorList.putExtra("title", subTitle + " > " + selectedTitle.getText().toString());
			goMajorList.putExtra("index", position);
			goMajorList.putExtra("url", majorListUrl.get(position));
			startActivity(goMajorList);
		} else {
		
		Intent goSyList = new Intent(Sub.this,SyList.class);
		goSyList.putExtra("title", subTitle + " > " + selectedTitle.getText().toString());
		goSyList.putExtra("index", position);
		goSyList.putExtra("url", syListUrl.get(position));
		startActivity(goSyList);
		
		}
	}
	
}
