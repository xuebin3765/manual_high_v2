package com.manaul.highschool.main;

import android.content.Context;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.manaul.highschool.fragment.HomeStoreWebViewFragment;
import com.manaul.highschool.fragment.WebViewMenuFragment;
import com.manaul.highschool.manager.UserManager;
import com.manaul.highschool.model.UserModel;
import com.manaul.highschool.utils.DebugUtil;
import com.manaul.highschool.utils.StatusBarUtils;
import com.manaul.highschool.view.SelectionDialog;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

/**
 * Author: xb
 * Date: 2018/1/18
 * Description:<p>{TODO: 网页}
 */
public class CustomWebViewAct extends FragmentActivity implements View.OnClickListener
,WebViewMenuFragment.OnClickListener{

    private ViewHolder mViewHolder;

    public static final String URL = "order_url";

    public static final String NAME = "name";

    private String mUrl;

    private String name;

    private Context mContext;

    private WebViewMenuFragment mWebViewMenuFragment;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        StatusBarUtils.StatusBarLightMode(this);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_webview_layout);
        mContext = this;
        Intent intent = getIntent();
        if (intent != null) {
            name = intent.getStringExtra(NAME);
            mUrl = getUrl(intent.getStringExtra(URL));
        }
        mViewHolder = new ViewHolder(getWindow().getDecorView());
    }

    private String getUrl(String url) {
        if (url==null|| url.isEmpty()) return "";
        UserModel info = UserManager.getUser(mContext);
//        if (info!=null){
//            if (url.contains(Constants.HKFANR_URL)){
//                long time = System.currentTimeMillis();
//                url = url.replace("{1}", Util.getMD5(Constants.HKFANR_URL +
//                        info.getSfmUid().trim() +(time+"").trim()
//                        +Constants.HKFANR_SECRET))
//                        .replace("{2}", "" + time);
//            }
//            url = url.replace("{0}", "" + info.getSfmUid());
//        }
        return url;
    }


    private class ViewHolder {
        @ViewInject(id = R.id.title_bar_back)
        View mBackBtn;
        @ViewInject(id=R.id.close_btn)
        View mCloseBtn;
        @ViewInject(id = R.id.title_bar_home)
        View mHomeBtn;
        @ViewInject(id = R.id.store_name_txt)
        TextView mName;
        @ViewInject(id = R.id.progress_bar)
        ProgressBar mProgressBar;

        private HomeStoreWebViewFragment mWebViewFragment;

        private ViewHolder(View view) {
            FinalActivity.initInjectedView(this, view);
            mBackBtn.setOnClickListener(CustomWebViewAct.this);
            mHomeBtn.setOnClickListener(CustomWebViewAct.this);
            mCloseBtn.setOnClickListener(CustomWebViewAct.this);
            mWebViewMenuFragment = new WebViewMenuFragment();
            mWebViewMenuFragment.setOnClickListener(CustomWebViewAct.this);
            update();
        }

        private void update() {
            mName.setText(name);
            loadWebView(mUrl);
        }

        private void removeWebView() {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (mWebViewFragment != null) {
                transaction.remove(mWebViewFragment).commitAllowingStateLoss();
                getSupportFragmentManager().executePendingTransactions();
            }
        }

        private void loadWebView(String url) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (mWebViewFragment != null) {
                transaction.remove(mWebViewFragment);
            }
            mWebViewFragment = HomeStoreWebViewFragment.newInstance(url);
            mWebViewFragment.setWebChromeClient(new CustomWebChromeClient());
            mWebViewFragment.setWebViewClient(new CustomWebViewClient());
            transaction.replace(R.id.web_view_container, mWebViewFragment).commitAllowingStateLoss();
            getSupportFragmentManager().executePendingTransactions();
        }
    }

    private class CustomWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                mViewHolder.mProgressBar.setVisibility(View.GONE);
            } else {
                if (mViewHolder.mProgressBar.getVisibility() == View.GONE) {
                    mViewHolder.mProgressBar.setVisibility(View.VISIBLE);
                }
                mViewHolder.mProgressBar.setProgress(newProgress);
            }
        }

        /**
         * 覆盖默认的window.alert展示界面，避免title里显示为“：来自file:////”
         */
        public boolean onJsAlert(WebView view, String url, String message,
                                 JsResult result) {

            SelectionDialog.show(CustomWebViewAct.this,message);
            result.confirm();// 因为没有绑定事件，需要强行confirm,否则页面会变黑显示不了内容。
            return true;
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
//            if (title!=null&&!title.isEmpty()){
//                mViewHolder.mName.setText(title);
//            }
        }


    }

    private class CustomWebViewClient extends WebViewClient {
        //重写父类方法，让新打开的网页在当前的WebView中显示
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
        @Override
        public void onPageFinished(WebView view, String url) {
//            if (mViewHolder.mWebViewFragment.canGoBack()) {
//                mViewHolder.mCloseBtn.setVisibility(View.GONE);
//            } else {
//                mViewHolder.mCloseBtn.setVisibility(View.VISIBLE);
//            }
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//            super.onReceivedSslError(view, handler, error);
//            Log.e("xie","ssl_error");
            handler.proceed();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close_btn:
                finish();
                break;
            case R.id.title_bar_back:
                if (mViewHolder.mWebViewFragment.canGoBack()) {
                    mViewHolder.mWebViewFragment.goBack();
                    mViewHolder.mCloseBtn.setVisibility(View.VISIBLE);
                } else {
                    finish();
                }
                break;
            case R.id.title_bar_home:
                if (mWebViewMenuFragment==null) return;
                mWebViewMenuFragment.show(getSupportFragmentManager(),"");
        }
    }

    @Override
    public void toHome() {
        if (mViewHolder.mWebViewFragment==null) return;
        if (!mViewHolder.mWebViewFragment.canGoBack()) {
            mViewHolder.mWebViewFragment.reload();
            return;
        }
        while (mViewHolder.mWebViewFragment.canGoBack()) {
            mViewHolder.mWebViewFragment.goBack();
        }
    }

    @Override
    public void refresh() {
        if (mViewHolder.mWebViewFragment==null) return;
        mViewHolder.mWebViewFragment.reload();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DebugUtil.d("ondestory 置null");
        mViewHolder.mWebViewFragment = null;
    }
}
