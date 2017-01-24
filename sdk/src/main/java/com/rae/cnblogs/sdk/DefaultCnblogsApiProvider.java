package com.rae.cnblogs.sdk;

import android.content.Context;

import com.rae.cnblogs.sdk.impl.AdvertApiImpl;
import com.rae.cnblogs.sdk.impl.BlogApiImpl;
import com.rae.cnblogs.sdk.impl.BookmarksApiImpl;
import com.rae.cnblogs.sdk.impl.CategoryApiImpl;
import com.rae.cnblogs.sdk.impl.NewsApiImpl;
import com.rae.cnblogs.sdk.impl.UserApiImpl;

/**
 * 博客园默认接口实现
 * Created by ChenRui on 2017/1/19 20:45.
 */
class DefaultCnblogsApiProvider extends CnblogsApiProvider {

    private Context mContext;

    DefaultCnblogsApiProvider(Context applicationContext) {
        mContext = applicationContext;
    }


    @Override
    public int getApiVersion() {
        return 1;
    }

    @Override
    public IBlogApi getBlogApi() {
        return new BlogApiImpl(mContext);
    }

    @Override
    public ICategoryApi getCategoryApi() {
        return new CategoryApiImpl(mContext);
    }

    @Override
    public IUserApi getUserApi() {
        return new UserApiImpl(mContext);
    }

    @Override
    public IBookmarksApi getBookmarksApi() {
        return new BookmarksApiImpl(mContext);
    }

    @Override
    public IAdvertApi getAdvertApi() {
        return new AdvertApiImpl(mContext);
    }

    @Override
    public INewsApi getNewsApi() {
        return new NewsApiImpl(mContext);
    }
}
