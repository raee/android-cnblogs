package com.rae.cnblogs.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.w("layout", "height = " + getRootView().getMeasuredHeight());
        if (getChildCount() > 0) {
            ViewGroup child = (ViewGroup) getChildAt(0);
            int childCount = child.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View view = child.getChildAt(i);
                int size = MeasureSpec.getSize(view.getMeasuredHeight());
                Log.i("layout", view + ";size = " + size);
                if (size == 0) {
                    view.getLayoutParams().height = getRootView().getMeasuredHeight();
                }
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }
}
