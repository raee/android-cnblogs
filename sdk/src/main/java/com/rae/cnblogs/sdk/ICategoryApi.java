package com.rae.cnblogs.sdk;

import com.rae.cnblogs.sdk.bean.Category;
import com.rae.core.sdk.ApiUiArrayListener;

/**
 * 分类接口
 * Created by ChenRui on 2016/11/30 0030 17:19.
 */
public interface ICategoryApi {

    /**
     * 获取分类
     *
     * @param listener
     */
    void getCategory(ApiUiArrayListener<Category> listener);
}
