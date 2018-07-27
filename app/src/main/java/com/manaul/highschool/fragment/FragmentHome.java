package com.manaul.highschool.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.manaul.highschool.adapter.GridViewAdapter;
import com.manaul.highschool.adapter.ViewPagerAdapter;
import com.manaul.highschool.bean.Subject;
import com.manaul.highschool.main.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页
 */
public class FragmentHome extends Fragment {

    private String[] titles = {"美食", "电影", "酒店住宿", "休闲娱乐", "甜品饮品",
            "水上乐园", "汽车服务", "美发", "丽人", "景点",
            "足疗按摩", "运动健身", "健身", "超市", "买菜",
            "今日新单", "外卖", "自助餐", "KTV", "机票/火车票",
            "周边游", "小吃快餐", "面膜", "美甲美睫", "火锅",
            "生日蛋糕", "母婴亲子", "生活服务", "婚纱摄影", "学习培训",
            "家装", "结婚"};

    private List<Subject> subjectList;

    private LinearLayout ll_dot;

    //每页展示的主题个数
    private final int pageSize = 10;

    //当前页索引
    private int currentIndex;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_center_home , container , false);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        ll_dot = (LinearLayout) view.findViewById(R.id.ll_dot);
        subjectList = new ArrayList<>();
        for (String title : titles) {
            subjectList.add(new Subject(title, R.drawable.setting_normal));
        }
        //需要的页面数
        int pageCount = (int) Math.ceil(subjectList.size() * 1.0 / pageSize);
        List<View> viewList = new ArrayList<>();
        for (int i = 0; i < pageCount; i++) {
            GridView gridView = (GridView) inflater.inflate(R.layout.layout_grid_view , viewPager , false);
//            GridView gridView = (GridView) getLayoutInflater().inflate(R.layout.layout_grid_view, viewPager, false);
            gridView.setAdapter(new GridViewAdapter(getContext(), subjectList, i, pageSize));
            viewList.add(gridView);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int pos = position + currentIndex * pageSize;
                    Toast.makeText(getContext(), subjectList.get(pos).getName(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        viewPager.setAdapter(new ViewPagerAdapter(viewList));
        for (int i = 0; i < pageCount; i++) {
            ll_dot.addView(inflater.inflate(R.layout.view_dot, null));
        }
        //使第一个小圆点呈选中状态
        ll_dot.getChildAt(0).findViewById(R.id.v_dot).setBackgroundResource(R.drawable.dot_selected);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageSelected(int position) {
                ll_dot.getChildAt(currentIndex).findViewById(R.id.v_dot).setBackgroundResource(R.drawable.dot_normal);
                ll_dot.getChildAt(position).findViewById(R.id.v_dot).setBackgroundResource(R.drawable.dot_selected);
                currentIndex = position;
            }

            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            public void onPageScrollStateChanged(int arg0) {
            }
        });
        return view;
    }
}
