package com.manaul.highschool.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Constant {
	public static final boolean IS_TEST = false;
	public static final boolean SHOW_LOG = true;

	// appId : 1：高中生手册
	public static final int APP_ID = 1;
	// 平台：1：Android ； 2：ios ； 3：web
	public static final int APP_PLATFORM = 1;
	public static final String HOST_IMG = "http://ovb08chzs.bkt.clouddn.com/";

	public static final String APP_TYPE = "gzxxsc";

	public static final int DATABASE_VERSION = 1; // 数据库版
	public static final String T_NAVIGATE = "t_navigate";
	public static final String T_PROJECT = "t_project";
	public static final String T_ITEM_CONTENT = "t_item_content";
	public static final String T_LIST_ITEM_SHARE = "t_item_share";

	// bmob APKID gzxxsc
	public static final String BMOB_APKID = "34961277dca1fc9cc5cefcb2434e2763";

	public static final int PLUGINVERSION = 7;
	// 软件价格--- 按月计费
//	public static final double MONWEY_30 = 0.01;
//	public static final double MONWEY_90 = 0.01;
//	public static final double MONWEY_180 = 0.01;
//	public static final double MONWEY_NEVER = 0.01;

	public static final double MONWEY_30 = 6;
	public static final double MONWEY_90 = 15;
	public static final double MONWEY_180 = 25;
	public static final double MONWEY_NEVER = 45;

	/**
	 * 获取版本号 string
	 * @param context
	 * @return
	 */
	public static String getVersionName(Context context) {
		String version = "";
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info;
			info = manager.getPackageInfo(context.getPackageName(), 0);
			version = info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return version;
	}

	/**
	 * 获取版本号
	 * @param context
	 * @return
	 */
	public static int getVersionCode(Context context) {
		int versionCode = 0;
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info;
			info = manager.getPackageInfo(context.getPackageName(), 0);
			versionCode = info.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionCode;
	}
}
