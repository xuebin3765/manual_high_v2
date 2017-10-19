package com.manaul.highschool.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 
 * @author lovebin
 *
 */
@SuppressLint("SdCardPath")
public class SharedConfig {

	Context context;
	SharedPreferences shared;

	public SharedConfig(Context context) {
		this.context = context;
		shared = context.getSharedPreferences(Constant.APP_TYPE, Context.MODE_PRIVATE);
	}

	public SharedPreferences getConfig() {
		return shared;
	}

	public void clearConfig() {
		if (shared != null) {
			shared.edit().clear().commit();
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
