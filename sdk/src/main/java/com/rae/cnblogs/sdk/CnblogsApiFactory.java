package com.rae.cnblogs.sdk;

import android.content.Context;

import com.rae.cnblogs.sdk.impl.AdvertApiImpl;
import com.rae.cnblogs.sdk.impl.BlogApiImpl;
import com.rae.cnblogs.sdk.impl.BookmarksApiImpl;
import com.rae.cnblogs.sdk.impl.CategoryApiImpl;
import com.rae.cnblogs.sdk.impl.UserApiImpl;

/**
 * 博客园接口实例化
 * Created by ChenRui on 2016/11/28 23:38.
 */
public final class CnblogsApiFactory {
    public static IBlogApi getBlogApi(Context context) {
        return new BlogApiImpl(context);
    }

    public static ICategoryApi getCategoryApi(Context context) {
        return new CategoryApiImpl(context);
    }

    /**
     * 获取用户接口
     */
    public static IUserApi getUserApi(Context context) {
        return new UserApiImpl(context);
    }

    /**
     * 获取收藏接口
     */
    public static IBookmarksApi getBookmarksApi(Context context) {
        return new BookmarksApiImpl(context);
    }

    /**
     * 获取广告接口
     */
    public static IAdvertApi getAdvertApi(Context context) {
        return new AdvertApiImpl(context);
    }


    /**
     * 获取新闻接口
     */
    public static INewsApi getNewsApi(Context context) {
        return new BlogApiImpl(context);
    }
}
