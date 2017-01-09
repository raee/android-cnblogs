package com.rae.cnblogs.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * 可拖动的视图
 * Created by ChenRui on 2017/1/9 0009 9:14.
 */
public class RaeDrawerLayout extends DrawerLayout {

    private ViewDragHelper mDragHelper;
    private View mDrawView;
    private final ViewDrawCallBack mViewDrawCallBack = new ViewDrawCallBack();
    private static final int MIN_FLING_VELOCITY = 200; // dips per second
    private float mMinFlingVelocity;
    private float mScrollPercent;

    public RaeDrawerLayout(Context context) {
        super(context);
        initView();
    }

    public RaeDrawerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public RaeDrawerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        this.mDrawView = getChildAt(0);
        mMinFlingVelocity = MIN_FLING_VELOCITY * getResources().getDisplayMetrics().density;
    }

    private void initView() {
        mDragHelper = ViewDragHelper.create(this, mViewDrawCallBack);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        mDragHelper.processTouchEvent(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                smooth();
                break;
        }
        return super.onTouchEvent(ev);
    }


    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        boolean ret = super.drawChild(canvas, child, drawingTime);


        float op = (1 - mScrollPercent);
        if (op > 0) {
            final int mScrimColor = 0x99000000;
            final int baseAlpha = (mScrimColor & 0xff000000) >>> 24;
            final int alpha = (int) (baseAlpha * (1 - mScrollPercent));
            final int color = alpha << 24;
            canvas.clipRect(0, 0, getWidth(), mDrawView.getTop());
            canvas.drawColor(color);
        }
        return ret;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    public void smooth() {
        int dy = 0;
        float y = mDrawView.getY();
        if (y > mMinFlingVelocity) {
            dy = getHeight();
        }

        smooth(dy);
    }

    public void smooth(int dy) {
        if (mDragHelper.smoothSlideViewTo(mDrawView, 0, dy)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        Log.d("rae", "computeScroll");
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public void swipeComplete() {
        setVisibility(View.GONE);
    }

    public void swipeUp() {
        setVisibility(View.VISIBLE);
        mDragHelper.smoothSlideViewTo(mDrawView, 0, 0);
    }

    private class ViewDrawCallBack extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == mDrawView;
        }

        @Override
        public int clampViewPositionVertical(View child, int top, int dy) {
            final int topBound = getPaddingTop();
            final int bottomBound = getHeight();
            return Math.min(Math.max(top, topBound), bottomBound);
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            Log.w("rae", "onViewReleased:" + yvel);

        }


        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);

            mScrollPercent = Math.abs((float) top / mDrawView.getHeight());
            Log.d("rae", "onViewPositionChanged：" + dy + ";" + mScrollPercent);
            invalidate();

            if (mScrollPercent >= 1) {
                swipeComplete();
            }
        }

    }
}
