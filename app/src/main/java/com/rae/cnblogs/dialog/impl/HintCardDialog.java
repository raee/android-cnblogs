package com.rae.cnblogs.dialog.impl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.rae.cnblogs.R;
import com.rae.cnblogs.RaeImageLoader;
import com.rae.cnblogs.dialog.IAppDialog;
import com.rae.cnblogs.dialog.IAppDialogClickListener;
import com.rae.cnblogs.widget.HintCardLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 卡片式的对话框
 * Created by ChenRui on 2017/1/24 0024 16:29.
 */
public class HintCardDialog extends SlideDialog {

    @BindView(R.id.tv_title)
    TextView mTitleView;

    @BindView(R.id.tv_message)
    TextView mMessageView;

    @BindView(R.id.btn_cancel)
    ImageView mCancelView;

    @BindView(R.id.layout_hint_card)
    HintCardLayout mContentLayout;

    @BindView(R.id.btn_ensure)
    TextView mEnSureView;

    @BindView(R.id.img_dialog_hint)
    ImageView mImageView;

    public HintCardDialog(Context context) {
        super(context);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initDialog() {
        if (getWindow() != null) {
            getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
        }
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        super.initDialog();
        mContentLayout.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                dismiss();
            }
        });
        mCancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    protected int getLayoutId() {
        return R.layout.dialog_hint_card;
    }

    @Override
    protected void onWindowLayout(Window window, WindowManager.LayoutParams attr) {

        window.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT | Gravity.RIGHT);
        attr.height = WindowManager.LayoutParams.MATCH_PARENT;

        int margin = (int) getContext().getResources().getDimension(R.dimen.default_hint_card_dialog_margin);
        InsetDrawable drawable = new InsetDrawable(new ColorDrawable(Color.TRANSPARENT), margin, 0, margin, 0);
        window.setBackgroundDrawable(drawable);
    }

    @Override
    public void setOnEnSureListener(IAppDialogClickListener listener) {
        mEnSureView.setOnClickListener(newClickListener(IAppDialog.BUTTON_POSITIVE, listener));
    }

    @Override
    public void setMessage(String message) {
        mMessageView.setText(message);
    }

    @Override
    public void setTitle(String title) {
        mTitleView.setText(title);
    }

    @Override
    public void setEnSureText(String text) {
        mEnSureView.setText(text);
    }

    @Override
    public void setImage(int type, String url) {
        RaeImageLoader.displayImage(url, mImageView);
    }

    @Override
    public void setEnsureButtonVisibility(int visibility) {
        mEnSureView.setVisibility(visibility);
    }

    public void showCloseButton() {
        mCancelView.setVisibility(View.VISIBLE);
    }

}
