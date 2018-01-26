package com.manaul.highschool.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.manaul.highschool.main.R;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by Yum on 2016/12/8.
 */
public class StatusBarUtils {
    public static void setWindowStatusBarColor(Activity activity, int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = activity.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(activity.getResources().getColor(colorResId));

                //底部导航栏
                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setWindowStatusBarColor(Dialog dialog, int colorResId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = dialog.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(dialog.getContext().getResources().getColor(colorResId));

                //底部导航栏
                //window.setNavigationBarColor(activity.getResources().getColor(colorResId));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//=============================================另一種============================================================
    private static final int INVALID_VAL = -1;
    private static final int COLOR_DEFAULT = Color.parseColor("#20000000");

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void compat(Activity activity, int statusColor)
    {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            if (statusColor != INVALID_VAL)
            {
                activity.getWindow().setStatusBarColor(statusColor);
            }
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
        {
            int color = COLOR_DEFAULT;
            ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
            if (statusColor != INVALID_VAL)
            {
                color = statusColor;
            }
            View statusBarView = new View(activity);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    getStatusBarHeight(activity));
            statusBarView.setBackgroundColor(color);
            contentView.addView(statusBarView, lp);
        }
    }

    public static void compat(Activity activity)
    {
        compat(activity, INVALID_VAL);
    }


    public static int getStatusBarHeight(Context context)
    {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
        {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

//    //================================狀態欄動畫=======================================
//
//    public static void addWindowStatusBar(Activity activity, int colorResId, View statusBarView, View navigationView, EditText keywordSearch) {
//        try {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                //透明状态栏
//                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//                //透明导航栏
//                //activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//                activity.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.transparent));
//                //ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
//                LinearLayout contentView = (LinearLayout) activity.findViewById(R.id.root);
//
//                contentView.addView(statusBarView, 0);
//                AnimationUtils.typeStatusBarDown(activity, statusBarView, new Animation.AnimationListener() {
//                    @Override
//                    public void onAnimationStart(Animation animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animation animation) {
//                        AnimationUtils.typeSearchMenuDown(activity,navigationView,keywordSearch);
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animation animation) {
//
//                    }
//                });
//
//            }else  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//                activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//                //activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//
//                SystemBarTintManager tintManager = new SystemBarTintManager(activity);
//                tintManager.setStatusBarTintColor(activity.getResources().getColor(R.color.transparent));
//                tintManager.setStatusBarTintEnabled(true);
//                LinearLayout contentView = (LinearLayout) activity.findViewById(R.id.root);
//                contentView.addView(statusBarView, 0);
//                AnimationUtils.typeStatusBarDown(activity, statusBarView, new Animation.AnimationListener() {
//                    @Override
//                    public void onAnimationStart(Animation animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animation animation) {
//                        AnimationUtils.typeSearchMenuDown(activity,navigationView,keywordSearch);
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animation animation) {
//
//                    }
//                });
//            }
//
//            else {
//                AnimationUtils.typeSearchMenuDown(activity, navigationView,keywordSearch);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     * @param window 需要设置的窗口
     * @param dark 是否把状态栏字体及图标颜色设置为深色
     * @return  boolean 成功执行返回true
     *
     */
    public static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }





    /**
     * 设置状态栏字体图标为深色，需要MIUIV6以上
     * @param window 需要设置的窗口
     * @param dark 是否把状态栏字体及图标颜色设置为深色
     * @return  boolean 成功执行返回true
     *
     */
    public static boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if(dark){
                    extraFlagField.invoke(window,darkModeFlag,darkModeFlag);//状态栏透明且黑色字体
                }else{
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result=true;
            }catch (Exception e){

            }
        }
        return result;
    }
    /**
     * 官方在Android6.0中提供了亮色状态栏模式
     */
    public static boolean setActiveStatusBarLightMode(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN| View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            return true;
        }
        return false;
    }

    /**
     * 目前只看到了小米和魅族公开了各自的实现方法，支持底层Android4.4以上的版本。而Android官方在6.0版本才有了深色状态栏字体API。
     * @param activity
     */
 /*   public static void setStatusBarLightMode(Activity activity){
        //魅族的Android6.0
        if (!setActiveStatusBarLightMode(activity)){//不是6.0，设置不成功，那就尝试 魅族和小米的方法，其他的机型都不可行
            //只有小米的MIUI,V6以上版本
            MIUISetStatusBarLightMode(activity.getWindow(),true);
            //魅族的Flyme4.0以上版本
            //FlymeSetStatusBarLightMode(activity.getWindow(),true);
        }
    }*/

    /**
     *设置状态栏黑色字体图标，
     * 适配4.4以上版本MIUIV、Flyme和6.0以上版本其他Android
     * @param activity
     * @return 1:MIUUI 2:Flyme 3:android6.0
     */
    public static int StatusBarLightMode(Activity activity){

        DebugUtil.d("高亮模式开始");
        int result=0;
        //ToastUtil.toastDefault(activity,"高亮模式开始");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ) {//如果要高亮，就高亮。
            if (setActiveStatusBarLightMode(activity)) {
                result = 3;
            }
            if(MIUISetStatusBarLightMode(activity.getWindow(), true)){
                result=1;
            }
            if(FlymeSetStatusBarLightMode(activity.getWindow(), true)){
                result=2;
            }
        }
        DebugUtil.d("高亮模式开始 result："+result);
        //ToastUtil.toastDefault(activity,"高亮模式开始 result："+result);
        if (result != 0){//如果不能，就不要沉浸式了
//            StatusBarUtil.setColor(activity, ContextCompat.getColor(activity,R.color.white),0);
            transparencyBar(activity);//如果可以高亮字体，那么就 沉浸式。（需要设置对应的activity的布局android:fitsSystemWindows="true"）
            Handler h = new Handler(activity.getMainLooper());
            h.postDelayed(()->StatusBarUtils.setStatusBarDarkMode(activity),400);
            h.postDelayed(()->StatusBarUtils.setStatusBarDarkMode(activity),500);
            h.postDelayed(()->StatusBarUtils.setStatusBarDarkMode(activity),600);
            h.postDelayed(()->StatusBarUtils.setStatusBarDarkMode(activity),700);
            h.postDelayed(()->StatusBarUtils.setStatusBarDarkMode(activity),1500);

            //5.0以上的 会无效？不知道为何，测试大概是500ms的时候，可以生效，為了最快速度顯示狀態欄，那么这里只好這樣了。
        }

        device_type = result;
        DebugUtil.d("高亮模式结束 transparencyBar");
        //ToastUtil.toastDefault(activity,"高亮模式结束 transparencyBar");
        return result;
    }

    /**
     * 修改状态栏为全透明
     * @param activity
     */
    @TargetApi(19)
    public static void transparencyBar(Activity activity){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);

        } else
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window =activity.getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 清除MIUI或flyme或6.0以上版本状态栏黑色字体
     */
    public static void StatusBarDarkMode(Activity activity, int type){
        if(type==1){
            MIUISetStatusBarLightMode(activity.getWindow(), false);
        }else if(type==2){
            FlymeSetStatusBarLightMode(activity.getWindow(), false);
        }else if(type==3){
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }

    }

    public static void clearStatusBarDarkMode(Activity activity, int type){
        if(type==1){
            MIUISetStatusBarLightMode(activity.getWindow(), false);
        }else if(type==2){
            FlymeSetStatusBarLightMode(activity.getWindow(), false);
        }else if(type==3){
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
    }

    public static void clearStatusBarDarkMode(Activity activity){//在一些机器上面会出现抖动。。。？？？5.0和6.0的，不知为何，这里暂且不使用了。
        //if(type==1){
            MIUISetStatusBarLightMode(activity.getWindow(), false);
        //}else if(type==2){
            FlymeSetStatusBarLightMode(activity.getWindow(), false);
        //}else if(type==3){
            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        //}
    }
    public static void setStatusBarDarkMode(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT ) {
            if(MIUISetStatusBarLightMode(activity.getWindow(), true)){
                //result=1;
            }else if(FlymeSetStatusBarLightMode(activity.getWindow(), true)){
                //result=2;
            }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN| View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                //result=3;
            }
        }
    }

    public static int device_type = 0 ;
}
