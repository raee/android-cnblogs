package com.rae.cnblogs.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * blogger
 * Created by ChenRui on 2017/2/9 0009 10:48.
 */
public class BloggerLayout extends ScrollView {

    // 帐号布局高度
    private int mAccountLayoutHeight;


    public BloggerLayout(Context context) {
        super(context);
    }

    public BloggerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BloggerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BloggerLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private float mStartY;

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                mStartY = ev.getRawY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//
//                this.scrollBy(0, ev.getRawY() - mStartY);
//
//                return false;
//        }
//        return super.dispatchTouchEvent(ev);
//    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }


}
