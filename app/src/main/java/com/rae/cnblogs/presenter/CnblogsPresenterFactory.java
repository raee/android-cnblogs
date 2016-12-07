package com.rae.cnblogs.presenter;

import android.content.Context;

import com.rae.cnblogs.presenter.impl.BlogContentPresenterImpl;
import com.rae.cnblogs.presenter.impl.BlogListPresenterImpl;
import com.rae.cnblogs.presenter.impl.HomePresenterImpl;

/**
 * Created by ChenRui on 2016/12/2 00:23.
 */
public final class CnblogsPresenterFactory {

    public static IHomePresenter getHomePresenter(Context context, IHomePresenter.IHomeView view) {
        return new HomePresenterImpl(context, view);
    }

    public static IBlogListPresenter getBlogListPresenter(Context context, IBlogListPresenter.IBlogListView view) {
        return new BlogListPresenterImpl(context, view);
    }

    public static IBlogContentPresenter getBlogContentPresenter(Context context, IBlogContentPresenter.IBlogContentView view) {
        return new BlogContentPresenterImpl(context, view);
    }
}
