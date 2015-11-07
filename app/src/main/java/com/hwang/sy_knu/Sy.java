package com.hwang.sy_knu;

import java.util.ArrayList;
import java.util.Iterator;
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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hwang.sy_knu.Data.HttpClient;
import com.hwang.sy_knu.Data.DetailContext;
import com.hwang.sy_knu.adapter.ContextAdapter;

public class Sy extends Activity implements Observer {

	private HttpClient getSy;

	private ProgressBar loading;
	private TextView errorMessage;

	private ArrayList<DetailContext> syContext;
	private ListView syContextList;
	private ContextAdapter syContextAdapter;

	// 받는 정보
	private String syContextTitle;
	private String syContextUrl;
	private String remark;

	// 주는 정보
	private String htmlSource;

	// 한영 연속 방지
	private boolean mFlag = true;

	private int findStartSyType;
	private boolean Eng;

	// 액션바
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.overflow_menu_sy, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			break;

		case R.id.detail_sy:
			Intent Go_SY_Detail = new Intent(getApplicationContext(),
					Detail.class);
			Go_SY_Detail.putExtra("htmlsource", htmlSource);
			startActivity(Go_SY_Detail);

			break;

		case R.id.eng_sy:

			if (!mFlag) {
				return false;
			} else {

				mFlag = false;

				if (Eng == true) {
					Eng = false;
					syContextUrl = syContextUrl.replace("PlanDetailEng","PlanDetail");
					syContext.clear();
					loading.setVisibility(View.VISIBLE);
					getSy = new HttpClient(HttpClient.HTTP_GET_SY, syContextUrl);
					getSy.addObserver(this);
					getSy.connect();
				} else if (Eng == false) {
					Eng = true;
					syContextUrl = syContextUrl.replace("PlanDetail", "PlanDetailEng");
					syContext.clear();
					loading.setVisibility(View.VISIBLE);
					getSy = new HttpClient(HttpClient.HTTP_GET_SY, syContextUrl);
					getSy.addObserver(this);
					getSy.connect();
				} else {
					syContext.clear();
					getSy.connect();
				}
			}

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
		setContentView(R.layout.sy_context);
		getActionBar().setDisplayShowHomeEnabled(false);

		loading = (ProgressBar) findViewById(R.id.loading_sy_context);
		errorMessage = (TextView) findViewById(R.id.check_network_sy_context);

		Intent get_info = getIntent();
		syContextTitle = get_info.getStringExtra("title");
		syContextUrl = get_info.getStringExtra("url");
		remark = get_info.getStringExtra("remark");

		getActionBar().setTitle(syContextTitle);

		syContextList = (ListView) findViewById(R.id.sy_context_list);
		syContext = new ArrayList<DetailContext>();

		findStartSyType = syContextUrl.indexOf("viewPlanDetail") + 14;
		if (syContextUrl.substring(findStartSyType, findStartSyType + 3)
				.equals("Eng")) {
			Eng = true;
		} else {
			Eng = false;
		}

