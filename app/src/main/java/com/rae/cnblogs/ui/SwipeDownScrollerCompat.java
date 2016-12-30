package com.rae.cnblogs.ui;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

/**
 * 向下滑动关闭View处理
 * Created by ChenRui on 2016/12/28 23:17.
 */
public class SwipeDownScrollerCompat {

    public interface SwipeDownScrollerHandler {
        boolean canSwipe();

        void onComputeScrollOffset(int offset, float diff);
    }

    /**
     * 判定为拖动的最小移动像素数
     */
    private int mTouchSlop;
    private Scroller mScroller;
    private float mStartY;
    private boolean mEnableSwipe;

    private SwipeDownScrollerHandler mSwipeDownScrollerHandler;
    private View mTargetView;


    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (mScroller.computeScrollOffset()) {
                mTargetView.setTranslationY(mScroller.getCurrY());
                mSwipeDownScrollerHandler.onComputeScrollOffset(mScroller.getCurrY(), getDiff(mScroller.getCurrY(), mScroller.getFinalY()));
                mHandler.sendEmptyMessage(0);
            } else if (mTargetView.getTranslationY() > 0) {
                mTargetView.setTranslationY(0);
                mTargetView.setVisibility(View.GONE);
            }
            return false;
        }
    });

    public SwipeDownScrollerCompat(View targetView, SwipeDownScrollerHandler handler) {
        mTargetView = targetView;
        Context context = targetView.getContext();
        // 第一步，创建Scroller的实例
        mScroller = new Scroller(context);
        mSwipeDownScrollerHandler = handler;
    }

    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchSlop = mTargetView.getHeight() / 4;
                mStartY = event.getRawY();
                mHandler.removeMessages(0);
                mEnableSwipe = mSwipeDownScrollerHandler.canSwipe();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!mEnableSwipe) {
                    mTargetView.setTranslationY(0);
                    break;
                }
                int offset = (int) (event.getRawY() - mStartY);
                mSwipeDownScrollerHandler.onComputeScrollOffset(offset, getDiff(event.getRawY(), mTargetView.getHeight()));
                if (offset > 0)
                    mTargetView.setTranslationY(offset);
                else
                    mTargetView.setTranslationY(0);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                if (!mEnableSwipe || mTargetView.getTranslationY() <= 0) {
                    mTargetView.setTranslationY(0);
                    break;
                }

                float offsetY = Math.abs(event.getRawY() - mStartY);
                int startY = (int) mTargetView.getTranslationY();
                int dy = -startY;


                if (offsetY > mTouchSlop) {
                    dy = (mTargetView.getHeight() - startY);
                }

                Log.w("Rae", "offet = " + offsetY + "; touch = " + mTouchSlop + "; height = " + mTargetView.getHeight() + "; dy = " + dy + "; startY=" + startY);


                // 借用Scroller 平滑过渡
                mScroller.startScroll(0, startY, 0, dy);
                mHandler.sendEmptyMessage(0);

                break;
        }

        return false;
    }

    private float getDiff(float startY, float finalY) {
        return (startY / finalY);
    }
}
