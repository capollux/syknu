package com.hwang.sy_knu;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hwang.sy_knu.Data.DetailContext;
import com.hwang.sy_knu.adapter.ContextAdapter;
import com.hwang.sy_knu.info.AppInfo;

@SuppressLint({ "JavascriptInterface", "SetJavaScriptEnabled" })
public class StuScan extends Activity {

	private ProgressBar loading;
	private TextView errorMessage;

	private ArrayList<DetailContext> stuScanArray;
	private ListView stuScanList;
	private ContextAdapter stuScanAdapter;

	private WebView mWebView;

	private String stuScanServer = "http://curr.knu.ac.kr/SugangSupport/cour/lectReq/lectReqCntEnq/list.action";
	private String stuScanCode;

	final Handler handler = new MyHandler(this);
	private boolean mFlag = true;

	private String htmlSrc;

	// 액션바
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.overflow_menu_stu_scan, menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			break;

		case R.id.list_refresh:
			// 새로고침

			if (!mFlag) {
				return false;
			} else {
				loading.setVisibility(View.VISIBLE);
				stuScanArray.clear();
				mWebView.loadUrl("javascript:window.HTMLOUT.showHTML(lectReqCntEnqGrid.load({"
						+ "'lectReqCntEnq.search_open_yr_trm': '',"
						+ "'lectReqCntEnq.captcha_cde': '',"
						+ "'lectReqCntEnq.search_subj_cde': '"
						+ stuScanCode.substring(0, 4)
						+ stuScanCode.substring(5, 8)
						+ "',"
						+ "'lectReqCntEnq.search_sub_class_cde': '"
						+ stuScanCode.substring(8) + "'" + "}))");
				mFlag = false;
				handler.sendEmptyMessageDelayed(3, 1500);

			}

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
		setContentView(R.layout.sy_knu_stu_scan);
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setTitle("수강신청 인원조회");

		Intent get_info = getIntent();
		stuScanCode = get_info.getStringExtra("code");

		loading = (ProgressBar) findViewById(R.id.loading_sy_stu_scan);
		errorMessage = (TextView) findViewById(R.id.check_network_sy_stu_scan);

		stuScanList = (ListView) findViewById(R.id.sy_stu_scan_context);
		stuScanArray = new ArrayList<DetailContext>();

		mWebView = (WebView) findViewById(R.id.webview);

		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setSupportZoom(true);
		mWebView.getSettings().setBuiltInZoomControls(true);

		mWebView.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
		mWebView.setWebViewClient(new WebViewClientClass());

