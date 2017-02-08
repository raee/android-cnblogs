package com.rae.cnblogs.sdk;

import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.core.sdk.ApiUiArrayListener;

/**
 * 搜索接口
 * Created by ChenRui on 2017/2/8 0008 9:22.
 */
public interface ISearchApi {

    /**
     * 搜索建议
     *
     * @param keyWord 关键字
     */
    void getSuggestion(String keyWord, ApiUiArrayListener<String> listener);

    /**
     * 搜索博主
     *
     * @param keyword 关键字
     */
    void searchBlogAuthor(String keyword, ApiUiArrayListener<UserInfoBean> listener);

    /**
     * 搜索博客
     *
     * @param keyword
     */
    void searchBlogList(String keyword, int page, ApiUiArrayListener<BlogBean> listener);

    /**
     * 搜索知识库
     */
    void searchKbList(String keyword, int page, ApiUiArrayListener<BlogBean> listener);

    /**
     * 搜索新闻
     */
    void searchNewsList(String keyword, int page, ApiUiArrayListener<BlogBean> listener);
}
