package com.rae.cnblogs.sdk;

import com.rae.cnblogs.sdk.bean.Blog;

/**
 * 博客相关API
 * Created by ChenRui on 2016/11/28 23:34.
 */
public interface IBlogApi {

    /**
     * 获取首页博客列表
     *
     * @param page 页码
     */
    void getHomeBlogs(int page, ICnblogsListener<Blog> listener);

    /**
     * 获取分类博客列表
     *
     * @param page       页码
     * @param parentId   父ID
     * @param categoryId 分类ID
     */
    void getBlogs(int page, int parentId, int categoryId, ICnblogsListener<Blog> listener);

    /**
     * 获取博客文章内容
     *
     * @param id 博客ID
     */
    void getContents(String id, ICnblogsListener<Blog> listener);
}