		loading.setVisibility(View.VISIBLE);
		mWebView.loadUrl(stuScanServer);

	}

	private class WebViewClientClass extends WebViewClient {
		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			Log.i("INFO", "onPageFinished url=" + url);

			mWebView.loadUrl("javascript:window.HTMLOUT.showHTML(lectReqCntEnqGrid.load({"
					+ "'lectReqCntEnq.search_open_yr_trm': '',"
					+ "'lectReqCntEnq.captcha_cde': '',"
					+ "'lectReqCntEnq.search_subj_cde': '"
					+ stuScanCode.substring(0, 4)
					+ stuScanCode.substring(5, 8)
					+ "',"
					+ "'lectReqCntEnq.search_sub_class_cde': '"
					+ stuScanCode.substring(8) + "'" + "}))");

		}

		@Override
		public void doUpdateVisitedHistory(WebView view, String url,
				boolean isReload) {
			Log.i("WebView", "History: " + url);
			super.doUpdateVisitedHistory(view, url, isReload);

			if (!url.equals(stuScanServer)) {
				loading.setVisibility(View.INVISIBLE);
				errorMessage.setText("조회 가능 기간이 아닙니다.(또는 연결 오류)");
				errorMessage.setVisibility(View.VISIBLE);
			}

		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);

			switch (errorCode) {
			case ERROR_AUTHENTICATION:
				break; // 서버에서 사용자 인증 실패
			case ERROR_BAD_URL:
				break; // 잘못된 URL
			case ERROR_CONNECT:
				break; // 서버로 연결 실패
			case ERROR_FAILED_SSL_HANDSHAKE:
				break; // SSL handshake 수행 실패
			case ERROR_FILE:
				break; // 일반 파일 오류
			case ERROR_FILE_NOT_FOUND:
				break; // 파일을 찾을 수 없습니다
			case ERROR_HOST_LOOKUP:
				break; // 서버 또는 프록시 호스트 이름 조회 실패
			case ERROR_IO:
				break; // 서버에서 읽거나 서버로 쓰기 실패
			case ERROR_PROXY_AUTHENTICATION:
				break; // 프록시에서 사용자 인증 실패
			case ERROR_REDIRECT_LOOP:
				break; // 너무 많은 리디렉션
			case ERROR_TIMEOUT:
				break; // 연결 시간 초과
			case ERROR_TOO_MANY_REQUESTS:
				break; // 페이지 로드중 너무 많은 요청 발생
			case ERROR_UNKNOWN:
				break; // 일반 오류
			case ERROR_UNSUPPORTED_AUTH_SCHEME:
				break; // 지원되지 않는 인증 체계
			case ERROR_UNSUPPORTED_SCHEME:
				break; // URI가 지원되지 않는 방식
			}
			loading.setVisibility(View.INVISIBLE);
			errorMessage.setText("조회 가능 기간이 아닙니다.(또는 연결 오류입니다.)");
			errorMessage.setVisibility(View.VISIBLE);
		}

	}

	class MyJavaScriptInterface {
		@JavascriptInterface
		public void showHTML(final String html) {
			if (html.equals("undefined")) {
				Log.e("excuteJS", "finish");

				handler.sendEmptyMessageDelayed(1, 1000); // 페이지 로드까지 시간차 때문에

			} else {
				htmlSrc = html;
				Log.e("html", "success");
				handler.sendEmptyMessage(2);
			}
		}

	}

	private void parseHTML(String str) {

		Document doc = Jsoup.parse(str);

		String[] heads = { "", "", "", "", "", "", "", "", "", "", "", "" };

		Elements rows = doc.select("div.data table tbody tr");

		for (Element row : rows) {

			Iterator<Element> iterElemHead = row.getElementsByTag("th")
					.iterator();

			for (int i = 0; i < 12; i++) {
				if (iterElemHead.hasNext()) {
					heads[i] = iterElemHead.next().text();
				}
			}
		}

		rows = doc.select("div.data table tbody tr.data.selected");

		for (Element row : rows) {

			Iterator<Element> iterElemContext = row.getElementsByTag("td")
					.iterator();

			for (int i = 0; i < 12; i++) {
				if (iterElemContext.hasNext()) {
					if (i == 2 || i == 4) {
						iterElemContext.next().text();
					} else {
						stuScanArray.add(new DetailContext(heads[i],
								iterElemContext.next().text()));
					}
				}
			}
		}
		
		stuScanAdapter = new ContextAdapter(
				getApplicationContext(), stuScanArray);
		stuScanList.setAdapter(stuScanAdapter);
		loading.setVisibility(View.INVISIBLE);

		stuScanAdapter.notifyDataSetChanged();

		if (stuScanArray.isEmpty()) {
			errorMessage.setText("조회 가능 기간이 아닙니다.\n(또는 연결 오류입니다.)");
			errorMessage.setVisibility(View.VISIBLE);
		} else {
			errorMessage.setVisibility(View.INVISIBLE);
		}
		
	}

	@SuppressLint("HandlerLeak")
	private class MyHandler extends Handler {

		private final WeakReference<StuScan> mActivity;

		public MyHandler(StuScan activity) {
			mActivity = new WeakReference<StuScan>(activity);
		}

		@Override
		public void handleMessage(Message msg) {

			StuScan activity = mActivity.get();

			if (activity != null) {
				switch (msg.what) {
				case 1:
					mWebView.loadUrl("javascript:window.HTMLOUT.showHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
					break;
				case 2:
					parseHTML(htmlSrc);
					break;
				case 3:
					mFlag = true;
					break;
				default:

				}

			}
		}
	}

}
