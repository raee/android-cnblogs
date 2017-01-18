package com.rae.cnblogs.sdk.parser;

import android.text.Html;

import com.rae.core.sdk.ApiUiListener;
import com.rae.core.sdk.exception.ApiErrorCode;
import com.rae.core.sdk.exception.ApiException;
import com.rae.core.sdk.net.IApiJsonResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * Created by ChenRui on 2017/1/18 0018 18:27.
 */
public class NewsContentParser implements IApiJsonResponse {
    private final ApiUiListener<String> mListener;

    public NewsContentParser(ApiUiListener<String> listener) {
        mListener = listener;
    }

    @Override
    public void onJsonResponse(String json) {
        // 解析HTML
        Document document = Jsoup.parse(json);
        Elements elements = document.select("Content");
        mListener.onApiSuccess(Html.fromHtml(elements.html()).toString().replace("src=\"//", "src=\"http://"));
    }

    @Override
    public void onJsonResponseError(int errorCode, Throwable e) {
        mListener.onApiFailed(new ApiException(ApiErrorCode.valueOf(errorCode)), e == null ? ApiErrorCode.valueOf(errorCode).getMessage() : e.getMessage());
    }
}
