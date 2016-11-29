package com.rae.cnblogs.sdk.impl;

import android.content.Context;

import com.rae.core.sdk.RaeBaseApi;

/**
 * 请求父类,所有接口请求都继承该类
 * Created by ChenRui on 2016/11/30 00:00.
 */
public class CnblogsBaseApi extends RaeBaseApi {

    public CnblogsBaseApi(Context context) {
        super(context);
    }
}
