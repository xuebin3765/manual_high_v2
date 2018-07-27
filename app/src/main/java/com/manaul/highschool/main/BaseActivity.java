package com.manaul.highschool.main;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.manaul.highschool.bean.ADInfo;
import com.manaul.highschool.bean.AdPicture;
import com.manaul.highschool.bean.DataOther;
import com.manaul.highschool.bean.UpdateApk;
import com.manaul.highschool.bean.User;
import com.manaul.highschool.service.DownloadService;
import com.manaul.highschool.utils.Constant;
import com.manaul.highschool.utils.DebugUtil;
import com.manaul.highschool.utils.SPrefUtil;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

/**
 * 基础activity，包含导航处理
 * Created by Administrator on 2018/3/2.
 */

public class BaseActivity extends AppCompatActivity {

    public ActionBar mActionbar = null;
    public Context mContext;
    public User user;
    public List<ADInfo> adInfoList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActionbar = getSupportActionBar();
        mContext = this;
        setTopView(); // 设置顶部导航相关参数
        validate(); // 验证各种数据
        user = BmobUser.getCurrentUser(User.class);

    }

    /**
     * 验证各种数据
     */
    /**
     * 验证相关信息
     */
    private void validate() {
        long validateTime = SPrefUtil.getInstance(mContext).getLongByKey("validateTime");
        long validateLoginTime = SPrefUtil.getInstance(mContext).getLongByKey("validateLoginTime");

        // 验证登陆时间 1000 * 60 * 60 * 5
//        if (validateLoginTime == 0 || (System.currentTimeMillis() - validateLoginTime) > 1000) {
        if (validateLoginTime == 0 || (System.currentTimeMillis() - validateLoginTime) > 1000 * 60 * 60 * 5) {
            validateLogin(); // 验证登陆
            writeSysConstants(); //  加载数据
            SPrefUtil.getInstance(mContext).setLongByKey("validateLoginTime", System.currentTimeMillis());
        }
        // 验证登陆时间 1000 * 60 * 60 * 24
        if (validateTime == 0 || (System.currentTimeMillis() - validateTime) > 1000 * 60 * 60 * 24) {
            checkUpdate(); // 检查更新
            SPrefUtil.getInstance(mContext).setLongByKey("validateTime", System.currentTimeMillis());
        }
    }

    private void checkUpdate() {
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

    public User getUser(){
        if(user != null)
            return user;
        else
            return BmobUser.getCurrentUser(User.class);
    }

    /**
     * 验证登陆是否有效
     */
    public void validateLogin() {
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

    /**
     * 设置顶部导航相关参数
     */
    private void setTopView() {
        if (mActionbar != null) {

            mActionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            mActionbar.setDisplayShowCustomEnabled(true);
            mActionbar.setCustomView(R.layout.title_to_center_home);
            TextView tvTitle = (TextView) mActionbar.getCustomView().findViewById(R.id.title_to_center_text);
            tvTitle.setText(mContext.getResources().getString(R.string.app_name));
        }
    }

    public ActionBar getActionbar(){
        if(mActionbar == null)
            return getSupportActionBar();
        else
            return mActionbar;
    }
}
