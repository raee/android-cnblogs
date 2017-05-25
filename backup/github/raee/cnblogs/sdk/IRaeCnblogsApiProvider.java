package com.github.raee.cnblogs.sdk;

import com.rae.cnblogs.sdk.IAdvertApi;

/**
 * 接口提供列表
 * Created by ChenRui on 2017/1/25 0025 14:40.
 */
public interface IRaeCnblogsApiProvider {

    /**
     * 获取广告接口
     */
    IAdvertApi getAdvertApi();
}
