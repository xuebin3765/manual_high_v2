package com.manaul.highschool.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.manaul.highschool.adapter.CyclePagerAdapter;
import com.manaul.highschool.adapter.MyGridAdapterProject;
import com.manaul.highschool.bean.Banner;
import com.manaul.highschool.bean.Project;
import com.manaul.highschool.bean.UpdateApk;
import com.manaul.highschool.bean.User;
import com.manaul.highschool.dao.NavigateDao;
import com.manaul.highschool.dao.SQLiteHelper;
import com.manaul.highschool.service.DownloadService;
import com.manaul.highschool.utils.Constant;
import com.manaul.highschool.utils.DebugUtil;
import com.manaul.highschool.utils.SharedPreferenceUtil;
import com.manaul.highschool.utils.SystemUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

/**
 *
 *
 * Created by Administrator on 2017/8/7.
 */

public class NewHomeActivity extends AppCompatActivity {

    private Context mContext;
    private TextView tvTitle;

    private long validateTime;
    private long validateLoginTime;
    private User user;

    private SQLiteHelper sqliteHelper;
    private SQLiteDatabase db;
    private NavigateDao navigateDao;

    private ViewPager viewPager;
    private TextView tv_intro;
    private LinearLayout dot_layout;
    private LinearLayout fg3_lin_bk;
    private LinearLayout fg3_tool;
    private LinearLayout fg3_tool_01;
    private LinearLayout fg3_tool_02;
    private LinearLayout fg3_tool_03;
    private LinearLayout fg3_tool_04;

    List<Banner> bannerHomeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_home);
        ActionBar mActionbar = getSupportActionBar();
        mContext = this;

        initView();

        validate(mContext); // 验证各种数据
        if (mActionbar != null) {
            mActionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            mActionbar.setDisplayShowCustomEnabled(true);
            mActionbar.setCustomView(R.layout.title_to_center_home);
            tvTitle = (TextView) mActionbar.getCustomView().findViewById(R.id.title_to_center_text);
            tvTitle.setText(mContext.getResources().getString(R.string.app_name));

            mActionbar.getCustomView().findViewById(R.id.iv_home_user).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                Intent intent = new Intent(mContext, ActivityMy.class);
                intent.putExtra("top_title", "个人主页");
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);

                }
            });
            mActionbar.getCustomView().findViewById(R.id.iv_home_setting).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                Intent intent = new Intent(mContext, ActivitySetting.class);
                intent.putExtra("top_title", "设置");
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                }
            });
        }

        // 初始化页面
        viewPager = (ViewPager)findViewById(R.id.fg3_viewpager);
        tv_intro = (TextView)findViewById(R.id.fg3_tv_intro);
        dot_layout = (LinearLayout)findViewById(R.id.fg3_dot_layout);
