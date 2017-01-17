package com.rae.cnblogs.widget;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

/**
 * RecycleView
 * Created by ChenRui on 2016/12/3 17:26.
 */
public class RaeRecyclerView extends XRecyclerView {

    private RaeLoadMoreView mFootView;
    private float mTouchDownY;


    public RaeRecyclerView(Context context) {
        super(context);
        init();
    }

    public RaeRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RaeRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setLayoutManager(new LinearLayoutManager(getContext()));
        setPullRefreshEnabled(false);
        mFootView = new RaeLoadMoreView(getContext());
        mFootView.setVisibility(GONE);
        setLoadingMoreProgressStyle(ProgressStyle.BallScaleMultiple);
        setFootView(mFootView);

    }


    @Override
    public void setLoadingMoreProgressStyle(int style) {
        mFootView.setProgressStyle(style);
    }

    public boolean isOnTop() {
        if (getChildCount() == 0) {
            return true;
        }
        int top = getChildAt(0).getTop();
        if (top != 0) {
            return false;
        }

        RecyclerView.LayoutManager layoutManager = getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            int position = ((LinearLayoutManager) layoutManager).findFirstCompletelyVisibleItemPosition();
            if (position <= 1) {
                return true;
            } else if (position == -1) {
                position = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
                return position == 0;
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            boolean allViewAreOverScreen = true;
            int[] positions = ((StaggeredGridLayoutManager) layoutManager).findFirstVisibleItemPositions(null);
            for (int i = 0; i < positions.length; i++) {
                if (positions[i] == 0) {
                    return true;
                }
                if (positions[i] != -1) {
                    allViewAreOverScreen = false;
                }
            }
            if (allViewAreOverScreen) {
                positions = ((StaggeredGridLayoutManager) layoutManager).findFirstVisibleItemPositions(null);
                for (int i = 0; i < positions.length; i++) {
                    if (positions[i] == 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        boolean res = super.onTouchEvent(ev);
        Log.d("rae-RecyclerView", "onTouchEvent - " + ev.getAction() + " - " + res);
        return res;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean res = super.dispatchTouchEvent(ev);
        Log.d("rae-RecyclerView", "dispatchTouchEvent - " + ev.getAction() + " - " + res);
        return res;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean res = super.onInterceptTouchEvent(ev);
        Log.d("rae-RecyclerView", "onInterceptTouchEvent - " + ev.getAction() + " - " + res);
        return res;
    }
}
