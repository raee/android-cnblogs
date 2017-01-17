package com.rae.cnblogs.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

/**
 * 可拖动的视图
 * Created by ChenRui on 2017/1/9 0009 9:14.
 */
public class RaeDrawerLayout extends FrameLayout {


    private ViewDragHelper mDragHelper;
    private View mDrawView;
    private final ViewDrawCallBack mViewDrawCallBack = new ViewDrawCallBack();
    private static final int MIN_FLING_VELOCITY = 200; // dips per second
    private float mMinFlingVelocity;
    private float mScrollPercent;
    private RaeDrawerHandler mDrawerHandler;

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
    protected void onFinishInflate() {
        this.mDrawView = getChildAt(0);
        mMinFlingVelocity = MIN_FLING_VELOCITY * getResources().getDisplayMetrics().density;
    }

    private void initView() {
        mDragHelper = ViewDragHelper.create(this, mViewDrawCallBack);
    }

    // 事件分发
    float mStartY;
    boolean mCanDoDrawer;
    MotionEvent mStartMotionEvent;

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.w("rae", "dispatchTouchEvent - " + ev.getAction());

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartY = ev.getRawY();
                dispatchTouchEventSupport(ev);
                mStartMotionEvent = ev;
                mCanDoDrawer = false;
                return true;
            case MotionEvent.ACTION_MOVE:
                if (mStartY - ev.getRawY() < 0 && mDrawerHandler != null && mDrawerHandler.checkCanDoDrawer(this, ev)) {
                    Log.w("rae", "下");
                    mCanDoDrawer = true;
                    mDragHelper.processTouchEvent(mStartMotionEvent);
                    return true;
                }
                mCanDoDrawer = false;
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
//                mCanDoDrawer = false;
                break;
        }
        return dispatchTouchEventSupport(ev);
    }

    private boolean dispatchTouchEventSupport(MotionEvent ev) {
        return super.dispatchTouchEvent(ev);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.w("rae", "onInterceptTouchEvent - " + ev.getAction() + ";" + mCanDoDrawer);

        boolean result = mDragHelper.shouldInterceptTouchEvent(ev);
        return result | mCanDoDrawer;

//        mDragHelper.shouldInterceptTouchEvent(ev);
//        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.i("rae", "onTouchEvent - " + ev.getAction());
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
//        Log.d("rae", "computeScroll");
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

    public void toggle() {
        if (getVisibility() != View.VISIBLE) {
            swipeUp();
        } else {
            smooth(getHeight());
        }
    }

    public void setDrawerHandler(RaeDrawerHandler drawerHandler) {
        mDrawerHandler = drawerHandler;
    }

    public interface RaeDrawerHandler {
        boolean checkCanDoDrawer(RaeDrawerLayout view, MotionEvent event);
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
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);

            mScrollPercent = Math.abs((float) top / mDrawView.getHeight());
//            Log.d("rae", "onViewPositionChanged：" + dy + ";" + mScrollPercent);
            invalidate();

            if (mScrollPercent >= 1) {
                swipeComplete();
            }
        }

    }
}
