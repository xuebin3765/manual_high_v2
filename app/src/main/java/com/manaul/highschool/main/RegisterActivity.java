package com.manaul.highschool.main;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.manaul.highschool.bean.User;
import com.manaul.highschool.utils.FormatCheckUtils;
import com.manaul.highschool.utils.ProgressDialogUtils;
import com.manaul.highschool.utils.SharedConfig;
import com.manaul.highschool.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.Date;
import java.util.UUID;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegisterActivity extends AppCompatActivity{
	private SharedPreferences shareConfig;
	private EditText mEmail; // 用户邮箱
	private EditText mPwd; // 密码编辑
	private EditText mPwdCheck; // 密码编辑
	private Button mSureButton; // 确定按钮
	private Context mContext;

	private String pwd;
	private String email;

	@SuppressLint("InlinedApi")
	@TargetApi(Build.VERSION_CODES.KITKAT)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register);
		mContext = this;
		shareConfig = new SharedConfig(mContext).getConfig();

		Intent intent = getIntent();
		ActionBar mActionbar = getSupportActionBar();
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

		mPwd = (EditText) findViewById(R.id.pwd);
		mPwdCheck = (EditText) findViewById(R.id.re_pwd);
		mEmail = (EditText) findViewById(R.id.email);
		mSureButton = (Button) findViewById(R.id.register_btn_sure);

		mSureButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				register();
			}
		}); // 注册界面两个按钮的监听事

	}

	public void register() {
		pwd = mPwd.getText().toString().trim();
		email = mEmail.getText().toString().trim();

		if (!FormatCheckUtils.isAccount(email)) {
			ToastUtils.showToastShort(mContext, "请输入正确的邮箱地址");
			return;
		}

		String re_pwd = mPwdCheck.getText().toString().trim();
		if (!FormatCheckUtils.isPassword(pwd)) {
			ToastUtils.showToastShort(mContext, "密码应为6-18个字符");
			return;
		}

		if (!pwd.equals(re_pwd)) {
			ToastUtils.showToastShort(mContext, "两次密码输入不一致");
			return;
		}
		
		User user = new User();
		user.setUsername(email);
		user.setEmail(email);
		user.setPassword(pwd);
		user.setAccessToken(UUID.randomUUID() + "" + new Date().getTime());
		user.setIsVip(0);
		user.setVipEnd(System.currentTimeMillis());
		// 创建我们的进度条
		final ProgressDialogUtils dialogUtils = new ProgressDialogUtils(mContext);
		dialogUtils.show("正在注册，请等待...");
		user.signUp(new SaveListener<User>() {
		    @Override
		    public void done(User user, BmobException e) {
		        if(e==null){
		        	dialogUtils.hideDialog();
		        	Editor editor = shareConfig.edit();
					editor.putString("accessToken", user.getAccessToken());
					editor.putLong("validateLoginTime", System.currentTimeMillis());
					editor.commit();
					ToastUtils.showToastShort(mContext, "注册成功,已登陆");
					finish();
		        }else{
		        	dialogUtils.hideDialog();
		        	Log.e("22222", "register--"+e.getMessage());
		        }
		    }
		});
	}
	
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
