package com.manaul.highschool.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.manaul.highschool.main.R;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;

/**
 * Author: davin
 * Date: 2016/4/11
 * Description:<p>{TODO: webView右按钮的弹出框}
 */
public class WebViewMenuFragment extends DialogFragment {

    private Context mContext;

    private View mParent;
    private OnClickListener mOnClickListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int style = DialogFragment.STYLE_NORMAL;
        int theme = R.style.DownShowDialog;
        setStyle(style,theme);
        mContext = getActivity();
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams pl = window.getAttributes();
        pl.gravity = Gravity.BOTTOM;
        pl.width = LinearLayout.LayoutParams.MATCH_PARENT;
        window.setAttributes(pl);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mParent = inflater.inflate(R.layout.web_view_menu_fragment,container,false);
        new ViewHolder(mParent);
        return mParent;
    }

    private class ViewHolder implements View.OnClickListener{
        @ViewInject(id = R.id.home_btn)
        TextView mHomeBtn;
        @ViewInject(id = R.id.refresh_btn)
        TextView mRefreshBtn;
        public ViewHolder(View mParent) {
            FinalActivity.initInjectedView(this,mParent);
            mHomeBtn.setOnClickListener(this);
            mRefreshBtn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mOnClickListener==null) return;
            switch (v.getId()){
                case R.id.home_btn:
                    mOnClickListener.toHome();
                    dismiss();
                    break;
                case R.id.refresh_btn:
                    mOnClickListener.refresh();
                    dismiss();
                    break;
            }

        }
    }

    public void setOnClickListener(OnClickListener mOnClickListener){
        this.mOnClickListener = mOnClickListener;

    }

    public interface OnClickListener {
        void toHome();
        void refresh();
    }
}
