package com.manaul.highschool.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.manaul.highschool.bean.Banner;
import com.manaul.highschool.loader.AsyncImageLoader;
import com.manaul.highschool.utils.Constant;
import com.manaul.highschool.utils.SPrefUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;

@SuppressLint("HandlerLeak")
public class MainActivity extends Activity {

    private Context mContext;
    private int count = 5;

    private ImageView welcome_kp_image;
    private TextView welcome_kp_text;

    private boolean isClick = false;
	
    List<Banner> startBannerList = new ArrayList<>();

	@SuppressWarnings("unused")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.welcome);
		// bmob 初始化
		Bmob.initialize(mContext, Constant.BMOB_APKID);
		// 数据库初始化
		initDataDb();
		// 初始化控件对象
        welcome_kp_image = (ImageView) findViewById(R.id.welcome_kp_image);
        welcome_kp_text = (TextView) findViewById(R.id.welcome_kp_text);
        welcome_kp_text.setText(count+"");

        // 启动 开屏广告
        startSpreadUrl(mContext);
        // 网络获取图片
		// ----------------------------------------------
//        adUrl = "http://img06.tooopen.com/images/20170511/tooopen_sy_209123771256.jpg";

        handler.sendEmptyMessageDelayed(0, 1000);
        welcome_kp_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
				String spreadUrl = SPrefUtil.getInstance(mContext).getStringByKey("spreadUrl");
				if(spreadUrl != null){
					WebView webView = new WebView(mContext);
					webView.loadUrl(spreadUrl);
				}
			}
        });

	}

	/**
	 * 启动开屏广告
	 * @param mContext
	 */
	private void startSpreadUrl(Context mContext) {
		String spreadImageUrl = SPrefUtil.getInstance(mContext).getStringByKey("spreadImageUrl");

		if(spreadImageUrl != null){
			AsyncImageLoader loader = new AsyncImageLoader(mContext);
			loader.setCache2File(true); // false
			loader.setCachedDir(mContext.getCacheDir().getAbsolutePath());
			loader.downloadImage(spreadImageUrl, true/* false */, new AsyncImageLoader.ImageCallback() {
				@Override
				public void onImageLoaded(Bitmap bitmap, String imageUrl) {
					if (bitmap != null) {
						welcome_kp_image.setImageBitmap(bitmap);
					}
				}
			});
		}
	}

	/**
	 * 初始化数据库
	 */
	private void initDataDb() {
		// 复制数据库
		String versionName = SPrefUtil.getInstance(mContext).getStringByKey("versionName");
		if (Constant.IS_TEST || versionName == null || !Constant.getVersionName(mContext).equals(versionName)) {
			boolean copyData = SPrefUtil.getInstance(mContext).copyFileFromAssets(mContext);
			if (copyData) {
				SPrefUtil.getInstance(mContext).setStringByKey("versionName", Constant.getVersionName(mContext));
				SPrefUtil.getInstance(mContext).setIntByKey("versionCode", Constant.getVersionCode(mContext));
			}
		}
	}

	private int getCount() {
		count--;
		if (count == 0) {
			Intent intent = null;
			// if (isFirst) {
			// intent = new Intent(this, WelcomeActivity.class);
			// } else {
			intent = new Intent(this, HomeActivity.class);
			// }
			startActivity(intent);
			finish();
		}
		return count;
	}

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 0) {
				int count = getCount();
				handler.sendEmptyMessageDelayed(0, 1000);
				if (count > 0) {
					welcome_kp_text.setText(count+"");
				}
			}
		}

	};
	
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

}
