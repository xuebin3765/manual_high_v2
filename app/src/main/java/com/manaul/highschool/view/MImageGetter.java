package com.manaul.highschool.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.Html.ImageGetter;
import android.widget.TextView;

import com.manaul.highschool.loader.AsyncImageLoader;
import com.manaul.highschool.loader.UrlDrawable;
import com.manaul.highschool.main.R;
import com.manaul.highschool.utils.Constant;

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
		final UrlDrawable urlDrawable = new UrlDrawable();
		// 网络图片地址
		source = Constant.HOST_IMG +"/image/"+ source;
		AsyncImageLoader loader = new AsyncImageLoader(mContext);
		loader.setCache2File(true);
		loader.setCachedDir(mContext.getCacheDir().getAbsolutePath());
		loader.downloadImage(source, true, new AsyncImageLoader.ImageCallback() {
			@Override
			public void onImageLoaded(Bitmap bitmap, String imageUrl) {
				if (bitmap == null) {
					// 下载失败，设置默认图图片
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
}
