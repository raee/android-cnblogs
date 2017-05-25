package com.rae.cnblogs.sdk.impl;

import android.content.Context;

import com.rae.cnblogs.sdk.api.ISearchApi;
import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.bean.BlogType;
import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.cnblogs.sdk.parser.BaiduSuggestionParser;
import com.rae.cnblogs.sdk.parser.SearchBlogListParser;
import com.rae.cnblogs.sdk.parser.SearchBloggerParser;
import com.rae.core.sdk.ApiUiArrayListener;

/**
 * 搜索接口
 * Created by ChenRui on 2017/2/8 0008 9:23.
 */
public class SearchApiImpl extends CnblogsBaseApi implements ISearchApi {

    public SearchApiImpl(Context context) {
        super(context);
    }

    @Override
    public void getSuggestion(String keyWord, ApiUiArrayListener<String> listener) {
        get(ApiUrls.API_BAIDU_SUGGESTION, newParams().add("wd", keyWord).add("cb", "cnblogs"), new BaiduSuggestionParser(listener));
    }

    @Override
    public void searchBlogAuthor(String keyword, ApiUiArrayListener<UserInfoBean> listener) {
        get(ApiUrls.API_SEARCH_BLOGGER, newParams().add("t", keyword), new SearchBloggerParser(listener));
    }

    @Override
    public void searchBlogList(String keyword, int page, ApiUiArrayListener<BlogBean> listener) {
        get(ApiUrls.API_SEARCH_BLOG_LIST, newParams().add("Keywords", keyword).add("pageindex", page), new SearchBlogListParser(listener, BlogType.BLOG));
    }

    @Override
    public void searchKbList(String keyword, int page, ApiUiArrayListener<BlogBean> listener) {
        get(ApiUrls.API_SEARCH_KB_LIST, newParams().add("Keywords", keyword).add("pageindex", page), new SearchBlogListParser(listener, BlogType.KB));
    }

    @Override
    public void searchNewsList(String keyword, int page, ApiUiArrayListener<BlogBean> listener) {
        get(ApiUrls.API_SEARCH_NEWS_LIST, newParams().add("Keywords", keyword).add("pageindex", page), new SearchBlogListParser(listener, BlogType.NEWS));
    }
}
