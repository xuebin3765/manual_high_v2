package com.manaul.highschool.main;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.manaul.highschool.bean.UpdateApk;
import com.manaul.highschool.service.DownloadService;
import com.manaul.highschool.utils.Constant;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class ActivitySetting extends AppCompatActivity implements OnClickListener {
	private RelativeLayout update_soft;


	@TargetApi(Build.VERSION_CODES.KITKAT)
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		setContentView(R.layout.activity_center_setting);

		String topTitle = intent.getStringExtra("top_title");
		ActionBar mActionbar = getSupportActionBar();
		if (mActionbar != null) {
			mActionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
			mActionbar.setDisplayShowCustomEnabled(true);
			mActionbar.setCustomView(R.layout.title_to_center);
			TextView tvTitle = (TextView) mActionbar.getCustomView().findViewById(R.id.title_to_center_text);
			tvTitle.setText(topTitle);
			ImageView iv_tbb_back = (ImageView) mActionbar.getCustomView().findViewById(R.id.iv_tbb_back);
			ImageView iv_tbb_user = (ImageView) mActionbar.getCustomView().findViewById(R.id.iv_tbb_user);
			iv_tbb_back.setVisibility(View.VISIBLE);
			iv_tbb_back.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});
			iv_tbb_user.setVisibility(View.GONE);
		}

		update_soft = (RelativeLayout) findViewById(R.id.update_soft);

		update_soft.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.update_soft:
			// 创建我们的进度条
			final ProgressDialog proDia = new ProgressDialog(ActivitySetting.this);
			proDia.setTitle("正在检查更新");
			proDia.setMessage("请耐心等待");
			proDia.onStart();
			proDia.show();

			BmobQuery<UpdateApk> query = new BmobQuery<UpdateApk>();
			query.findObjects(new FindListener<UpdateApk>() {

				@Override
				public void done(List<UpdateApk> arg0, BmobException arg1) {
					proDia.dismiss();// 隐藏对话框
					if (arg0 != null && arg0.size() > 0) {
						final UpdateApk apk = arg0.get(0);
						if (!apk.getVersion().equals(Constant.getVersionName(getApplicationContext()))) {

							/* 系统提示对话框 */
							new AlertDialog.Builder(ActivitySetting.this).setTitle("版本更新")// 设置对话框标题
									.setMessage(apk.getMessage())// 设置显示的内容
									.setPositiveButton("后台下载", new DialogInterface.OnClickListener() {// 添加确定按钮
								@Override
								public void onClick(DialogInterface dialog, int which) {// 确定按钮的响应事件
									Intent intent = new Intent(getApplicationContext(), DownloadService.class);
									intent.putExtra("url", apk.getApkurl());
									startService(intent);
								}
							}).setNegativeButton("下次再说", new DialogInterface.OnClickListener() {// 添加返回按钮
								@Override
								public void onClick(DialogInterface dialog, int which) {// 响应事件

								}
							}).show();// 在按键响应事件中显示此对话框
						} else {
							Toast.makeText(getApplicationContext(),
									"当前已经是最新版本:" + Constant.getVersionName(getApplicationContext()), Toast.LENGTH_LONG)
									.show();
						}
					}else {
						Toast.makeText(getApplicationContext(),
								"当前已经是最新版本:" + Constant.getVersionName(getApplicationContext()), Toast.LENGTH_LONG)
								.show();
					}
				}
			});
			break;
		default:
			break;
		}

	}


	@Override
	public void finish() {
		super.finish();
		// db.close();
		Log.e("db ", " db close;");
	}
}
