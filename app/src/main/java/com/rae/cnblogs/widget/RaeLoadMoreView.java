package com.rae.cnblogs.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.jcodecraeer.xrecyclerview.LoadingMoreFooter;

/**
 * 加载更多
 * Created by ChenRui on 2016/12/3 17:56.
 */
public class RaeLoadMoreView extends LoadingMoreFooter {

    public RaeLoadMoreView(Context context) {
        this(context, null);
    }

    public RaeLoadMoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setPadding(0, 32, 0, 32);
    }
}