//        initData();
        // 初始化数据
        final String homeBanners = SharedPreferenceUtil.getInstance(mContext).getStringByKey("homeBanners");
        DebugUtil.d(homeBanners);
        if(bannerHomeList.size() <= 0){
            if(homeBanners != null){
                // 从 本地缓存中获取
                JSONArray jsonArray = JSON.parseArray(homeBanners);
                if(jsonArray != null && jsonArray.size() > 0){
                    for(int i = 0 ; i < jsonArray.size() ; i++ )
                        bannerHomeList.add(JSON.toJavaObject(jsonArray.getJSONObject(i), Banner.class));
                }
            }else {
                cacheBannerHomePic();

            }
        }
        if(bannerHomeList != null && bannerHomeList.size() > 0){
            // 初始化文字下方的圆点
            initDots();
            viewPager.setAdapter(new CyclePagerAdapter(mContext , bannerHomeList));
            int centerValue = Integer.MAX_VALUE/2;
            int value = centerValue % bannerHomeList.size();
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
        View girdItemLYView = getLayoutInflater().inflate(R.layout.layout_gird_item, null);
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
    }

    private void initView() {

        // 加载备考模块
        fg3_lin_bk = (LinearLayout) findViewById(R.id.fg3_linear_bk);
        // 工具
        fg3_tool = (LinearLayout) findViewById(R.id.fg3_tool);
        //
        fg3_tool_01 = (LinearLayout) findViewById(R.id.fg3_tool_01);
        fg3_tool_02 = (LinearLayout) findViewById(R.id.fg3_tool_02);
        fg3_tool_03 = (LinearLayout) findViewById(R.id.fg3_tool_03);
        fg3_tool_04 = (LinearLayout) findViewById(R.id.fg3_tool_04);
    }

    /**
     * 验证相关信息
     * @param mContext
     */
    private void validate(Context mContext) {
        user = BmobUser.getCurrentUser(User.class);
        // 存到系统数据中
        validateTime = SharedPreferenceUtil.getInstance(mContext ).getLongByKey("validateTime");
        // 存在用户数据中
        validateLoginTime = SharedPreferenceUtil.getInstance(mContext).getLongByKey("validateLoginTime");
        // 验证登陆时间 1000 * 60 * 60 * 5
        if (validateLoginTime == 0 || (System.currentTimeMillis() - validateLoginTime) > 1000 * 60 * 60 * 5) {
            validateLogin(); // 验证登录
            cacheBannerHomePic(); // 缓存相关数据
            // 存在用户数据中
            SharedPreferenceUtil.getInstance(mContext).setLongByKey("validateLoginTime", System.currentTimeMillis());
        }
        // 验证登陆时间 1000 * 60 * 60 * 24
        if (validateTime == 0 || (System.currentTimeMillis() - validateTime) > 1000 * 60 * 60 * 24) {
            checkUpdate(); // 检查更新
            // 存到系统数据中
            SharedPreferenceUtil.getInstance(mContext).setLongByKey("validateTime", System.currentTimeMillis());
        }
    }

    protected void checkUpdate() {
        BmobQuery<UpdateApk> query = new BmobQuery<UpdateApk>();
        query.findObjects(new FindListener<UpdateApk>() {
            @Override
            public void done(List<UpdateApk> arg0, BmobException arg1) {
            if (arg0 != null && arg0.size() > 0) {
                final UpdateApk apk = arg0.get(0);
                DebugUtil.d(" Constant.getVersionCode(mContext) = "+ SystemUtil.getVersionCode(mContext));
                if (mContext != null && apk.getVersionCode() != SystemUtil.getVersionCode(mContext)) {
                    new AlertDialog.Builder(mContext).setTitle("有新版本啦！")//
                            .setMessage(apk.getMessage())//
                            .setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(mContext, DownloadService.class);
                                    intent.putExtra("url", apk.getApkurl());
                                    startService(intent);
                                }
                            }).setNegativeButton("稍后更新", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).show();
                }
            }
            }
        });
    }

    /**
     * 缓存banner图
     */
    public void cacheBannerHomePic(){
        BmobQuery<Banner> query = new BmobQuery<Banner>();
        query.order("-sort");
        query.addWhereEqualTo("appId" , SystemUtil.APP_ID);
        query.addWhereEqualTo("type" , Constant.BANNER_TYPE_HOME);
        query.findObjects(new FindListener<Banner>() {
            @Override
            public void done(List<Banner> object, BmobException e) {
                if(e==null){
                    String jsonObject = JSONArray.toJSONString(object);
                    SharedPreferenceUtil.getInstance(mContext).setStringByKey("homeBanners" , jsonObject);
                    bannerHomeList = object;
                }else{
                    DebugUtil.d("cacheBannerPic 失败："+e.getMessage()+","+e.getErrorCode());
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
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    //更新数据与圆点
    private void updateIntroAndDot() {
        int currentPage = viewPager.getCurrentItem() % bannerHomeList.size();
        tv_intro.setText(bannerHomeList.get(currentPage).getTitle());
        for (int i = 0; i < dot_layout.getChildCount(); i++)
            dot_layout.getChildAt(i).setEnabled(i==currentPage);

    }

    //初始化文字下方的圆点
    private void initDots() {
        for (int i=0; i< bannerHomeList.size(); i++)
        {
            View view = new View(this);
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
            handler.sendEmptyMessageDelayed(0, 6000);
        }
    };

    private void validateLogin() {
        if (user != null) {
            BmobQuery<User> bmobQuery = new BmobQuery<User>();
            bmobQuery.getObject(user.getObjectId(), new QueryListener<User>() {
                @Override
                public void done(User object, BmobException e) {
                    if (e == null && object != null) {
                        if (!user.getAccessToken().equals(object.getAccessToken())) {
                            new AlertDialog.Builder(mContext).setTitle("被迫下线")
                                    .setMessage("您的账户在其他设备登陆，您已被迫下线。请重新登陆或修改密码。")// ������ʾ������
                                    .setPositiveButton("重新登陆", new DialogInterface.OnClickListener() {// ���ȷ����ť
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            User.logOut();
                                        }
                                    }).show();
                        }
                    }
                }
            });
        }
    }

    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次返回退出", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
//                Intent home = new Intent(Intent.ACTION_MAIN);
//                home.addCategory(Intent.CATEGORY_HOME);
//                startActivity(home);
//                LoggerUtil.showLog("回到桌面，不退出");
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void finish() {
        super.finish();
        db.close();
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
