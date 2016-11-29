package com.rae.cnblogs.sdk.impl;

import android.content.Context;

import com.rae.cnblogs.sdk.IBlogApi;
import com.rae.cnblogs.sdk.ICnblogsListener;
import com.rae.cnblogs.sdk.bean.Blog;
import com.rae.cnblogs.sdk.parser.BlogJsonParser;

/**
 * 博客接口
 * Created by ChenRui on 2016/11/30 00:06.
 */
public class BlogApiImpl extends CnblogsBaseApi implements IBlogApi {

    private static final String API_URL_HOME = "http://www.cnblogs.com/mvc/AggSite/PostList.aspx";

    public BlogApiImpl(Context context) {
        super(context);
    }

    @Override
    public void getHomeBlogs(int page, ICnblogsListener<Blog> listener) {
        post(API_URL_HOME,
                newParams().add("CategoryType", "SiteHome")
                        .add("ParentCategoryId", "0")
                        .add("CategoryId", "808")
                        .add("PageIndex", page)
                        .add("ItemListActionName", "PostList"),
                new BlogJsonParser(listener)
        );
    }

    @Override
    public void getBlogs(int page, int parentId, int categoryId, ICnblogsListener<Blog> listener) {

    }

    @Override
    public void getContents(String id, ICnblogsListener<Blog> listener) {

    }
}
