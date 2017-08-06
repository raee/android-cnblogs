package com.rae.cnblogs.presenter.impl;

import android.content.Context;
import android.text.TextUtils;

import com.rae.cnblogs.RxObservable;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.CnblogsApiProvider;
import com.rae.cnblogs.sdk.UserProvider;

import io.reactivex.Observable;

/**
 * 基类
 * Created by ChenRui on 2016/12/2 00:23.
 */
public abstract class BasePresenter<V> {
    protected Context mContext;
    protected Context mApplicationContext;

    protected V mView;

    protected String mTag;

    public BasePresenter(Context context, V view) {
        mView = view;
        mContext = context;
        mApplicationContext = context.getApplicationContext();
        mTag = getClass().getSimpleName();
    }

    protected boolean isEmpty(String text) {
        return TextUtils.isEmpty(text) || text.isEmpty() || text.trim().isEmpty();
    }

    protected CnblogsApiProvider getApiProvider() {
        return CnblogsApiFactory.getInstance(mContext);
    }

//    protected IRaeCnblogsApiProvider getServerApi() {
//        return RaeCnblogsApiFactory.getInstance(mContext);
//    }

    public String getString(int resId) {
        return mContext.getString(resId);
    }

    public void destroy() {
        cancelRequest();
        mView = null;
        mContext = null;
    }

    /**
     * 释放当前请求
     */
    public void cancelRequest() {
        RxObservable.dispose(mTag);
        RxObservable.dispose("thread");
    }

    protected boolean isNotLogin() {
        return !UserProvider.getInstance().isLogin();
    }

    public <T> Observable<T> createObservable(Observable<T> observable) {
        return RxObservable.create(observable, mTag);
    }
}
