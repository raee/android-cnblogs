package com.rae.cnblogs.sdk.impl;

import android.content.Context;

import com.rae.cnblogs.sdk.IBlogApi;
import com.rae.cnblogs.sdk.INewsApi;
import com.rae.cnblogs.sdk.bean.Blog;
import com.rae.cnblogs.sdk.bean.BlogComment;
import com.rae.cnblogs.sdk.parser.BlogCommentParser;
import com.rae.cnblogs.sdk.parser.BlogContentParser;
import com.rae.cnblogs.sdk.parser.BlogJsonParser;
import com.rae.cnblogs.sdk.parser.KBParser;
import com.rae.cnblogs.sdk.parser.NewsCommentParser;
import com.rae.cnblogs.sdk.parser.NewsContentParser;
import com.rae.cnblogs.sdk.parser.NewsParser;
import com.rae.core.sdk.ApiUiArrayListener;
import com.rae.core.sdk.ApiUiListener;

import java.util.HashMap;

/**
 * 博客接口
 * Created by ChenRui on 2016/11/30 00:06.
 */
public class BlogApiImpl extends CnblogsBaseApi implements IBlogApi, INewsApi {

    public BlogApiImpl(Context context) {
        super(context);
    }

    @Override
    protected boolean enablePerCache(String url, HashMap<String, String> params) {
        return true; // 允许首次从缓存加载
    }

    @Override
    public void getBlog(String blogId, ApiUiListener<Blog> listener) {

    }

    @Override
    public void getBlogs(int page, String type, String parentId, String categoryId, ApiUiArrayListener<Blog> listener) {
        post(ApiUrls.API_URL_HOME,
                newParams().add("CategoryType", type)
                        .add("ParentCategoryId", parentId)
                        .add("CategoryId", categoryId)
                        .add("PageIndex", page)
                        .add("ItemListActionName", "PostList"),
                new BlogJsonParser(listener)
        );
    }

    @Override
    public void getContents(String id, ApiUiListener<String> listener) {
        get(ApiUrls.API_URL_CONTENT + id, null, new BlogContentParser(listener));
    }

    @Override
    public void getComment(int page, String id, String blogApp, ApiUiArrayListener<BlogComment> listener) {
        get(ApiUrls.API_URL_COMMENT, newParams().add("postId", id).add("blogApp", blogApp).add("pageIndex", page), new BlogCommentParser(listener));
    }

    @Override
    public void getKbArticles(int page, ApiUiArrayListener<Blog> listener) {
        post(ApiUrls.API_KB_LIST.replace("@page", String.valueOf(page)), null, new KBParser(listener));
    }

    @Override
    public void getNews(int page, ApiUiArrayListener<Blog> listener) {
        get(ApiUrls.API_NEWS_LIST.replace("@page", String.valueOf(page)), null, new NewsParser(listener));
    }

    @Override
    public void getNewsContent(String newsId, ApiUiListener<String> listener) {
        get(ApiUrls.API_NEWS_CONTENT.replace("@id", newsId), null, new NewsContentParser(listener));
    }

    @Override
    public void getNewsComment(String newsId, int page, ApiUiArrayListener<BlogComment> listener) {
        get(ApiUrls.API_NEWS_COMMENT, newParams().add("contentId", newsId), new NewsCommentParser(listener));
    }
}
