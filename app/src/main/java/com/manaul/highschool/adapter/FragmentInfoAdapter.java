package com.manaul.highschool.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * 资讯下拉加载，上拉刷新资讯
 * Created by Administrator on 2018/3/2.
 */

public class FragmentInfoAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> list;

    public FragmentInfoAdapter(Context context, ArrayList<String> list) {
        this.context = context;
        this.list = list;
    }

    public void addLast(ArrayList<String> list) {
        this.list.addAll(list);
    }

    @Override
    public int getCount() {
        if(list==null){
            return 0;
        }
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = new TextView(context);
        textView.setText(list.get(position));
        return textView;
    }
}
