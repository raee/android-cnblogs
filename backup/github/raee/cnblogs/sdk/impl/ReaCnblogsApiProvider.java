package com.github.raee.cnblogs.sdk.impl;

import android.content.Context;

import com.rae.cnblogs.sdk.IAdvertApi;
import com.github.raee.cnblogs.sdk.IRaeCnblogsApiProvider;

/**
 * 默认提供接口
 * Created by ChenRui on 2017/1/25 0025 14:41.
 */
public class ReaCnblogsApiProvider implements IRaeCnblogsApiProvider {

    private Context mContext;

    public ReaCnblogsApiProvider(Context context) {
        mContext = context;
    }

    @Override
    public IAdvertApi getAdvertApi() {
        return new AdvertApiImpl(mContext);
    }
}
