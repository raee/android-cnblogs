package com.rae.cnblogs.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.rae.cnblogs.R;
import com.rae.cnblogs.ThemeCompat;

/**
 * blogger
 * Created by ChenRui on 2017/2/9 0009 10:48.
 */
public class BloggerLayout extends ScrollView {

    private MotionEvent mActionDownEvent;

    public interface ScrollPercentChangeListener {
        void onScrollPercentChange(float percent);
    }

    private View mActionBarView; // 固定的导航栏
    private View mTabView; // Tab
    private View mListView; // 列表
    private float mTouchDownY;
    private ScrollPercentChangeListener mOnScrollPercentChangeListener;


    public BloggerLayout(Context context) {
        super(context);
        initView();
    }

    public BloggerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public BloggerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BloggerLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
    }

    public void setOnScrollPercentChangeListener(ScrollPercentChangeListener onScrollPercentChangeListener) {
        this.mOnScrollPercentChangeListener = onScrollPercentChangeListener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int height = MeasureSpec.getSize(heightMeasureSpec); // 父容器高度

        if (mActionBarView == null) {
            mActionBarView = ((View) getParent()).findViewById(R.id.tool_bar);
        }

        if (mTabView == null) {
            mTabView = findViewById(R.id.tab_category);
        }

        if (mListView == null) {
            mListView = findViewById(R.id.vp_blogger);
        }

        // 计算列表的高度 = 父容器的高度 - Tab的高度 - 导航栏的高度
        int listViewHeight = height - mTabView.getMeasuredHeight() - mActionBarView.getMeasuredHeight() - ((RelativeLayout.LayoutParams) mActionBarView.getLayoutParams()).topMargin;
        if (listViewHeight > 0) {
            mListView.getLayoutParams().height = listViewHeight;
        }

    }

    /**
     * 计算滚动的百分比
     * <pre>
     *     百分比 = Tab 距离顶部的高度 / （Tab 距离顶部的高度 - 导航栏底部距离顶部的高度）
     * </pre>
     *
     * @return
     */
    private float measureScrollPercent() {
        if (mTabView == null || mActionBarView == null) return 0;
        int tabScrollHeight = mTabView.getTop() - mActionBarView.getBottom();
        return (getScrollY() * 0.1f) / (tabScrollHeight * 0.1f);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();

        float percent = measureScrollPercent();
        if (mOnScrollPercentChangeListener != null) {
            mOnScrollPercentChangeListener.onScrollPercentChange(percent);
        }
        int alphaHex = Math.round(0xFF * percent);
        int color;
        if (ThemeCompat.isNight()) {
            // 夜间模式：#1F1F21
            color = Color.argb(alphaHex, 0x1F, 0X1F, 0X21);
        } else {
            color = Color.argb(alphaHex, 0XFF, 0XFF, 0XFF); // 白色背景
        }
        mActionBarView.setBackgroundColor(color);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchDownY = ev.getRawY();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mActionDownEvent = null;
                break;
        }


        Log.d("rae", "dispatchTouchEvent");
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (measureScrollPercent() < 1
                && ev.getAction() == MotionEvent.ACTION_MOVE
                && Math.abs(mTouchDownY - ev.getRawY()) > 30
                ) {
            return true;
        }
        return super.onInterceptTouchEvent(ev); // 拦截事件
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.d("rae", "onTouchEvent = " + ev.getAction());


        switch (ev.getAction()) {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                float diffY = ev.getRawY() - mTouchDownY;

                // 向上滑动，自动滚动
                if (measureScrollPercent() >= 0 && diffY < 0) {
                    post(new Runnable() {
                        @Override
                        public void run() {
                            smoothScrollTo(0, getHeight());
                        }
                    });
                }

                if (measureScrollPercent() <= 1 && diffY > 0) {
                    post(new Runnable() {
                        @Override
                        public void run() {
                            smoothScrollTo(0, 0);
                        }
                    });
                }


                break;
        }


        //  如果已经滑动到最底部，把事件分发给列表去处理
        if (checkInBottom()) {

            if (mActionDownEvent == null && ev.getAction() == MotionEvent.ACTION_MOVE) {
                ev.setAction(MotionEvent.ACTION_DOWN);
                mActionDownEvent = ev;
            }
            mListView.dispatchTouchEvent(ev);
            return true;
        }


        return super.onTouchEvent(ev);
    }

    /**
     * 是否滑动到底部了
     */
    private boolean checkInBottom() {
        // 公式 :  滑动的高度 + 屏幕高度 = 当前高度
        int h = getChildAt(0).getMeasuredHeight();
        int scrollY = getScrollY();
        int sh = getResources().getDisplayMetrics().heightPixels;

        boolean result = scrollY + sh >= h;
//        Log.w("rae", "是否到达底部=" + result + ";" + scrollY + " + " + sh + " = " + h);
        return result;

    }
}
