package com.manaul.highschool.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.manaul.highschool.bottomnavigation.BottomNavigationBar;
import com.manaul.highschool.bottomnavigation.BottomNavigationItem;
import com.manaul.highschool.fragment.FragmentHome;
import com.manaul.highschool.fragment.FragmentInfo;
import com.manaul.highschool.adapter.FragmentAdapter;
import com.manaul.highschool.fragment.FragmentNote;
import com.manaul.highschool.fragment.FragmentUser;

import java.util.ArrayList;
import java.util.List;

/**
 * 带底部菜单的首页
 * Created by Administrator on 2018/3/1.
 */

public class HomeActivity extends BaseActivity {

    private ViewPager viewpager;
    private BottomNavigationBar bottom_navigation_bar;
    //数据源的集合
    private List<Fragment> list = new ArrayList<Fragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_v2);
        initView(); //实例化数据
        initData();   //添加数据
        initEvent();    //设置点击事件
    }

    private void initView() {
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        bottom_navigation_bar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
    }

    private void initData() {
        //给ViewPager设置数据
        list.add(new FragmentHome());
        list.add(new FragmentInfo());
        list.add(new FragmentNote());
        list.add(new FragmentUser());
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), list);
        viewpager.setAdapter(adapter);

        //要先设计模式后再添加图标！
        //设置按钮模式  MODE_FIXED表示固定   MODE_SHIFTING表示转移
        bottom_navigation_bar.setMode(BottomNavigationBar.MODE_FIXED);

        //设置背景风格
        // BACKGROUND_STYLE_STATIC表示静态的  ，
        //BACKGROUND_STYLE_RIPPLE表示涟漪的，也就是可以变化的 ，跟随setActiveColorResource里面的颜色变化
        bottom_navigation_bar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);

        bottom_navigation_bar.setActiveColor(R.color.body_bai)
                .setInActiveColor(R.color.title_top)
                .setBarBackgroundColor(R.color.orange);
        //添加并设置图标、图标的颜色和文字
        bottom_navigation_bar
                .addItem(new BottomNavigationItem(R.drawable.ic_home_black_24dp, "首页"))
                .addItem(new BottomNavigationItem(R.drawable.ic_book_black_24dp, "资讯"))
                .addItem(new BottomNavigationItem(R.drawable.ic_layers_black_24dp, "备考"))
                .addItem(new BottomNavigationItem(R.drawable.ic_person_black_24dp, "我的"))
                .initialise();

        //设置选中第一个添加的按钮
        bottom_navigation_bar.selectTab(0, false);
        //显示ViewPager中添加的第一个页面
        viewpager.setCurrentItem(0);


    }

    //联动显示
    private void initEvent() {
        //监听ViewPager的滑动
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
            }

            @Override
            public void onPageSelected(int i) {
                //页面滑动后，按钮改变
                bottom_navigation_bar.selectTab(i, false);
            }

            @Override
            public void onPageScrollStateChanged(int i) {
            }
        });

        //监听下面按钮的点击
        bottom_navigation_bar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                viewpager.setCurrentItem(position);//改变ViewPager的显示
            }

            @Override
            public void onTabUnselected(int position) {
            }

            @Override
            public void onTabReselected(int position) {
            }
        });

    }
}
