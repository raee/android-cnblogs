package com.rae.cnblogs.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.rae.cnblogs.widget.compat.HintCardDragCompat;

/**
 * 可拖拽的布局
 * Created by ChenRui on 2017/1/24 0024 17:24.
 */
public class HintCardLayout extends FrameLayout {

    private HintCardDragCompat mCardDragCompat;

    public HintCardLayout(Context context) {
        super(context);
        initView();
    }

    public HintCardLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public HintCardLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
    }


    @Override
    public void computeScroll() {
        mCardDragCompat.computeScroll();
    }

    public boolean dispatchTouchEventSupport(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mCardDragCompat = new HintCardDragCompat((ViewGroup) getRootView());
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        mCardDragCompat.dispatchTouchEvent(ev);

        return this.dispatchTouchEventSupport(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mCardDragCompat.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mCardDragCompat.processTouchEvent(event);
        return true;
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener listener) {
        mCardDragCompat.setOnDismissListener(listener);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mCardDragCompat.setDragView(getChildAt(0));
        mCardDragCompat.reset();
    }
}