		getSy = new HttpClient(HttpClient.HTTP_GET_SY, syContextUrl);
		getSy.addObserver(this);
		getSy.connect();
		loading.setVisibility(View.VISIBLE);

	}

	private void parseHtml(String str) {
		htmlSource = str;
		Document doc = Jsoup.parse(str);

		Elements rows = doc.select("table.form tbody tr td");

		int i = 0;
		for (Element row : rows) {
			Iterator<Element> iterElem = row.getElementsByTag("td").iterator();
			if (iterElem.hasNext()) {

				if (i == 7) {
					String tmp = iterElem.next().text();
					if (tmp.length() > 20) { // 수업 시간 정렬

						if (Eng == true) {
							syContext.add(new DetailContext(
									DetailContext.Heads_Eng[i], tmp.replace(
											"B ", "B\n")));
						} else if (Eng == false) {
							syContext.add(new DetailContext(
									DetailContext.Heads[i], tmp.replace("B ",
											"B\n")));
						}
					} else {

						if (Eng == true) {
							syContext.add(new DetailContext(
									DetailContext.Heads_Eng[i], tmp));
						} else if (Eng == false) {
							syContext.add(new DetailContext(
									DetailContext.Heads[i], tmp));
						}
					}

				} else if (i == 8) { // 수업 시간 중복 제거
					String tmp = iterElem.next().text();
					if (!tmp.isEmpty()) {
						int finish = tmp.indexOf(" ");
						String onestring;
						try {
							onestring = tmp.substring(0, finish);
						} catch (StringIndexOutOfBoundsException e) {
							onestring = tmp;
						}
						if (onestring.equals("상주캠퍼스")) { // 상주캠퍼스일때는 두번째 공백을
															// 기준으로 강의실을 나눠야 한다.
							int finish2 = tmp.indexOf(" ", finish + 1);
							if (Eng == true) {
								syContext.add(new DetailContext(
										DetailContext.Heads_Eng[i], tmp
												.substring(0, finish2)));
							} else if (Eng == false) {
								syContext.add(new DetailContext(
										DetailContext.Heads[i], tmp.substring(
												0, finish2)));
							}
						} else {
							if (Eng == true) {
								syContext.add(new DetailContext(
										DetailContext.Heads_Eng[i], onestring));
							} else if (Eng == false) {
								syContext.add(new DetailContext(
										DetailContext.Heads[i], onestring));
							}
						}
					} else {
						if (Eng == true) {
							syContext.add(new DetailContext(
									DetailContext.Heads_Eng[i], tmp));
						} else if (Eng == false) {
							syContext.add(new DetailContext(
									DetailContext.Heads[i], tmp));
						}
					}
				} else if (i == 10) { // 면담 시간 스크립트 소스 파싱
					try {
						Elements rows_script = doc
								.select("body script[type=text/javascript]");
						Element row_script = rows_script.first();
						Iterator<Element> iterElem_script = row_script
								.getElementsByTag("script").iterator();
						String script = iterElem_script.next().toString();
						int fromIndex = script.indexOf("if( '06'=='01' ){") + 2;
						int start = script.indexOf("(\"", fromIndex) + 2;
						int end = script.indexOf("\");", fromIndex);
						String newstr = script.substring(start, end);

						if (Eng == true) {
							syContext.add(new DetailContext(
									DetailContext.Heads_Eng[i], newstr));
						} else if (Eng == false) {
							syContext.add(new DetailContext(
									DetailContext.Heads[i], newstr));
						}
					} catch (IndexOutOfBoundsException e) {
						if (Eng == true) {
							syContext.add(new DetailContext(
									DetailContext.Heads_Eng[i], ""));
						} else if (Eng == false) {
							syContext.add(new DetailContext(
									DetailContext.Heads[i], ""));
						}

					}
				} else {
					if (Eng == true) {
						syContext.add(new DetailContext(
								DetailContext.Heads_Eng[i], iterElem.next()
										.text()));
					} else if (Eng == false) {
						syContext.add(new DetailContext(DetailContext.Heads[i],
								iterElem.next().text()));
					}

				}

			}
			i++;
		}

		if (Eng == true) {
			syContext.add(new DetailContext("Remark\n(Grade/\nDivision)",
					remark));
		} else if (Eng == false) {
			syContext.add(new DetailContext("비고\n(학년/\n교과구분)", remark));
		}

		rows = doc.select("body script[type=text/javascript]");
		for (Element row : rows) {
			Iterator<Element> iterElem = row.getElementsByTag("script")
					.iterator();
			String script = iterElem.next().toString();
			for (int j = 12; j < 18; j++) {
				try {
					String newstr;
					int start;
					int fromIndex;
					int end;
					switch (j) {
					case 12:
						fromIndex = script.indexOf("if( '01'=='01' ){") + 2;
						start = script.indexOf("(\"", fromIndex) + 2;
						end = script.indexOf("\");", fromIndex);
						newstr = script.substring(start, end);
						break;

					case 13:
						fromIndex = script.indexOf("if( '02'=='01' ){") + 2;
						start = script.indexOf("(\"", fromIndex) + 2;
						end = script.indexOf("\");", fromIndex);
						newstr = script.substring(start, end);
						break;
					case 14:
						fromIndex = script.indexOf("if( '03'=='01' ){") + 2;
						start = script.indexOf("(\"", fromIndex) + 2;
						end = script.indexOf("\");", fromIndex);
						newstr = script.substring(start, end);
						break;
					case 15:
						fromIndex = script.indexOf("if( '04'=='01' ){") + 2;
						start = script.indexOf("(\"", fromIndex) + 2;
						end = script.indexOf("\");", fromIndex);
						newstr = script.substring(start, end);
						break;
					case 16:
						fromIndex = script.indexOf("if( '05'=='01' ){") + 2;
						start = script.indexOf("(\"", fromIndex) + 2;
						end = script.indexOf("\");", fromIndex);
						newstr = script.substring(start, end);
						break;
					case 17:
						fromIndex = script.indexOf("if( '07'=='01' ){") + 2;
						start = script.indexOf("(\"", fromIndex) + 2;
						end = script.indexOf("\");", fromIndex);
						newstr = script.substring(start, end);
						break;

					default:
						newstr = "-";
					}

					if (Eng == true) {
						syContext.add(new DetailContext(
								DetailContext.Heads_Eng[j], newstr.replace(
										"<br>", " ").replace("<p>", "\n")));
					} else if (Eng == false) {
						syContext.add(new DetailContext(DetailContext.Heads[j],
								newstr.replace("<br>", " ")
										.replace("<p>", "\n")));
					}

				} catch (IndexOutOfBoundsException e) {
					if (Eng == true) {
						syContext.add(new DetailContext(
								DetailContext.Heads_Eng[j], ""));
					} else if (Eng == false) {
						syContext.add(new DetailContext(DetailContext.Heads[j],
								""));
					}
				}
			}
		}

		syContextAdapter = new ContextAdapter(getApplicationContext(),
				syContext);
		syContextList.setAdapter(syContextAdapter);

		loading.setVisibility(View.INVISIBLE);

		syContextAdapter.notifyDataSetChanged();
		mFlag = true;

		if (syContext.isEmpty()) {
			if (!str.equals("")) { // 접속은 되었지만 정상적인 SY사이트가 아닌 경우에 ( str 값은 있다 )
				errorMessage.setText("서버 점검 중");
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

}