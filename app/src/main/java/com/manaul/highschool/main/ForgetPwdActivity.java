package com.manaul.highschool.main;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.manaul.highschool.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class ForgetPwdActivity extends AppCompatActivity{
	private EditText email;
	private Button forget_btn_sure;

	private Context mContext;

	@SuppressLint("InlinedApi")
	@TargetApi(Build.VERSION_CODES.KITKAT)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.forget_pwd);
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

		email = (EditText) findViewById(R.id.email);

		forget_btn_sure = (Button) findViewById(R.id.forget_btn_sure);
		forget_btn_sure.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				updatePwd();
			}
		});

	}

	public void updatePwd() {
		final String em = email.getText().toString().trim();

		if (!FormatCheckUtils.isAccount(em)) {
			ToastUtils.showToastShort(mContext, "请输入正确的邮箱地址");
			return;
		}

		final ProgressDialogUtils progressDialogUtils = new ProgressDialogUtils(mContext);
		progressDialogUtils.show("正在发送验证邮件，请等待...");

		User.resetPasswordByEmail(em, new UpdateListener() {
			@Override
			public void done(BmobException e) {
				if (e == null) {
					Log.e("22222", "成功");
					progressDialogUtils.hideDialog();
					new AlertDialog.Builder(mContext).setTitle("重置密码")// 设置对话框标题
							.setMessage("邮件发送成功，请登录邮箱(" + em + ")重置密码！")// 设置显示的内容
							.setPositiveButton("确定", new DialogInterface.OnClickListener() {// 添加确定按钮
						@Override
						public void onClick(DialogInterface dialog, int which) {// 确定按钮的响应事件
							finish();
						}
					}).show();

				} else {
					progressDialogUtils.hideDialog();
					ToastUtils.showToastShort(mContext, "失败:" + e.getMessage());
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
