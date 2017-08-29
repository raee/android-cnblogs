package com.rae.cnblogs.widget;

import android.content.Context;
import android.util.AttributeSet;

import in.srain.cube.views.ptr.PtrClassicDefaultHeader;

public class HomeSearchHeader extends PtrClassicDefaultHeader {

    public HomeSearchHeader(Context context) {
        super(context);
    }

    public HomeSearchHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HomeSearchHeader(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void initViews(AttributeSet attrs) {
        super.initViews(attrs);
//        LayoutInflater.from(getContext()).inflate(R.layout.item_home_search, this, true);

    }
}
