package com.rae.cnblogs.presenter.impl;

import android.content.Context;
import android.text.TextUtils;

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

    protected boolean isEmpty(String text) {
        return TextUtils.isEmpty(text) || text.isEmpty() || text.trim().isEmpty();
    }
}
