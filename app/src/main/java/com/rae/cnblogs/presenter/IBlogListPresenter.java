package com.rae.cnblogs.presenter;

import com.rae.cnblogs.sdk.bean.Blog;

import java.util.List;

/**
 * 首页
 * Created by ChenRui on 2016/12/2 00:21.
 */
public interface IBlogListPresenter extends IRaePresenter {

    interface IBlogListView {

        void onLoadBlogList(List<Blog> data);

        void onLoadFailed(String msg);

        int getPage();

        String getCategoryId();

        String getParentId();
    }
}
