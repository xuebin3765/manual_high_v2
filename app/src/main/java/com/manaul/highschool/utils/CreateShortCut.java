package com.manaul.highschool.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Parcelable;

import com.manaul.highschool.main.NewHomeActivity;
import com.manaul.highschool.main.R;


/**
 * 创建快捷方式
 * @author lovebin
 *
 */
public class CreateShortCut {
	public CreateShortCut(Activity activity) {
		Intent addIntent = new Intent(
				"com.android.launcher.action.INSTALL_SHORTCUT");
		addIntent.putExtra("duplicate", false);
		String title = activity.getResources().getString(R.string.app_name);
		Parcelable icon = Intent.ShortcutIconResource.fromContext(activity,
				R.drawable.android_manual_logo);
		Intent myIntent = new Intent(activity, NewHomeActivity.class);
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
		addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, myIntent);
		activity.sendBroadcast(addIntent);
	}
}
