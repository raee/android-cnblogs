package com.rae.cnblogs.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.rae.cnblogs.R;

/**
 * Loading and Empty view
 * Created by ChenRui on 2016/11/10 0010 14:28.
 */
public class PlaceholderView extends FrameLayout {

    private View mEmptyView;
    private View mLoadingView;
    private ImageView mEmptyImageView;
    private TextView mEmptyMessageView;
    private Drawable mDefaultEmptyIcon;
    private View mRetryView;

    public PlaceholderView(Context context) {
        super(context);
        initView();
    }

    public PlaceholderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        initAttr(attrs);
    }

    public PlaceholderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PlaceholderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }


    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_placeholder, this);
        mEmptyView = findViewById(R.id.ll_placeholder_empty);
        mLoadingView = findViewById(R.id.ll_placeholder_loading);
        mEmptyImageView = (ImageView) findViewById(R.id.img_placeholder_empty);
        mEmptyMessageView = (TextView) findViewById(R.id.tv_placeholder_empty_message);
        mRetryView = findViewById(R.id.btn_placeholder_retry);
    }

    private void initAttr(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.PlaceholderView);
        int count = a.getIndexCount();
        for (int i = 0; i < count; i++) {
            int index = a.getIndex(i);
            switch (index) {
                case R.styleable.PlaceholderView_mode:
                    int mode = a.getInt(index, 1);
                    switchMode(mode);
                    break;
                case R.styleable.PlaceholderView_empty_message:
                    String msg = a.getString(index);
                    if (!TextUtils.isEmpty(msg)) {
                        setEmptyMessage(msg.equals("null") ? null : msg);
                    }
                    break;
                case R.styleable.PlaceholderView_empty_icon:
                    mDefaultEmptyIcon = a.getDrawable(index);
                    setEmptyIcon(mDefaultEmptyIcon);
                    break;
            }
        }
        a.recycle();
    }

    /**
     * 切换显示类型
     *
     * @param mode 参考attr.xml 定义的PlaceholderView#mode 取值
     */
    private void switchMode(int mode) {
        if (mode == 0) {
            empty();
        } else {
            loading();
        }
    }

    /**
     * show loading view
     */
    public void loading() {
        show();
        mEmptyView.setVisibility(GONE);
        mLoadingView.setVisibility(VISIBLE);
    }

    /**
     * show empty view
     */
    public void empty() {
        show();
        if (mDefaultEmptyIcon == null) {
            mDefaultEmptyIcon = getResources().getDrawable(R.drawable.ic_empty_placeholder);
        }
        setEmptyIcon(mDefaultEmptyIcon);
        mEmptyView.setVisibility(VISIBLE);
        mLoadingView.setVisibility(GONE);
    }

    public void empty(int defaultEmptyIcon) {
        show();
        setEmptyIcon(defaultEmptyIcon);
        mEmptyView.setVisibility(VISIBLE);
        mLoadingView.setVisibility(GONE);
    }

    /**
     * 网络错误
     */
    public void networkError() {
        show();
        setEmptyIcon(R.drawable.ic_network_error_placeholder);
        mRetryView.setVisibility(VISIBLE);
        mEmptyView.setVisibility(VISIBLE);
        mLoadingView.setVisibility(GONE);
    }

    /**
     * show empty view with text
     *
     * @param msg
     */
    public void empty(String msg) {
        setEmptyMessage(msg);
        empty();
    }

    /**
     * empty message
     *
     * @param msg
     */
    public void setEmptyMessage(String msg) {
        mEmptyMessageView.setVisibility(TextUtils.isEmpty(msg) || TextUtils.equals(msg, "@null") ? GONE : VISIBLE);
        mEmptyMessageView.setText(msg);
    }

    /**
     * 重试按钮点击
     *
     * @param listener
     */
    public void setOnRetryClickListener(final OnClickListener listener) {
        mRetryView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                loading();
                listener.onClick(v);
            }
        });
    }

    public void dismiss() {
        setVisibility(GONE);
    }

    public void show() {
        setVisibility(VISIBLE);
    }

    public void setEmptyIcon(Drawable icon) {
        if (icon != null) {
            mEmptyImageView.setImageDrawable(icon);
        }
    }

    public void setEmptyIcon(int resId) {
        if (resId > 0) {
            mEmptyImageView.setImageResource(resId);
        }
    }
}
