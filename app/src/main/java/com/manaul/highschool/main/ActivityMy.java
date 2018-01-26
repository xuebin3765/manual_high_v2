package com.manaul.highschool.main;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.manaul.highschool.manager.UserManager;
import com.manaul.highschool.model.UserModel;
import com.manaul.highschool.utils.DataUtil;
import com.manaul.highschool.utils.SharedPreferenceUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.Date;

public class ActivityMy extends AppCompatActivity implements OnClickListener {
	private TextView loginTitle;
	private TextView loginText;
	private LinearLayout login;
	private RelativeLayout my_vip;
	private RelativeLayout my_share;
	private RelativeLayout my_comment;
	private RelativeLayout my_update_pwd;
	private RelativeLayout my_logout;
	private RelativeLayout my_about_me;

	private Context mContext;
	
	private UserModel userModel ;

	@TargetApi(Build.VERSION_CODES.KITKAT)
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mContext = this;
		MobclickAgent.onResume(mContext);
		init();
	}

	@SuppressLint({ "InlinedApi", "NewApi" })
	private void init() {
		setContentView(R.layout.activity_center_my);
		ActionBar mActionbar = getSupportActionBar();
		Intent intent = getIntent();

		if (mActionbar != null) {
			mActionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
			mActionbar.setDisplayShowCustomEnabled(true);
			mActionbar.setCustomView(R.layout.title_to_center);
			TextView tvTitle = (TextView) mActionbar.getCustomView().findViewById(R.id.title_to_center_text);
			tvTitle.setText(intent.getStringExtra("top_title"));
			ImageView iv_tbb_back = (ImageView) mActionbar.getCustomView().findViewById(R.id.iv_tbb_back);
			ImageView iv_tbb_user = (ImageView) mActionbar.getCustomView().findViewById(R.id.iv_tbb_user);

			iv_tbb_back.setVisibility(View.VISIBLE);
			iv_tbb_user.setVisibility(View.INVISIBLE);
			iv_tbb_back.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});
		}

		login = (LinearLayout) findViewById(R.id.my_login);
		loginTitle = (TextView) findViewById(R.id.my_login_title);
		loginText = (TextView) findViewById(R.id.my_login_text);
		
		login.setOnClickListener(this);
		my_vip = (RelativeLayout) findViewById(R.id.my_vip);
		my_share = (RelativeLayout) findViewById(R.id.my_share);
		my_comment = (RelativeLayout) findViewById(R.id.my_comment);
		my_update_pwd = (RelativeLayout) findViewById(R.id.my_update_pwd);
		my_logout = (RelativeLayout) findViewById(R.id.my_logout);
		my_about_me = (RelativeLayout) findViewById(R.id.my_about_me);

		my_share.setOnClickListener(this);
		my_vip.setOnClickListener(this);
		my_comment.setOnClickListener(this);
		my_update_pwd.setOnClickListener(this);
		my_logout.setOnClickListener(this);
		my_about_me.setOnClickListener(this);

		userModel = UserManager.getUser(mContext);

		if (userModel != null) {
			loginTitle.setText("用户名："+userModel.getAccount());
			login.setClickable(false);
			my_update_pwd.setVisibility(View.VISIBLE);
//			if(DataUtil.vipIsEnd(user.getVipEnd())){
//				loginText.setText("VIP有效期："+ DataUtil.getAfterTime(new Date(user.getVipEnd()),0));
//			}else {
//				loginText.setText("VIP有效期：未开通" );
//			}

			if(DataUtil.vipIsEnd(userModel.getCreated())){
				loginText.setText("VIP有效期："+ DataUtil.getAfterTime(new Date(userModel.getCreated()),0));
			}else {
				loginText.setText("VIP有效期：未开通" );
			}
			my_logout.setVisibility(View.VISIBLE);

		} else {
			loginTitle.setText("未登陆，点击登陆");
			my_update_pwd.setVisibility(View.GONE);
			loginText.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.my_about_me) {
			Intent intentVip = new Intent(mContext, ActivityAboutMe.class);
			intentVip.putExtra("top_title", mContext.getResources().getString(R.string.my_about_my));
			startActivity(intentVip);
			overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
		}

		if (v.getId() == R.id.my_share) {
			String appName = mContext.getResources().getString(R.string.app_name);
			String shareMessage = "【" + appName
					+ "】包括高中各个科目知识点、练习题、真题、模拟题还有名师讲课视频。你也来试试吧，与全国的高中生一起交流。点击下载：http://a.app.qq.com/o/simple.jsp?pkgname="
					+ getPackageName();
			Intent intentShare = new Intent(Intent.ACTION_SEND);
			intentShare.setType("text/plain");
			intentShare.putExtra(Intent.EXTRA_SUBJECT, "分享");
			intentShare.putExtra(Intent.EXTRA_TEXT, shareMessage);
			intentShare.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(Intent.createChooser(intentShare, getTitle()));
		}
		if (v.getId() == R.id.my_comment) {
			Uri uri = Uri.parse("market://details?id=" + getPackageName());
			Intent intentmark = new Intent(Intent.ACTION_VIEW, uri);
			intentmark.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intentmark);
		}
		if (v.getId() == R.id.my_vip) {
			Intent intent = null;
			if(userModel != null){
				intent =new Intent(mContext, VipActivity.class);
				intent.putExtra("top_title", mContext.getResources().getString(R.string.my_vip));
				
			}else {
				intent =new Intent(mContext, LoginActivity.class);
				intent.putExtra("top_title", mContext.getResources().getString(R.string.login));
			}
			startActivity(intent);
			overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
			
		}

		if (v.getId() == R.id.my_login) {
			Intent intent = new Intent(mContext, LoginActivity.class);
			intent.putExtra("top_title",mContext.getResources().getString(R.string.login));
			startActivity(intent);
			overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
		}

		if (v.getId() == R.id.my_update_pwd) {
			Intent updatePwd = new Intent(mContext, ResetpwdActivity.class);
			updatePwd.putExtra("top_title", mContext.getResources().getString(R.string.update_pwd));
			startActivity(updatePwd);
			overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
		}

		if (v.getId() == R.id.my_logout) {
			SharedPreferenceUtil.getInstance(mContext).setLongByKey("userId" , 0);
			SharedPreferenceUtil.getInstance(mContext).setStringByKey("userAccessToken" , null);
			init();
		}

	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

}
