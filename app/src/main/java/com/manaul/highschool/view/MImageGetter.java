package com.manaul.highschool.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.Html.ImageGetter;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.widget.TextView;

import com.manaul.highschool.loader.AsyncImageLoader;
import com.manaul.highschool.loader.UrlDrawable;
import com.manaul.highschool.main.R;
import com.manaul.highschool.utils.Constant;

import java.io.InputStream;

public class MImageGetter implements ImageGetter {
	Context mContext;
	TextView container;
	int width;

	public MImageGetter(TextView text, Context context) {
		this.mContext = context;
		this.container = text;
		width = context.getResources().getDisplayMetrics().widthPixels;
	}

	public Drawable getDrawable(String source) {

		// ------------------从本地资源获取----------------------
		Drawable drawable = null;
		InputStream is = null;
		try {
			if(source.startsWith("/")){
				source = source.substring(1);
			}
//			source = source.substring(source.lastIndexOf(Constant.APP_TYPE));
			is = mContext.getResources().getAssets().open(source);

			TypedValue typedValue = new TypedValue();
			typedValue.density = TypedValue.DENSITY_DEFAULT;
			drawable = Drawable.createFromResourceStream(null, typedValue, is, "src");
			DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
			int dwidth = dm.widthPixels-10;//padding left + padding right  屏幕宽度
			float dheight = (float)drawable.getIntrinsicHeight()*(float)dwidth/(float)drawable.getIntrinsicWidth();
			int dh = (int)(dheight+0.5);

			int imageWidth = drawable.getIntrinsicWidth();
			int wid = imageWidth >= dwidth?imageWidth:dwidth; // 图片宽度大于屏幕宽度，用图片宽度�?

			int hei = dh;

			drawable.setBounds(0, 0, wid, hei);
			return drawable;
		} catch (Exception e) {
			// ----------------------------------------------
			final UrlDrawable urlDrawable = new UrlDrawable();
			// 网络图片地址
			source = Constant.HOST_IMG + source;
			AsyncImageLoader loader = new AsyncImageLoader(mContext);
			loader.setCache2File(true); // false
			loader.setCachedDir(mContext.getCacheDir().getAbsolutePath());
			loader.downloadImage(source, true/* false */, new AsyncImageLoader.ImageCallback() {
				@Override
				public void onImageLoaded(Bitmap bitmap, String imageUrl) {
					if (bitmap == null) {
//					// 下载失败，设置默认图�??
						Resources res=mContext.getResources();
						bitmap=BitmapFactory.decodeResource(res, R.drawable.error);
					}

					urlDrawable.bitmap = Bitmap.createScaledBitmap(bitmap, width, bitmap.getHeight(), true); ;
					urlDrawable.setBounds(0, 0, width, bitmap.getHeight());
					container.invalidate();
					container.setText(container.getText());
				}
			});
			return urlDrawable;
		}

//		// ------------------从本地资源获取----------------------
//		Drawable drawable = null;
//	    InputStream is = null;
//		try {
//			if(source.startsWith("/")){
//				source = source.substring(1);
//			}
////			source = source.substring(source.lastIndexOf(Constant.APP_TYPE));
//			is = mContext.getResources().getAssets().open(source);
//			
//            TypedValue typedValue = new TypedValue();
//            typedValue.density = TypedValue.DENSITY_DEFAULT;
//            drawable = Drawable.createFromResourceStream(null, typedValue, is, "src");
//            DisplayMetrics dm = mContext.getResources().getDisplayMetrics();  
//    		int dwidth = dm.widthPixels-10;//padding left + padding right  屏幕宽度
//    		float dheight = (float)drawable.getIntrinsicHeight()*(float)dwidth/(float)drawable.getIntrinsicWidth();
//    		int dh = (int)(dheight+0.5);
//    		
//    		int imageWidth = drawable.getIntrinsicWidth();
//    		int wid = imageWidth >= dwidth?imageWidth:dwidth; // 图片宽度大于屏幕宽度，用图片宽度�?
//    		
//            int hei = dh;
//            
//            drawable.setBounds(0, 0, wid, hei);
//            return drawable;
//        } catch (Exception e) {
//        	System.out.println(e);
//            return null;
//        }

//		// ----------------------------------------------
//		final UrlDrawable urlDrawable = new UrlDrawable();
//		// 网络图片地址
//		source = Constant.HOST_IMG + source;
//		AsyncImageLoader loader = new AsyncImageLoader(mContext);
//		loader.setCache2File(true); // false
//		loader.setCachedDir(mContext.getCacheDir().getAbsolutePath());
//		loader.downloadImage(source, true/* false */, new AsyncImageLoader.ImageCallback() {
//			@Override
//			public void onImageLoaded(Bitmap bitmap, String imageUrl) {
//				if (bitmap == null) {
////					// 下载失败，设置默认图�??
//					Resources res=mContext.getResources();
//					bitmap=BitmapFactory.decodeResource(res, R.drawable.error);
//				}
//
//				urlDrawable.bitmap = Bitmap.createScaledBitmap(bitmap, width, bitmap.getHeight(), true); ;
//				urlDrawable.setBounds(0, 0, width, bitmap.getHeight());
//				container.invalidate();
//				container.setText(container.getText());
//			}
//		});
//		return urlDrawable;
	}

}
