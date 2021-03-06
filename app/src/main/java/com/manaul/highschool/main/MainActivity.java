package com.manaul.highschool.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.manaul.highschool.loader.AsyncImageLoader;
import com.manaul.highschool.loader.UrlDrawable;
import com.manaul.highschool.utils.Constant;
import com.manaul.highschool.utils.SharedConfig;
import com.manaul.highschool.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import c.b.BP;
import cn.bmob.v3.Bmob;

@SuppressLint("HandlerLeak")
public class MainActivity extends Activity {
	
	private String versionName;
    private Context mContext;
    private SharedPreferences shared;
    private int count = 5;

    private ImageView welcome_kp_image;
    private TextView welcome_kp_text;
    
    String adUrl = null;
	
	@SuppressWarnings("unused")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.welcome);

        shared = new SharedConfig(mContext).getConfig();

        // 复制数据库
        versionName = shared.getString("versionName", null);
        if (Constant.IS_TEST || versionName == null || !Constant.getVersionName(mContext).equals(versionName)) {
            boolean copyData = SharedConfig.copyFileFromAssets(mContext);
            if (copyData) {
                SharedPreferences.Editor editor = shared.edit();
                editor.putString("versionName", Constant.getVersionName(mContext));
                editor.putInt("versionCode", Constant.getVersionCode(mContext));
                editor.commit();
            }
        }
        

		Bmob.initialize(mContext, Constant.BMOB_APKID);
		BP.init(Constant.BMOB_APKID); // 支付初始化

		adUrl = shared.getString("ad", null);
		
		// 初始化控件对象
        welcome_kp_image = (ImageView) findViewById(R.id.welcome_kp_image);
        welcome_kp_text = (TextView) findViewById(R.id.welcome_kp_text);
        welcome_kp_text.setText(count+"");
        
        // 网络获取图片
		// ----------------------------------------------
//        adUrl = "http://img06.tooopen.com/images/20170511/tooopen_sy_209123771256.jpg";
        if(adUrl != null){
        	final UrlDrawable urlDrawable = new UrlDrawable();
    		AsyncImageLoader loader = new AsyncImageLoader(mContext);
    		loader.setCache2File(true); // false
    		loader.setCachedDir(mContext.getCacheDir().getAbsolutePath());
    		loader.downloadImage(adUrl, true/* false */, new AsyncImageLoader.ImageCallback() {
    			@Override
    			public void onImageLoaded(Bitmap bitmap, String imageUrl) {
				if (bitmap != null) {
					welcome_kp_image.setImageBitmap(bitmap);
				}
    			}
    		});
        }
        
        handler.sendEmptyMessageDelayed(0, 1000);
        welcome_kp_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ToastUtils.showToastShort(mContext , "点击的广告");
            }
        });

	}

	private int getCount() {
		count--;
		if (count == 0) {
			Intent intent = null;
			// if (isFirst) {
			// intent = new Intent(this, WelcomeActivity.class);
			// } else {
			intent = new Intent(this, NewHomeActivity.class);
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
