package com.manaul.highschool.fragment;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.manaul.highschool.adapter.CyclePagerAdapter;
import com.manaul.highschool.adapter.MyGridAdapterProject;
import com.manaul.highschool.bean.ADInfo;
import com.manaul.highschool.bean.AdPicture;
import com.manaul.highschool.bean.DataOther;
import com.manaul.highschool.bean.Project;
import com.manaul.highschool.dao.NavigateDao;
import com.manaul.highschool.dao.SQLiteHelper;
import com.manaul.highschool.main.ActivityItem;
import com.manaul.highschool.main.R;
import com.manaul.highschool.utils.DebugUtil;
import com.manaul.highschool.utils.SPrefUtil;
import com.manaul.highschool.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * 首页
 */
public class FragmentNote extends Fragment {

    private Context mContext;
    private SQLiteHelper sqliteHelper;
    private SQLiteDatabase db;
    private NavigateDao navigateDao;
    private ViewPager viewPager;
    private TextView tv_intro;
    private LinearLayout dot_layout;
    private LinearLayout fg3_lin_bk;

    private List<ADInfo> adInfoList = new ArrayList<>();

    private TextView zfbRedTitle;
    private TextView zfbRedText;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_new_home , container , false);
        mContext = getContext();
        initView(view);

        // 初始化页面
        viewPager = (ViewPager)view.findViewById(R.id.fg3_viewpager);
        tv_intro = (TextView)view.findViewById(R.id.fg3_tv_intro);
        dot_layout = (LinearLayout)view.findViewById(R.id.fg3_dot_layout);
