package com.manaul.highschool.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.manaul.highschool.main.R;

/**
 * @author stlshen
 */
public class ToastUtil {
	private static Toast toast = null ;

	private static final int TOAST_LARGE = 1;

	private static final int TOAST_TICK = 2;

	private static final int TOAST_DEFAULT = 3;

	private static final int TOAST_ADD = 4;

	private static final int TOAST_=5;

	private static final String TOAST_INDEX = "Toast_index";

	/**
	 * 没网络提示
	 * @param context
	 * @param msg
	 */
	public static void toastLarge(Context context, String msg) {
		if (context == null||msg==null||msg.equals("")) return;
		View layout = View.inflate(context, R.layout.toast_large_layout, null);
		TextView text = (TextView) layout.findViewById(R.id.msg);
		if (toast == null) {
			text.setText(msg);
			toast = new Toast(context);
			toast.setGravity(Gravity.CENTER, 0, 10);
			toast.setDuration(Toast.LENGTH_SHORT);
			toast.setView(layout);
		}else {
			text.setText(msg);
		}
		toast.show();
	}

	/**
	 * 提交评论(打钩图标，大圆角框)
	 * 
	 * @param context
	 * @param msg
	 */
	public static void toastTick(Context context, String msg) {
		if (context == null||msg==null||msg.equals("")) return;
		View layout = View.inflate(context, R.layout.toast_tick_layout, null);
		TextView message = (TextView) layout.findViewById(R.id.msg);
		if (toast == null) {
			message.setText(msg);
			toast = new Toast(context);
			toast.setGravity(Gravity.CENTER, 0, 10);
			toast.setDuration(Toast.LENGTH_SHORT);
			toast.setView(layout);
		}else {
			message.setText(msg);
		}
		toast.show();
	}

	/**
	 * 系统默认居中提示
	 * @param context
	 * @param msg
	 */
	public static void toast(Context context, String msg){
		if (context==null||msg==null||msg.equals(""))return;
		if (toast!=null){
			toast.setText(msg);
		}else {
			toast = new Toast(context);
			toast= Toast.makeText(context,msg, Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER,0,0);
		}
		toast.show();
	}
}
