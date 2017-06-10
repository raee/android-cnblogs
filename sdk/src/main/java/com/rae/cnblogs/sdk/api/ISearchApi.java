package com.rae.cnblogs.sdk.api;

import com.rae.cnblogs.sdk.Parser;
import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.cnblogs.sdk.parser.BaiduSuggestionParser;
import com.rae.cnblogs.sdk.parser.SearchBlogListParser;
import com.rae.cnblogs.sdk.parser.SearchBloggerParser;
import com.rae.cnblogs.sdk.parser.SearchKbListParser;
import com.rae.cnblogs.sdk.parser.SearchNewsListParser;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

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
    @GET(ApiUrls.API_BAIDU_SUGGESTION)
    @Parser(BaiduSuggestionParser.class)
    Observable<List<String>> getSuggestion(@Query("wd") String keyWord);

    /**
     * 搜索博主
     *
     * @param keyword 关键字
     */
    @GET(ApiUrls.API_SEARCH_BLOGGER)
    @Parser(SearchBloggerParser.class)
    Observable<List<UserInfoBean>> searchBlogAuthor(@Query("t") String keyword);

    /**
     * 搜索博客
     *
     * @param keyword 关键字
     */
    @GET(ApiUrls.API_SEARCH_BLOG_LIST)
    @Parser(SearchBlogListParser.class)
    Observable<List<BlogBean>> searchBlogList(@Query("Keywords") String keyword, @Query("pageindex") int page);

    /**
     * 搜索知识库
     */
    @GET(ApiUrls.API_SEARCH_KB_LIST)
    @Parser(SearchKbListParser.class)
    Observable<List<BlogBean>> searchKbList(@Query("Keywords") String keyword, @Query("pageindex") int page);

    /**
     * 搜索新闻
     */
    @GET(ApiUrls.API_SEARCH_NEWS_LIST)
    @Parser(SearchNewsListParser.class)
    Observable<List<BlogBean>> searchNewsList(@Query("Keywords") String keyword, @Query("pageindex") int page);
}
