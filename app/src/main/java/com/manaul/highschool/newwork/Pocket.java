package com.manaul.highschool.newwork;

import android.content.Context;

import com.manaul.highschool.utils.Constant;
import com.manaul.highschool.utils.DebugUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp.HttpsHelp;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * 设计理念：一个API 对应一个Pocket 实例。同一个API 不能同一时间发出两次，
 * 就是说一个Pocket 只能发出一个请求，后发出的请求
 * 会覆盖上次的请求。
 * 默认超时为20s
 * <p>
 * Author: vector.huang
 * Email: 642378415@qq.com
 * Date: 2015/10/15
 * Description:<p>{TODO: 用一句话描述}
 */
public class Pocket {

    private static OkHttpClient mHttpClientHttps; //全部请求使用同一个OkHttpClient
    private Call mCall; //保存最后一次请求的Call

    private HttpUrl mHttpUrl;//API 的url
//    private int mRequestCode;//请求代码，区分同一个API 的不同请求
    private PocketListener mListener;

    private Context mContext;
    private Class<?> mCallbackCls;
    private int versionCode;
    private String versionName;
    private int platform = Constant.APP_PLATFORM;
    private int mRequestCode;//请求代码，区分同一个API 的不同请求

    public Pocket() {
        this(null);
    }

    public Pocket(Context context) {
        this(context, null);
    }

    /**
     * 默认超时时间20秒
     *
     * @param context     上下文参数
     * @param callbackCls 回调
     */
    public Pocket(Context context, Class<?> callbackCls) {
        this(context, callbackCls, 20);
    }

    public Pocket(Context context, Class<?> callbackCls, int timeout) {
        mCallbackCls = callbackCls;
        mContext = context;
        if (mHttpClientHttps == null) {
            mHttpClientHttps = new HttpsHelp(context).builder()
                    .connectTimeout(timeout, TimeUnit.SECONDS)
                    .readTimeout(timeout, TimeUnit.SECONDS)
                    .writeTimeout(timeout, TimeUnit.SECONDS)
                    .build();
        }
        versionCode = Constant.getVersionCode(mContext);
        versionName = Constant.getVersionName(mContext);

    }

    /**
     * 取消请求
     */
    private void cancel() {
        if (mCall != null && !mCall.isCanceled())
            mCall.cancel();
    }

//    /**
//     * post 请求
//     *
//     * @param listener    监听器
//     * @param param       请求参数
//     */
//    public void post(PocketListener listener, PocketParameter param , String url ,  int requestCode) {
//        cancel();//取消上次的请求
//
//        mHttpUrl = HttpUrl.parse(url);
//        mListener = listener;
//        mRequestCode = requestCode;
//
//        PocketParameter parameter = new PocketParameter();
//
//
//        // 添加公共参数
//        param.add("versionCode",versionCode+"");
//        param.add("versionName",versionName);
//        param.add("platform",platform+"");
//        param.add("appId", SystemUtil.APP_ID+"");
//
//        Request request = new Request.Builder()
//                .url(mHttpUrl)
//                .header("Content-Type", "application/x-apps.koudai_webapp.www-form-urlencoded;charset=utf-8")
//                .post(param.getRequestBody())
//                .build();
//        mCall = mHttpClientHttps.newCall(request);
//        mCall.enqueue(newCallback());
//        DebugUtil.d("url -- " + mHttpUrl.toString() + "?" + param.toString());
//    }

    /**
     * post 请求
     *
     * @param listener    监听器
     * @param params       请求参数
     */
    public void post(PocketListener listener,Map<String, String> params  , String url ,  int requestCode) {

        cancel();//取消上次的请求

        mHttpUrl = HttpUrl.parse(url);
        mListener = listener;
        mRequestCode = requestCode;

        PocketParameter parameter = new PocketParameter();
        if(params != null){
            Set<String> keys = params.keySet();
            Iterator<String> iterator = keys.iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                parameter.add(key , params.get(key));
            }
        }

        // 添加公共参数
        parameter.add("versionCode",versionCode+"");
        parameter.add("versionName",versionName);
        parameter.add("platform",platform+"");
        parameter.add("appId", Constant.APP_ID+"");

        Request request = new Request.Builder()
                .url(mHttpUrl)
                .header("Content-Type", "application/x-apps.koudai_webapp.www-form-urlencoded;charset=utf-8")
                .post(parameter.getRequestBody())
                .build();
        mCall = mHttpClientHttps.newCall(request);
        mCall.enqueue(newCallback());
        DebugUtil.d("url -- " + mHttpUrl.toString() + "?" + parameter.toString());
    }

    /**
     * get 请求
     *
     * @param listener    监听器
     * @param params      请求参数
     * @param requestCode 请求代码
     */
    public void get(PocketListener listener , String url , Map<String, String> params , int requestCode) {
        cancel();//取消上次的请求
        mListener = listener;
        mRequestCode = requestCode;
        mHttpUrl = HttpUrl.parse(url);
        if (params == null ) params = new HashMap<>();
        // 添加公共参数
        params.put("versionCode",versionCode+"");
        params.put("versionName",versionName);
        params.put("platform",platform+"");
        params.put("appId", Constant.APP_ID+"");
        Set<String> keys = params.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            mHttpUrl = mHttpUrl.newBuilder().addQueryParameter(key, params.get(key)).build();
        }
        Request request = new Request.Builder()
                .url(mHttpUrl)
                .header("Content-Type", "application/json;charset=utf-8")
                .get()
                .build();
        mCall = mHttpClientHttps.newCall(request);
        mCall.enqueue(newCallback());
    }

    public String getUrl() {
        return mHttpUrl.toString();
    }

    private Callback newCallback() {
        if (mCallbackCls == null) {
            return new HttpCallback(mContext, mRequestCode, mListener);
        }
        try {
            Callback callback = (Callback) mCallbackCls.getConstructor(Context.class, int.class, PocketListener.class)
                    .newInstance(mContext, mRequestCode, mListener);
            return callback;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
