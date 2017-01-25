package com.rae.cnblogs.widget.compat;

import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;

/**
 * 向下可拖拽的
 * Created by ChenRui on 2017/1/18 0018 9:56.
 */
public class RaeDragDownCompat extends ViewDragHelper.Callback {

    public interface DragDownHandler {

        /**
         * 检查是否可以拖拽
         *
         * @param dy 开始坐标和当前坐标距离
         * @param ev 当前手势
         */
        boolean checkCanDrag(float dy, MotionEvent ev);
    }

    private static final int MIN_FLING_VELOCITY = 180; // dips per second

    private static final String TAG = "RAE.RaeDragDownCompat";
    protected final ViewGroup mParentView;
    private View mDragView;
    private static final float mMinFlingVelocitySpeed = 10; // 速度
    private final float mMinFlingVelocity;
    private float mScrollPercent;
    private float mStartY;
    private MotionEvent mStartMotionEvent;

    private ViewDragHelper mDragHelper;

    private VelocityTracker mVelocityTracker;

    private DragDownHandler mDragDownHandler;


    public RaeDragDownCompat(ViewGroup parentView) {
        mParentView = parentView;
        mMinFlingVelocity = MIN_FLING_VELOCITY * parentView.getResources().getDisplayMetrics().density;
        mDragHelper = ViewDragHelper.create(parentView, 0.5f, this);

    }

    public void setDragView(View dragView) {
        if (dragView == null)
            throw new NullPointerException("drag view can't be null!");
        mDragView = dragView;
    }

    public void setDragDownHandler(DragDownHandler dragDownHandler) {
        mDragDownHandler = dragDownHandler;
    }

    private int getParentHeight() {
        return mParentView.getHeight();
    }

    private int getParentPaddingTop() {
        return mParentView.getPaddingTop();
    }

    public int getDragViewTop() {
        return mDragView.getTop();
    }

    @Override
    public boolean tryCaptureView(View child, int pointerId) {
        return child == mDragView;
    }

    @Override
    public int clampViewPositionVertical(View child, int top, int dy) {
        final int topBound = getParentPaddingTop();
        final int bottomBound = getParentHeight();
        return Math.min(Math.max(top, topBound), bottomBound);
    }

    @Override
    public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
        super.onViewPositionChanged(changedView, left, top, dx, dy);
        mScrollPercent = Math.abs((float) top / mDragView.getHeight());
        mParentView.invalidate();
        if (mScrollPercent >= 1) {
            dragComplete();
        }
    }

    public void smoothScrollBy(int dy) {
        if (mDragHelper.smoothSlideViewTo(mDragView, 0, dy)) {
            ViewCompat.postInvalidateOnAnimation(mParentView);
        }
    }

    public void smoothScrollToTop() {
//        Log.d(TAG, "smoothScrollToTop");
        smoothScrollBy(0);
    }

    public void smoothScrollToBottom() {
        smoothScrollBy(getParentHeight());
    }

    public void toggleSmoothScroll() {
        if (mDragView.getTop() >= getParentHeight() || mParentView.getVisibility() != View.VISIBLE) {
            mParentView.setVisibility(View.VISIBLE);
            smoothScrollToTop();
        } else {
            smoothScrollToBottom();
        }
    }

    public void computeScroll() {

//        Log.d(TAG, "computeScroll");
        if (mDragHelper.continueSettling(true)) {
            Log.d(TAG, "computeScroll");
            ViewCompat.postInvalidateOnAnimation(mParentView);
        }
    }

    // 完成拖拽
    private void dragComplete() {
        mParentView.setVisibility(View.GONE);
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (mDragDownHandler == null) return false;

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartY = ev.getRawY();
                mStartMotionEvent = ev;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mDragDownHandler != null && mDragDownHandler.checkCanDrag(mStartY - ev.getRawY(), ev)) {
                    doDrag(mStartMotionEvent);
                    return true;
                }
                break;
        }

        return false;
    }


    public void processTouchEvent(MotionEvent ev) {
        mDragHelper.processTouchEvent(ev);
//        Log.d(TAG, "processTouchEvent action = " + action);

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }

        mVelocityTracker.addMovement(ev);

        switch (ev.getAction()) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mVelocityTracker.computeCurrentVelocity(1);
                float yVelocity = mVelocityTracker.getYVelocity();
                int dy = 0;
                float y = mDragView.getY();
                if (y > mMinFlingVelocity || yVelocity > mMinFlingVelocitySpeed) {
                    dy = getParentHeight();
                }
//                Log.w("rae", "yVelocity = " + yVelocity);
                smoothScrollBy(dy);
                break;
        }
    }

    public boolean shouldInterceptTouchEvent(MotionEvent ev) {
        boolean b = mDragHelper.shouldInterceptTouchEvent(ev);
//        Log.d(TAG, "shouldInterceptTouchEvent = " + b + "; action = " + ev.getAction());
        return b;
    }

    public float getScrollPercent() {
        return mScrollPercent;
    }

    public void doDrag(MotionEvent startMotionEvent) {

        if (startMotionEvent != null) {
            mDragHelper.captureChildView(mDragView, startMotionEvent.getPointerId(0));
        }

    }
}
