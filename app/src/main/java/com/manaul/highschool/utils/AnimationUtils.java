package com.manaul.highschool.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.manaul.highschool.callback.GetScrollYListener;

/**
 * Author: vector.huang
 * Email: 642378415@qq.com
 * Date: 2015/10/12
 * Description:<p> 全部的复杂的动画放进这个工具类封装
 */
public class AnimationUtils {

    private static int mShortHeight;
    private static int mLongHeight;

    /**
     * TextView从固定行到无限行的过度动画
     *
     * @param tv
     */
    public static void showWarpContent(final TextView tv) {
        //获取TextView 原有的数据
        mShortHeight = tv.getMeasuredHeight();
        int oldWidth = tv.getMeasuredWidth();
        final TextPaint paint = tv.getPaint();

        //计算warp_content 的时候的高度
        StaticLayout staticLayout = new StaticLayout(tv.getText().toString(), paint, oldWidth, Layout.Alignment.ALIGN_NORMAL, 1.5f, 0, false);
        staticLayout.draw(new Canvas());
        int top = staticLayout.getLineTop(staticLayout.getLineCount());

        //加上padding
        mLongHeight = top + tv.getPaddingTop() + tv.getPaddingBottom();

        //设置动画
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(tv, "height", mShortHeight, mLongHeight);
        objectAnimator.setDuration(200).setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                tv.setMaxLines(Integer.MAX_VALUE);
            }
        });
        objectAnimator.start();
    }

    /**
     * 返回showWarpContent 之前的状态
     * @see #showWarpContent(TextView)
     *
     * @param tv
     */
    public static void hideWarpContent(final TextView tv, final int maxLines) {
        //设置动画
        ObjectAnimator objectAnimator = ObjectAnimator.ofInt(tv, "height", mLongHeight,mShortHeight);
        objectAnimator.setDuration(200).setInterpolator(new AccelerateDecelerateInterpolator());
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                tv.setMaxLines(maxLines);
            }
        });
        objectAnimator.start();
    }


    public static void showViewSildeIn(View view, long duration) {
        AnimationSet set = new AnimationSet(true);
        Animation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setDuration(duration);
        set.addAnimation(animation);

        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        animation.setDuration(duration);
        set.addAnimation(animation);
        view.startAnimation(set);
    }

    public static void showViewSildeOut(View view, long duration) {
        AnimationSet set = new AnimationSet(true);
        Animation animation = new AlphaAnimation(1.0f, 0.0f);
        animation.setDuration( duration);
        set.addAnimation(animation);
        animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, -1.0f);
        animation.setDuration(duration);
        set.addAnimation(animation);
        view.startAnimation(set);
    }


    public static void loadingDown(Context context, final View view){
        final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        int width = 0;
        ValueAnimator valueAnimator = null;
        /*if (view.getTag().equals("quationTag")){

        }else {
            valueAnimator = ValueAnimator.ofInt(0,DensityUtil.dip2px(context,110f));
        }*/
        valueAnimator = ValueAnimator.ofInt(0,DensityUtil.dip2px(context,70f));
        valueAnimator.setDuration(300);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Log.i("update", (valueAnimator.getAnimatedValue()).toString());
                //Float valueF = (Float) valueAnimator.getAnimatedValue();
                //float valuef = valueF;
                params.height = (int) valueAnimator.getAnimatedValue();
                view.setLayoutParams(params);
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                view.setVisibility(View.VISIBLE);
            }
        });
        valueAnimator.start();
    }
    public static void loadingUp(Context context, final View view){
        final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
        int width = 0;
        //ValueAnimator valueAnimator = ValueAnimator.ofInt(DensityUtil.dip2px(context,60f),0);
        ValueAnimator valueAnimator = ValueAnimator.ofInt(view.getHeight(),0);
        valueAnimator.setDuration(300);
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                Log.i("update", (valueAnimator.getAnimatedValue()).toString());
                //Float valueF = (Float) valueAnimator.getAnimatedValue();
                //float valuef = valueF;
                params.height = (int) valueAnimator.getAnimatedValue();
                view.setLayoutParams(params);
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.GONE);
            }
        });
        valueAnimator.start();
    }

    //搜索菜单栏下拉
    public static void typeSearchMenuDown(Context context, View view, EditText keywordSearch){
        TranslateAnimation translateAnimation = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
             translateAnimation = new TranslateAnimation(0, 0, -DimenUtils.dip2px(context, 48), 0);//48
            translateAnimation.setFillAfter(true);
            translateAnimation.setDuration(300 * 48 / (StatusBarUtils.getStatusBarHeight(context) + 48));
            //translateAnimation.setInterpolator(null);
            translateAnimation.setInterpolator(new LinearInterpolator());
            view.startAnimation(translateAnimation);
            view.setVisibility(View.VISIBLE);
        }else{
            translateAnimation = new TranslateAnimation(0, 0, -DimenUtils.dip2px(context, (StatusBarUtils.getStatusBarHeight(context) + 48)), 0);//48
            translateAnimation.setFillAfter(true);
            translateAnimation.setDuration(300);
            //translateAnimation.setInterpolator(null);
            translateAnimation.setInterpolator(new LinearInterpolator());
            view.startAnimation(translateAnimation);
            view.setVisibility(View.VISIBLE);
        }
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                keywordSearch.requestFocus();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
    //搜索狀態欄下拉
    public static void typeStatusBarDown(Context context, View view, Animation.AnimationListener animationListener){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TranslateAnimation translateAnimation = new TranslateAnimation(0, 0, -DimenUtils.dip2px(context, StatusBarUtils.getStatusBarHeight(context)), 0);//48
            translateAnimation.setFillAfter(true);
            //translateAnimation.setInterpolator(null);
            translateAnimation.setInterpolator(new LinearInterpolator());
            translateAnimation.setDuration(300 * StatusBarUtils.getStatusBarHeight(context) / (StatusBarUtils.getStatusBarHeight(context) + 48));
            view.startAnimation(translateAnimation);
            view.setVisibility(View.VISIBLE);
            translateAnimation.setAnimationListener(animationListener);
        }
    }
    //搜索狀態欄上拉
    public static void typeStatusBarUp(Context context, View view){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TranslateAnimation translateAnimation = new TranslateAnimation(0, 0,0, -DimenUtils.dip2px(context, StatusBarUtils.getStatusBarHeight(context)));//48
            translateAnimation.setFillAfter(true);
            //translateAnimation.setInterpolator(null);
            translateAnimation.setInterpolator(new LinearInterpolator());
            translateAnimation.setDuration(300 * StatusBarUtils.getStatusBarHeight(context) / (StatusBarUtils.getStatusBarHeight(context) + 48));
            view.startAnimation(translateAnimation);
        }
    }
    //搜索菜单栏上拉
    public static void typeSearchMenuUp(Context context, View view){
        TranslateAnimation translateAnimation = new TranslateAnimation(0,0,0,-DimenUtils.dip2px(context,(StatusBarUtils.getStatusBarHeight(context) + 48)));//48
        translateAnimation.setFillAfter(true);
        translateAnimation.setDuration(300);
        view.startAnimation(translateAnimation);
    }
    //搜索内容渐变出现
    public static void typeSearchContentShow(View view){
        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setDuration(300);
        alphaAnimation.setInterpolator(new LinearInterpolator());
        view.startAnimation(alphaAnimation);
    }


    //搜索内容渐变隐藏
    public static void typeSearchContentHide(View view){
        AlphaAnimation alphaAnimation = new AlphaAnimation(1,0);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setDuration(300);
        alphaAnimation.setInterpolator(new LinearInterpolator());
        view.startAnimation(alphaAnimation);
    }

    public static void show(View view){
        //DebugUtil.d("显示");
        if (view.getVisibility() == View.VISIBLE)
            return;
        view.setVisibility(View.VISIBLE);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setDuration(200);
        alphaAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        view.startAnimation(alphaAnimation);

        //DebugUtil.d("显示完毕");
    }
    public static void hide(View view){
        //DebugUtil.d("隐藏");
        if (view.getVisibility() == View.GONE)
            return;
        view.setVisibility(View.GONE);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1,0);
        alphaAnimation.setFillAfter(true);
        alphaAnimation.setDuration(200);
        alphaAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        view.startAnimation(alphaAnimation);
        //DebugUtil.d("隐藏完毕");
    }

    public static View getChildAtPosition(final AdapterView view, final int position) {
        final int index = position - view.getFirstVisiblePosition();
        if ((index >= 0) && (index < view.getChildCount())) {
            return view.getChildAt(index);
        } else {
            return null;
        }
    }
    //由于listview 的滑动api有bug,smoothScrollToPositionFromTop，当item大小不一的时候，滑动定位会不准确。这里采取一个不完美解决方案。
    public static void smoothScrollToPositionFromTopWithBugWorkAround(final AbsListView listView,
                                                                      final int position,
                                                                      final int offset,
                                                                      final int duration, BaseAdapter adapter, GetScrollYListener listener){
        View child = getChildAtPosition(listView, position);
        // There's no need to scroll if child is already at top or view is already scrolled to its end
        if ((child != null) && ((child.getTop() == 0) || ((child.getTop() > 0) && !listView.canScrollVertically(1)))) {
            return;
        }
        listView.smoothScrollToPosition(position);
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState== AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
                    listView.setOnScrollListener(listener);//到不了顶，就强制置顶
                    if (adapter != null) {
                        listView.setAdapter(adapter);//setSelection(0);经常性无效，所以我这里用setAdapter实现。
                        adapter.notifyDataSetChanged();
                    }
                    //listView.setSelection(position);
                    //adapter.notifyDataSetChanged();
                    DebugUtil.d("当前第一个位置:"+view.getFirstVisiblePosition());
                    //listView.smoothScrollToPositionFromTop(position, offset, duration);
                }
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }

    public static void smoothScrollToPositionFromTopWithBugWorkAround(final PullToRefreshListView listView,
                                                                      final int position,
                                                                      final int offset,
                                                                      final int duration, BaseAdapter adapter, GetScrollYListener listener){
        View child = getChildAtPosition(listView.getRefreshableView(), position);
        // There's no need to scroll if child is already at top or view is already scrolled to its end
        if ((child != null) && ((child.getTop() == 0) || ((child.getTop() > 0) && !listView.canScrollVertically(1)))) {
            return;
        }
        listView.getRefreshableView().smoothScrollToPosition(position);
        //这里又重新设置了滚动监听，那么在滚动完毕以后，再还原 原来的 隐藏显示 置顶按钮的监听
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(scrollState== AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
                    listView.setOnScrollListener(listener);//到不了顶，就强制置顶
                    if (adapter != null) {
                        listView.setAdapter(adapter);//setSelection(0);经常性无效，所以我这里用setAdapter实现。
                        adapter.notifyDataSetChanged();
                    }
                    //listView.setSelection(position);
                    //adapter.notifyDataSetChanged();
                    DebugUtil.d("当前第一个位置:"+view.getFirstVisiblePosition());
                    //listView.smoothScrollToPositionFromTop(position, offset, duration);
                }
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }
    public static void showSmallTool(View smallview, View small_tool_back_btn, View small_tool_item, View toolview_molayer, View toolview_bg){
        float x = 1f;
        int duration = (int) (280 * x);
        if (smallview.getVisibility() == View.GONE) {
            TranslateAnimation animation = new TranslateAnimation(-DimenUtils.dip2px(smallview.getContext(),187.5f), 0, 0, 0);
            animation.setDuration(duration);
            //animation.setFillAfter(true);
            //smallview.startAnimation(animation);
            animation.setInterpolator(new AccelerateDecelerateInterpolator());
            PropertyValuesHolder valuesHolder = PropertyValuesHolder.ofKeyframe(View.TRANSLATION_X,
                    Keyframe.ofFloat(0f, -DimenUtils.dip2px(smallview.getContext(),125)),//* 6.0
                    Keyframe.ofFloat(0.30f, DimenUtils.dip2px(smallview.getContext(),25)),//* 1.0
                    Keyframe.ofFloat(0.55f, -DimenUtils.dip2px(smallview.getContext(),10)),//* 1.0
                    Keyframe.ofFloat(0.70f, DimenUtils.dip2px(smallview.getContext(),5)),//* 1.0
                    Keyframe.ofFloat(0.90f, -DimenUtils.dip2px(smallview.getContext(),2)),//* 1.0
                    Keyframe.ofFloat(1.0f, 0)
            );
            ObjectAnimator.ofPropertyValuesHolder(small_tool_item,valuesHolder).setDuration((long) (1000 * x)).start();//五个item抖动

            smallview.setVisibility(View.VISIBLE);

            toolview_molayer.setVisibility(View.VISIBLE);
            toolview_bg.setVisibility(View.VISIBLE);
            AlphaAnimation alphaAnimation1 = new AlphaAnimation(0,1);
            alphaAnimation1.setDuration(duration);
            //AlphaAnimation alphaAnimation2 = new AlphaAnimation(0,1);
            //alphaAnimation2.setDuration(300);

            toolview_molayer.startAnimation(alphaAnimation1);//蒙层渐出
            toolview_bg.startAnimation(animation);//北京滑出
            small_tool_back_btn.startAnimation(animation);//箭头跟随白色背景一起滑出
        }
    }
    public static void hideSmallTool(View smallview, View small_tool_back_btn, View small_tool_item, View toolview_molayer, View toolview_bg){
        int duration = 280;
        small_tool_back_btn.clearAnimation();//先把前面的动画取消掉
        small_tool_item.clearAnimation();
        toolview_molayer.clearAnimation();
        toolview_bg.clearAnimation();

        if (smallview.getVisibility() == View.VISIBLE) {
            TranslateAnimation animation = new TranslateAnimation(0, -DimenUtils.dip2px(smallview.getContext(),187.5f), 0, 0);
            animation.setDuration(duration);
            //animation.setFillAfter(true);
            smallview.startAnimation(animation);
            smallview.setVisibility(View.GONE);

            toolview_molayer.setVisibility(View.GONE);
            toolview_bg.setVisibility(View.GONE);
            AlphaAnimation alphaAnimation1 = new AlphaAnimation(1,0);
            alphaAnimation1.setDuration(duration);

            //AlphaAnimation alphaAnimation2 = new AlphaAnimation(1,0);
            //alphaAnimation2.setDuration(300);

            toolview_molayer.startAnimation(alphaAnimation1);
            toolview_bg.startAnimation(animation);
        }
    }

}
