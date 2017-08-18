package com.rae.cnblogs.widget;

import android.content.Context;
import android.util.AttributeSet;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;

/**
 * 下拉刷新
 * Created by ChenRui on 2016/12/2 01:03.
 */
public class AppLayout extends PtrClassicFrameLayout {
    public AppLayout(Context context) {
        super(context);
        init();
    }

    public AppLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AppLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
    }
}
