package com.rae.cnblogs.sdk.impl;

import android.content.Context;
import android.text.TextUtils;

import com.rae.cnblogs.sdk.api.INewsApi;
import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.bean.BlogCommentBean;
import com.rae.cnblogs.sdk.parser.CnBlogsWebApiResponse;
import com.rae.cnblogs.sdk.parser.NewsCommentParser;
import com.rae.cnblogs.sdk.parser.NewsContentParser;
import com.rae.cnblogs.sdk.parser.NewsListParser;
import com.rae.core.sdk.ApiUiArrayListener;
import com.rae.core.sdk.ApiUiListener;
import com.rae.core.sdk.exception.ApiErrorCode;
import com.rae.core.sdk.exception.ApiException;
import com.rae.core.sdk.net.IApiJsonResponse;

import org.jsoup.Jsoup;

import java.util.HashMap;

/**
 * 新闻API
 * Created by ChenRui on 2017/1/22 0022 17:42.
 */
public class NewsApiImpl extends CnblogsBaseApi implements INewsApi {

    public NewsApiImpl(Context context) {
        super(context);
    }

    @Override
    protected boolean enablePerCache(String url, HashMap<String, String> params) {
        if (TextUtils.isEmpty(url) || !mShouldCache) return false;

        // 新闻列表
        if (url.contains(ApiUrls.API_NEWS_LIST.replace("@page/20", "")) && params.get("page").equals("1")) {
            return true;
        }

        // 新闻评论列表
        if (url.contains(ApiUrls.API_NEWS_COMMENT)) {
            return true;
        }


        return super.enablePerCache(url, params);
    }

    @Override
    public void getNews(int page, ApiUiArrayListener<BlogBean> listener) {
        get(ApiUrls.API_NEWS_LIST.replace("@page", String.valueOf(page)), newParams().add("page", page), new NewsListParser(listener));
    }

    @Override
    public void getNewsContent(String newsId, ApiUiListener<String> listener) {
        get(ApiUrls.API_NEWS_CONTENT.replace("@id", newsId), null, new NewsContentParser(listener));
    }

    @Override
    public void getNewsComment(String newsId, int page, ApiUiArrayListener<BlogCommentBean> listener) {
        get(ApiUrls.API_NEWS_COMMENT, newParams().add("contentId", newsId).add("page", page), new NewsCommentParser(listener));
    }

    @Override
    public void addNewsComment(String id, String parentCommentId, String content, final ApiUiListener<Void> listener) {
        xmlHttpRequestWithJsonBody(ApiUrls.API_NEWS_COMMENT_ADD,
                newParams()
                        .add("Content", content)
                        .add("ContentID", id)
                        .add("parentCommentId", TextUtils.isEmpty(parentCommentId) ? "0" : parentCommentId),
                new IApiJsonResponse() {
                    @Override
                    public void onJsonResponse(String s) {
                        if (!TextUtils.isEmpty(s) && s.startsWith("<table")) {
                            listener.onApiSuccess(null);
                        } else {
                            listener.onApiFailed(new ApiException(ApiErrorCode.ERROR_SERVER), Jsoup.parse(s).text());
                        }
                    }

                    @Override
                    public void onJsonResponseError(int i, Throwable throwable) {
                        listener.onApiFailed(new ApiException(i, throwable), ApiErrorCode.ERROR_SERVER.getMessage());
                    }
                });
    }

    @Override
    public void addNewsComment(String id, BlogCommentBean comment, String content, ApiUiListener<Void> listener) {
        StringBuilder sb = new StringBuilder();
        sb.append("@");
        sb.append(comment.getBlogApp());
        sb.append("\n");
        sb.append("[quote]");
        sb.append(comment.getBody());
        sb.append("[/quote]");
        sb.append("\n");
        sb.append(content);
        content = sb.toString();
        addNewsComment(id, comment.getId(), content, listener);
    }

    @Override
    public void deleteNewsComment(String newsId, String commentId, final ApiUiListener<Void> listener) {
        xmlHttpRequestWithJsonBody(ApiUrls.API_NEWS_COMMENT_DELETE,
                newParams()
                        .add("commentId", commentId)
                        .add("contentID", newsId),
                new IApiJsonResponse() {
                    @Override
                    public void onJsonResponse(String s) {
                        if (!TextUtils.equals("0", s)) {
                            listener.onApiSuccess(null);
                        } else {
                            listener.onApiFailed(new ApiException(ApiErrorCode.ERROR_SERVER), ApiErrorCode.ERROR_SERVER.getMessage());
                        }
                    }

                    @Override
                    public void onJsonResponseError(int i, Throwable throwable) {
                        listener.onApiFailed(new ApiException(ApiErrorCode.ERROR_SERVER, throwable), ApiErrorCode.ERROR_SERVER.getMessage());
                    }
                });
    }

    @Override
    public void like(String newsId, ApiUiListener<Void> listener) {
        xmlHttpRequestWithJsonBody(ApiUrls.API_NEWS_LIKE, newParams().add("contentId", newsId).add("action", "agree"), new CnBlogsWebApiResponse<>(Void.class, listener));
    }

}
