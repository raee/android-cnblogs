package com.rae.cnblogs.sdk;

import android.content.Context;

import com.rae.cnblogs.sdk.impl.BlogApiImpl;
import com.rae.cnblogs.sdk.impl.CategoryApiImpl;

/**
 * 博客园接口实例化
 * Created by ChenRui on 2016/11/28 23:38.
 */
public final class CnblogsApiFactory {
    public static IBlogApi getBlogApi(Context context) {
        return new BlogApiImpl(context);
    }

    public static ICategoryApi getCategoryApi(Context context) {
        return new CategoryApiImpl(context);
    }
}
