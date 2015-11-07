package com.hwang.sy_knu.info;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.widget.TextView;

import com.hwang.sy_knu.R;

public class AppInfo extends Activity {

	private String appVersion;
	
	private TextView currVer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_info);
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setTitle("어플리케이션 정보");
		
		
		try {
			PackageInfo i = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
			appVersion = i.versionName;
		} catch(NameNotFoundException e) { 
			appVersion = "존재하지 않는 버전";
		}
		
		currVer = (TextView)findViewById(R.id.curr_ver);
		currVer.setText(appVersion);
		
		

	}
}
