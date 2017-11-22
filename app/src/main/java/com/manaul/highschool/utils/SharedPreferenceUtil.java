package com.manaul.highschool.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Administrator on 2017/11/22.
 */

public class SharedPreferenceUtil {
    private SharedPreferences sp = null;
    private static SharedPreferenceUtil Instance = null;
    private static final String KEY_SP = "UserInfoManager";

    private SharedPreferenceUtil(Context context) {
        sp = context.getSharedPreferences(KEY_SP, Context.MODE_PRIVATE);
    }

    public static SharedPreferenceUtil getInstance(Context context) {
        if (Instance == null) {
            Instance = new SharedPreferenceUtil(context);
        }
        return Instance;
    }

    public void setByStringKey(String key, String value) {
        sp.edit().putString(key, value).commit();
    }

    public String getByStringKey(String key) {
        return sp.getString(key,null);
    }

    public void setBooleanByKey(String key, Boolean value) {
        sp.edit().putBoolean(key, value).commit();
    }

    public Boolean getBooleanByKey(String key) {
        return sp.getBoolean(key, false);
    }

    public void setIntByKey(String key, int value) {
        sp.edit().putInt(key, value).commit();
    }

    public void setLongByKey(String key, long value) {
        sp.edit().putLong(key, value).commit();
    }

    public long getLongByKey(String key) {
        return sp.getLong(key, 0);
    }

    public int getIntByKey(String key) {
        return sp.getInt(key, 0);
    }

    public void clearSharedPreferences() {
        try {
            sp.edit().clear().commit();
        } catch (Exception e) {
        }

    }

    /**
     *
     * @param context
     * @param filepath
     * @param fileName
     * @return
     */
    public static boolean copyFileFromAssets(Context context, String filepath, String fileName) {
        boolean result = false;
        try {
            File file = new File(filepath + fileName);
            if (file.exists() && file.isFile()) {
                file.delete();
                Log.e("create", "删除");
            }
            if (file.exists() == false) {
                File f = new File(filepath);
                if (!f.exists()) {
                    f.mkdir();
                }
                try {
                    InputStream is = context.getAssets().open(fileName);
                    OutputStream os = new FileOutputStream(filepath + fileName);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = is.read(buffer)) > 0) {
                        os.write(buffer, 0, length);
                    }
                    Log.e("create", "复制");
                    os.flush();
                    os.close();
                    is.close();
                    result = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
        }
        return result;
    }

    /**
     *
     * @param context
     * @return
     */
    public static boolean copyFileFromAssets(Context context) {
        boolean result = false;
        try {
            String filepath = "/data/data/" + context.getPackageName() + "/databases/";
            String fileName = Constant.APP_TYPE+".db";
            File file = new File(filepath + fileName);
            if (file.exists() && file.isFile()) {
                file.delete();
                Log.e("create", "删除");
            }
            if (file.exists() == false) {
                File f = new File(filepath);
                if (!f.exists()) {
                    f.mkdir();
                }
                try {
                    InputStream is = context.getAssets().open(fileName);
                    OutputStream os = new FileOutputStream(filepath + fileName);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = is.read(buffer)) > 0) {
                        os.write(buffer, 0, length);
                    }
                    Log.e("create", "创建");
                    os.flush();
                    os.close();
                    is.close();
                    result = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
        }
        return result;
    }
}
