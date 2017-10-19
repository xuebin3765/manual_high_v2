package com.manaul.highschool.main;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ActivityVideo extends Activity {

	private RelativeLayout id_top_center_back_lin;
	private TextView layout_top_no_c_textview;

	private Context mContext;

	@TargetApi(Build.VERSION_CODES.KITKAT)
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mContext = this;
		setContentView(R.layout.video);

		// 
		Intent intent = getIntent();
		String topTitle = intent.getStringExtra("top_title");
		id_top_center_back_lin = (RelativeLayout) findViewById(R.id.id_top_center_back_lin);
		id_top_center_back_lin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();

			}
		});

		layout_top_no_c_textview = (TextView) findViewById(R.id.layout_top_no_c_textview);
		layout_top_no_c_textview.setText(topTitle);

	}

	@Override
	protected void onResume() {
		super.onResume();
//		MobclickAgent.onResume(mContext);
	}

	public void onPause() {
		super.onPause();
//		MobclickAgent.onPause(this);
	}

}
