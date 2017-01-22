package com.rae.cnblogs.presenter.impl;

import android.content.Context;
import android.text.TextUtils;

import com.rae.cnblogs.presenter.IBlogContentPresenter;
import com.rae.cnblogs.sdk.IBlogApi;
import com.rae.cnblogs.sdk.INewsApi;
import com.rae.cnblogs.sdk.bean.Blog;
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
        Blog blog = mView.getBlog();
        if (blog == null) return;
        if (!TextUtils.isEmpty(blog.getContent())) {
            mView.onLoadContentSuccess(blog);
            return;
        }

        if (blog.isNews()) {
            // 新闻
            mNewsApi.getNewsContent(blog.getId(), this);
        } else if (blog.isKb()) {
            // 知识库
            mBlogApi.getKbContent(blog.getId(), this);
        } else {
            // 博文
            mBlogApi.getBlogContent(blog.getId(), this);
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
