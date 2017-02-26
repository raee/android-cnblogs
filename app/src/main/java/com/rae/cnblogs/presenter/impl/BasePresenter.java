package com.rae.cnblogs.presenter.impl;

import android.content.Context;
import android.text.TextUtils;

import com.github.raee.cnblogs.sdk.IRaeCnblogsApiProvider;
import com.github.raee.cnblogs.sdk.RaeCnblogsApiFactory;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.CnblogsApiProvider;

/**
 * 基类
 * Created by ChenRui on 2016/12/2 00:23.
 */
public abstract class BasePresenter<V> {
    protected Context mContext;

    protected V mView;

    public BasePresenter(Context context, V view) {
        mView = view;
        mContext = context;
    }

    protected boolean isEmpty(String text) {
        return TextUtils.isEmpty(text) || text.isEmpty() || text.trim().isEmpty();
    }

    protected CnblogsApiProvider getApiProvider() {
        return CnblogsApiFactory.getInstance(mContext);
    }

    protected IRaeCnblogsApiProvider getServerApi() {
        return RaeCnblogsApiFactory.getInstance(mContext);
    }

    public String getString(int resId) {
        return mContext.getString(resId);
    }

    public void destroy() {
        mView = null;
        mContext = null;
    }
}
