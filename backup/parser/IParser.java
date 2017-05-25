package com.rae.cnblogs.sdk.parser;

import com.rae.core.sdk.ApiUiArrayListener;
import com.rae.core.sdk.ApiUiListener;
import com.rae.core.sdk.net.IApiJsonResponse;

/**
 * Created by ChenRui on 2017/2/7 0007 11:57.
 */
public interface IParser<T> extends IApiJsonResponse {

    void setApiListener(ApiUiListener<T> listener);

    void setApiListener(ApiUiArrayListener<T> listener);
}
