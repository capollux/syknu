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

public class Main extends Activity implements OnItemClickListener, Observer {

	private ProgressBar loading;
	private TextView errorMessage;

	private ArrayList<MenuData> mainMenu;
	private ListView mainList;
	private Adapter mainAdapter;

	// private String SY_Server ="http://www.knu.ac.kr/bbs/syllabus/index.jsp";
	// private String SY_Server ="http://sy.knu.ac.kr/20151/20150209/index.htm";
	public static String SY_SERVER_URL = "http://knu.ac.kr/wbbs/wbbs/contents/index.action?menu_url=curriculum2/index&noDeco=true"; 																																																												

	private List<String> syListUrl;

	// 액션바
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.overflow_menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			break;

		case R.id.saved_sy:
			Intent goSySaved = new Intent(Main.this, SyList.class);
			goSySaved.putExtra("title", "즐겨찾기");
			goSySaved.putExtra("open_saved", 1); // Saved 에서 여는거 = 1
			startActivity(goSySaved);

			break;

		case R.id.app_info:
			Intent Go_App_Info = new Intent(getApplicationContext(),
					AppInfo.class);
			startActivity(Go_App_Info);

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
		getActionBar().setTitle(R.string.app_name_full);

		loading = (ProgressBar) findViewById(R.id.loading_main);
		errorMessage = (TextView) findViewById(R.id.check_network_main);

		mainList = (ListView) findViewById(R.id.main_sub_list);
		mainList.setOnItemClickListener(this);

		mainMenu = new ArrayList<MenuData>();
		syListUrl = new ArrayList<String>();		

		HttpClient getMain = new HttpClient(HttpClient.HTTP_GET_MAIN, SY_SERVER_URL);
		getMain.addObserver(this);
		getMain.connect(); 
		loading.setVisibility(View.VISIBLE);
		
	}

	private void parseHtml(String str) {

		
		Elements rows;
		Document doc = Jsoup.parse(str);
				
		rows = doc.select("td[height=28] a[style=CURSOR: hand]");

		for (Element row : rows) {
			Iterator<Element> iterElem = row.getElementsByTag("font")
					.iterator();
			if (iterElem.hasNext()) {

				mainMenu.add(new MenuData(iterElem.next().text()));

			}
		}

		rows = doc.select("td[height=28] a[target=frmUrl]");

		for (Element row : rows) {
			syListUrl.add(row.attr("href"));
		}

		for (Element row : rows) {
			Iterator<Element> iterElem = row.getElementsByTag("font").iterator();
			if (iterElem.hasNext()) {
				mainMenu.add(new MenuData(iterElem.next().text()));
			}
		}

		// 리스트가 존재할때만
		if (!mainMenu.isEmpty()) {
			int ListSize = mainMenu.size();
			mainMenu.remove(ListSize - 1);
			mainMenu.remove(ListSize - 2);
		}

		mainAdapter = new Adapter(getApplicationContext(), mainMenu);
		mainList.setAdapter(mainAdapter);

		loading.setVisibility(View.INVISIBLE);

		mainAdapter.notifyDataSetChanged();

		if (mainMenu.isEmpty()) {
			if (!str.equals("")) { // 접속은 되었지만 정상적인 SY사이트가 아닌 경우에 ( str 값은 있다 )
				errorMessage.setText("내용이 없습니다.");
			} else {
				errorMessage.setText("네트워크 연결 오류");
			}
			
			errorMessage.setVisibility(View.VISIBLE); // 네트워크가 연결이 안되면 str=""
		} else {
			errorMessage.setVisibility(View.INVISIBLE);
		}

	}

	@Override
	public void update(Observable observable, Object data) {
		// TODO Auto-generated method stub

		parseHtml((String) data);

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		TextView selectedTitle = (TextView) view
				.findViewById(R.id.main_sub_title);
		if (position >= 0 && position <= 3) {
			Intent goSubMenu = new Intent(Main.this, Sub.class);
			goSubMenu.putExtra("title", selectedTitle.getText().toString());
			goSubMenu.putExtra("index", position);
			startActivity(goSubMenu);
		} else if (position >= 4 && position <= 5) {
			Intent goSyList = new Intent(Main.this,
					SyList.class);
			goSyList.putExtra("title", selectedTitle.getText()
					.toString());
			goSyList.putExtra("index", position);
			goSyList.putExtra("url", syListUrl.get(position - 4));
			startActivity(goSyList);
		} else if (selectedTitle.getText().toString().equals("계절학기")) {
			Intent goSyList = new Intent(Main.this,
					SyList.class);
			goSyList.putExtra("title", selectedTitle.getText()
					.toString());
			goSyList.putExtra("index", position);
			goSyList.putExtra("url", syListUrl.get(2));
			startActivity(goSyList);

		} else if (selectedTitle.getText().toString().equals("검색")) {

			Intent goSearch = new Intent(Main.this,
					Search.class);
			goSearch.putExtra("title", selectedTitle.getText().toString());
			goSearch.putExtra("index", position);
			startActivity(goSearch);
		} else {

		}
	}

}
