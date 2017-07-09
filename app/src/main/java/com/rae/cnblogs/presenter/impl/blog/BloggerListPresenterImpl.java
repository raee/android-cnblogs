package com.rae.cnblogs.presenter.impl.blog;

import android.content.Context;

import com.rae.cnblogs.RxObservable;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.api.IFriendsApi;
import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.bean.CategoryBean;

import java.util.List;

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
        createObservable(mFriendsApi.getBlogList(pageIndex, category.getCategoryId())).subscribe(new ApiDefaultObserver<List<BlogBean>>() {
            @Override
            protected void onError(String message) {
                mView.onLoadFailed(mPageIndex, message);
            }

            @Override
            protected void accept(List<BlogBean> blogBeans) {
                mView.onLoadBlogList(mPageIndex, blogBeans);
            }
        });
    }
}
