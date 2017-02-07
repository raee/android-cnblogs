package com.rae.cnblogs.sdk;

import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.bean.BlogCommentBean;
import com.rae.core.sdk.ApiUiArrayListener;
import com.rae.core.sdk.ApiUiListener;

/**
 * 博客相关API
 * Created by ChenRui on 2016/11/28 23:34.
 */
public interface IBlogApi {

    /**
     * 获取分类博客列表
     *
     * @param page       页码
     * @param parentId   父ID
     * @param categoryId 分类ID
     */
    void getBlogList(int page, String type, String parentId, String categoryId, ApiUiArrayListener<BlogBean> listener);

    /**
     * 获取博客文章内容
     *
     * @param id 博客ID
     */
    void getBlogContent(String id, ApiUiListener<String> listener);

    /**
     * 获取评论列表
     *
     * @param id      博客ID
     * @param blogApp 博主ID
     */
    void getBlogComments(int page, String id, String blogApp, ApiUiArrayListener<BlogCommentBean> listener);

    /**
     * 分页获取知识库
     */
    void getKbArticles(int page, ApiUiArrayListener<BlogBean> listener);


    /**
     * 获取知识库内容
     *
     * @param id 知识库ID
     */
    void getKbContent(String id, ApiUiListener<String> listener);

    /**
     * 推荐/喜欢 博客
     *
     * @param id      博客ID
     * @param blogApp 该文的博主ID
     */
    void likeBlog(String id, String blogApp, ApiUiListener<Void> listener);

    /**
     * 取消推荐博客
     *
     * @param id      博客ID
     * @param blogApp 该文的博主ID
     */
    void unLikeBlog(String id, String blogApp, ApiUiListener<Void> listener);

    /**
     * 知识库点赞
     *
     * @param id       知识库ID
     * @param listener 回调
     */
    void likeKb(String id, ApiUiListener<Void> listener);

    /**
     * 发表博客评论
     *
     * @param id              博客ID
     * @param blogApp         该文的博主ID
     * @param parentCommentId 引用回复评论ID，为空则发表新评论
     * @param content         评论内容
     */
    void addBlogComment(String id, String blogApp, String parentCommentId, String content, ApiUiListener<Void> listener);

    /**
     * 引用评论，并发表评论
     *
     * @param id      博客ID
     * @param blogApp 该文的博主ID
     * @param comment 引用回复
     * @param content 评论内容
     */
    void addBlogComment(String id, String blogApp, BlogCommentBean comment, String content, ApiUiListener<Void> listener);

    /**
     * 删除博客评论
     *
     * @param commentId 评论ID
     */
    void deleteBlogComment(String commentId, ApiUiListener<Void> listener);


    /**
     * 是否缓存结果
     *
     * @param shouldCache
     */
    void setShouldCache(boolean shouldCache);

}
