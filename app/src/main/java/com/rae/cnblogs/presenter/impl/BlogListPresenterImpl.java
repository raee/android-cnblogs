package com.rae.cnblogs.presenter.impl;

import android.content.Context;

import com.rae.cnblogs.presenter.IBlogListPresenter;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.IBlogApi;
import com.rae.cnblogs.sdk.bean.Blog;
import com.rae.core.sdk.ApiUiArrayListener;
import com.rae.core.sdk.exception.ApiException;

import java.util.List;

/**
 * Created by ChenRui on 2016/12/2 00:25.
 */
public class BlogListPresenterImpl extends BasePresenter<IBlogListPresenter.IBlogListView> implements IBlogListPresenter, ApiUiArrayListener<Blog> {

    private IBlogApi mApi;

    public BlogListPresenterImpl(Context context, IBlogListPresenter.IBlogListView view) {
        super(context, view);
        mApi = CnblogsApiFactory.getBlogApi(mContext);
    }

    @Override
    public void start() {
        // 加载列表
        mApi.getBlogs(mView.getPage(), mView.getParentId(), mView.getCategoryId(), this);
    }

    @Override
    public void onApiFailed(ApiException ex, String msg) {
        mView.onLoadFailed(msg);
    }

    @Override
    public void onApiSuccess(List<Blog> data) {
        mView.onLoadBlogList(data);
    }
}
