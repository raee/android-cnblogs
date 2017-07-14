package com.rae.cnblogs.presenter.impl.blog;

import android.content.Context;

import com.rae.cnblogs.sdk.api.IFriendsApi;
import com.rae.cnblogs.sdk.bean.CategoryBean;

/**
 * 知识库
 * Created by ChenRui on 2017/2/4 0004 14:09.
 */
public class BloggerListPresenterImpl extends BlogListPresenterImpl {

    private IFriendsApi mFriendsApi;

    public BloggerListPresenterImpl(Context context, IBlogListView view) {
        super(context, view);
        mFriendsApi = getApiProvider().getFriendApi();
    }

    @Override
    protected void onLoadData(CategoryBean category, int pageIndex) {
        createObservable(mFriendsApi.getBlogList(Math.max(1, pageIndex), category.getCategoryId())).subscribe(getBlogObserver());
    }
}
