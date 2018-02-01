package com.manaul.highschool.utils;

import android.util.Log;

/**
 * Created by Administrator on 2017/11/22.
 */

public class LoggerUtil {
    private boolean debug = Constant.SHOW_LOG;

    public static void showLog(String message){
        if (Constant.SHOW_LOG)
            Log.i(Constant.APP_TYPE , message+"");
    }
}
