package com.rae.cnblogs.sdk;

import com.rae.cnblogs.sdk.bean.Blog;
import com.rae.cnblogs.sdk.bean.BlogComment;
import com.rae.core.sdk.ApiUiArrayListener;
import com.rae.core.sdk.ApiUiListener;

/**
 * 博客相关API
 * Created by ChenRui on 2016/11/28 23:34.
 */
public interface IBlogApi {

    /**
     * 根据博客ID 获取博客
     *
     * @param blogId   博客Id
     * @param listener 回调
     */
    void getBlog(String blogId, ApiUiListener<Blog> listener);

    /**
     * 获取分类博客列表
     *
     * @param page       页码
     * @param parentId   父ID
     * @param categoryId 分类ID
     */
    void getBlogs(int page, String type, String parentId, String categoryId, ApiUiArrayListener<Blog> listener);

    /**
     * 获取博客文章内容
     *
     * @param id 博客ID
     */
    void getContents(String id, ApiUiListener<String> listener);

    /**
     * 获取评论列表
     * @param page
     * @param id
     * @param blogApp
     * @param listener
     */
    void getComment(int page, String id, String blogApp, ApiUiArrayListener<BlogComment> listener);
}
