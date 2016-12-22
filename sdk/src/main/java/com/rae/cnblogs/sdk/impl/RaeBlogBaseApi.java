package com.rae.cnblogs.sdk.impl;

import android.content.Context;

import com.rae.core.sdk.ApiUiListener;
import com.rae.core.sdk.RaeBaseApi;
import com.rae.core.sdk.net.IApiJsonResponse;

/**
 * 博客园服务端接口
 * Created by ChenRui on 2016/12/22 23:00.
 */
public class RaeBlogBaseApi extends RaeBaseApi {

    public RaeBlogBaseApi(Context context) {
        super(context);
    }

    protected String url(String path) {
        return ApiUrls.RAE_API_DOMAIN + path;
    }

    @Override
    protected <T> IApiJsonResponse getDefaultJsonResponse(Class<T> cls, ApiUiListener<T> listener) {
        return super.getDefaultJsonResponse(cls, listener);
    }
}
