package com.rae.cnblogs.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.rae.cnblogs.R;
import com.rae.cnblogs.ThemeCompat;

import skin.support.widget.SkinCompatBackgroundHelper;
import skin.support.widget.SkinCompatSupportable;

/**
 * Loading and Empty view
 * Created by ChenRui on 2016/11/10 0010 14:28.
 */
public class PlaceholderView extends FrameLayout implements SkinCompatSupportable {

    private SkinCompatBackgroundHelper mBackgroundTintHelper;
    private View mEmptyView;
    private View mLoadingView;
    private ImageView mEmptyImageView;
    private TextView mEmptyMessageView;
    private Drawable mDefaultEmptyIcon;
    private View mRetryView;
    private View mContentView;
    private TextView mLoadingTextView;
    private String mLoadingText;

    public PlaceholderView(Context context) {
        super(context);
        initView(null, 0);
    }

    public PlaceholderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(attrs, 0);
        initAttr(attrs);
    }

    public PlaceholderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PlaceholderView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(attrs, defStyleRes);
        mBackgroundTintHelper = new SkinCompatBackgroundHelper(this);
        mBackgroundTintHelper.loadFromAttributes(attrs, defStyleAttr);
    }


    private void initView(AttributeSet attrs, int defStyleAttr) {

        mBackgroundTintHelper = new SkinCompatBackgroundHelper(this);
        mBackgroundTintHelper.loadFromAttributes(attrs, defStyleAttr);

        mContentView = LayoutInflater.from(getContext()).inflate(R.layout.view_placeholder, this, false);
        mEmptyView = mContentView.findViewById(R.id.ll_placeholder_empty);
        mLoadingView = mContentView.findViewById(R.id.ll_placeholder_loading);
        mLoadingTextView = (TextView) mContentView.findViewById(R.id.tv_loading);
        mEmptyImageView = (ImageView) mContentView.findViewById(R.id.img_placeholder_empty);
        mEmptyMessageView = (TextView) mContentView.findViewById(R.id.tv_placeholder_empty_message);
        mRetryView = mContentView.findViewById(R.id.btn_placeholder_retry);
        setBackgroundColor(ContextCompat.getColor(getContext(), R.color.transparent));
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
                case R.styleable.PlaceholderView_app_background:
                    mContentView.setBackground(a.getDrawable(index));
                    break;
                case R.styleable.PlaceholderView_empty_icon:
                    mDefaultEmptyIcon = a.getDrawable(index);
                    setEmptyIcon(mDefaultEmptyIcon);
                    break;
                case R.styleable.PlaceholderView_loading_message:
                    mLoadingText = a.getString(index);
                    mLoadingTextView.setText(mLoadingText);
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
        if (mLoadingText == null)
            mLoadingText = getContext().getString(R.string.loading);
        mLoadingTextView.setText(mLoadingText);
        mEmptyView.setVisibility(GONE);
        mLoadingView.setVisibility(VISIBLE);
    }

    public void loading(String text) {
        loading();
        mLoadingTextView.setText(text);
    }

    /**
     * show newThread view
     */
    public void empty() {
        show();
        if (mDefaultEmptyIcon == null) {
            mDefaultEmptyIcon = ThemeCompat.getDrawable(getContext(), "ic_empty_placeholder");
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

    public void retry(String msg) {
        empty(msg);
        mRetryView.setVisibility(View.VISIBLE);
    }

    /**
     * 网络错误
     */
    public void networkError() {
        show();
        setEmptyIcon(ThemeCompat.getDrawableId(getContext(), "ic_network_error_placeholder"));
        mRetryView.setVisibility(VISIBLE);
        mEmptyView.setVisibility(VISIBLE);
        mLoadingView.setVisibility(GONE);
    }

    /**
     * show newThread view with text
     *
     * @param msg
     */
    public void empty(String msg) {
        setEmptyMessage(msg);
        empty();
    }


    /**
     * newThread message
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
        mContentView.setVisibility(GONE);
    }

    public void show() {
        mContentView.setVisibility(VISIBLE);
    }

    public void setEmptyIcon(Drawable icon) {
        if (icon != null) {
            mEmptyImageView.setImageDrawable(icon);
            mDefaultEmptyIcon = mEmptyImageView.getDrawable();
        }
    }

    public void setEmptyIcon(int resId) {
        if (resId > 0) {
            mEmptyImageView.setImageResource(resId);
            mDefaultEmptyIcon = mEmptyImageView.getDrawable();
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        addView(mContentView);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeView(mContentView);
    }

    /**
     * 是否已经消失
     */
    public boolean isDismiss() {
        return mContentView.getVisibility() != View.VISIBLE;
    }

    /**
     * 注册adapter监听
     *
     * @param adapter
     */
    public void registerAdapterDataObserver(final RecyclerView.Adapter adapter) {
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                if (adapter.getItemCount() > 0) {
                    dismiss();
                } else {
                    empty();
                }
            }
        });
    }

    @Override
    public void applySkin() {
        if (mBackgroundTintHelper != null) {
            mBackgroundTintHelper.applySkin();
        }
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        super.setOnClickListener(l);
        if (mContentView != null)
            mContentView.setOnClickListener(l);
    }
}
