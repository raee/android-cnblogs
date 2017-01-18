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
}
