package com.github.raee.cnblogs.sdk.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rae.core.sdk.ApiUiArrayListener;
import com.rae.core.sdk.ApiUiListener;
import com.rae.core.sdk.exception.ApiErrorCode;
import com.rae.core.sdk.exception.ApiException;
import com.rae.core.sdk.net.RaeSimpleJsonResponse;

/**
 * Created by ChenRui on 2016/12/22 23:37.
 */
public class RaeBlogApiResponse<T> extends RaeSimpleJsonResponse<T> {
    public RaeBlogApiResponse(Class<T> aClass, ApiUiListener<T> listener) {
        super(aClass, listener);
    }

    public RaeBlogApiResponse(Class<T> aClass, ApiUiArrayListener<T> listener) {
        super(aClass, listener);
    }

    @Override
    protected String parseJson(String json) {
        JSONObject obj = JSON.parseObject(json);
        int code = obj.getIntValue("code");
        String message = obj.getString("message");
        String dataJson = obj.get("data").toString();

        if (code != 200) {
            notifyApiFailed(ApiErrorCode.ERROR_EMPTY_DATA, message);
            return null;
        }


        return dataJson;
    }

    private void notifyApiFailed(ApiErrorCode errorCode, String msg) {
        if (mListener != null) {
            mListener.onApiFailed(new ApiException(errorCode), msg);
        }
        if (mArrayListener != null) {
            mArrayListener.onApiFailed(new ApiException(errorCode), msg);
        }
    }


}
