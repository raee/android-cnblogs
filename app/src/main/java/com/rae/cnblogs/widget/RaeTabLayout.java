package com.rae.cnblogs.widget;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;

/**
 * Created by ChenRui on 2016/12/2 00:50.
 */
public class RaeTabLayout extends TabLayout {
    public RaeTabLayout(Context context) {
        super(context);
    }

    public RaeTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RaeTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


//    @NonNull
//    @Override
//    public Tab newTab() {
//        Tab tab = super.newTab();
//        RaeTextView view = new RaeTextView(getContext());
//        view.setBlogId(android.R.id.text1);
//        tab.setCustomView(view);
//        return tab;
//    }
}
