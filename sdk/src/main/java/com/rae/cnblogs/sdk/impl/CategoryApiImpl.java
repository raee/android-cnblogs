package com.rae.cnblogs.sdk.impl;

import android.content.Context;

import com.rae.cnblogs.sdk.ICategoryApi;
import com.rae.cnblogs.sdk.bean.Category;
import com.rae.core.sdk.ApiUiArrayListener;

/**
 * 博客分类
 * Created by ChenRui on 2016/11/30 0030 17:36.
 */
public class CategoryApiImpl extends CnblogsBaseApi implements ICategoryApi {

    public CategoryApiImpl(Context context) {
        super(context);
    }

    @Override
    public void getCategory(ApiUiArrayListener<Category> listener) {
        get(ApiUrls.API_URL_CATEGORY_LIST, null, null);
    }
}
