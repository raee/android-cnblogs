package com.rae.cnblogs.sdk;

import android.content.Context;

/**
 * 博客园接口实例化
 * Created by ChenRui on 2016/11/28 23:38.
 */
public final class CnblogsApiFactory {
    private Context mContext;

    private CnblogsApiFactory(Context context) {
        mContext = context;
    }

    public IBlogApi getBlogApi() {
        return null;
    }
}
