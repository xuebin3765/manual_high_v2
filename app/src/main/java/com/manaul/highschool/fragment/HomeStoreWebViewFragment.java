package com.manaul.highschool.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.manaul.highschool.main.R;
import com.manaul.highschool.newwork.PocketDownloader;
import com.manaul.highschool.utils.DebugUtil;
import com.manaul.highschool.utils.SystemUtil;
import com.manaul.highschool.utils.ToastUtil;

/**
 * Author: vector.huang
 * Email: 642378415@qq.com
 * Date: 2015/12/31
 * Description:<p>{TODO: 用一句话描述}
 */
public class HomeStoreWebViewFragment extends BaseFragment implements DownloadListener {

    private String mUrl;
    private WebView mWebView;
    private WebChromeClient mWebChromeClient;
    private WebViewClient mWebViewClient;

    public static HomeStoreWebViewFragment newInstance(String url) {
        HomeStoreWebViewFragment webView = new HomeStoreWebViewFragment();
        webView.mUrl = url;
        return webView;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        init(R.layout.home_store_web_view_fragment, inflater, container);
        return mParent;
    }

    @Override
    public void onInitData() {
        mWebView = (WebView) mParent.findViewById(R.id.web_view);
        WebSettings webSettings = mWebView.getSettings();
        // 设置WebView属性，能够执行Javascript脚本
        webSettings.setJavaScriptEnabled(true);
        // 设置禁止访问文件数据
        webSettings.setAllowFileAccess(false);
        // 设置不支持缩放
        webSettings.setBuiltInZoomControls(false);
        // 设置适应屏幕
        webSettings.setUseWideViewPort(true); // 置webView推荐使用的窗口，设置为true
        webSettings.setLoadWithOverviewMode(true); // 设置webView加载的页面的模式，也设置为true.
        // 开启 DOM storage API 功能
        webSettings.setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT >= 21) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        mWebView.setDownloadListener(this);

        if (mWebChromeClient != null) {
            mWebView.setWebChromeClient(mWebChromeClient);
        }
        if (mWebViewClient != null) {
            mWebView.setWebViewClient(mWebViewClient);
        }
        if (mUrl != null && !mUrl.isEmpty()) {
            mWebView.loadUrl(mUrl);
        }
    }

    public void loadUrl(String url) {
        mUrl = url;
        mWebView.loadUrl(mUrl);
    }

    public void goBack() {
        mWebView.goBack();
    }

    public boolean canGoBack() {
        return mWebView.canGoBack();
    }

    public void setWebChromeClient(WebChromeClient client) {
        mWebChromeClient = client;
    }

    public void setWebViewClient(WebViewClient client) {
        mWebViewClient = client;
    }

    public void reload() {
        mWebView.reload();
    }

    @Override
    public void onDestroy() {
        if (mWebView != null) {

            //((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.removeAllViews();
            mWebView.destroy();
            DebugUtil.d("ondestory 置null");
            mWebView = null;
            mWebChromeClient = null;
            mWebViewClient = null;
        }
        super.onDestroy();
    }

    String mOldUrl;

    /* 下载文件 */
    @Override
    public void onDownloadStart(String url,
                                String userAgent,
                                String contentDisposition,
                                String mimetype,
                                long contentLength) {
        if (url == null || url.isEmpty()) {
            return;
        }

        if (mOldUrl != null && mOldUrl.equals(url)) {
            /* 通一次的下载 */
            return;
        }

        mOldUrl = url;
        /* 10s 内无法下载同一个 */
        mWebView.postDelayed(() -> mOldUrl = null, 10000);

        /* url 提取文件名 */
        String filename = url.substring(url.lastIndexOf("/") + 1).trim();
        if (filename.isEmpty()) {
            filename = SystemUtil.APP_LOCAL_DATA + System.currentTimeMillis();
        }

        /* 开始下载 */
        PocketDownloader pocketDownloader = new PocketDownloader(getActivity());
        pocketDownloader.download(url, SystemUtil.APP_LOCAL_DATA+"/"+filename,
                mimetype);
        ToastUtil.toast(getActivity(),"后台下载中...");
        /* 打开下载View */
        mWebView.postDelayed(() -> pocketDownloader.openView(), 2000);
    }
}
