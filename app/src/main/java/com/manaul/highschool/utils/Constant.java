package com.manaul.highschool.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Constant {

	public static final int BANNER_TYPE_START = 1;
	public static final int BANNER_TYPE_HOME = 2;
	
	public static final String HOST_IMG = "http://ovb08chzs.bkt.clouddn.com/";

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


	public static String QQ_APPID = "1106085721";
	public static String QQ_APP_KEY = "C6TO0qA7Z4aAgbAT";
	
	public static String QQ_GDT_AD_ID_BANNER = "5040429255163790";
	public static String QQ_GDT_AD_ID_CHAPING = "4050822285069761";
	public static String QQ_GDT_AD_ID_KAIPING = "2010027225460703";
	
	public static String WEI_XIN_PINGTAI = "";

	public static int DETAIL_IMAGE_WIDTH = 4;
	public static int DETAIL_IMAGE_HEIGHT = 3;

	/**
	 * 需要登录才能看到的缓存文件，和用户id 绑定，名称使用
	 * $preference_custom_cache + id
	 */
	public final static String PREFERENCE_CUSTOM_CACHE = "preference_custom_cache";
	public final static String KEY_DEFAULT_ADDRESS = "key_default_address";
	public final static String KEY_ORDER_ID = "key_order_id";

	/**
	 * 获取加密后的字符
	 * 
	 * @param pw
	 * @return
	 */
	public static String stringMD5(String pw) {
		try {
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			byte[] inputByteArray = pw.getBytes();
			messageDigest.update(inputByteArray);
			byte[] resultByteArray = messageDigest.digest();
			return byteArrayToHex(resultByteArray);
		} catch (NoSuchAlgorithmException e) {
		}
		return null;
	}

	private static String byteArrayToHex(byte[] byteArray) {

		char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		char[] resultCharArray = new char[byteArray.length * 2];
		int index = 0;
		for (byte b : byteArray) {
			resultCharArray[index++] = hexDigits[b >>> 4 & 0xf];
			resultCharArray[index++] = hexDigits[b & 0xf];
		}
		return new String(resultCharArray);
	}

}
