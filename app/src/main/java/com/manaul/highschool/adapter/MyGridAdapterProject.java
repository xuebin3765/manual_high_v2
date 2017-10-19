package com.manaul.highschool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.manaul.highschool.bean.Project;
import com.manaul.highschool.main.R;

import java.util.List;

/**
 * @Description:gridview的Adapter
 * @author http://blog.csdn.net/finddreams
 */
public class MyGridAdapterProject extends BaseAdapter {
	private Context mContext;

	private List<Project> projectList = null;

	public MyGridAdapterProject(Context mContext , List<Project> projects) {
		super();
		this.mContext = mContext;
		this.projectList = projects;
	}

	@Override
	public int getCount() {
		return projectList.size();
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
		tv.setText(projectList.get(position).getName());
		return convertView;
	}

}
