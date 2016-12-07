package com.rae.cnblogs.presenter;

import com.rae.cnblogs.sdk.bean.Blog;

/**
 * 博客查看
 * Created by ChenRui on 2016/12/7 22:02.
 */
public interface IBlogContentPresenter {

    /**
     * 加载博客内容
     */
    void loadContent();

    interface IBlogContentView {

        Blog getBlog();

        void onLoadContentSuccess(Blog blog);

        void onLoadContentFailed(String msg);
    }
}
