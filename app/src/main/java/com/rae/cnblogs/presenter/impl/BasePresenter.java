package com.rae.cnblogs.presenter.impl;

import android.content.Context;

/**
 * Created by ChenRui on 2016/12/2 00:23.
 */
public abstract class BasePresenter<V> {
    protected final Context mContext;
    V mView;

    public BasePresenter(Context context, V view) {
        mView = view;
        mContext = context;
    }
}
