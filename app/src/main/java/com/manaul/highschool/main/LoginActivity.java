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
import com.manaul.highschool.manager.UserManager;
import com.manaul.highschool.newwork.Pocket;
import com.manaul.highschool.newwork.PocketListener;
import com.manaul.highschool.newwork.PocketParameter;
import com.manaul.highschool.newwork.StatusCode;
import com.manaul.highschool.url.UserUrl;
import com.manaul.highschool.utils.DebugUtil;
import com.manaul.highschool.utils.FormatCheckUtils;
import com.manaul.highschool.utils.ProgressDialogUtils;
import com.manaul.highschool.utils.SharedPreferenceUtil;
import com.manaul.highschool.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class LoginActivity extends AppCompatActivity implements OnClickListener , PocketListener {

	private EditText mAccount; // 用户名编
	private EditText mPwd; // 密码编辑
	private Button mLoginButton; // 登录按钮
	private String topTitle;
	private Context mContext;
	private TextView login_forget_pwd;
	private TextView login_register;

	private Pocket mPocket;
	private ProgressDialogUtils dialogUtils;

	private final int rcLogin = 1;

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

		mPocket = new Pocket(mContext);
		dialogUtils = new ProgressDialogUtils(mContext);

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

	public void toLogin(){
		String userName = mAccount.getText().toString().trim();
		String userPwd = mPwd.getText().toString().trim();
		if(userName.length() < 3 || userName.length() > 10){
			ToastUtil.toast(mContext , "用户名为3~10位字母或数字");
		}
		if(userPwd.length() < 3 || userPwd.length() > 10){
			ToastUtil.toast(mContext , "密码为3~10位字母或数字");
		}
		Map<String , String > map = new HashMap<>();
		map.put("account", userName);
		map.put("password",userPwd);
		mPocket.post(this, map , UserUrl.login , rcLogin);
	}

	public void login() {// 登录按钮监听事件
		String userName = mAccount.getText().toString().trim();
		String userPwd = mPwd.getText().toString().trim();
		Log.i("222222222" , userName);
		if (!FormatCheckUtils.isAccount(userName)) {
			dialogUtils.hideDialog();
			ToastUtil.toast(mContext, "请输入正确的邮箱地址");
			return;
		}

		if (!FormatCheckUtils.isPassword(userPwd)) {
			dialogUtils.hideDialog();
			ToastUtil.toast(mContext, "密码应为6-18个字符");
			return;
		}

		// 创建我们的进度条

		User bu2 = new User(); 
		bu2.setUsername(userName);
		bu2.setPassword(userPwd);
		bu2.login(new SaveListener<User>() {

			@Override
			public void done(User user, BmobException e) {
				if (e == null) {
					// 登陆成功后更新accessToken 防止多个账号多个设备登陆
					user.setAccessToken(UUID.randomUUID().toString());
					user.update(user.getObjectId(), new UpdateListener() {
						@Override
						public void done(BmobException e) {
							if(e!=null){
								DebugUtil.d("update-token-"+e.getMessage());
							}
						}
					});
					dialogUtils.hideDialog();
					SharedPreferenceUtil.getInstance(mContext).setLongByKey("validateLoginTime", System.currentTimeMillis());
					ToastUtil.toast(mContext, "登陆成功");
					finish();
				} else {
					dialogUtils.hideDialog();
					ToastUtil.toast(mContext, "用户名或密码错误");
					DebugUtil.d("login--"+e.getMessage());
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
		}else if (v.getId() == R.id.login_register) { // 注册
			Intent intent_Login_to_Register = new Intent(mContext, RegisterActivity.class);
			intent_Login_to_Register.putExtra("top_title", "注册");
			startActivity(intent_Login_to_Register);
			overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
		}else if (v.getId() == R.id.login_btn_login) { // 登陆
			dialogUtils.show("正在登陆");
			toLogin();
		}else if (v.getId() == R.id.login_forget_pwd) { // 忘记密码
			Intent intent_Login_to_reset = new Intent(LoginActivity.this, ForgetPwdActivity.class); // 切换Login
			intent_Login_to_reset.putExtra("top_title", "忘记密码");
			startActivity(intent_Login_to_reset);
			overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
		}
	}

	@Override
	public void onOK(String json, String message, int requestCode) {
		dialogUtils.hideDialog();
		UserManager.write2Pre(mContext,json);
		ToastUtil.toastTick(mContext , "登陆成功");
		finish();
	}

	@Override
	public void onError(int errorCode, String msg, int requestCode) {
		if(errorCode == StatusCode.CODE_RECORD_NOT_FOUND_USER){
			login();
		}else {
			dialogUtils.hideDialog();
			ToastUtil.toast(mContext , msg);
		}
	}
}
