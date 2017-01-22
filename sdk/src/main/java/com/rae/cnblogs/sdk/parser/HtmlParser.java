package com.rae.cnblogs.sdk.parser;

import com.rae.core.sdk.ApiUiArrayListener;
import com.rae.core.sdk.ApiUiListener;
import com.rae.core.sdk.exception.ApiErrorCode;
import com.rae.core.sdk.exception.ApiException;
import com.rae.core.sdk.net.IApiJsonResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * 解析HTML
 * Created by ChenRui on 2017/1/22 0022 14:45.
 */
public abstract class HtmlParser<T> implements IApiJsonResponse {
    protected ApiUiListener<T> mListener;
    protected ApiUiArrayListener<T> mArrayListener;

    public HtmlParser(ApiUiListener<T> listener) {
        mListener = listener;
    }

    public HtmlParser(ApiUiArrayListener<T> arrayListener) {
        mArrayListener = arrayListener;
    }

    @Override
    public void onJsonResponse(String html) {
        try {
            // 解析XML
            Document document = Jsoup.parse(html);
            onParseHtmlDocument(document);
        } catch (Exception e) {
            onJsonResponseError(ApiErrorCode.ERROR_JSON_PARSE.getErrorCode(), e);
        }
    }

    /**
     * 解析HTML 文档
     */
    protected abstract void onParseHtmlDocument(Document document);

    @Override
    public void onJsonResponseError(int errorCode, Throwable e) {
        ApiErrorCode code = ApiErrorCode.valueOf(errorCode);
        ApiException ex = new ApiException(errorCode, e);
        String msg = e == null ? code.getMessage() : e.getMessage();
        notifyError(ex, msg);
    }

    private void notifyError(ApiException ex, String msg) {
        if (mListener != null)
            mListener.onApiFailed(ex, msg);

        if (mArrayListener != null)
            mArrayListener.onApiFailed(ex, msg);
    }
}
