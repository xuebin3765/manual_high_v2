package com.manaul.highschool.utils;

import android.content.Context;

/**
 * Author: vector.huang
 * Email: 642378415@qq.com
 * Date: 2015/10/27
 * Description:<p>{TODO: 用一句话描述}
 */
public class DimenUtils {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 将px值转换为dip 值
     * @param context
     * @param pxValue
     * @return
     */
    public static float px2dip(Context context, int pxValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return pxValue/scale;
    }


    /**
       * 将sp值转换为px值，保证文字大小不变
       *
       * @param spValue
       * @return
       */
    public static int sp2px(Context context, float spValue) {
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * scale + 0.5f);
    }

    /**
     * 将px值转换为dip 值
     * @param context
     * @param psValue
     * @return
     */
    public static float px2sp(Context context, int psValue){
        final float scale = context.getResources().getDisplayMetrics().scaledDensity;
        return psValue/scale;
    }
}
