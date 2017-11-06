package com.rae.cnblogs.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.rae.cnblogs.R;

import java.lang.ref.WeakReference;

/**
 * 工具类提示信息
 * Created by ChenRui on 2017/11/6 0006 11:46.
 */
public class ToolbarToastView extends RaeTextView implements ValueAnimator.AnimatorUpdateListener {


    public static final int TYPE_REPLY_ME = 12;

    private static class ToolbarToastViewHandler extends Handler {

        private WeakReference<ToolbarToastView> mView;


        public ToolbarToastViewHandler(WeakReference<ToolbarToastView> view) {
            mView = view;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mView == null || mView.get() == null) return;
            if (msg.what == 0) {
                mView.get().dismiss();
            }
        }
    }

    public interface OnDismissListener {
        void onDismiss(ToolbarToastView view);
    }

    private ToolbarToastViewHandler mHandler = new ToolbarToastViewHandler(new WeakReference<>(this));
    private int mType;
    private ValueAnimator mValueAnimator;
    private OnDismissListener mOnDismissListener;


    public ToolbarToastView(Context context) {
        super(context);
    }

    public ToolbarToastView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ToolbarToastView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mValueAnimator == null) {
            mValueAnimator = ValueAnimator.ofInt(getMeasuredHeight());
            mValueAnimator.addUpdateListener(this);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mValueAnimator.removeUpdateListener(this);
        mValueAnimator.cancel();
        mHandler.removeMessages(0);
        mHandler = null;
        mValueAnimator = null;
    }

    public void setOnDismissListener(OnDismissListener onDismissListener) {
        mOnDismissListener = onDismissListener;
    }

    public int getType() {
        return mType;
    }

    public void setType(int type) {
        mType = type;
    }

    /**
     * 显示
     */
    public void show() {
        mHandler.removeMessages(0);
        setTranslationY(0);
        if (this.getVisibility() != View.VISIBLE) {
            setVisibility(View.VISIBLE);
            Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.toolbar_toast_in);
            startAnimation(anim);
        }
    }

    /**
     * 显示
     */
    public void show(String msg) {
        setText(msg);
        show();
    }


    public void dismiss() {
        if (getVisibility() == View.VISIBLE) {
            mValueAnimator.start();
        }
    }

    public void autoShow() {
        show();
        // 自动隐藏
        mHandler.sendEmptyMessageDelayed(0, 5000);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        int value = -(int) animation.getAnimatedValue();
        if (Math.abs(value) >= getMeasuredHeight()) {
            setVisibility(View.GONE);
            if (mOnDismissListener != null) {
                mOnDismissListener.onDismiss(this);
            }
        }
        setTranslationY(value);
    }
}
