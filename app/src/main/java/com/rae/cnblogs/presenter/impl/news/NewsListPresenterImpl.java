package com.rae.cnblogs.presenter.impl.news;

import android.content.Context;

import com.rae.cnblogs.presenter.impl.blog.BlogListPresenterImpl;
import com.rae.cnblogs.sdk.INewsApi;
import com.rae.cnblogs.sdk.bean.CategoryBean;

/**
 * 新闻
 * Created by ChenRui on 2017/2/4 0004 14:09.
 */
public class NewsListPresenterImpl extends BlogListPresenterImpl {
    private INewsApi mNewsApi;

    public NewsListPresenterImpl(Context context, IBlogListView view) {
        super(context, view);
        mNewsApi = getApiProvider().getNewsApi();

    }

    @Override
    protected void onLoadData(CategoryBean category, int pageIndex) {
        mNewsApi.getNews(pageIndex, this);
    }
}
