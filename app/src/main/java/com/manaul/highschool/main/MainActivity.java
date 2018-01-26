package com.manaul.highschool.main;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.manaul.highschool.bean.Banner;
import com.manaul.highschool.loader.AsyncImageLoader;
import com.manaul.highschool.loader.UrlDrawable;
import com.manaul.highschool.utils.Constant;
import com.manaul.highschool.utils.DebugUtil;
import com.manaul.highschool.utils.SharedPreferenceUtil;
import com.manaul.highschool.utils.SystemUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import c.b.BP;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

@SuppressLint("HandlerLeak")
public class MainActivity extends Activity {
	
	private String versionName;
    private Context mContext;
    private int count = 5;

    private ImageView welcome_kp_image;
    private TextView welcome_kp_text;
    List<Banner> startBannerList = new ArrayList<>();

	@SuppressWarnings("unused")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.welcome);
		versionName = SharedPreferenceUtil.getInstance(mContext).getStringByKey("versionName");
        // 复制数据库
        if (SystemUtil.APP_TEST || versionName == null || !SystemUtil.getVersionName(mContext).equals(versionName)) {
            boolean copyData = SharedPreferenceUtil.getInstance(mContext).copyFileFromAssets(mContext);
            if (copyData) {
				SharedPreferenceUtil.getInstance(mContext).setStringByKey("versionName", SystemUtil.getVersionName(mContext));
				SharedPreferenceUtil.getInstance(mContext).setIntByKey("versionCode", SystemUtil.getVersionCode(mContext));
            }
        }
        

		Bmob.initialize(mContext, Constant.BMOB_APKID);
		BP.init(Constant.BMOB_APKID); // 支付初始化

		// 初始化数据
		final String startBanners = SharedPreferenceUtil.getInstance(mContext).getStringByKey("startBanners");
		DebugUtil.d(startBanners);
		if(startBannerList.size() <= 0){
			if(startBanners != null){
				// 从 本地缓存中获取
				JSONArray jsonArray = JSON.parseArray(startBanners);
				if(jsonArray != null && jsonArray.size() > 0){
					for(int i = 0 ; i < jsonArray.size() ; i++ )
						startBannerList.add(JSON.toJavaObject(jsonArray.getJSONObject(i), Banner.class));

				}
			}else {
				cacheBannerHomePic();
			}
		}


		// 初始化控件对象
		welcome_kp_image = (ImageView) findViewById(R.id.welcome_kp_image);
		welcome_kp_text = (TextView) findViewById(R.id.welcome_kp_text);
		welcome_kp_text.setText(count+"");

		// 网络获取图片
		// ----------------------------------------------
//        adUrl = "http://img06.tooopen.com/images/20170511/tooopen_sy_209123771256.jpg";
		if(startBannerList != null && startBannerList.size() > 0){
			if(startBannerList.size() == 1 && startBannerList.get(0).getImageUtl() != null){
				final UrlDrawable urlDrawable = new UrlDrawable();
				AsyncImageLoader loader = new AsyncImageLoader(mContext);
				loader.setCache2File(true); // false
				loader.setCachedDir(mContext.getCacheDir().getAbsolutePath());
				loader.downloadImage(startBannerList.get(0).getImageUtl(), true/* false */, new AsyncImageLoader.ImageCallback() {
					@Override
					public void onImageLoaded(Bitmap bitmap, String imageUrl) {
						if (bitmap != null) {
							welcome_kp_image.setImageBitmap(bitmap);
						}
					}
				});
			}
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

	/**
	 * 缓存banner图
	 */
	public void cacheBannerHomePic(){
		BmobQuery<Banner> query = new BmobQuery<Banner>();
		query.order("-sort");
		query.addWhereEqualTo("appId" , SystemUtil.APP_ID);
		query.addWhereEqualTo("type" , Constant.BANNER_TYPE_START);
		query.findObjects(new FindListener<Banner>() {
			@Override
			public void done(List<Banner> object, BmobException e) {
				if(e==null){
					String jsonObject = JSONArray.toJSONString(object);
					SharedPreferenceUtil.getInstance(mContext).setStringByKey("startBanners" , jsonObject);
					startBannerList = object;
				}else{
					DebugUtil.d("cacheBannerPic 失败："+e.getMessage()+","+e.getErrorCode());
				}
			}
		});
	}

}
