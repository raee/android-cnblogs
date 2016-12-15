package com.rae.cnblogs.sdk.impl;

import android.content.Context;

import com.rae.cnblogs.sdk.IBlogApi;
import com.rae.cnblogs.sdk.bean.Blog;
import com.rae.cnblogs.sdk.bean.BlogComment;
import com.rae.cnblogs.sdk.parser.BlogCommentParser;
import com.rae.cnblogs.sdk.parser.BlogContentParser;
import com.rae.cnblogs.sdk.parser.BlogJsonParser;
import com.rae.core.sdk.ApiUiArrayListener;
import com.rae.core.sdk.ApiUiListener;

/**
 * 博客接口
 * Created by ChenRui on 2016/11/30 00:06.
 */
public class BlogApiImpl extends CnblogsBaseApi implements IBlogApi {


    public BlogApiImpl(Context context) {
        super(context);
    }

//    @Override
//    public void getHomeBlogs(int page, ApiUiArrayListener<Blog> listener) {
//        post(ApiUrls.API_URL_HOME,
//                newParams().add("CategoryType", "SiteHome")
//                        .add("ParentCategoryId", "0")
//                        .add("CategoryId", "808")
//                        .add("PageIndex", page)
//                        .add("ItemListActionName", "PostList"),
//                new BlogJsonParser(listener)
//        );
//    }

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
    public void getComment(int page, String id, String blogApp, ApiUiListener<BlogComment> listener) {
        get(ApiUrls.API_URL_COMMENT, newParams().add("postId", id).add("blogApp", blogApp).add("pageIndex", page), new BlogCommentParser(listener));
    }
}
