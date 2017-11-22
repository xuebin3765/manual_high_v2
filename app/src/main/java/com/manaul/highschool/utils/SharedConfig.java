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


	
	
}
