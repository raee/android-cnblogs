package com.rae.cnblogs.widget.compat;

import android.content.DialogInterface;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;

/**
 * 可拖拽的卡片
 * Created by ChenRui on 2017/1/24 0024 17:27.
 */
public class HintCardDragCompat extends ViewDragHelper.Callback {


    private static final int MIN_FLING_VELOCITY = 120; // dips per second
    private final ViewGroup mParentView;
    private View mDragView;
    private static final float mMinFlingVelocitySpeed = 10; // 速度
    private final float mMinFlingVelocity;
    private ViewDragHelper mDragHelper;
    private VelocityTracker mVelocityTracker;
    private int mSrcTop; // 原始位置
    private float mStartY;
    private int mCurrentAction;
    private DialogInterface.OnDismissListener mOnDismissListener;

    public HintCardDragCompat(ViewGroup parentView) {
        mParentView = parentView;
        mMinFlingVelocity = MIN_FLING_VELOCITY * parentView.getResources().getDisplayMetrics().density;
        mDragHelper = ViewDragHelper.create(parentView, 0.5f, this);

    }

    public void setDragView(View dragView) {
        if (dragView == null)
            throw new NullPointerException("drag view can't be null!");
        if (mDragView == null) {
            dragView.post(new Runnable() {
                @Override
                public void run() {
                    int[] location = new int[2];
                    mDragView.getLocationOnScreen(location);
                    mSrcTop = location[1];
                }
            });
        }
        mDragView = dragView;
    }


    private int getParentHeight() {
        return mParentView.getHeight();
    }

    private int getParentPaddingTop() {
        return mParentView.getPaddingTop();
    }


    @Override
    public boolean tryCaptureView(View child, int pointerId) {
        return child == mDragView;
    }

    @Override
    public int clampViewPositionVertical(View child, int top, int dy) {
        return top;
    }

    @Override
    public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
        super.onViewPositionChanged(changedView, left, top, dx, dy);
        mParentView.invalidate();
    }

    public void smoothScrollBy(int dy) {
        if (mDragHelper.smoothSlideViewTo(mDragView, 0, dy)) {
            ViewCompat.postInvalidateOnAnimation(mParentView);
        }
    }

    public void computeScroll() {
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(mParentView);
        } else {
            if (mCurrentAction == MotionEvent.ACTION_UP || mCurrentAction == MotionEvent.ACTION_CANCEL) {
                float y = mDragView.getTop();
                if (mOnDismissListener != null && (y <= 0 || Math.abs(y / mDragView.getHeight()) >= 1)) {
                    mOnDismissListener.onDismiss(null);
                }
            }
        }
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        return false;
    }

    public void processTouchEvent(MotionEvent ev) {
        mDragHelper.processTouchEvent(ev);

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        }

        mVelocityTracker.addMovement(ev);
        mCurrentAction = ev.getAction();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mStartY = mDragView.getY();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mVelocityTracker.computeCurrentVelocity(1);
                float yVelocity = mVelocityTracker.getYVelocity();
                int dy = mSrcTop;
                float y = mDragView.getY();
                float diff = Math.abs(y - mStartY);
                int height = mDragView.getHeight() / 2;


                if (diff > mMinFlingVelocity || yVelocity > mMinFlingVelocitySpeed) {
                    // 判断是滚动方向在上面还是下面

                    // 在上面
                    if (y < height) {
                        dy = -mDragView.getHeight();
                    } else {
                        // 在下面
                        dy = getParentHeight();
                    }
                    smoothScrollBy(dy);
                }
                // 点击事件
                else if (Math.abs(mStartY - y) == 0 || dy <= 0) {
                    break;
                } else {
                    reset();
                    smoothScrollBy(dy);
                }
                break;
        }
    }

    public boolean shouldInterceptTouchEvent(MotionEvent ev) {
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener listener) {
        mOnDismissListener = listener;
    }

    public void reset() {
        mCurrentAction = 0;
    }
}
