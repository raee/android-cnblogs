package com.rae.cnblogs.presenter.impl;

import android.content.Context;
import android.text.TextUtils;

import com.rae.cnblogs.presenter.IBlogContentPresenter;
import com.rae.cnblogs.sdk.IBlogApi;
import com.rae.cnblogs.sdk.INewsApi;
import com.rae.core.sdk.ApiUiListener;
import com.rae.core.sdk.exception.ApiException;

/**
 * 博客查看实现
 * Created by ChenRui on 2016/12/7 22:03.
 */
public class BlogContentPresenterImpl extends BasePresenter<IBlogContentPresenter.IBlogContentView> implements IBlogContentPresenter, ApiUiListener<String> {

    private IBlogApi mBlogApi;
    private INewsApi mNewsApi;

    public BlogContentPresenterImpl(Context context, IBlogContentView view) {
        super(context, view);
        mBlogApi = getApiProvider().getBlogApi();
        mNewsApi = getApiProvider().getNewsApi();
    }

    @Override
    public void loadContent() {
        if (mView.getBlog() == null) return;
        if (!TextUtils.isEmpty(mView.getBlog().getContent())) {
            mView.onLoadContentSuccess(mView.getBlog());
            return;
        }

        if (mView.getBlog().isNews()) {
            mNewsApi.getNewsContent(mView.getBlog().getId(), this);
        } else {
            mBlogApi.getContents(mView.getBlog().getId(), this);
        }
    }

    @Override
    public void onApiFailed(ApiException ex, String msg) {
        mView.onLoadContentFailed(msg);
    }

    @Override
    public void onApiSuccess(String data) {
        mView.getBlog().setContent(data);
        mView.onLoadContentSuccess(mView.getBlog());
    }
}
