package com.manaul.highschool.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

// ����ҳ��ʹ�õ�pageview����??
public class ImagePagerAdapter extends PagerAdapter {

	private List<ImageView> views = new ArrayList<ImageView>();
	public ImagePagerAdapter(List<ImageView> imageViewList) {
		this.views = imageViewList;
	}
	
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		
		return arg0 == arg1;
	}
	
	@Override
	public int getCount() {
		return views.size();
	}

	@Override
	public void destroyItem(View container, int position, Object object) {
		((ViewPager)container).removeView(views.get(position));
	}

	@Override
	public Object instantiateItem(View container, int position) {
		((ViewPager)container).addView(views.get(position));
		return views.get(position);
	}
}
