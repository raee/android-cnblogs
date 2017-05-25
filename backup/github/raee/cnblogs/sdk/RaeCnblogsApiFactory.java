package com.github.raee.cnblogs.sdk;

import android.content.Context;

import com.github.raee.cnblogs.sdk.impl.ReaCnblogsApiProvider;

/**
 * 服务端接口
 * Created by ChenRui on 2017/1/25 0025 14:42.
 */
public final class RaeCnblogsApiFactory {
    private static IRaeCnblogsApiProvider sRaeCnblogsApiProvider;

    public static IRaeCnblogsApiProvider getInstance(Context context) {
        if (sRaeCnblogsApiProvider == null) {
            sRaeCnblogsApiProvider = new ReaCnblogsApiProvider(context.getApplicationContext());
        }
        return sRaeCnblogsApiProvider;
    }
}
