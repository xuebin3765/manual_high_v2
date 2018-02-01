package com.manaul.highschool.newwork;

import android.app.Activity;
import android.content.Context;

import com.manaul.highschool.model.BadRequest;
import com.manaul.highschool.utils.DebugUtil;
import com.manaul.highschool.utils.ToastUtils;

import org.apache.http.conn.ConnectTimeoutException;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 *
 * Created by Administrator on 2018/1/24.
 */

public class HttpCallback implements Callback {
    private PocketListener mListener;
    private Context mContext;
    private int mRequestCode;

    public HttpCallback(Context context, int requestCode , PocketListener listener) {
        mContext = context;
        mListener = listener;
        mRequestCode = requestCode;
    }

    @Override
    public void onFailure(Call call, final IOException e) {
        if (mListener == null) return;

        if (mContext instanceof Activity) {
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int errorCode = StatusCode.CODE_UNKNOWN;
                    String msg = "未知错误";
                    if (e != null && e.getMessage() != null && e.getMessage().equalsIgnoreCase("Canceled")) {
                        errorCode = StatusCode.CODE_CANCEL;
                    }
                    if (e instanceof ConnectException) {
                        errorCode = StatusCode.CODE_CONNECT_ERROR;
                        msg = "网络出错，请检查设置";
                    } else if (e instanceof SocketTimeoutException) {
                        errorCode = StatusCode.CODE_TIMEOUT;
                        msg = "连接超时";
                    } else if (e instanceof ConnectTimeoutException) {
                        errorCode = StatusCode.CODE_TIMEOUT;
                        msg = "连接超时";
                    } else if (e instanceof SocketException) {
                        errorCode = StatusCode.CODE_CANCEL;
                        msg = "请求被取消";
                    } else if (e instanceof UnknownHostException) {
                        errorCode = StatusCode.CODE_CONNECT_ERROR;
                        msg = "网络出错，请检查设置";
                    }
                    if (errorCode != StatusCode.CODE_CANCEL) {
                        try {
                            mListener.onError(errorCode, msg , mRequestCode);
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            });
        } else {
            int errorCode = StatusCode.CODE_UNKNOWN;
            if (e != null && e.getMessage() != null && e.getMessage().equalsIgnoreCase("Canceled")) {
                errorCode = StatusCode.CODE_CANCEL;
            }
            String msg = "未知错误（1.0.1）";
            if (e instanceof ConnectException) {
                errorCode = StatusCode.CODE_CONNECT_ERROR;
                msg = "网络出错，请检查设置";
            } else if (e instanceof SocketTimeoutException) {
                errorCode = StatusCode.CODE_TIMEOUT;
                msg = "连接超时";
            } else if (e instanceof ConnectTimeoutException) {
                errorCode = StatusCode.CODE_TIMEOUT;
                msg = "连接超时";
            } else if (e instanceof SocketException) {
                errorCode = StatusCode.CODE_CANCEL;
                msg = "请求被取消";
            } else if (e instanceof UnknownHostException) {
                errorCode = StatusCode.CODE_CONNECT_ERROR;
                msg = "网络出错，请检查设置";
            }

            if (errorCode != StatusCode.CODE_CANCEL) {
                try {
                    mListener.onError(errorCode, msg , mRequestCode);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        if (mListener == null) return;
        final int code = response.code();
        final String json = response.body().string();
        DebugUtil.d("url -- code = " + code + json);
        if (mContext instanceof Activity) {
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    response(code, json);
                }
            });
        } else {
            response(code, json);
        }
    }

    private void response(int code, final String json) {
        switch (code) {
            case 401:
                mListener.onError(StatusCode.CODE_NOT_LOGIN, "登录失败，请检查账号或密码是否正确" , mRequestCode);
                if (mContext instanceof Activity) {
                    DebugUtil.d("账号已过期，重新登录 json = "+json);
                    new Throwable().printStackTrace();
                    ToastUtils.showToastShort(mContext.getApplicationContext(), "账号已过期，重新登录");
                }
                break;
            case 403: //没有权限调用这个接口
                mListener.onError(StatusCode.CODE_DENIED, "Access is denied" , mRequestCode);
                break;
            case 404:
                mListener.onError(StatusCode.CODE_NOT_FOUND, "pager not found(404)" , mRequestCode);
                break;
            default://200 400
                BadRequest badRequest = BadRequest.createInstanceByJson(json);
                if (badRequest == null) {
                    mListener.onError(StatusCode.CODE_UNKNOWN, "system error(1,1)" , mRequestCode);
                    break;
                }
                if (badRequest.getStatus() == StatusCode.CODE_SUCCESS) {
                    mListener.onOK(json , badRequest.getMessage() , mRequestCode);
                    break;
                }
                mListener.onError(badRequest.getStatus(), badRequest.getMessage() , mRequestCode);
                break;
        }
    }


}
