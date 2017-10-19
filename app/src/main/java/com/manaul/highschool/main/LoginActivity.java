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

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class LoginActivity extends AppCompatActivity implements OnClickListener {

	private SharedPreferences shareConfig;
	private EditText mAccount; // 用户名编
	private EditText mPwd; // 密码编辑
	private Button mLoginButton; // 登录按钮
	private String topTitle;
	private Context mContext;
	private TextView login_forget_pwd;
	private TextView login_register;

	@TargetApi(Build.VERSION_CODES.KITKAT)
	@SuppressLint("InlinedApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);

	}

	void init() {
		mContext = this;
		shareConfig = new SharedConfig(mContext).getConfig();
		User user = BmobUser.getCurrentUser(User.class);
		if (user != null) {
			finish();
		}

		Intent intent = getIntent();
		topTitle = intent.getStringExtra("top_title");

		ActionBar mActionbar = getSupportActionBar();
		mContext = this;
		if (mActionbar != null) {
			mActionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
			mActionbar.setDisplayShowCustomEnabled(true);
			mActionbar.setCustomView(R.layout.title_to_center);
			TextView tvTitle = (TextView) mActionbar.getCustomView().findViewById(R.id.title_to_center_text);
			tvTitle.setText(topTitle);
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

		// 通过id找到相应的控
		mAccount = (EditText) findViewById(R.id.login_edit_account);
		mPwd = (EditText) findViewById(R.id.login_edit_pwd);

		mLoginButton = (Button) findViewById(R.id.login_btn_login);

		login_forget_pwd = (TextView) findViewById(R.id.login_forget_pwd);
		login_register = (TextView) findViewById(R.id.login_register);

		login_forget_pwd.setOnClickListener(this); // 忘记密码
		login_register.setOnClickListener(this); // 注册

		mLoginButton.setOnClickListener(this); // 登陆
	}

	public void login() {// 登录按钮监听事件

		String userName = mAccount.getText().toString().trim();
		String userPwd = mPwd.getText().toString().trim();
		Log.i("222222222" , userName);
		if (!FormatCheckUtils.isAccount(userName)) {
			ToastUtils.showToastShort(mContext, "请输入正确的邮箱地址");
			return;
		}

		if (!FormatCheckUtils.isPassword(userPwd)) {
			ToastUtils.showToastShort(mContext, "密码应为6-18个字符");
			return;
		}

		// 创建我们的进度条
		final ProgressDialogUtils pDialogUtils = new ProgressDialogUtils(mContext);
		pDialogUtils.show("正在登陆，请等待...");

		User bu2 = new User(); 
		bu2.setUsername(userName);
		bu2.setPassword(userPwd);

		bu2.login(new SaveListener<User>() {

			@Override
			public void done(User user, BmobException e) {
				if (e == null) {
					// 登陆成功后更新accessToken 防止多个账号多个设备登陆
					user.setAccessToken(UUID.randomUUID() + "" + new Date().getTime());
					user.update(user.getObjectId(), new UpdateListener() {
						@Override
						public void done(BmobException e) {
							if(e!=null){
								Log.e("22222", "update-token-"+e.getMessage());
							}
						}
					});
					pDialogUtils.hideDialog();
					Editor editor = shareConfig.edit();
					editor.putLong("validateLoginTime", System.currentTimeMillis());
					editor.commit();
					ToastUtils.showToastShort(mContext, "登陆成功");
					finish();
				} else {
					pDialogUtils.hideDialog();
					ToastUtils.showToastShort(mContext, "用户名或密码错误");
					Log.e("22222", "login--"+e.getMessage());
				}
			}
		});

	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		init();

	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	public void onClick(View v) {
		
		if (v.getId() == R.id.id_top_center_back_lin) {
			finish();
		}
		
		if (v.getId() == R.id.login_register) { // 注册

			Intent intent_Login_to_Register = new Intent(mContext, RegisterActivity.class);
			intent_Login_to_Register.putExtra("top_title", "注册");
			startActivity(intent_Login_to_Register);
			overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
		}
		if (v.getId() == R.id.login_btn_login) { // 登陆

			login();
		}
		if (v.getId() == R.id.login_forget_pwd) { // 忘记密码

			Intent intent_Login_to_reset = new Intent(LoginActivity.this, ForgetPwdActivity.class); // 切换Login
			intent_Login_to_reset.putExtra("top_title", "忘记密码");
			startActivity(intent_Login_to_reset);
			overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
		}
	}
}
