package com.rae.cnblogs.presenter.impl.blog;

import android.content.Context;
import android.text.TextUtils;

import com.rae.cnblogs.sdk.api.ISearchApi;
import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.bean.BlogType;
import com.rae.cnblogs.sdk.bean.CategoryBean;

import java.util.List;

import io.reactivex.Observable;

/**
 * 搜索博客
 * Created by ChenRui on 2017/8/29 0029 12:41.
 */
public class SearchBlogPresenterImpl extends BlogListPresenterImpl {

    private final BlogType mType;
    private final String mBlogApp;
    ISearchApi mSearchApi;

    public SearchBlogPresenterImpl(Context context, IBlogListView view, BlogType type, String blogApp) {
        super(context, view);
        mType = type;
        mBlogApp = blogApp;
        mSearchApi = getApiProvider().getSearchApi();
    }

    @Override
    protected void onLoadData(CategoryBean category, int pageIndex) {
        if (category == null || TextUtils.isEmpty(category.getName())) {
            mView.onLoadFailed(pageIndex, "请输入搜索关键字");
            return;
        }
        Observable<List<BlogBean>> observable;

        if (mType == BlogType.NEWS) {
            observable = createObservable(mSearchApi.searchNewsList(category.getName(), pageIndex));
        } else if (mType == BlogType.KB) {
            observable = createObservable(mSearchApi.searchKbList(category.getName(), pageIndex));
        } else {
            String keyword = category.getName();
            // 搜索个人博客
            if (!TextUtils.isEmpty(mBlogApp)) {
                keyword = "blog:" + mBlogApp + " " + keyword;
                observable = createObservable(mSearchApi.searchBlogAppList(keyword, pageIndex));
            } else {
                observable = createObservable(mSearchApi.searchBlogList(keyword, pageIndex));
            }
        }

        observable.subscribe(getBlogObserver());
    }
}
