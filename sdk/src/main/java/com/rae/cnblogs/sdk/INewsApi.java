package com.rae.cnblogs.sdk;

import com.rae.cnblogs.sdk.bean.Blog;
import com.rae.cnblogs.sdk.bean.BlogComment;
import com.rae.core.sdk.ApiUiArrayListener;
import com.rae.core.sdk.ApiUiListener;

/**
 * Created by ChenRui on 2017/1/18 0018 18:24.
 */
public interface INewsApi {

    void getNews(int page, ApiUiArrayListener<Blog> listener);

    void getNewsContent(String newsId, ApiUiListener<String> listener);

    void getNewsComment(String newsId, int page, ApiUiArrayListener<BlogComment> listener);

    /**
     * 发表新闻评论
     *
     * @param newsId          新闻ID
     * @param parentCommentId 引用回复评论ID，为空则发表新评论
     * @param content         评论内容
     */
    void addNewsComment(String newsId, String parentCommentId, String content, ApiUiListener<Void> listener);

    /**
     * 引用评论，并发表评论
     *
     * @param newsId  新闻ID
     * @param comment 引用回复
     * @param content 评论内容
     */
    void addNewsComment(String newsId, BlogComment comment, String content, ApiUiListener<Void> listener);

    /**
     * 删除新闻评论
     *
     * @param newsId    新闻ID
     * @param commentId 评论ID
     * @param listener
     */
    void deleteNewsComment(String newsId, String commentId, ApiUiListener<Void> listener);
}
