package com.manaul.highschool.callback;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.widget.AbsListView;

import com.manaul.highschool.utils.AndroidUtils;
import com.manaul.highschool.utils.AnimationUtils;

/**
 * Created by Yum on 2016/12/16.
 * 不规则测量高度，用于置顶按钮的显示与隐藏
 */
public class GetScrollYListener implements AbsListView.OnScrollListener{
    View go_top_btn;
    Context context;
    public GetScrollYListener(View go_top_btn, Context context) {
        this.go_top_btn = go_top_btn;
        this.context = context;
    }

    private SparseArray<ItemRecod> recordSp = new SparseArray<ItemRecod>(0);
    private int mCurrentfirstVisibleItem = 0;
    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {
        mCurrentfirstVisibleItem = i;
        View firstView = absListView.getChildAt(0);
        if (null != firstView) {
            ItemRecod itemRecord = recordSp.get(i);
            if (null == itemRecord) {
                itemRecord = new ItemRecod();
            }
            itemRecord.height = firstView.getHeight();
            itemRecord.top = firstView.getTop();
            recordSp.append(i, itemRecord);
        }
        //DebugUtil.d("滑动高度:"+getScrollY());
        if (getScrollY() >= 4* AndroidUtils.getScreenSize(context)[1]) {
            AnimationUtils.show(go_top_btn);
        }
        else {
            AnimationUtils.hide(go_top_btn);
        }
    }
    private int getScrollY() {
        int height = 0;
        for (int i = 0; i < mCurrentfirstVisibleItem; i++) {
            ItemRecod itemRecod = recordSp.get(i);
            if (itemRecod != null)
                height += itemRecod.height;
        }
        ItemRecod itemRecod = recordSp.get(mCurrentfirstVisibleItem);
        if (null == itemRecod) {
            itemRecod = new ItemRecod();
        }
        return height - itemRecod.top;
    }


    class ItemRecod {
        int height = 0;
        int top = 0;
    }
}