//        initData();
        // 初始化数据
        final String adInfo = SPrefUtil.getInstance(mContext).getStringByKey("adInfo");

        if(adInfoList.size() <= 0){
            if(adInfo != null){
                // 从 本地缓存中获取
                JSONArray jsonArray = JSON.parseArray(adInfo);
                if(jsonArray != null && jsonArray.size() > 0){
                    for(int i = 0 ; i < jsonArray.size() ; i++ ){
                        adInfoList.add(JSON.toJavaObject(jsonArray.getJSONObject(i) , ADInfo.class));
                        String jsonObject = JSONArray.toJSONString(adInfoList);
                        Log.i("editor adInfo = " , jsonObject);
                    }
                }
            }else {
                BmobQuery<ADInfo> query = new BmobQuery<ADInfo>();
                //查询的数据
                query.order("-sort");
                //执行查询方法
                query.findObjects(new FindListener<ADInfo>() {
                    @Override
                    public void done(List<ADInfo> object, BmobException e) {
                        if(e==null){
                            String jsonObject = JSONArray.toJSONString(object);
                            adInfoList = object;
                            DebugUtil.d("bmob adInfo = " +jsonObject);
                            SPrefUtil.getInstance(mContext).setStringByKey("adInfo" , jsonObject);
                        }else{
                            DebugUtil.d("bmob  失败："+e.getMessage()+","+e.getErrorCode());
                        }
                    }
                });
            }
        }
        if(adInfoList != null && adInfoList.size() > 0){
            // 初始化文字下方的圆点
            initDots();
            viewPager.setAdapter(new CyclePagerAdapter(mContext , adInfoList));
            int centerValue = Integer.MAX_VALUE/2;
            int value = centerValue % adInfoList.size();
            //设置viewPager的第一页为最大整数的中间数，实现伪无限循环
            viewPager.setCurrentItem(centerValue-value);
            updateIntroAndDot();//更新数据与圆点
            handler.sendEmptyMessageDelayed(0,6000);
            initListener();
        }

        // 加载工具

        sqliteHelper = new SQLiteHelper(mContext);
        db = sqliteHelper.getWritableDatabase();
        navigateDao = new NavigateDao(db);

        final List<Project> projectList = navigateDao.findProjectSimple();
        View girdItemLYView = inflater.inflate(R.layout.layout_gird_item, null);
        GridView gridView = (GridView)girdItemLYView.findViewById(R.id.layout_gird_item_girdview);
        gridView.setNumColumns(3);
        gridView.setAdapter(new MyGridAdapterProject(mContext, projectList));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, ActivityItem.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("project", projectList.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        fg3_lin_bk.addView(girdItemLYView); // 添加到布局文件



        return view;
    }

    private void initView(View view) {

        // 加载备考模块
        fg3_lin_bk = (LinearLayout) view.findViewById(R.id.fg3_linear_bk);

        // 加载支付宝领红包文字
        zfbRedTitle = (TextView) view.findViewById(R.id.zfb_red_title);
        zfbRedText = (TextView) view.findViewById(R.id.zfb_red_text);

        String zfbText = SPrefUtil.getInstance(mContext).getStringByKey("zfbRedText");
        String homeTitle = SPrefUtil.getInstance(mContext).getStringByKey("homeTitle");
        if(zfbText != null && homeTitle != null){
            zfbRedTitle.setVisibility(View.VISIBLE);
            zfbRedText.setVisibility(View.VISIBLE);
            zfbRedText.setText(zfbText);
            zfbRedTitle.setText(homeTitle);
            zfbRedText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardManager cm = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
                    // 将文本内容放到系统剪贴板里。
                    cm.setText(zfbRedText.getText());
                    ToastUtils.showToastShort(mContext , "复制成功，去支付宝领取");
                }
            });
        }

    }

    public void writeSysConstants() {
        // 加载轮播图
        initBanners();
        // 加载常量数据
        initConstant();
        BmobQuery<AdPicture> bmobQuery = new BmobQuery<AdPicture>();
        bmobQuery.findObjects(new FindListener<AdPicture>() {
            @Override
            public void done(List<AdPicture> object, BmobException e) {
                if(e==null && object != null && object.size() > 0){
                    DebugUtil.d(object.toString());
                    AdPicture ad = object.get(0);
                    SPrefUtil.getInstance(mContext).setStringByKey("spreadUrl" , ad.getUrl());
                    SPrefUtil.getInstance(mContext).setStringByKey("spreadImageUrl" , ad.getImageUtl());
                }
            }
        });

    }

    private void initConstant() {
        BmobQuery<DataOther> bmobQuery = new BmobQuery<DataOther>();
        bmobQuery.findObjects(new FindListener<DataOther>() {
            @Override
            public void done(List<DataOther> object, BmobException e) {
                if(e==null && object != null && object.size() > 0){
                    DebugUtil.d(object.toString());
                    DataOther dataOther = object.get(0);
                    SPrefUtil.getInstance(mContext).setStringByKey("homeTitle" , dataOther.getHomeTitle());
                    SPrefUtil.getInstance(mContext).setStringByKey("zfbRedText" , dataOther.getZfbRedText());
                    SPrefUtil.getInstance(mContext).setStringByKey("zfbRedUrl" , dataOther.getZfbRedUrl());
                }
            }
        });
    }

    private void initBanners() {
        BmobQuery<ADInfo> query = new BmobQuery<ADInfo>();
        //查询的数据
        query.order("-sort");
        //执行查询方法
        query.findObjects(new FindListener<ADInfo>() {
            @Override
            public void done(List<ADInfo> object, BmobException e) {
                if(e==null){
                    String jsonObject = JSONArray.toJSONString(object);
                    adInfoList = object;
                    SPrefUtil.getInstance(mContext).setStringByKey("adInfo" , jsonObject);
                }
            }
        });
    }

    //初始化监听器，当页面改变时，更新其相应数据
    private void initListener() {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                updateIntroAndDot();
            }
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            public void onPageScrollStateChanged(int state) {}
        });
    }

    //更新数据与圆点
    private void updateIntroAndDot() {
        int currentPage = viewPager.getCurrentItem() % adInfoList.size();
        tv_intro.setText(adInfoList.get(currentPage).getTitle());
        for (int i = 0; i < dot_layout.getChildCount(); i++)
            dot_layout.getChildAt(i).setEnabled(i==currentPage);

    }

    //初始化文字下方的圆点
    private void initDots() {
        for (int i=0; i< adInfoList.size(); i++) {
            View view = new View(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(8,8);
            if(i!=0) {
                params.leftMargin = 5;
            }
            view.setLayoutParams(params);
            view.setBackgroundResource(R.drawable.select_dot);
            dot_layout.addView(view);
        }
    }

    /**
     * 用于设置自动轮播
     */
    private Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg){
            viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
            handler.sendEmptyMessageDelayed(0, 4000);
        }
    };
}
