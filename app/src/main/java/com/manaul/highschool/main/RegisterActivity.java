package com.manaul.highschool.main;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.manaul.highschool.manager.UserManager;
import com.manaul.highschool.model.UserModel;
import com.manaul.highschool.newwork.Pocket;
import com.manaul.highschool.newwork.PocketListener;
import com.manaul.highschool.url.UserUrl;
import com.manaul.highschool.utils.DataJson2ObjectUtil;
import com.manaul.highschool.utils.FormatCheckUtils;
import com.manaul.highschool.utils.SharedPreferenceUtil;
import com.manaul.highschool.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements PocketListener , OnClickListener{
//	private SharedPreferences shareConfig;
	private EditText mEmail; // 用户邮箱
	private EditText mPwd; // 密码编辑
	private EditText mPwdCheck; // 密码编辑
	private Button mSureButton; // 确定按钮
	private Context mContext;

	private String pwd;
	private String email;
	private String re_pwd;
	private Pocket pocket;

	private final int rcRegister = 1;

	@SuppressLint("InlinedApi")
	@TargetApi(Build.VERSION_CODES.KITKAT)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.register);
		mContext = this;
		pocket = new Pocket(mContext);
		// 用户相关存到用户中

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
		mSureButton.setOnClickListener(this); // 注册界面两个按钮的监听事



	}

	public void register() {
		pwd = mPwd.getText().toString().trim();
		email = mEmail.getText().toString().trim();
		re_pwd = mPwdCheck.getText().toString().trim();

		if (!FormatCheckUtils.isAccount(email)) {
			ToastUtil.toast(mContext, "请输入正确的邮箱地址");
			return;
		}

		if (!FormatCheckUtils.isPassword(pwd)) {
			ToastUtil.toast(mContext, "密码应为6-18个字符");
			return;
		}

		if (!pwd.equals(re_pwd)) {
			ToastUtil.toast(mContext, "两次密码输入不一致");
			return;
		}

		Map<String , String> map = new HashMap<>();
		map.put("account" , email);
		map.put("password" , pwd );
		map.put("cPassword" , re_pwd);

		pocket.post(this , map , UserUrl.register , rcRegister);

//		UserModel userModel = new UserModel();
//		userModel.setAccount();
//		user.setUsername(email);
//		user.setEmail(email);
//		user.setPassword(pwd);
//		user.setAccessToken(UUID.randomUUID() + "" + new Date().getTime());
//		user.setIsVip(0);
//		user.setVipEnd(System.currentTimeMillis());
//		// 创建我们的进度条
//		final ProgressDialogUtils dialogUtils = new ProgressDialogUtils(mContext);
//		dialogUtils.show("正在注册，请等待...");
//		user.signUp(new SaveListener<User>() {
//		    @Override
//		    public void done(User user, BmobException e) {
//		        if(e==null){
//		        	dialogUtils.hideDialog();
//					pfUser.setStringByKey("userAccessToken", user.getAccessToken());
//					pfUser.setLongByKey("userId" , user.getIsVip());
//		        	Editor editor = shareConfig.edit();
//					editor.putString("accessToken", user.getAccessToken());
//					editor.putLong("validateLoginTime", System.currentTimeMillis());
//					editor.commit();
//					ToastUtils.showToastShort(mContext, "注册成功,已登陆");
//					finish();
//		        }else{
//		        	dialogUtils.hideDialog();
//		        	Log.e("22222", "register--"+e.getMessage());
//		        }
//		    }
//		});
	}
	
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.register_btn_sure:
				register();

				break;
		}
	}

	@Override
	public void onOK(String json , String message , int requestCode) {
		// 注册请求回调
		if(requestCode == rcRegister){
			UserModel userModel = UserManager.write2Pre(mContext , json);
			if(userModel != null){
				ToastUtil.toastTick(mContext , message );
				finish();
			}else {
				ToastUtil.toastLarge(mContext , "未知错误");
			}
		}
	}

	@Override
	public void onError(int errorCode, String msg , int requestCode) {
		ToastUtil.toastLarge(mContext , msg);
	}
}
