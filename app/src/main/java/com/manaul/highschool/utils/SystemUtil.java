package com.manaul.highschool.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * 系统常量参数
 * Created by Administrator on 2018/1/24.
 */

public class SystemUtil {

    public static final String APP_DATABASE_NAME = "gzxxsc";
    public static final String APP_LOCAL_DATA = "manual_high";

    public static final String APP_APK_NAME="manual_high_"+ System.currentTimeMillis()+"_.apk";
    // 是否为测试版本
    public static final boolean APP_TEST = true;
    // 是否打印日志
    public static final boolean APP_SHOW_LOG = true;
    // appId : 1：高中生手册
    public static final int APP_ID = 1;
    // 平台：1：Android ； 2：ios ； 3：web
    public static final int APP_PLATFORM = 1;
    /**
     * 不需要登录就能看到的缓存文件，只要缓存在，都是可以看到的
     * 这个是不需要加上id的
     */
    public final static String PREFERENCE_PUBLIC_CACHE = "preference_custom_cache";



    public static String getVersionName(Context context) {
        String version = "";
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info;
            info = manager.getPackageInfo(context.getPackageName(), 0);
            version = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    public static int getVersionCode(Context context) {
        int versionCode = 0;
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info;
            info = manager.getPackageInfo(context.getPackageName(), 0);
            versionCode = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }
}
