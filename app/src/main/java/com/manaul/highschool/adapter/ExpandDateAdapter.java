package com.manaul.highschool.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.manaul.highschool.bean.ItemContent;
import com.manaul.highschool.bean.Navigate;
import com.manaul.highschool.main.R;

import java.util.ArrayList;
import java.util.List;


@SuppressLint("InflateParams")
public class ExpandDateAdapter extends BaseExpandableListAdapter {

	private Context context;// 该数据成员必不可�?
	// 大小组的数据源可以自定义其数据类型，并不�?定就是我这样
	private List<Navigate> listDate = new ArrayList<Navigate>();

	public ExpandDateAdapter(Context context, List<Navigate> listDate) {
		super();
		this.context = context;
		this.listDate = listDate;
	}

	// 得到大组成员的view
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.member_listview, null);
		}
		if (listDate != null && listDate.size() > 0) { 
			TextView title = (TextView) view.findViewById(R.id.content_001);
			// 设置大组成员名称
			title.setText((groupPosition + 1) + "、"
					+ getGroup(groupPosition).toString() + "("
					+ listDate.get(groupPosition).getItemContents().size()+"篇)");
//			if (isExpanded) {
//				// 大组展开背景
				view.setBackgroundResource(R.color.item_bg);
			// } else {
			// // 大组合并背景
			// view.setBackgroundResource(R.color.body_bai);
			// }
		}
		return view;
	}

	// 得到大组成员的id
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	// 得到大组成员名称
	public Object getGroup(int groupPosition) {
		return listDate.get(groupPosition).getName();
	}

	// 得到大组成员总数
	public int getGroupCount() {
		return listDate.size();

	}

	// 得到小组成员的view
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			// member_childitem.xml文件定义小组列表的显示风�?
			view = inflater.inflate(R.layout.member_childitem, null);
		}
		List<ItemContent> listChildren = listDate.get(groupPosition).getItemContents();
		
		if (listChildren != null && listChildren.size() > 0) {
			
			ItemContent itemContent = listChildren.get(childPosition);
			TextView title = (TextView) view.findViewById(R.id.child_item_title);
			TextView intro = (TextView) view.findViewById(R.id.child_item_intro);
			TextView vip = (TextView) view.findViewById(R.id.child_item_title_vip);
			title.setText(itemContent.getTitle());
			intro.setText(itemContent.getIntro());
			if(itemContent.getPrice() > 0)
				vip.setVisibility(View.VISIBLE);
			vip.setText("VIP");


		}
		return view;
	}

	// 得到小组成员id
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	// 得到小组成员的名�?
	public Object getChild(int groupPosition, int childPosition) {
		return listDate.get(groupPosition).getItemContents()
				.get(childPosition).getTitle();
	}

	// 得到小组成员的数�?
	public int getChildrenCount(int groupPosition) {
		return listDate.get(groupPosition).getItemContents().size();
	}

	public boolean hasStableIds() {
		return true;
	}

	// 得到小组成员是否被�?�择
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
}
