package com.manaul.highschool.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.manaul.highschool.main.R;
import com.manaul.highschool.utils.DebugUtil;
import com.manaul.highschool.utils.ToastUtil;

/**
 * Fragment 都是继承这个
 * <p>
 * v4.app.Fragment;
 * <p>
 * Created by vector.huang on 2015/4/10.
 */
public abstract class BaseFragment extends Fragment {
    public String requestCode = "";
    protected View mParent;

//    public ShareV4Fragment mShare;

    public Context mContext;


    /**
     * Fragment当前状态是否可见
     */
    protected boolean isVisible;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }

    /**
     * 初始化parent,如果parent 已经在Activity 的View 容器里面，那么就移除这个parent，让onCreateView 返回的View 再次加入view 容器
     *
     * @param inflater
     * @param container
     */

    public void init(int layoutId, LayoutInflater inflater, ViewGroup container) {
        DebugUtil.d("init -- " + mParent);
        if (mParent == null) {
            mParent = inflater.inflate(layoutId, container, false);
            onInitData();
            initCompleted = true;
            setUserVisibleHint(isVisible);
        }
        if (mParent != null) {
            ViewGroup parentContainer = (ViewGroup) mParent.getParent();
            if (parentContainer != null) {
                parentContainer.removeView(mParent);
            }
        }
    }


    /**
     * 初始化数据，在parent 为空的时候回调,也就是说需要重新映射布局的时候调用
     */
    public void onInitData() {
    }

    protected boolean initCompleted = false;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVisible = getUserVisibleHint();
        if (isVisibleToUser) {
            if (initCompleted) {
                onVisible();
            }
        } else {
            if (initCompleted) {
                onInvisible();
            }
        }
    }

    public void refresh(){}


    public boolean isUserVisible() {
        return isVisible && initCompleted;
    }

    /**
     * 可见
     */
    protected void onVisible() {
    }

    /**
     * 不可见
     */
    protected void onInvisible() {
    }

    public void startActivity(Class<?> cls) {

        startActivity(new Intent(getActivity(), cls));
    }

   /*启动activity的动画*/
    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        getActivity().overridePendingTransition(R.anim.right_in,R.anim.anim_fade_out);
    }

//    private GoLoadingFragment mLoadingFragment;

    public void showDialog() {
        DebugUtil.d("showDialog");
        try {
            hideDialog();
//            mLoadingFragment = new GoLoadingFragment();
//            mLoadingFragment.show(getFragmentManager(), "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideDialog() {
        DebugUtil.d("hideDialog");
        try {
//            if (mLoadingFragment != null) {
//                if (!mLoadingFragment.isHidden()) {
//                    mLoadingFragment.dismissAllowingStateLoss();
//                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 封装了Toast，直接toast（String content）
     * 保证运行在UI线程中
     *
     * @param content content of your want to Toast
     */
    public void toast(String content) {
        getActivity().runOnUiThread(()->{
            ToastUtil.toast(getActivity(), content);
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//            if (MainApplication.getRefWatcher() != null)
//                MainApplication.getRefWatcher().watch(this);
    }
}
