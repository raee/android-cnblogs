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
    ISearchApi mSearchApi;

    public SearchBlogPresenterImpl(Context context, IBlogListView view, BlogType type) {
        super(context, view);
        mType = type;
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
            observable = createObservable(mSearchApi.searchBlogList(category.getName(), pageIndex));
        }

        observable.subscribe(getBlogObserver());
    }
}
