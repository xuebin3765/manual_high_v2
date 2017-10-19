package com.manaul.highschool.loader;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;

@SuppressWarnings("deprecation")
public class UrlDrawable extends BitmapDrawable {
	public Bitmap bitmap;

	@Override
	public void draw(Canvas canvas) {
		if (bitmap != null) {
			canvas.drawBitmap(bitmap, 0, 0, getPaint());
		}
	}
}