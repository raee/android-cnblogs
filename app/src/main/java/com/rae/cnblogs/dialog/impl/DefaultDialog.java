package com.rae.cnblogs.dialog.impl;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.rae.cnblogs.R;
import com.rae.cnblogs.dialog.IAppDialog;
import com.rae.cnblogs.dialog.IAppDialogClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 默认的对话框类型
 * Created by ChenRui on 2017/1/24 0024 14:42.
 */
public class DefaultDialog extends AppDialog {

    @BindView(R.id.tv_title)
    TextView mMessageView;

    @BindView(R.id.btn_cancel)
    TextView mCancelView;

    @BindView(R.id.btn_ensure)
    TextView mEnSureView;
    private View mContentView;

    public DefaultDialog(Context context) {
        super(context);
    }

    @Override
    protected void initDialog() {
        initWindowAttr(getWindow());
        initView();
        super.initDialog();
    }

    /**
     * 初始化视图
     */
    private void initView() {
        setContentView(R.layout.dialog_blog_default);
        mContentView = findViewById(android.R.id.content);
        ButterKnife.bind(this);

    }

    /**
     * 初始化窗口类型
     */
    protected void initWindowAttr(Window window) {
        if (window == null) return;

        window.requestFeature(Window.FEATURE_NO_TITLE);
        WindowManager.LayoutParams lp = window.getAttributes();
        window.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT | Gravity.RIGHT);
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

        int margin = (int) getContext().getResources().getDimension(R.dimen.default_dialog_margin);
        InsetDrawable drawable = new InsetDrawable(new ColorDrawable(Color.TRANSPARENT), margin, 0, margin, 0);
        window.setBackgroundDrawable(drawable);
    }

    @Override
    public void show() {
        super.show();
        Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.scale_in);
        mContentView.startAnimation(anim);
    }

    @Override
    public void setTitle(String title) {
        setMessage(title);
    }

    @Override
    public void setMessage(String message) {
        mMessageView.setText(message);
    }

    @Override
    public void setCancelText(String text) {
        mCancelView.setText(text);
    }

    @Override
    public void setEnSureText(String text) {
        mEnSureView.setText(text);
    }

    @Override
    public void setOnCancelListener(IAppDialogClickListener listener) {
        mCancelView.setOnClickListener(newClickListener(IAppDialog.BUTTON_NEGATIVE, listener));
    }

    @Override
    public void setOnEnSureListener(IAppDialogClickListener listener) {
        mEnSureView.setOnClickListener(newClickListener(IAppDialog.BUTTON_POSITIVE, listener));
    }

    @Override
    public void setEnsureButtonVisibility(int visibility) {
        mEnSureView.setVisibility(visibility);
    }

    @Override
    public void setCancelButtonVisibility(int visibility) {
        mCancelView.setVisibility(visibility);
    }
}
