package com.rae.cnblogs.sdk;

import android.content.Context;

import com.rae.cnblogs.sdk.api.IBlogApi;
import com.rae.cnblogs.sdk.api.ICategoryApi;
import com.rae.cnblogs.sdk.api.IFriendsApi;
import com.rae.cnblogs.sdk.api.IUserApi;
import com.rae.cnblogs.sdk.api.impl.CategoryApiImpl;

/**
 * 博客园默认接口实现
 * Created by ChenRui on 2017/1/19 20:45.
 */
class DefaultCnblogsApiProvider extends CnblogsApiProvider {

    protected DefaultCnblogsApiProvider(Context applicationContext) {
        super(applicationContext);
    }

    @Override
    public int getApiVersion() {
        return 1;
    }

    @Override
    public IBlogApi getBlogApi() {
        return mRetrofit.create(IBlogApi.class);
    }

    @Override
    public ICategoryApi getCategoriesApi() {
        return new CategoryApiImpl(mContext);
    }

    @Override
    public IUserApi getUserApi() {
        return mRetrofit.create(IUserApi.class);
    }

    //
//    @Override
//    public IBookmarksApi getBookmarksApi() {
//        return null;
//    }
//
//
//    @Override
//    public INewsApi getNewsApi() {
//        return null;
//    }
//
    @Override
    public IFriendsApi getFriendApi() {
        return mRetrofit.create(IFriendsApi.class);
    }
//
//    @Override
//    public ISearchApi getSearchApi() {
//        return null;
//    }

    @Override
    public void cancel() {
    }
}
