package com.rae.cnblogs.presenter.impl;

import android.content.Context;
import android.text.TextUtils;

import com.rae.cnblogs.presenter.IBlogListPresenter;
import com.rae.cnblogs.sdk.IBlogApi;
import com.rae.cnblogs.sdk.INewsApi;
import com.rae.cnblogs.sdk.bean.Blog;
import com.rae.cnblogs.sdk.bean.Category;
import com.rae.core.sdk.ApiUiArrayListener;
import com.rae.core.sdk.exception.ApiException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChenRui on 2016/12/2 00:25.
 */
public class BlogListPresenterImpl extends BasePresenter<IBlogListPresenter.IBlogListView> implements IBlogListPresenter, ApiUiArrayListener<Blog> {

    private IBlogApi mApi;
    private INewsApi mNewsApi;
    private int mPageIndex = 1;
    private final List<Blog> mBlogList = new ArrayList<>();

    public BlogListPresenterImpl(Context context, IBlogListPresenter.IBlogListView view) {
        super(context, view);
        mApi = getApiProvider().getBlogApi();
        mNewsApi = getApiProvider().getNewsApi();
    }

    @Override
    public void start() {
        mPageIndex = 1;
        loadData();
    }

    private void loadData() {
        // 加载列表
        Category category = mView.getCategory();
        if (TextUtils.equals("news", category.getType())) { // 新闻
            mNewsApi.getNews(mPageIndex, this);
        } else if (TextUtils.equals("kb", category.getType())) { // 知识库
            mApi.getKbArticles(mPageIndex, this);
        } else { // 博客列表
            mApi.getBlogList(mPageIndex, category.getType(), category.getParentId(), category.getCategoryId(), this);
        }
    }

    @Override
    public void onApiFailed(ApiException ex, String msg) {
        mView.onLoadFailed(mPageIndex, msg);
    }

    @Override
    public void onApiSuccess(List<Blog> data) {

        // 无重复添加
        data.removeAll(mBlogList);

        if (mPageIndex <= 1) {
            mBlogList.addAll(0, data);
        } else {
            mBlogList.addAll(data);
        }

        mView.onLoadBlogList(mPageIndex, mBlogList);
        mPageIndex++;
    }

    @Override
    public void loadMore() {
        loadData();
    }
}
