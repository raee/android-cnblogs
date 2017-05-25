package com.rae.cnblogs.sdk;

import android.content.Context;

import com.rae.cnblogs.sdk.api.IBlogApi;

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

//    @Override
//    public ICategoryApi getCategoryApi() {
//        return null;
//    }
//
//    @Override
//    public IUserApi getUserApi() {
//        return null;
//    }
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
//    @Override
//    public IFriendsApi getFriendApi() {
//        return null;
//    }
//
//    @Override
//    public ISearchApi getSearchApi() {
//        return null;
//    }

    @Override
    public void cancel() {
    }
}
