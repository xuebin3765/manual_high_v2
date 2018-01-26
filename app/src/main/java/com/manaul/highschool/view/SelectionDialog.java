package com.manaul.highschool.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.manaul.highschool.main.R;

/**
 * 选择弹出对话框
 * @author stlshen
 *
 */
public class SelectionDialog extends Dialog {

	private OnBtnListener mBtnListener;
	private LinearLayout mConfirmAddressBtn;
	private View mDivider;
	private LinearLayout mCancelAddressBtn;
	private TextView mBtnConfirm;
	private TextView mBtnCancel;
	private TextView mTv_title;
	private TextView mTextTitle;
	private String mConfirm;
	private String mCancel;
	private String mContent;
	private String mTitle;

	public SelectionDialog(Context context) {
		super(context, R.style.BaseDialog);
		initViews();
		initListener();
	}

	public SelectionDialog(Context context, String title) {
		super(context, R.style.BaseDialog);
		initViews();
		initListener();
        setContent(title);
	}

	public SelectionDialog(Context context, String title, OnConfirmListener listener) {
		super(context, R.style.BaseDialog);
		initViews();
		initListener();
        setContent(title);
        setOnConfirmListener(listener);
	}

    public static void show(Context context, String title, OnConfirmListener listener){
        new SelectionDialog(context,title,listener).show();
    }


    public static void show(Context context, String title){
        SelectionDialog dialog = new SelectionDialog(context,title,()->{});
		dialog.mCancelAddressBtn.setVisibility(View.GONE);
		dialog.mDivider.setVisibility(View.GONE);
		dialog.show();
    }

	private void initViews() {

		this.setCancelable(true);
		this.setCanceledOnTouchOutside(false);
		this.setContentView(R.layout.dlg_delete_address);
		mConfirmAddressBtn = (LinearLayout) findViewById(R.id.ll_btn_confirm_address);
		mCancelAddressBtn = (LinearLayout) findViewById(R.id.ll_btn_cancle_address);
		mBtnConfirm= (TextView) findViewById(R.id.btn_confirm_tv);
		mBtnCancel = (TextView) findViewById(R.id.btn_cancle_tv);
		mTv_title = (TextView) findViewById(R.id.title);
		mTextTitle= (TextView) findViewById(R.id.tv_show_text);
		mDivider= findViewById(R.id.divider);
	}


	private void initListener() {
		mConfirmAddressBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mBtnListener != null) {
					dismiss();
					mBtnListener.confirm();
				}
			}
		});
		mCancelAddressBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mBtnListener != null) {
					dismiss();
					mBtnListener.cancel();
				}
			}
		});
	}
	public String getConfirm() {
		return mConfirm;
	}

	public void setConfirm(String mConfirm) {
		this.mConfirm = mConfirm;
		mBtnConfirm.setText(this.mConfirm);
	}

	public String getCancel() {
		return mCancel;
	}

	public void setCancel(String mCancel) {
		this.mCancel = mCancel;
		mBtnCancel.setText(mCancel);
	}

	public String getContent() {
		return mContent;
	}

	public SelectionDialog setContent(String content) {
		this.mContent = content;
		mTextTitle.setText(content);
		return this;
	}

	public String getmTitle() {
		return mTitle;
	}

	public void setmTitle(String mTitle) {
		this.mTitle = mTitle;
		this.mTv_title.setVisibility(View.VISIBLE);
		this.mTv_title.setText(mTitle);
	}

	/*设置点击消失?*/
	public void setIsCancelable(boolean isCancelable) {
		this.setCancelable(isCancelable);
	}

	public void setOnBtnListener(OnBtnListener listener) {
		this.mBtnListener = listener;
	}

	public SelectionDialog setOnConfirmListener(OnConfirmListener listener) {
        if(listener == null){
            mBtnListener = null;
            return this;
        }
        mBtnListener = new OnBtnListener() {
            @Override
            public void confirm() {
                    listener.confirm();
            }

            @Override
            public void cancel() {

            }
        };
		return  this;
	}

	public SelectionDialog hideCancel(){
		mCancelAddressBtn.setVisibility(View.GONE);
		mDivider.setVisibility(View.GONE);
		return this;
	}

	public interface OnBtnListener {
		public void confirm();
		public void cancel();
	}

	public interface OnConfirmListener {
		void confirm();
	}

	public void show() {
		super.show();
	}

}
