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

import com.manaul.highschool.bean.User;
import com.manaul.highschool.utils.FormatCheckUtils;
import com.manaul.highschool.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class ResetpwdActivity extends AppCompatActivity {

	private Context mContext;
	private EditText mPwd_old;
	private EditText mPwd_new;
	private EditText mPwdCheck;
	private Button mSureButton;

	@TargetApi(Build.VERSION_CODES.KITKAT)
	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.resetpwd);

		mContext = this;

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

		mPwd_old = (EditText) findViewById(R.id.resetpwd_edit_pwd_old);
		mPwd_new = (EditText) findViewById(R.id.resetpwd_edit_pwd_new);
		mPwdCheck = (EditText) findViewById(R.id.resetpwd_edit_pwd_check);
		mSureButton = (Button) findViewById(R.id.resetpwd_btn_sure);

		mSureButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				resetpwd();
			}
		});

	}

	@SuppressWarnings("static-access")
	public void resetpwd() {
		String oldPwd = mPwd_old.getText().toString().trim();
		String newPwd = mPwd_new.getText().toString().trim();
		String newRPwd = mPwdCheck.getText().toString().trim();

		if (!FormatCheckUtils.isPassword(oldPwd)) {
			ToastUtil.toast(mContext, "密码应为6-18个字符");
			return;
		}

		if (!newPwd.equals(newRPwd)) {
			ToastUtil.toast(mContext, "两次密码输入不一致");
			return;
		}
		
		User user = BmobUser.getCurrentUser(User.class);

		if (user != null) {
			user.updateCurrentUserPassword(oldPwd, newPwd, new UpdateListener() {
				
				@Override
				public void done(BmobException e) {
					if(e==null){
						ToastUtil.toast(mContext, "密码修改成功，请使用新密码重新登陆");
						User.logOut();  //清除缓存用户对象
						// 登陆
						Intent intent = new Intent(mContext, LoginActivity.class);
						intent.putExtra("top_title", "登陆");
						startActivity(intent);
						overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
						finish();
			        }else{
						ToastUtil.toast(mContext, "修改失败，请检查旧密码是否正确"+e.getMessage());
			        }
				}
			});
		} else {
			ToastUtil.toast(mContext, "登陆失效，请重新登陆");
			// 登陆
			Intent intent = new Intent(mContext, LoginActivity.class);
			intent.putExtra("top_title", "登陆");
			startActivity(intent);
			overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
			finish();
		}
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