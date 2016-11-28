package com.rae.cnblogs.sdk;

import java.util.List;

/**
 * 接口回调
 * Created by ChenRui on 2016/11/28 23:41.
 */
public interface ICnblogsListener<T> {

    /**
     * 回调成功
     *
     * @param data
     */
    void onApiSuccess(List<T> data);

    /**
     * 回调失败
     *
     * @param errorCode
     */
    void onApiError(ApiErrorCode errorCode);
}
