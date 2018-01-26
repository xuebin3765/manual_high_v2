package com.manaul.highschool.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataUtil {
	/**
	 * 根据路径，获取Assets下的图片的Bitmap
	 * 
	 * @param context
	 * @param fileName
	 * @return
	 */
	public static Bitmap getImageFromAssetsFile(Context context, String fileName) {
		Bitmap image = null;
		AssetManager am = context.getResources().getAssets();
		try {
			InputStream is = am.open(fileName);
			image = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}

	/**
	 * 从HTML源码中提取图片路径，�??后以�??�?? String 类型�?? List 返回，如果不包含任何图片，则返回�??�?? size=0 的List
	 * �??要注意的是，此方法只会提取以下格式的图片�??.jpg|.bmp|.eps|.gif|.mif|.miff|.png|.tif|.tiff|.svg
	 * |.wmf|.jpe|.jpeg|.dib|.ico|.tga|.cut|.pic
	 * 
	 * @param htmlCode
	 *            HTML源码
	 * @return <img>标签 src 属�?�指向的图片地址的List集合
	 * @author Carl He
	 */
	public static List<String> getImageSrc(String htmlCode) {
		List<String> imageSrcList = new ArrayList<>();
		Pattern p = Pattern.compile(
				"<img\\b[^>]*\\bsrc\\b\\s*=\\s*('|\")?([^'\"\n\r\f>]+(\\.jpg|\\.bmp|\\.eps|\\.gif|\\.mif|\\.miff|\\.png|\\.tif|\\.tiff|\\.svg|\\.wmf|\\.jpe|\\.jpeg|\\.dib|\\.ico|\\.tga|\\.cut|\\.pic)\\b)[^>]*>",
				Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(htmlCode);
		String quote = null;
		String src = null;
		while (m.find()) {
			quote = m.group(1);
			src = (quote == null || quote.trim().length() == 0) ? m.group(2).split("\\s+")[0] : m.group(2);
			imageSrcList.add(src);
		}
		return imageSrcList;
	}

	/**
	 * 给定日期 指定天数后的日期，
	 * @param date 给定日期
	 * @param day 天数,整数，表示多少天之后；负数，表示多少天之前
	 * @return
	 */
	public static String getAfterTime(Date date, int day) {
		Date dBefore = null;
		Calendar calendar = Calendar.getInstance(); // 得到日历
		calendar.setTime(date);// 把当前时间赋给日期
		calendar.add(calendar.DATE, day);
		dBefore = calendar.getTime();
		return getCurrentTime(dBefore);
	}

	/**
	 * 给定日期 指定天数后的日期，
	 * @param date 给定日期
	 * @param day 天数,整数，表示多少天之后；负数，表示多少天之前
	 * @return
	 */
	public static long getAfterTimeLong(Date date, int day) {
		Calendar calendar = Calendar.getInstance(); // 得到日历
		calendar.setTime(date);// 把当前时间赋给日期
		calendar.add(calendar.DATE, day);
		return calendar.getTime().getTime();
	}

	// 验证会员是否过期。true:未过期 ； false ： 过期了
	public static boolean vipIsEnd(long end){
		return end - System.currentTimeMillis() >= 0;
	}

	/**
	 * 获得当前的格式化时间
	 * 
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getCurrentTime(Date date) {
		if( (date.getTime() - System.currentTimeMillis())/(1000*60*60*24) > 999){
			return "永久";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 设置时间格式
		String defaultEndDate = sdf.format(date); // 格式化当前时
		return defaultEndDate;
	}

	@SuppressLint("SimpleDateFormat")
	public static Date getCurrentTimeDate(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 设置时间格式
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			return null;
		}
	}
}
