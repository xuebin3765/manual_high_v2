package com.manaul.highschool.utils;

import android.util.Log;

/**
 * Created by Yum on 2016/11/22.
 */
public class DebugUtil {
    public static boolean isDebug=true;
    private static final String TAG = "manual_debug";
    public static void d(String msg){
        if (isDebug)
            Log.d(getTag(),getCurrentClassName()+getCurrentMethodName()+msg);
    }
    public static void i(String msg){
        if (isDebug)
            Log.i(getTag(),getCurrentClassName()+getCurrentMethodName()+msg);
    }
    public static void e(String msg){
        if (isDebug)
            Log.e(getTag(),getCurrentClassName()+getCurrentMethodName()+msg);
    }
    public static void w(String msg){
        if (isDebug)
            Log.w(getTag(),getCurrentClassName()+getCurrentMethodName()+msg);
    }

    //获取当前类名
    public static String getTag(){
        return TAG;
    }

    public static String getCurrentMethodName() {
        int level = 2;//[1]是你当前方法执行堆栈,[2]就是上一级的方法堆栈 以此类推
        StackTraceElement[] stacks = new Throwable().getStackTrace();
        String methodName = stacks[level].getMethodName();
        return "["+methodName+"方法()]： ";
    }
    public static String getCurrentClassName() {
        int level = 2;//[1]是你当前方法执行堆栈,[2]就是上一级的方法堆栈 以此类推
        StackTraceElement[] stacks = new Throwable().getStackTrace();
        String className = stacks[level].getClassName();
        return "->["+className+"类]";
    }
}
