package com.manaul.highschool.main;

import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.manaul.highschool.adapter.CyclePagerAdapter;
import com.manaul.highschool.adapter.MyGridAdapterProject;
import com.manaul.highschool.bean.ADInfo;
import com.manaul.highschool.bean.AdPicture;
import com.manaul.highschool.bean.DataOther;
import com.manaul.highschool.bean.Project;
import com.manaul.highschool.bean.UpdateApk;
import com.manaul.highschool.bean.User;
import com.manaul.highschool.dao.NavigateDao;
import com.manaul.highschool.dao.SQLiteHelper;
import com.manaul.highschool.service.DownloadService;
import com.manaul.highschool.utils.Constant;
import com.manaul.highschool.utils.DebugUtil;
import com.manaul.highschool.utils.SPrefUtil;
import com.manaul.highschool.utils.ToastUtils;
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
    private List<ADInfo> adInfoList = new ArrayList<>();

    private TextView zfbRedTitle;
    private TextView zfbRedText;

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

        // 加载支付宝领红包文字
        zfbRedTitle = (TextView) findViewById(R.id.zfb_red_title);
        zfbRedText = (TextView) findViewById(R.id.zfb_red_text);

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
                    ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    // 将文本内容放到系统剪贴板里。
                    cm.setText(zfbRedText.getText());
                    ToastUtils.showToastShort(mContext , "复制成功，去支付宝领取");
                }
            });
        }

    }

    private void validate(Context mContext) {
        user = BmobUser.getCurrentUser(User.class);

        long validateTime = SPrefUtil.getInstance(mContext).getLongByKey("validateTime");
        long validateLoginTime = SPrefUtil.getInstance(mContext).getLongByKey("validateLoginTime");

        // 验证登陆时间 1000 * 60 * 60 * 5
        if (validateLoginTime == 0 || (System.currentTimeMillis() - validateLoginTime) > 1000) {
//        if (validateLoginTime == 0 || (System.currentTimeMillis() - validateLoginTime) > 1000 * 60 * 60 * 5) {
            vaildateLogin(); // 验证登陆
            writeSysConstants(); //  加载数据
            SPrefUtil.getInstance(mContext).setLongByKey("validateLoginTime", System.currentTimeMillis());
        }
        // 验证登陆时间 1000 * 60 * 60 * 24
        if (validateTime == 0 || (System.currentTimeMillis() - validateTime) > 1000 * 60 * 60 * 24) {
            checkUpdate(); // 检查更新
            SPrefUtil.getInstance(mContext).setLongByKey("validateTime", System.currentTimeMillis());
        }
    }

    protected void checkUpdate() {
        BmobQuery<UpdateApk> query = new BmobQuery<UpdateApk>();
        query.findObjects(new FindListener<UpdateApk>() {
            @Override
            public void done(List<UpdateApk> arg0, BmobException arg1) {
                if (arg0 != null && arg0.size() > 0) {
                    final UpdateApk apk = arg0.get(0);
                    boolean update = false;
                    if (mContext != null && apk.getVersionCode() > Constant.getVersionCode(mContext)) {
                        update = true;
                    } else if (mContext != null && !apk.getVersion().equals(Constant.getVersionName(mContext))) {
                        update = true;
                    }
                    if (update) {

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
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            public void onPageScrollStateChanged(int state) {
            }
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
        for (int i=0; i< adInfoList.size(); i++)
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
            handler.sendEmptyMessageDelayed(0, 4000);
        }
    };

    private void vaildateLogin() {
        if (user != null) {
            BmobQuery<User> bmobQuery = new BmobQuery<User>();
            bmobQuery.getObject(user.getObjectId(), new QueryListener<User>() {
                @Override
                public void done(User object, BmobException e) {
                    if (e == null && object != null) {
                        if (!user.getAccessToken().equals(object.getAccessToken())) {
                            new AlertDialog.Builder(mContext).setTitle("被迫下线")//
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
