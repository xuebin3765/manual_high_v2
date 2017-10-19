package com.manaul.highschool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.manaul.highschool.bean.Navigate;
import com.manaul.highschool.main.R;

import java.util.List;

/**
 * @Description:gridview的Adapter
 * @author http://blog.csdn.net/finddreams
 */
public class MyGridAdapter extends BaseAdapter {
	private Context mContext;

	private List<Navigate> navigateList = null;

	public MyGridAdapter(Context mContext , List<Navigate> navigateList) {
		super();
		this.mContext = mContext;
		this.navigateList = navigateList;
	}

	@Override
	public int getCount() {
		return navigateList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.layout_gridview_item, parent, false);
		}
		TextView tv = BaseViewHolder.get(convertView, R.id.gird_tv_item);
		tv.setText(navigateList.get(position).getName());
		return convertView;
	}

}
