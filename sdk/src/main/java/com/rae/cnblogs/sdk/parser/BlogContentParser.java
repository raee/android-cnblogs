package com.rae.cnblogs.sdk.parser;

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
public class BlogContentParser implements IApiJsonResponse {

    private final ApiUiListener<String> mListener;

    public BlogContentParser(ApiUiListener<String> listener) {
        mListener = listener;
    }

    @Override
    public void onJsonResponse(String json) {
        // 解析XML
        Document document = Jsoup.parse(json);
        mListener.onApiSuccess(document.select("string").text());
    }

    @Override
    public void onJsonResponseError(int errorCode, Throwable e) {
        ApiErrorCode code = ApiErrorCode.valueOf(errorCode);
        mListener.onApiFailed(new ApiException(), e == null ? code.getMessage() : e.getMessage());
    }
}
