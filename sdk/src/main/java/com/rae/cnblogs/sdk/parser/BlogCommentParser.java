package com.rae.cnblogs.sdk.parser;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.rae.cnblogs.sdk.Utils;
import com.rae.cnblogs.sdk.bean.BlogComment;
import com.rae.cnblogs.sdk.bean.BlogCommentModel;
import com.rae.core.sdk.ApiUiArrayListener;
import com.rae.core.sdk.exception.ApiErrorCode;
import com.rae.core.sdk.exception.ApiException;
import com.rae.core.sdk.net.IApiJsonResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * 博文解析
 * Created by ChenRui on 2016/11/30 0030 17:00.
 */
public class BlogCommentParser implements IApiJsonResponse {

    private final ApiUiArrayListener<BlogComment> mListener;

    public BlogCommentParser(ApiUiArrayListener<BlogComment> listener) {
        mListener = listener;
    }

    @Override
    public void onJsonResponse(String json) {
        if (TextUtils.isEmpty(json)) {
            onJsonResponseError(ApiErrorCode.ERROR_EMPTY_DATA.getErrorCode(), null);
            return;
        }
        BlogCommentModel model = JSON.parseObject(json, BlogCommentModel.class);
        if (model == null) {
            onJsonResponseError(ApiErrorCode.ERROR_EMPTY_DATA.getErrorCode(), null);
            return;
        }
        String html = model.getCommentsHtml();
        if (TextUtils.isEmpty(html)) {
            onJsonResponseError(ApiErrorCode.ERROR_EMPTY_DATA.getErrorCode(), null);
            return;
        }

        // 解析XML
        Document document = Jsoup.parse(html);

        Elements feeds = document.select(".feedbackItem");
        List<BlogComment> result = new ArrayList<>();
        for (Element feed : feeds) {
            String id = Utils.getNumber(feed.select(".layer").attr("href"));
            String authorName = feed.select("#a_comment_author_" + id).text();
            String blogApp = Utils.getBlogApp(feed.select("#a_comment_author_" + id).attr("href"));
            String date = Utils.getDate(feed.select(".comment_date").text());
            String body = feed.select(".blog_comment_body").text();
            String like = Utils.getNumber(feed.select(".comment_digg").text());
            String unlike = Utils.getNumber(feed.select(".comment_bury").text());

            BlogComment m = new BlogComment();
            m.setId(id);
            m.setAuthorName(authorName);
            m.setBlogApp(blogApp);
            m.setDate(date);
            m.setBody(body);
            m.setLike(like);
            m.setUnlike(unlike);
            result.add(m);
        }

        mListener.onApiSuccess(result);
    }

    @Override
    public void onJsonResponseError(int errorCode, Throwable e) {
        ApiErrorCode code = ApiErrorCode.valueOf(errorCode);
        mListener.onApiFailed(new ApiException(), e == null ? code.getMessage() : e.getMessage());
    }
}
