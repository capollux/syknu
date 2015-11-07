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

public class Major extends Activity implements OnItemClickListener, Observer {

	private ProgressBar loading;
	private TextView errorMessage;

	private ArrayList<MenuData> majorMenu;
	private ListView majorList;
	private Adapter majorAdapter;

	private String majorTitle;
	private String majorListURL;

	// 주는 정보
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
			Intent goSySaved = new Intent(Major.this, SyList.class);
			goSySaved.putExtra("title", "즐겨찾기");
			goSySaved.putExtra("open_saved", 1); // Saved 에서 여는거 = 1
			startActivity(goSySaved);

			break;

		case R.id.app_info:
			Intent goAppInfo = new Intent(getApplicationContext(),
					AppInfo.class);
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

		loading = (ProgressBar) findViewById(R.id.loading_main);
		errorMessage = (TextView) findViewById(R.id.check_network_main);

		Intent getInfo = getIntent();
		majorTitle = getInfo.getStringExtra("title");
		majorListURL = getInfo.getStringExtra("url");

		getActionBar().setTitle(majorTitle);

		majorList = (ListView) findViewById(R.id.main_sub_list);
		majorList.setOnItemClickListener(this);

		majorMenu = new ArrayList<MenuData>();
		syListUrl = new ArrayList<String>();

		loading.setVisibility(View.VISIBLE);
		HttpClient getMajor = new HttpClient(HttpClient.HTTP_GET_MAJOR,
				majorListURL);
		getMajor.addObserver(this);
		getMajor.connect();

	}

	private void parseHtml(String str) {

		Document doc = Jsoup.parse(str);
		Elements rows = doc.select("div.courbox_back ul li a");
		for (Element row : rows) {
			syListUrl.add("http://yes.knu.ac.kr" + row.attr("href"));
		}

		rows = doc.select("div.courbox_back ul li");

		for (Element row : rows) {
			Iterator<Element> iterElem = row.getElementsByTag("a").iterator();
			if (iterElem.hasNext()) {
				String Add = iterElem.next().text();
				if (!Add.equals("")) {
					majorMenu.add(new MenuData(Add));
				}
			}
		}

		majorAdapter = new Adapter(getApplicationContext(), majorMenu);
		majorList.setAdapter(majorAdapter);

		loading.setVisibility(View.INVISIBLE);

		majorAdapter.notifyDataSetChanged();

		if (majorMenu.isEmpty()) {
			if (!str.equals("")) { // 접속은 되었지만 정상적인 SY사이트가 아닌 경우에 ( str 값은 있다 )
				errorMessage.setText("내용이 없습니다.");
			} else {
				errorMessage.setText("네트워크 연결 오류");
			}
			
			errorMessage.setVisibility(View.VISIBLE);
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
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		TextView mainTitle = (TextView) view.findViewById(R.id.main_sub_title);
		Intent goSyList = new Intent(Major.this, SyList.class);
		goSyList.putExtra("title", majorTitle + " > " + mainTitle.getText());
		goSyList.putExtra("position", position);
		goSyList.putExtra("url", syListUrl.get(position));
		startActivity(goSyList);
	}

}
