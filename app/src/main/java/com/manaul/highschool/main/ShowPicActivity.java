package com.manaul.highschool.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.manaul.highschool.adapter.ImagePagerAdapter;
import com.manaul.highschool.loader.AsyncImageLoader;
import com.manaul.highschool.utils.Constant;
import com.manaul.highschool.utils.SharedConfig;
import com.manaul.highschool.view.ZoomImageView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

@SuppressLint("InlinedApi")
public class ShowPicActivity extends AppCompatActivity {

	int dwidth;
	int dheight;
	private Context mContext;
	private ViewPager mViewPager;
	private List<ImageView> imageViewList = new ArrayList<ImageView>();
	private int presentImage = -1;

	private TextView tvTitle ;
	
	private SharedPreferences shareConfig;
	
	@SuppressWarnings("deprecation")
	@SuppressLint("InlinedApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_show_pic);
		mContext = this;
		shareConfig = new SharedConfig(mContext).getConfig();
		
		
		ArrayList<String> imgUrlArray = new ArrayList<String>();
		String images = shareConfig.getString("images", null);
		
		images.substring(0, images.length() - 2);
		String urlImg[] = images.split("&&");
		if(urlImg != null && urlImg.length > 0){
			for (String string : urlImg) {
				if(string != null && string.length() > 0){
					imgUrlArray.add(string);
				}
			}
		}
		
		// 获得图片路径，处理路�??
		Bundle bundle = getIntent().getExtras();
		String imgUrl = bundle.getString("imgUrl");
		// 当前的位�??
		presentImage = imgUrlArray.indexOf("/"+imgUrl);
		// 顶部返回按钮
		ActionBar mActionbar = getSupportActionBar();
		if (mActionbar != null) {
			mActionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
			mActionbar.setDisplayShowCustomEnabled(true);
			mActionbar.setCustomView(R.layout.title_to_center);
			tvTitle = (TextView) mActionbar.getCustomView().findViewById(R.id.title_to_center_text);
			tvTitle.setText("图片预览");
			ImageView iv_tbb_back = (ImageView) mActionbar.getCustomView().findViewById(R.id.iv_tbb_back);
			ImageView iv_tbb_user = (ImageView) mActionbar.getCustomView().findViewById(R.id.iv_tbb_user);

			iv_tbb_back.setVisibility(View.VISIBLE);
			iv_tbb_user.setVisibility(View.VISIBLE);
			iv_tbb_back.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});
		}
		// 顶部标题设置
		tvTitle.setText(" \t" + (presentImage + 1) + " / " + imgUrlArray.size());
		
		if(imgUrlArray != null && imgUrlArray.size() > 0){
			for (String url : imgUrlArray) {
//				if(url.startsWith("/")){
//					url = url.substring(1);
//				}
//				Bitmap bitmap = DataUtil.getImageFromAssetsFile(mContext, url);
//				if(bitmap != null){
//					ZoomImageView imageView = new ZoomImageView(mContext);
//					imageView.setImageBitmap(bitmap);
//					imageViewList.add(imageView);
//				}
				
				// --------------------------- 
				//网络图片地址  
				url = Constant.HOST_IMG + url.substring(1);
		        AsyncImageLoader loader = new AsyncImageLoader(mContext);
		        loader.setCache2File(true); //false  
		        loader.setCachedDir(mContext.getCacheDir().getAbsolutePath());  
		        //下载图片，第二个参数是否缓存至内存中
		        loader.downloadImage(url, true/*false*/, new AsyncImageLoader.ImageCallback() {  
		            @Override  
		            public void onImageLoaded(Bitmap bitmap, String imageUrl) {  
		            	ZoomImageView imageView = new ZoomImageView(mContext);
		            	if (bitmap != null) {
							imageView.setImageBitmap(bitmap);
						}else {
							// 下载失败，设置默认图片
							Resources res=mContext.getResources();
							bitmap= BitmapFactory.decodeResource(res, R.drawable.error);
							imageView.setImageBitmap(bitmap);
						}
						imageViewList.add(imageView);
		            }  
		        });
			}
		}
		
		if(imageViewList == null || imageViewList.size() == 0){
			finish();
		}
		mViewPager = (ViewPager)findViewById(R.id.id_viewpager);
		PagerAdapter pagerAdapter = new ImagePagerAdapter(imageViewList);
		mViewPager.setAdapter(pagerAdapter);
		
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				tvTitle.setText(" \t" + (arg0 + 1) + " / " + imageViewList.size());
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		mViewPager.setCurrentItem(presentImage);
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
