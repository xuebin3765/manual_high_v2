package com.manaul.highschool.utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Administrator on 2017/8/7.
 */

public class ProgressDialogUtils {
    private Context mContext;
    private ProgressDialog dialog;

    public ProgressDialogUtils(Context context) {
        this.mContext = context;
    }

    public void show(String message) {
        try {
            this.hideDialog();

            if (dialog == null) {
                dialog = new ProgressDialog(mContext);
                dialog.setCancelable(true);
            }
            dialog.setMessage(message);
            dialog.show();
        } catch (Exception e) {
            // 在其他线程调用dialog会报错
        }
    }

    public void hideDialog() {
        if (dialog != null && dialog.isShowing()){
            try {
                dialog.dismiss();
            } catch (Exception e) {
            }
        }
    }
}
