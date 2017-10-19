package com.manaul.highschool.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {

	public static void showToastLength(Context context , String message){
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}

	public static void showToastShort(Context context , String message){
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}
}
