package com.rae.cnblogs.sdk;

import com.rae.cnblogs.sdk.bean.BookmarksBean;
import com.rae.core.sdk.ApiUiArrayListener;
import com.rae.core.sdk.ApiUiListener;

/**
 * 收藏接口
 * Created by ChenRui on 2017/1/14 13:53.
 */
public interface IBookmarksApi {

    /**
     * 添加到收藏夹
     *
     * @param m 实例化使用构造函数{@link BookmarksBean#BookmarksBean(String, String, String)}
     */
    void addBookmarks(BookmarksBean m, ApiUiListener<Void> listener);

    /**
     * 获取收藏列表
     *
     * @param page 页码
     */
    void getBookmarks(int page, ApiUiArrayListener<BookmarksBean> listener);

    /**
     * 删除收藏
     *
     * @param url 可以传ID，或者URL
     */
    void delBookmarks(String url, ApiUiListener<Void> listener);
}
