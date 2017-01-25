package com.rae.cnblogs.sdk;

/**
 * 接口提供者
 * Created by ChenRui on 2017/1/19 20:41.
 */
public abstract class CnblogsApiProvider {
    /**
     * 获取接口版本号
     */
    public abstract int getApiVersion();

    /**
     * 获取博客接口
     */
    public abstract IBlogApi getBlogApi();

    /**
     * 获取博客分类接口
     */
    public abstract ICategoryApi getCategoryApi();


    /**
     * 获取用户接口
     */
    public abstract IUserApi getUserApi();

    /**
     * 获取收藏接口
     */
    public abstract IBookmarksApi getBookmarksApi();


    /**
     * 获取新闻接口
     */
    public abstract INewsApi getNewsApi();
}
