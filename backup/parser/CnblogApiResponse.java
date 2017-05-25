package com.rae.cnblogs.sdk.parser;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.android.volley.VolleyError;
import com.rae.cnblogs.sdk.bean.CnblogsApiErrorBean;
import com.rae.core.sdk.ApiUiArrayListener;
import com.rae.core.sdk.ApiUiListener;
import com.rae.core.sdk.exception.ApiErrorCode;
import com.rae.core.sdk.exception.ApiHttpException;
import com.rae.core.sdk.net.RaeSimpleJsonResponse;

/**
 * 官方解析
 * Created by ChenRui on 2017/1/14 01:32.
 */
public class CnblogApiResponse<T> extends RaeSimpleJsonResponse<T> {

    public CnblogApiResponse(Class<T> aClass, ApiUiListener<T> listener) {
        super(aClass, listener);
    }

    public CnblogApiResponse(Class<T> aClass, ApiUiArrayListener<T> listener) {
        super(aClass, listener);
    }

    @Override
    public void onJsonResponseError(int i, Throwable throwable) {
        VolleyError error;
        String json;
        if (throwable instanceof VolleyError
                && (error = (VolleyError) throwable).networkResponse != null
                && !TextUtils.isEmpty(json = new String(error.networkResponse.data))
                ) {
            parseCnblogApiError(json, error);
            return;
        }
        super.onJsonResponseError(i, throwable);
    }

    private void parseCnblogApiError(@NonNull String json, VolleyError error) {
        Log.e("api", "接口返回错误信息：" + json);
        String msg;
        ApiErrorCode errorCode = ApiErrorCode.ERROR_EMPTY_DATA;

        try {
            CnblogsApiErrorBean object = JSON.parseObject(json, CnblogsApiErrorBean.class);

            if (object.getMessage() != null)
                msg = object.getMessage();
            else
                msg = object.getError();

            notifyApiError(errorCode, msg);

        } catch (Exception e) {
            e.printStackTrace();
            notifyApiError(new ApiHttpException(error.networkResponse.statusCode), null);
        }


    }
}
