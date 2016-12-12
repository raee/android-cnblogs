package com.rae.cnblogs.sdk.parser;

import com.alibaba.fastjson.JSON;
import com.rae.cnblogs.sdk.bean.BlogComment;
import com.rae.cnblogs.sdk.bean.BlogCommentModel;
import com.rae.core.sdk.ApiUiListener;
import com.rae.core.sdk.exception.ApiErrorCode;
import com.rae.core.sdk.exception.ApiException;
import com.rae.core.sdk.net.IApiJsonResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * 博文解析
 * Created by ChenRui on 2016/11/30 0030 17:00.
 */
public class BlogCommentParser implements IApiJsonResponse {

    private final ApiUiListener<BlogComment> mListener;

    public BlogCommentParser(ApiUiListener<BlogComment> listener) {
        mListener = listener;
    }

    @Override
    public void onJsonResponse(String json) {

        BlogCommentModel model = JSON.parseObject(json, BlogCommentModel.class);
        //model.getCommentsHtml()

        // 解析XML
        Document document = Jsoup.parse(json);
    }

    @Override
    public void onJsonResponseError(int errorCode, Throwable e) {
        ApiErrorCode code = ApiErrorCode.valueOf(errorCode);
        mListener.onApiFailed(new ApiException(), e == null ? code.getMessage() : e.getMessage());
    }
}
