package com.rae.cnblogs.sdk.parser;

import android.text.TextUtils;

import com.rae.core.sdk.ApiUiListener;
import com.rae.core.sdk.exception.ApiErrorCode;
import com.rae.core.sdk.exception.ApiException;
import com.rae.core.sdk.net.IApiJsonResponse;

import org.w3c.dom.Document;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilderFactory;

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
        if (TextUtils.isEmpty(json)) {
            onJsonResponseError(ApiErrorCode.ERROR_EMPTY_DATA.getErrorCode(), null);
            return;
        }

        // 解析XML
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(json.getBytes("UTF-8"));
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream);
            String content = doc.getDocumentElement().getTextContent();
            mListener.onApiSuccess(content);
        } catch (Exception e) {
            e.printStackTrace();
            onJsonResponseError(ApiErrorCode.ERROR_JSON_PARSE.getErrorCode(), e);
        }
    }

    @Override
    public void onJsonResponseError(int errorCode, Throwable e) {
        ApiErrorCode code = ApiErrorCode.valueOf(errorCode);
        mListener.onApiFailed(new ApiException(), e == null ? code.getMessage() : e.getMessage());
    }
}
