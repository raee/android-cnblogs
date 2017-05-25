package com.rae.cnblogs.sdk.parser;

import com.rae.cnblogs.sdk.utils.ApiUtils;
import com.rae.cnblogs.sdk.bean.BlogCommentBean;
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
    private final ApiUiArrayListener<BlogCommentBean> mListener;

    public NewsCommentParser(ApiUiArrayListener<BlogCommentBean> listener) {
        mListener = listener;
    }

    @Override
    public void onJsonResponse(String json) {
        // 解析HTML
        List<BlogCommentBean> result = new ArrayList<>();
        Document document = Jsoup.parse(json);
        Elements elements = document.select(".user_comment");
        for (Element element : elements) {
            BlogCommentBean m = new BlogCommentBean();
            m.setId(ApiUtils.getNumber(element.select(".comment_main").attr("id")));
            m.setAuthorName(element.select(".comment-author").text());
            m.setLike(ApiUtils.getCount(".agree_" + m.getId()));
            m.setBody(element.select(".comment_main").text());
            m.setDate(ApiUtils.getDate(element.select(".time").text().replace("发表于 ", "").trim()));
            result.add(m);
        }
        mListener.onApiSuccess(result);
    }

    @Override
    public void onJsonResponseError(int errorCode, Throwable e) {
        mListener.onApiFailed(new ApiException(ApiErrorCode.valueOf(errorCode)), e == null ? ApiErrorCode.valueOf(errorCode).getMessage() : e.getMessage());
    }
}
