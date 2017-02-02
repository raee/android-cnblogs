package com.rae.cnblogs.sdk.parser;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.android.volley.VolleyError;
import com.rae.core.sdk.ApiUiArrayListener;
import com.rae.core.sdk.ApiUiListener;
import com.rae.core.sdk.exception.ApiErrorCode;
import com.rae.core.sdk.net.RaeSimpleJsonResponse;

import org.jsoup.Jsoup;

/**
 * 网页版的接口默认JSON解析
 * Created by ChenRui on 2017/1/22 0022 10:18.
 */
public class CnBlogsWebApiResponse<T> extends RaeSimpleJsonResponse<T> {
    public CnBlogsWebApiResponse(Class<T> aClass, ApiUiListener<T> listener) {
        super(aClass, listener);
    }

    public CnBlogsWebApiResponse(Class<T> aClass, ApiUiArrayListener<T> listener) {
        super(aClass, listener);
    }

    @Override
    public void onJsonResponse(String json) {

        // 删除评论的时候会返回true or false
        if (TextUtils.equals(json, "true")) {
            notifyApiSuccess(null);
            return;
        }

        if (TextUtils.equals(json, "false")) {
            notifyApiError(ApiErrorCode.ERROR_EMPTY_DATA, null);
            return;
        }

        if (mClass == Void.class && parseJson(json) == null) {
//            notifyApiSuccess(null);
            return;
        }
        super.onJsonResponse(json);
    }

    @Override
    protected String parseJson(String json) {
        // 解析公共部分
        JSONObject obj = JSON.parseObject(json);
        boolean isSuccess = false;
        String message = null;
        Object data = null;
        if (obj.containsKey("IsSuccess")) {
            isSuccess = obj.getBoolean("IsSuccess");
        }
        if (obj.containsKey("Message")) {
            message = obj.getString("Message");
        }
        if (obj.containsKey("Data")) {
            data = obj.get("Data");
        }
        if (isSuccess && data != null) {
            return data.toString();
        } else if (isSuccess && mClass == Void.class) {
            notifyApiSuccess(null);
            return null;
        } else {
            message = Jsoup.parse(message).text();
            notifyApiError(ApiErrorCode.ERROR_EMPTY_DATA, message);
            return null;
        }
    }

    @Override
    public void onJsonResponseError(int errorCode, Throwable e) {
        Log.e("rae", "错误：" + new String(((VolleyError) e).networkResponse.data));
        super.onJsonResponseError(errorCode, e);
    }
}
