package com.rae.cnblogs.sdk.parser;

import com.rae.cnblogs.sdk.Utils;
import com.rae.cnblogs.sdk.bean.BlogComment;
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
 * Created by ChenRui on 2017/1/18 0018 18:27.
 */
public class NewsCommentParser implements IApiJsonResponse {
    private final ApiUiArrayListener<BlogComment> mListener;

    public NewsCommentParser(ApiUiArrayListener<BlogComment> listener) {
        mListener = listener;
    }

    @Override
    public void onJsonResponse(String json) {
        // 解析HTML
        List<BlogComment> result = new ArrayList<>();
        Document document = Jsoup.parse(json);
        Elements elements = document.select(".user_comment");
        for (Element element : elements) {
            BlogComment m = new BlogComment();
            m.setId(Utils.getNumber(element.select(".comment_main").text()));
            m.setAuthorName(element.select(".comment-author").text());
            m.setLike(Utils.getCount(".agree_" + m.getId()));
            m.setBody(element.select(".comment_main").text());
            m.setDate(Utils.getDate(element.select(".time").text().replace("发表于 ", "").trim()));
            result.add(m);
        }
        mListener.onApiSuccess(result);
    }

    @Override
    public void onJsonResponseError(int errorCode, Throwable e) {
        mListener.onApiFailed(new ApiException(ApiErrorCode.valueOf(errorCode)), e == null ? ApiErrorCode.valueOf(errorCode).getMessage() : e.getMessage());
    }
}
