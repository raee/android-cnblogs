package com.rae.cnblogs.dialog.impl;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.rae.cnblogs.dialog.IAppDialog;
import com.rae.cnblogs.dialog.IAppDialogClickListener;

/**
 * 弹出窗口
 * Created by ChenRui on 2017/1/24 0024 14:09.
 */
public abstract class AppDialog extends Dialog implements IAppDialog {

    public AppDialog(Context context) {
        super(context);
        initDialog();
    }

    public AppDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initDialog();
    }

    public AppDialog(Context context, int themeResId) {
        super(context, themeResId);
        initDialog();
    }

    protected void initDialog() {
        setOnCancelListener((IAppDialogClickListener) null);
        setOnEnSureListener(null);
    }

    @Override
    public void setTitle(String title) {

    }

    @Override
    public void setMessage(String message) {

    }

    @Override
    public void setImage(int type, String url) {

    }

    @Override
    public void setButtonVisibility(int buttonType, int visibility) {
        switch (buttonType) {
            case IAppDialog.BUTTON_NEGATIVE:
                setCancelButtonVisibility(visibility);
                break;
            case IAppDialog.BUTTON_POSITIVE:
                setEnsureButtonVisibility(visibility);
                break;
        }
    }

    @Override
    public void setOnClickListener(int buttonType, IAppDialogClickListener listener) {
        switch (buttonType) {
            case IAppDialog.BUTTON_NEGATIVE:
                setOnCancelListener(listener);
                break;
            case IAppDialog.BUTTON_POSITIVE:
                setOnEnSureListener(listener);
                break;
        }
    }

    /**
     * 设置取消按钮点击
     *
     * @param listener
     */
    public void setOnCancelListener(IAppDialogClickListener listener) {

    }

    @Override
    public void setButtonText(int buttonType, String text) {
        switch (buttonType) {
            case IAppDialog.BUTTON_NEGATIVE:
                setCancelText(text);
                break;
            case IAppDialog.BUTTON_POSITIVE:
                setEnSureText(text);
                break;
        }
    }

    protected View.OnClickListener newClickListener(int buttonType, IAppDialogClickListener listener) {
        return new AppDialogClickListener(this, buttonType, listener);
    }

    /**
     * 设置取消文本
     */
    public void setCancelText(String text) {

    }

    /**
     * 设置确定文本
     *
     * @param text
     */
    public void setEnSureText(String text) {
    }

    /**
     * 设置确定按钮点击
     */
    public void setOnEnSureListener(IAppDialogClickListener listener) {
    }

    public void setCancelButtonVisibility(int visibility) {
    }

    public void setEnsureButtonVisibility(int visibility) {
    }

    protected class AppDialogClickListener implements View.OnClickListener {

        private final IAppDialog mDialog;
        private IAppDialogClickListener mListener;
        private int mButtonType;

        public AppDialogClickListener(IAppDialog dialog, int buttonType, IAppDialogClickListener listener) {
            this.mButtonType = buttonType;
            mListener = listener;
            mDialog = dialog;
        }

        @Override
        public void onClick(View v) {
            if (mListener == null) {
                mDialog.dismiss();
                return;
            }
            mListener.onClick(mDialog, mButtonType);
        }
    }

}
